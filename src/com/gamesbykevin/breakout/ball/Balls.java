package com.gamesbykevin.breakout.ball;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.panel.GamePanel;

import android.graphics.Canvas;

public class Balls extends Entity implements ICommon
{
	//list of balls in play
	private ArrayList<Ball> balls;
	
	//list of keys for the ball animations
	private ArrayList<Key> keys;
	
	/**
	 * The different animations for each ball
	 */
	public enum Key
	{
		Yellow, Blue, Green, Orange, Red, White
	}
	
	/**
	 * The number of balls allowed at once
	 */
	public static final int MAX_BALL_LIMIT = 5;
	
	public Balls(final Game game) throws Exception 
	{
		//call parent constructor
		super(game, Ball.WIDTH, Ball.HEIGHT);
		
		//create new list of balls
		this.balls = new ArrayList<Ball>();
		
		//create new list of keys
		this.keys = new ArrayList<Key>();
		
		//where animation is located
		final int y = 0;
		
		for (Key key : Key.values())
		{
			int x;
			
			//locate y-coordinate
			switch (key)
			{
				case Yellow:
					x = (0 * Ball.DIMENSIONS);
					break;
					
				case Blue:
					x = (1 * Ball.DIMENSIONS);
					break;
					
				case Green:
					x = (2 * Ball.DIMENSIONS);
					break;
					
				case Orange:
					x = (3 * Ball.DIMENSIONS);
					break;
					
				case Red:
					x = (4 * Ball.DIMENSIONS);
					break;
					
				case White:
					x = (5 * Ball.DIMENSIONS);
					break;
					
				default:
					throw new Exception("Key not found: " + key.toString());
			}
			
			//create new animation
			Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), x, y, Ball.DIMENSIONS, Ball.DIMENSIONS);
			
			//now add animation to the sprite sheet
			super.getSpritesheet().add(key, animation);
		}
	}

	@Override
	public void dispose() 
	{
		if (this.balls != null)
		{
			for (int i=0; i < this.balls.size(); i++)
			{
				if (this.balls.get(i) != null)
				{
					this.balls.get(i).dispose();
					this.balls.set(i, null);
				}
			}
			
			this.balls.clear();
			this.balls = null;
		}
		
		if (this.keys != null)
		{
			this.keys.clear();
			this.keys = null;
		}
	}

	/**
	 * Get the list of balls
	 * @return The list of balls in play
	 */
	public ArrayList<Ball> getBalls()
	{
		return this.balls;
	}
	
	/**
	 * Get the ball count
	 * @return The total number of balls currently in play (hidden = false)
	 */
	public int getCount()
	{
		//keep track of count
		int count = 0;
		
		//check every ball in array
		for (int i = 0; i < getBalls().size(); i++)
		{
			//if the ball is not hidden lets count it
			if (!getBalls().get(i).isHidden())
				count++;
		}
		
		//return our result
		return count;
	}
	
	/**
	 * Speed up all balls
	 */
	public void speedUp()
	{
		if (getBalls() != null)
		{
			for (Ball ball : getBalls())
			{
				ball.speedUp();
			}
		}
	}
	
	/**
	 * Speed down all balls
	 */
	public void speedDown()
	{
		if (getBalls() != null)
		{
			for (Ball ball : getBalls())
			{
				ball.speedDown();
			}
		}
	}
	
	/**
	 * Flag the balls frozen
	 * @param frozen true if you want to stop moving the balls
	 */
	public void setFrozen(final boolean frozen)
	{
		if (getBalls() != null)
		{
			for (Ball ball : getBalls())
			{
				ball.setFrozen(frozen);
			}
		}
	}
	
	/**
	 * Flag the balls on fire
	 * @param fire True if you want the balls on fire, false otherwise
	 */
	public void setFire(final boolean fire)
	{
		if (getBalls() != null)
		{
			for (Ball ball : getBalls())
			{
				//flag fire
				ball.setFire(fire);
			}
		}
	}
	
	/**
	 * Get a random key
	 * @return A random animation key for the balls (except red)
	 */
	private Key getRandomKey()
	{
		//if empty populate list
		if (this.keys.isEmpty())
		{
			for (Key key : Key.values())
			{
				//skip red
				if (key == Key.Red)
					continue;
				
				//add key to list
				this.keys.add(key);
			}
		}
		
		//pick random index
		final int index = GamePanel.RANDOM.nextInt(this.keys.size());
		
		//assign random chosen value
		final Key tmp = this.keys.get(index);
		
		//remove value from array list
		this.keys.remove(index);
		
		//return random chosen value
		return tmp;
	}
	
	/**
	 * Remove any existing balls and add a default ball<br>
	 * We also reset the paddle location and place the ball there.<br>
	 * This is when starting/restarting a new level
	 * @param paddle The paddle where we want to spawn the ball
	 */
	public void add(final Paddle paddle)
	{
		//remove all existing balls
		getBalls().clear();
		
		//reset paddle
		paddle.reset();
		
		//pick x-coordinate
		final double x = paddle.getX() + (paddle.getWidth() / 2) - (Ball.WIDTH / 2); 
		
		//add ball at paddle location
		add(x, paddle.getY() - Ball.HEIGHT);
		
		//freeze existing ball
		getBalls().get(0).setFrozen(true);
			
		//assign x-offset
		getBalls().get(0).setOffsetX(x - paddle.getX());
	}
	
	/**
	 * Add a ball at the default starting location.<br>
	 * If there are other balls currently in play a new ball will be spawned at one of those locations
	 */
	public void add()
	{
		add(Ball.START_X, Ball.START_Y);
	}
	
	/**
	 * Add a ball at the specified starting location
	 * If there are other balls currently in play a new ball will be spawned at one of those locations
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	private void add(double x, double y)
	{
		//don't add any additional balls if we reached our limit
		if (getBalls().size() >= MAX_BALL_LIMIT)
			return;
		
		//first see if we can reuse an existing ball
		for (Ball ball : getBalls())
		{
			//we can't re-use a ball already in play
			if (!ball.isHidden())
				continue;
			
			//pick random animation
			ball.setKey(getRandomKey());
			
			//reset ball
			ball.reset();
			
			//place ball accordingly
			ball.setX(x);
			ball.setY(y);
			
			//choose random velocity
			ball.setDX(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
			ball.setDY(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
			
			//make sure ball is no longer hidden
			ball.setHidden(false);
			
			//no need to continue
			return;
		}
		
		//create a new ball
		Ball ball = new Ball(getRandomKey());

		//position the ball
		ball.setX(x);
		ball.setY(y);
		
		//choose random velocity
		ball.setDX(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
		ball.setDY(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
		
		//add to list
		getBalls().add(ball);
	}
	
	@Override
	public void update() throws Exception 
	{
		//set the length
		final int rowMax = getGame().getBricks().getBricks().length;
		final int colMax = getGame().getBricks().getBricks()[0].length;
		
		if (getBalls() != null)
		{
			//update all balls
			for (int i = 0; i < getBalls().size(); i++)
			{
				//get the current ball
				Ball ball = getBalls().get(i);
				
				//if the ball is hidden skip it
				if (ball.isHidden())
					continue;
				
				//update ball
				ball.update();
				
				//check if the ball has hit any of the bricks
				for (int row = 0; row < rowMax; row++)
				{
					for (int col = 0; col < colMax; col++)
					{
						//if there was ball/brick collision no need to check the other bricks
						if (checkBrickCollision(ball, getGame().getBricks().getBricks()[row][col]))
						{
							//move to the end
							row = rowMax;
							col = colMax;
							
							//no need to check the other bricks since the ball already hit
							break;
						}
					}
				}
				
				//make sure the ball stays in bounds
				ball.verifyBounds();
			}
		}
	}
	
	/**
	 * Check the ball and brick to see if we have collision
	 * @param ball The ball we want to check
	 * @param brick The brick we want to check
	 * @return true if collision, false otherwise
	 */
	private boolean checkBrickCollision(final Ball ball, final Brick brick) throws Exception
	{
		if (!brick.isDead())
		{
			//if this ball has collision with the current brick
			if (ball.hasCollision(brick))
			{
				//if the brick is solid just bounce the ball off it
				if (brick.isSolid())
				{
					//flip y-velocity
					ball.setDY(-ball.getDY());
					
					//calculate the middle coordinate of the ball
					final double my = ball.getY() + (ball.getHeight() / 2);
					
					//get the end points of the brick
					final double bl = brick.getX();
					final double br = brick.getX() + brick.getWidth();
					
					//make sure close enough
					if (my > brick.getY() && my < brick.getY() + brick.getHeight())
					{
						//depending on x-coordinate may want to change x-velocity
						if (ball.getX() + ball.getWidth() >= br)
						{
							ball.setDX(-ball.getDX());
						}
						else if (ball.getX() <= bl)
						{
							ball.setDX(-ball.getDX());
						}
					}
				}
				else
				{
					//if the ball is not a fire ball flip the y-velocity
					if (!ball.hasFire())
						ball.setDY(-ball.getDY());
					
					//flag the brick as dead
					brick.setDead(true);
	
					//add particles
					brick.addParticles();
					
					//if the brick contains a power up we will add it
					if (brick.hasPowerup())
						super.getGame().getPowerups().add(brick);
				}
				
				//we have collision
				return true;
			}
			else
			{
				//no collision
				return false;
			}
		}
		else
		{
			//no collision
			return false;
		}
	}
	
	@Override
	public void reset() 
	{
		//remove all balls
		if (getBalls() != null)
			getBalls().clear();
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		if (getBalls() != null)
		{
			//render all balls
			for (int i = 0; i < getBalls().size(); i++)
			{
				//get the current ball
				final Ball ball = getBalls().get(i);

				//assign values
				super.setX(ball);
				super.setY(ball);
				super.setWidth(ball);
				super.setHeight(ball);
				super.getSpritesheet().setKey(ball.getKey());
				
				//render the current ball
				super.render(canvas);
			}
		}
	}
}