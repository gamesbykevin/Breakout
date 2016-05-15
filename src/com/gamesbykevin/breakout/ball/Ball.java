package com.gamesbykevin.breakout.ball;

import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;
import com.gamesbykevin.breakout.panel.GamePanel;
import com.gamesbykevin.breakout.wall.Wall;

import android.graphics.Canvas;

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
	 * The animation dimension on the sprite sheet
	 */
	public static final int DIMENSIONS = 64;
	
	/**
	 * The maximum speed allowed
	 */
	public static final double SPEED_MAX = Brick.HEIGHT;
	
	/**
	 * The minimum speed allowed
	 */
	public static final double SPEED_MIN = 6.15;
	
	/**
	 * The rate at which to increase the speed
	 */
	public static final double SPEED_INCREASE = 1.05; 
	
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
	
	protected Ball(final Game game, final Balls.Key key) 
	{
		super(game, WIDTH, HEIGHT);
		
		//assign key
		setKey(key);
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
	}
	
	/**
	 * 
	 * @return
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
	 * @return The animation key for the ball
	 */
	public Balls.Key getKey()
	{
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
		this.setDX(super.getX() * SPEED_INCREASE);
	}
	
	/**
	 * Increase the y-velocity based on speed increase rate
	 */
	public void speedUpY()
	{
		this.setDY(super.getY() * SPEED_INCREASE);
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
		this.setDX(super.getX() * SPEED_DECREASE);
	}
	
	/**
	 * Decrease the y-velocity based on speed decrease rate
	 */
	public void speedDownY()
	{
		this.setDY(super.getY() * SPEED_DECREASE);
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
			if (getDX() > SPEED_MAX)
				super.setDX(SPEED_MAX);
			if (getDX() < SPEED_MIN)
				super.setDX(SPEED_MIN);
		}
		else
		{
			if (-getDX() > SPEED_MAX)
				super.setDX(-SPEED_MAX);
			if (-getDX() < SPEED_MIN)
				super.setDX(-SPEED_MIN);
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
			if (getDY() > SPEED_MAX)
				super.setDY(SPEED_MAX);
			if (getDY() < SPEED_MIN)
				super.setDY(SPEED_MIN);
		}
		else
		{
			if (-getDY() > SPEED_MAX)
				super.setDY(-SPEED_MAX);
			if (-getDY() < SPEED_MIN)
				super.setDY(-SPEED_MIN);
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
		//anything here?
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
		if (getDY() > 0)
		{
			if (getY() + getHeight() >= GamePanel.HEIGHT - Wall.HEIGHT)
				setDY(-getDY());
		}
		else if (getDY() < 0)
		{
			if (getY() <= Wall.HEIGHT)
				setDY(-getDY());
		}
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}