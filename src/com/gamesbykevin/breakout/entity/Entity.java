package com.gamesbykevin.breakout.entity;

import android.graphics.Canvas;

public abstract class Entity extends com.gamesbykevin.androidframework.base.Entity 
{
	/**
	 * Default animation
	 */
	protected static final String DEFAULT = "Default";
	
	/**
	 * Default constructor
	 */
	public Entity(final int width, final int height) 
	{
		super();
		
		//assign dimensions
		super.setWidth(width);
		
		//assign dimensions
		super.setHeight(height);
	}
	
	/**
	 * Each child should have logic to update
	 */
	public abstract void update();
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}