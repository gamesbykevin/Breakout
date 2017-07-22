package com.gamesbykevin.breakout.ball;

import com.gamesbykevin.breakout.ball.Balls.Key;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.panel.GamePanel;
import com.gamesbykevin.breakout.thread.MainThread;
import com.gamesbykevin.breakout.wall.Wall;

public final class Ball extends Entity implements ICommon 
{
	/**
	 * Dimensions of ball
	 */
	public static final int WIDTH = 16;
	
	/**
	 * Dimensions of ball
	 */
	public static final int HEIGHT = 16;
	
	/**
	 * The starting x-coordinate for the ball
	 */
	public static final int START_X = (GamePanel.WIDTH / 2);
	
	/**
	 * The starting y-coordinate for the ball
	 */
	public static final int START_Y = (GamePanel.HEIGHT / 2) - HEIGHT;
	
	/**
	 * The animation dimension on the sprite sheet
	 */
	public static final int DIMENSIONS = 64;
	
	/**
	 * The maximum speed allowed
	 */
	public static double SPEED_MAX = Brick.HEIGHT_NORMAL;
	
	/**
	 * The minimum speed allowed
	 */
	public static final double SPEED_MIN = 6.15;
	
	/**
	 * The rate at which to increase the speed
	 */
	public static final double SPEED_INCREASE = 1.08; 
	
	/**
	 * The rate at which to decrease the speed
	 */
	public static final double SPEED_DECREASE = 0.95;
	
	//assign the animation type
	private Balls.Key key;
	
	//store the x-ratio for paddle collision
	private double xratio = 1.0;
	
	//is the ball frozen
	private boolean frozen = false;
	
	//is this a fire ball
	private boolean fire = false;
	
	//store the x-offset
	private int offsetX;
	
	//the frame count that the ball has been on fire
	private int frames = 0;
	
	/**
	 * The number of frames to keep the ball on fire
	 */
	private static final int FIRE_FRAME_LIMIT = (MainThread.FPS * 20);
	
	protected Ball(final Balls.Key key) 
	{
		super(null, WIDTH, HEIGHT);
		
		//assign key
		setKey(key);
	}
	
	/**
	 * Assign the x-offset
	 * @param offsetX The amount of pixels from the x-coordinate for the paddle
	 */
	public void setOffsetX(final double offsetX)
	{
		this.offsetX = (int)offsetX;
	}
	
	/**
	 * Get the x-offset
	 * @return The amount of pixels from the x-coordinate for the paddle
	 */
	public int getOffsetX()
	{
		return this.offsetX;
	}
	
	/**
	 * Flag the ball frozen
	 * @param frozen true if you intend to pause the ball, false otherwise
	 */
	public void setFrozen(final boolean frozen)
	{
		this.frozen = frozen;
	}
	
	/**
	 * Is the ball frozen?
	 * @return true = yes, false = no
	 */
	public boolean isFrozen()
	{
		return this.frozen;
	}
	
	/**
	 * Flag the ball on fire
	 * @param fire true if you want the ball to fly through the bricks, false otherwise
	 */
	public void setFire(final boolean fire)
	{
		this.fire = fire;
		
		//if fire is enabled reset frame count
		if (hasFire())
			this.frames = 0;
	}
	
	/**
	 * Does the ball have fire?
	 * @return true = yes, false = no
	 */
	public boolean hasFire()
	{
		return this.fire;
	}
	
	/**
	 * Assign the x-ratio
	 * @param xratio The adjustment when calculating the x-velocity
	 */
	public void setXRatio(final double xratio)
	{
		this.xratio = xratio;
	}
	
	/**
	 * Get the x-ratio
	 * @return The adjustment when calculating the x-velocity
	 */
	public double getXRatio()
	{
		return this.xratio;
	}
	
	/**
	 * Assign the animation key for the ball
	 * @param key The animation key for the ball
	 */
	public void setKey(final Balls.Key key)
	{
		this.key = key;
	}
	
	/**
	 * Get the animation key for the ball
	 * @return The animation key for the ball, if hasFire() returns true Key.Red will always returned
	 */
	public Balls.Key getKey()
	{
		//if fire is enabled return specific animation
		if (hasFire())
			return Key.Red;
		
		return this.key;
	}
	
	/**
	 * Speed up the ball speed for both x, y velocity
	 */
	public void speedUp()
	{
		//increase the speed overall
		speedUpX();
		speedUpY();
	}
	
	/**
	 * Increase the x-velocity based on speed increase rate
	 */
	public void speedUpX()
	{
		this.setDX(super.getDX() * SPEED_INCREASE);
	}
	
	/**
	 * Increase the y-velocity based on speed increase rate
	 */
	public void speedUpY()
	{
		this.setDY(super.getDY() * SPEED_INCREASE);
	}
	
	/**
	 * Speed down the ball speed for both x, y velocity
	 */
	public void speedDown()
	{
		//decrease the speed
		speedDownX();
		speedDownY();
	}
	
	/**
	 * Decrease the x-velocity based on speed decrease rate
	 */
	public void speedDownX()
	{
		this.setDX(super.getDX() * SPEED_DECREASE);
	}
	
	/**
	 * Decrease the y-velocity based on speed decrease rate
	 */
	public void speedDownY()
	{
		this.setDY(super.getDY() * SPEED_DECREASE);
	}
	
	/**
	 * Assign the x-velocity
	 */
	public void setDX(final double dx)
	{
		super.setDX(dx);
		
		//make sure we stay within range
		if (getDX() > 0)
		{
			if (getDX() >= SPEED_MAX)
			{
				super.setDX(SPEED_MAX);
			}
			else if (getDX() < SPEED_MIN)
			{
				super.setDX(SPEED_MIN);
			}
		}
		else
		{
			if (-getDX() >= SPEED_MAX)
			{
				super.setDX(-SPEED_MAX);
			}
			else if (-getDX() < SPEED_MIN)
			{
				super.setDX(-SPEED_MIN);
			}
		}
	}
	
	/**
	 * Assign the y-velocity
	 */
	public void setDY(final double dy)
	{
		super.setDY(dy);
		
		//make sure we stay within range
		if  (getDY() > 0)
		{
			if (getDY() >= SPEED_MAX)
			{
				super.setDY(SPEED_MAX);
			}
			else if (getDY() < SPEED_MIN)
			{
				super.setDY(SPEED_MIN);
			}
		}
		else
		{
			if (-getDY() >= SPEED_MAX)
			{
				super.setDY(-SPEED_MAX);
			}
			else if (-getDY() < SPEED_MIN)
			{
				super.setDY(-SPEED_MIN);
			}
		}
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void reset() 
	{
		//flag frozen false
		setFrozen(false);
		
		//flag fire false
		setFire(false);
	}
	
	@Override
	public void update() 
	{
		//if not frozen, lets move the ball
		if (!isFrozen())
		{
			//update location
			super.setX(super.getX() + (getXRatio() * super.getDX()));
			super.setY(super.getY() + super.getDY());
			
			//only count frames if not frozen
			if (hasFire())
			{
				//increase frames
				frames++;
				
				//if we have reached the limit
				if (frames > FIRE_FRAME_LIMIT)
					setFire(false);
			}
		}
	}

	/**
	 * Here we make sure the ball stays within the game bounds
	 */
	public void verifyBounds()
	{
		//make sure the ball stays in bounds
		if (getDX() > 0)
		{
			if (getX() + getWidth() >= GamePanel.WIDTH - Wall.WIDTH)
				setDX(-getDX());
		}
		else if (getDX() < 0)
		{
			if (getX() <= Wall.WIDTH)
				setDX(-getDX());
		}
		
		//make sure the ball stays in bounds
		if (getDY() < 0)
		{
			if (getY() < Wall.HEIGHT)
				setDY(-getDY());
		}
		
		//if the ball is not hidden lets check if it flew off the screen
		if (!isHidden())
		{
			//if the ball goes off the screen let's flag it hidden etc....
			if (getY() >= GamePanel.HEIGHT)
				setHidden(true);
		}
	}
}