package com.gamesbykevin.breakout.laser;

import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.opengl.Textures;
import com.gamesbykevin.breakout.wall.Wall;

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
		super(WIDTH, HEIGHT);
		
		super.setX(x);
		super.setY(y);
		
		//don't hide the laser by default
		super.setHidden(false);

		//assign the animation
		super.setTextureId(Textures.TEXTURE_ID_LASER);
	}

	public void update()
	{
		super.setY(super.getY() - Y_VELOCITY);
		
		//if off the screen this laser will be hidden
		if (super.getY() < Wall.HEIGHT)
			setHidden(true);
	}
}