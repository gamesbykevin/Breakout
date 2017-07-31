package com.gamesbykevin.breakout.ball;

import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.opengl.OpenGLSurfaceView;
import com.gamesbykevin.breakout.opengl.Textures;
import com.gamesbykevin.breakout.wall.Wall;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.FPS;
import static com.gamesbykevin.breakout.opengl.Textures.TEXTURE_ID_FIREBALL;

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
	 * The starting x-coordinate for the ball
	 */
	public static final int START_X = (OpenGLSurfaceView.WIDTH / 2);
	
	/**
	 * The starting y-coordinate for the ball
	 */
	public static final int START_Y = (OpenGLSurfaceView.HEIGHT / 2) - HEIGHT;
	
	/**
	 * The animation dimension on the sprite sheet
	 */
	public static final int DIMENSIONS = 64;
	
	/**
	 * The maximum speed allowed
	 */
	public static double SPEED_MAX = Brick.HEIGHT_NORMAL;
	
	/**
	 * The minimum speed allowed
	 */
	public static final int SPEED_MIN = 4;
	
	/**
	 * The rate at which to increase the speed
	 */
	public static final int SPEED_INCREASE = 1;

	/**
	 * The rate at which to decrease the speed
	 */
	public static final int SPEED_DECREASE = 1;

	//store the x-ratio for paddle collision
	private double xratio = 1.0;
	
	//is the ball frozen
	private boolean frozen = false;
	
	//is this a fire ball
	private boolean fire = false;
	
	//store the x-offset
	private int offsetX;

	//how much do we change the angle every update
	private static final float ANGLE_CHANGE = 18.0f;

	//the frame count that the ball has been on fire
	private int frames = 0;

	/**
	 * The number of frames to keep the ball on fire
	 */
	private static final int FIRE_FRAME_LIMIT = (FPS * 20);

	protected Ball()
	{
		super(WIDTH, HEIGHT);
	}
	
	/**
	 * Assign the x-offset
	 * @param offsetX The amount of pixels from the x-coordinate for the paddle
	 */
	public void setOffsetX(final double offsetX)
	{
		this.offsetX = (int)offsetX;
	}
	
	/**
	 * Get the x-offset
	 * @return The amount of pixels from the x-coordinate for the paddle
	 */
	public int getOffsetX()
	{
		return this.offsetX;
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

		//flag this ball rotating or not
		super.setRotation(fire);

		//if fire is enabled reset frame count
		if (hasFire()) {
			this.frames = 0;
		} else {
			//make sure there is no rotation
			setAngle(0.0f);
		}
	}
	
	/**
	 * Does the ball have fire?
	 * @return true = yes, false = no
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
		this.setDX(super.getDX() + SPEED_INCREASE);
	}
	
	/**
	 * Increase the y-velocity based on speed increase rate
	 */
	public void speedUpY()
	{
		this.setDY(super.getDY() + SPEED_INCREASE);
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
		this.setDX(super.getDX() * SPEED_DECREASE);
	}
	
	/**
	 * Decrease the y-velocity based on speed decrease rate
	 */
	public void speedDownY()
	{
		this.setDY(super.getDY() * SPEED_DECREASE);
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
			if (getDX() >= SPEED_MAX)
			{
				super.setDX(SPEED_MAX);
			}
			else if (getDX() < SPEED_MIN)
			{
				super.setDX(SPEED_MIN);
			}
		}
		else
		{
			if (-getDX() >= SPEED_MAX)
			{
				super.setDX(-SPEED_MAX);
			}
			else if (-getDX() < SPEED_MIN)
			{
				super.setDX(-SPEED_MIN);
			}
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
			if (getDY() >= SPEED_MAX)
			{
				super.setDY(SPEED_MAX);
			}
			else if (getDY() < SPEED_MIN)
			{
				super.setDY(SPEED_MIN);
			}
		}
		else
		{
			if (-getDY() >= SPEED_MAX)
			{
				super.setDY(-SPEED_MAX);
			}
			else if (-getDY() < SPEED_MIN)
			{
				super.setDY(-SPEED_MIN);
			}
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
		//flag frozen false
		setFrozen(false);
		
		//flag fire false
		setFire(false);
	}
	
	@Override
	public void update(GameActivity activity)
	{
		//if not frozen, lets move the ball
		if (!isFrozen())
		{
			//update location
			super.setX(super.getX() + (getXRatio() * super.getDX()));
			super.setY(super.getY() + super.getDY());

			//only count frames if not frozen
			if (hasFire())
			{
				//rotate the fireball
				setAngle(getAngle() + ANGLE_CHANGE);

				if (getAngle() > 360.0f)
					setAngle(0.0f);

				//increase frames
				frames++;
				
				//if we have reached the limit
				if (frames > FIRE_FRAME_LIMIT) {
					setFire(false);
					setAngle(0.0f);
				}
			}
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
			if (getX() + getWidth() >= OpenGLSurfaceView.WIDTH - Wall.WIDTH)
				setDX(-getDX());
		}
		else if (getDX() < 0)
		{
			if (getX() <= Wall.WIDTH)
				setDX(-getDX());
		}
		
		//make sure the ball stays in bounds
		if (getDY() < 0)
		{
			if (getY() < Wall.HEIGHT)
				setDY(-getDY());
		}
		
		//if the ball is not hidden lets check if it flew off the screen
		if (!isHidden())
		{
			//if the ball goes off the screen let's flag it hidden etc....
			if (getY() >= OpenGLSurfaceView.HEIGHT)
				setHidden(true);
		}
	}

	public void render(GL10 openGL) {

		//don't show if we are hiding
		if (isHidden())
			return;

		//do we render a fireball
		if (hasFire()) {

			//save the texture id
			final int textureId = getTextureId();

			//assign the correct texture
			super.setTextureId(TEXTURE_ID_FIREBALL);

			//render the fireball
			super.render(openGL);

			//restore original value
			super.setTextureId(textureId);

		} else {

			//render the ball
			super.render(openGL);
		}
	}
}