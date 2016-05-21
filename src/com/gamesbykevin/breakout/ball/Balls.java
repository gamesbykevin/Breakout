package com.gamesbykevin.breakout.ball;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;
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
		final int index = GamePanel.RANDOM.nextInt(Key.values().length);
		
		//assign random chosen value
		final Key tmp = Key.values()[index];
		
		//remove value from array list
		this.keys.remove(index);
		
		//return random chosen value
		return tmp;
	}
	
	/**
	 * Get the ball count
	 * @return The total number of the balls in play (hidden balls are not counted)
	 */
	private int getCount()
	{
		int count = 0;
		
		for (Ball ball : getBalls())
		{
			if (!ball.isHidden())
				count++;
		}
		
		//return our result
		return count;
	}
	
	/**
	 * Add ball to collection
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void add()
	{
		//don't add any additional balls if we reached our limit
		if (getCount() >= MAX_BALL_LIMIT)
			return;
		
		//first default to the start location
		double x = Ball.START_X;
		double y = Ball.START_Y;
		
		//check every ball to see if one can be re-used
		for (Ball ball : getBalls())
		{
			//if this ball is hidden it can be re-used
			if (ball.isHidden())
			{
				//flag hidden false
				ball.setHidden(false);
				
				//reset
				ball.reset();
				
				//assign random animation key
				ball.setKey(getRandomKey());
				
				//position the ball
				ball.setX(x);
				ball.setY(y);
				
				//choose random velocity
				ball.setDX(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
				ball.setDY(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
				
				//no need to continue
				return;
			}
			else
			{
				//this ball is a valid location to spawn the new ball
				x = GamePanel.RANDOM.nextBoolean() ? ball.getX() - Ball.WIDTH: ball.getX() + Ball.WIDTH;
				y = ball.getY() - Ball.HEIGHT;
			}
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
				
				if (ball != null)
				{
					//update ball
					ball.update();
					
					//make sure the ball stays in bounds
					ball.verifyBounds();
					
					//check if the ball has hit any of the bricks
					for (int row = 0; row < rowMax; row++)
					{
						for (int col = 0; col < colMax; col++)
						{
							//get the current brick
							final Brick brick = getGame().getBricks().getBricks()[row][col];
							
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
										
									//move to the end
									row = rowMax;
									col = colMax;
									
									//no need to check the other bricks since the ball already hit
									break;
								}
							}
						}
					}
				}
				else
				{
					//remove the ball since it is null
					getBalls().remove(i);
					
					//decrease index
					i--;
				}
			}
		}
	}
	
	@Override
	public void reset() 
	{
		if (getBalls() != null)
		{
			for (Ball ball : getBalls())
			{
				ball.reset();
			}
		}
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