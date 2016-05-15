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
	
	//the list of lasers
	private ArrayList<Laser> lasers;
	
	/**
	 * The different animations for each ball
	 */
	public enum Key
	{
		Yellow, Blue, Green, Orange, Red, White
	}
	
	public Balls(final Game game) throws Exception 
	{
		//call parent constructor
		super(game, Ball.WIDTH, Ball.HEIGHT);
		
		//create new list of balls
		this.balls = new ArrayList<Ball>();
		
		//create new list of lasers
		this.lasers = new ArrayList<Laser>();
		
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
		
		if (this.lasers != null)
		{
			for (int i=0; i < this.lasers.size(); i++)
			{
				if (this.lasers.get(i) != null)
				{
					this.lasers.get(i).dispose();
					this.lasers.set(i, null);
				}
			}
			
			this.lasers.clear();
			this.lasers = null;
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
				ball.setFire(fire);
			}
		}
	}
	
	/**
	 * Add ball to collection
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void add(final int x, final int y)
	{
		//create a new ball
		Ball ball = new Ball(getGame(), Balls.Key.Blue);

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
									//flip y-velocity
									ball.setDY(-ball.getDY());
									
									//flag the brick as dead
									brick.setDead(true);

									//if the brick contains a power up we will add it
									if (brick.hasPowerup())
										super.getGame().getPowerups().add(brick);
									
									//move to the ends
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
		
		if (getLasers() != null)
		{
			for (Laser laser : getLasers())
			{
				if (laser.isHidden())
					continue;
				
				//update location of laser
				laser.update();
				
				//check if it hit any bricks etc.....
				for (int row = 0; row < rowMax; row++)
				{
					for (int col = 0; col < colMax; col++)
					{
						//get the current brick
						final Brick brick = getGame().getBricks().getBricks()[row][col];
						
						if (!brick.isDead())
						{
							//if the laser hit the brick
							if (laser.hasCollision(brick))
							{
								//flag laser hidden
								laser.setHidden(true);
								
								//flag the brick as dead
								brick.setDead(true);

								//if the brick contains a power up we will add it
								if (brick.hasPowerup())
									super.getGame().getPowerups().add(brick);
								
								//move to the ends
								row = rowMax;
								col = colMax;
								
								//no need to check the other bricks since the ball already hit
								break;
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void reset() 
	{
		if (getBalls() != null)
			getBalls().clear();
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		if (getLasers() != null)
		{
			for (Laser laser : getLasers())
			{
				//only render what is not hidden
				if (laser.isHidden())
					continue;
				
				//render the laser
				laser.render(canvas);
			}
		}
		
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
	
	/**
	 * Add lasers to the mix
	 * @param paddle The paddle which is the starting location of the lasers
	 */
	public void addLasers(final Paddle paddle)
	{
		//count the number of lasers re-used
		int count = 0;
		
		for (Laser laser : getLasers())
		{
			//hidden lasers can be re-used, just make sure we didn't already re-use 2
			if (laser.isHidden() && count < 2)
			{
				//increase count
				count++;
				
				//flag false
				laser.setHidden(false);
				
				//y-coordinate will be the same
				laser.setY(paddle.getY());
				
				if (count == 1)
				{
					laser.setX(paddle.getX());
				}
				else if (count == 2)
				{
					laser.setX(paddle.getX() + paddle.getWidth() - Laser.WIDTH);
				}
			}
		}
		
		//no lasers were found so let's add the lasers to the list
		if (count < 1)
		{
			getLasers().add(new Laser(paddle.getX(), paddle.getY()));
			getLasers().add(new Laser(paddle.getX() + paddle.getWidth() - Laser.WIDTH, paddle.getY()));
		}
		else if (count < 2)
		{
			getLasers().add(new Laser(paddle.getX() + paddle.getWidth() - Laser.WIDTH, paddle.getY()));
		}
	}
	
	private ArrayList<Laser> getLasers()
	{
		return this.lasers;
	}
	
	/**
	 * A laser in game play
	 * @author GOD
	 *
	 */
	private class Laser extends Entity
	{
		/**
		 * Width of the laser
		 */
		private static final int WIDTH = 9;
		
		/**
		 * Width of the laser
		 */
		private static final int HEIGHT = 54;

		/**
		 * The rate at which the laser can move
		 */
		private static final int Y_VELOCITY = (HEIGHT / 2); 
		
		public Laser(final double x, final double y) 
		{
			super(null, WIDTH, HEIGHT);
			
			super.setX(x);
			super.setY(y);
		}

		@Override
		public void update() throws Exception 
		{
			super.setY(super.getY() - Y_VELOCITY);
		}
		
		@Override
		public void render(final Canvas canvas) throws Exception
		{
			super.render(canvas, Images.getImage(Assets.ImageGameKey.LaserRed));
		}
	}
}