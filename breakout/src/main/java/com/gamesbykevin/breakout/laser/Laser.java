package com.gamesbykevin.breakout.laser;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.wall.Wall;

import android.graphics.Canvas;

public final class Laser extends Entity
{
	/**
	 * Width of the laser
	 */
	public static final int WIDTH = 9;
	
	/**
	 * Width of the laser
	 */
	public static final int HEIGHT = 54;

	/**
	 * The rate at which the laser can move
	 */
	private static final double Y_VELOCITY = (Brick.HEIGHT_NORMAL * .75); 
	
	protected Laser(final double x, final double y) 
	{
		super(null, WIDTH, HEIGHT);
		
		super.setX(x);
		super.setY(y);
		
		//don't hide the laser by default
		super.setHidden(false);
	}

	@Override
	public void update() throws Exception 
	{
		super.setY(super.getY() - Y_VELOCITY);
		
		//if off the screen this laser will be hidden
		if (super.getY() < Wall.HEIGHT)
			setHidden(true);
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas, Images.getImage(Assets.ImageGameKey.LaserRed));
	}
}