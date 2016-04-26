package com.gamesbykevin.breakout.ball;

import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;

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
	private static final double SPEED_MAX = 5.0;
	
	/**
	 * The minimum speed allowed
	 */
	private static final double SPEED_MIN = 1.25;
	
	/**
	 * The rate at which to increase the speed
	 */
	private static final double SPEED_INCREASE = 1.05; 
	
	/**
	 * The rate at which to decrease the speed
	 */
	private static final double SPEED_DECREASE = 0.95;
	
	//assign the animation type
	private Balls.Key key;
	
	protected Ball(final Balls.Key key) 
	{
		super(WIDTH, HEIGHT);
		
		//assign key
		setKey(key);
	}
	
	/**
	 * 
	 * @param key
	 */
	public void setKey(final Balls.Key key)
	{
		this.key = key;
	}
	
	/**
	 * 
	 * @return
	 */
	public Balls.Key getKey()
	{
		return this.key;
	}
	
	/**
	 * Speed up the ball speed
	 */
	public void speedUp()
	{
		//increase the speed
		this.setDX(super.getX() * SPEED_INCREASE);
		this.setDY(super.getY() * SPEED_INCREASE);
	}
	
	/**
	 * Speed down the ball speed
	 */
	public void speedDown()
	{
		//increase the speed
		this.setDX(super.getX() * SPEED_DECREASE);
		this.setDY(super.getY() * SPEED_DECREASE);
	}
	
	/**
	 * Assign the x-velocity
	 */
	public void setDX(final double dx)
	{
		super.setDX(dx);
		
		//make sure we stay within range
		if (getDX() > SPEED_MAX)
			super.setDX(SPEED_MAX);
		if (getDX() < SPEED_MIN)
			super.setDX(SPEED_MIN);
	}
	
	/**
	 * Assign the y-velocity
	 */
	public void setDY(final double dy)
	{
		super.setDY(dy);
		
		//make sure we stay within range
		if (getDY() > SPEED_MAX)
			super.setDY(SPEED_MAX);
		if (getDY() < SPEED_MIN)
			super.setDY(SPEED_MIN);
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
		super.setX(super.getX() + super.getDX());
		super.setY(super.getY() + super.getDY());
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}