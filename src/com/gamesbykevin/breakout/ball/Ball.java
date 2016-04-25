package com.gamesbykevin.breakout.ball;

import com.gamesbykevin.breakout.entity.Entity;

import android.graphics.Canvas;

public class Ball extends Entity 
{
	/**
	 * Dimensions of ball
	 */
	public static final int WIDTH = 16;
	
	/**
	 * Dimensions of ball
	 */
	public static final int HEIGHT = 16;
	
	public Ball() 
	{
		super(WIDTH, HEIGHT);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void update() 
	{
		
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}