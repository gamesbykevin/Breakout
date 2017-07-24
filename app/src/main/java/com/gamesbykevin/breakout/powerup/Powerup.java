package com.gamesbykevin.breakout.powerup;

import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.opengl.OpenGLSurfaceView;

import android.graphics.Canvas;

public class Powerup extends Entity implements ICommon
{
	//different animations for the power ups
	public enum Key
	{
		Magnet(404), Expand(316), Shrink(338),
		Laser(448), ExtraLife(426), ExtraBalls(360),
		SpeedUp(294), SpeedDown(272), Fireball(250);

		private final int y;

		Key(int y) {
			this.y = y;
		}

		public int getY() {
			return this.y;
		}
	}
	
	/**
	 * Default width of a power up animation
	 */
	public static final int ANIMATION_WIDTH = 44;
	
	/**
	 * Default height of a power up animation
	 */
	public static final int ANIMATION_HEIGHT = 22;

	/**
	 * The number of columns we need to map the animation
	 */
	private static final int ANIMATION_COLS = 8;
	
	/**
	 * The number of rows we need to map the animation
	 */
	private static final int ANIMATION_ROWS = 1;
	
	/**
	 * The animation delay
	 */
	private static final long ANIMATION_DELAY = 75L;
	
	/**
	 * Default width of a power up
	 */
	public static final int WIDTH = 40;
	
	/**
	 * Default height of a power up
	 */
	public static final int HEIGHT = 20;

	/**
	 * The rate at which the power up falls
	 */
	public static final double Y_VELOCITY = (Brick.HEIGHT_NORMAL * .25);

	/**
	 * Default constructor
	 */
	public Powerup()
	{
		super(WIDTH, HEIGHT);

		//assign the y-velocity
		super.setDY(Y_VELOCITY);
		
		//reset the power up
		reset();
	}

	public Key getKey() {
		return Key.Expand;
	}
	
	@Override
	public void update(GameActivity activity)
	{
		//update animation
		super.getSpritesheet().update();
		
		//drop the power up
		super.setY(super.getY() + super.getDY());
		
		//if the power up fell off the screen we will hide it
		if (super.getY() > OpenGLSurfaceView.HEIGHT)
			setHidden(true);
	}

	@Override
	public void reset() 
	{
		//don't hide the power up
		setHidden(false);
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//render power up
		super.render(canvas);
	}
}