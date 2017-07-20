package com.gamesbykevin.breakout.wall;

import com.gamesbykevin.androidframework.resources.Images;

import android.graphics.Canvas;

public class Wall 
{
	/**
	 * The dimensions of a wall
	 */
	public static final int WIDTH = 20;
	
	/**
	 * The dimensions of a wall
	 */
	public static final int HEIGHT = 20;
	
	/**
	 * Default constructor
	 */
	public Wall()
	{
		//need to do anything here?
	}
	
	/**
	 * Render the wall image
	 * @param canvas
	 * @throws Exception
	 */
	public void render(final Canvas canvas) throws Exception
	{
		canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.Border), 0, 0, null);
	}
}