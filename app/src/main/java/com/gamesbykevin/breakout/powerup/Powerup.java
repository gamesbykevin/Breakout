package com.gamesbykevin.breakout.powerup;

import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.opengl.OpenGLSurfaceView;

import android.graphics.Canvas;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.FPS;
import static com.gamesbykevin.breakout.opengl.Textures.IDS;

public class Powerup extends Entity implements ICommon
{
	//different animations for the power ups
	public enum Key
	{
		Magnet(404), Expand(316), Shrink(338),
		Laser(448), ExtraLife(426), ExtraBalls(360),
		SpeedUp(294), SpeedDown(272), Fireball(250);

		private final int y;

		private int indexStart = 0;

		Key(int y) {
			this.y = y;
		}

		public int getY() {
			return this.y;
		}

		public void setIndexStart(int indexStart) {
			this.indexStart = indexStart;
		}

		public int getIndexStart() {
			return this.indexStart;
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

	//keep track of the elapsed frames
	private int frames = 0;

	//how many frames until we switch to the next animation
	private static final int ANIMATION_DELAY = (FPS / 8);

	//the current position
	private int index = 0;

	//number of animation images
	private static final int ANIMATION_COUNT = 8;

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
		//increase the frame delay
		frames++;

		//if enough time elapsed
		if (frames >= ANIMATION_DELAY) {

			//reset the count
			frames = 0;

			//go to the next animation
			index++;

			//restart animation when over
			if (index >= ANIMATION_COUNT)
				index = 0;
		}

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
	public void render(GL10 openGL)
	{
		//assign the animation
		super.setTextureId(IDS[getKey().getIndexStart() + index]);

		//render power up
		super.render(openGL);
	}
}