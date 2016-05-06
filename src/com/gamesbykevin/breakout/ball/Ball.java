package com.gamesbykevin.breakout.ball;

import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;

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
	public static final double SPEED_MAX = 10.0;
	
	/**
	 * The minimum speed allowed
	 */
	public static final double SPEED_MIN = 5.25;
	
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
	
	protected Ball(final Game game, final Balls.Key key) 
	{
		super(game, WIDTH, HEIGHT);
		
		//assign key
		setKey(key);
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
		
	}
	
	@Override
	public void update() 
	{
		//update location
		super.setX(super.getX() + (getXRatio() * super.getDX()));
		super.setY(super.getY() + super.getDY());
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}