package com.gamesbykevin.breakout.paddle;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.laser.Lasers;
import com.gamesbykevin.breakout.opengl.OpenGLSurfaceView;
import com.gamesbykevin.breakout.wall.Wall;

import android.graphics.Canvas;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.MANAGER;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.FPS;

public class Paddle extends Entity implements ICommon
{
	
	/**
	 * 100% Touch power (full)
	 */
	public static final float TOUCH_POWER_100 = 1.0f;
	
	/**
	 * 75% Touch power
	 */
	public static final float TOUCH_POWER_75 = 0.75f;

	/**
	 * 50% Touch power
	 */
	public static final float TOUCH_POWER_50 = 0.5f;
	
	/**
	 * 25% Touch power
	 */
	public static final float TOUCH_POWER_25 = 0.25f;
	
	/**
	 * 0% Touch power (none)
	 */
	public static final float TOUCH_POWER_0 = 0f;
	
	/**
	 * Dimensions of paddle
	 */
	public static final int WIDTH = 104;
	
	/**
	 * The amount we can increase/decrease the size of the paddle
	 */
	public static final int WIDTH_CHANGE = 13;
	
	/**
	 * Minimum dimensions of paddle
	 */
	public static final int MIN_WIDTH = 52;
	
	/**
	 * Maximum dimensions of paddle
	 */
	public static final int MAX_WIDTH = 208;
	
	/**
	 * Dimensions of paddle
	 */
	public static final int HEIGHT = 24;

	/**
	 * Default starting coordinate
	 */
	public static final int START_X = (OpenGLSurfaceView.WIDTH / 2) - (WIDTH / 2);
	
	/**
	 * Default starting coordinate
	 */
	public static final int START_Y = OpenGLSurfaceView.HEIGHT - (int)(OpenGLSurfaceView.HEIGHT * .20);
	
	/**
	 * The different ratios to adjust each ball velocity
	 */
	private static final float[] PADDLE_COLLISION_RATIOS = {.1f, .2f, .3f, .4f, .5f};
	
	/**
	 * The velocity adjustment when paddle collision occurs
	 */
	private static final float[] PADDLE_COLLISION_VELOCITY = { 1.35f, 1.15f, 0.75f, 0.5f, 0.0f };

	/**
	 * The different x-coordinates to tell how much we are tilting
	 */
	public static final float[] PADDLE_TILT_COORDINATES = {6.54f, 3.27f, .75f};
	
	/**
	 * The different power depending on the tilt coordinates
	 */
	//public static final float[] PADDLE_TILT_VELOCITY_POWER = {TOUCH_POWER_100, TOUCH_POWER_75, TOUCH_POWER_50};
	public static final float[] PADDLE_TILT_VELOCITY_POWER = {TOUCH_POWER_100, TOUCH_POWER_100, TOUCH_POWER_100};
	
	//our lasers object
	private Lasers lasers;
	
	//does this paddle have magnet capabilities
	private boolean magnet = false;
	
	//does this paddle have the ability to fire lasers
	private boolean laser = false;
	
	//how many frames have elapsed total
	private int framesLaser = 0;
	
	//how many frames have elapsed since last laser fire
	private int framesLaserCurrent = 0;
	
	/**
	 * The delay between each laser fire
	 */
	private static final int FRAMES_LASER_DELAY = (FPS / 2);
	
	/**
	 * How long we can shoot lasers for
	 */
	private static final int FRAMES_LASER_LIMIT = (FPS * 4);
	
	//how many frames have elapsed total
	private int framesMagnet = 0;
	
	/**
	 * How long do we have magnetism for?
	 */
	private static final int FRAMES_MAGNET_LIMIT = (FPS * 30);
	
	//is the paddle moving left
	private boolean left = false;
	
	//is the paddle moving right
	private boolean right = false;
	
	/**
	 * The speed at which the paddle can move
	 */
	private static final double MOVE_VELOCITY = (WIDTH / PADDLE_COLLISION_VELOCITY.length);
	
	//did we touch the screen to move the paddle
	private boolean touch = false;
	
	//the x-coordinate touched
	private float touchX = 0;
	
	//the amount of touch power to apply
	private float touchPower = 1.0f;
	
	/**
	 * The duration to vibrate when the ball hits the paddle
	 */
	private static final long VIBRATE_BALL_COLLISION = 125L;
	
	//flag to play different sound effects
	private boolean soundPaddleCollision, soundPaddleCollisionMagnet, soundLaserFire;   

	/**
	 * Dimension of the cursor
	 */
	private static final int CURSOR_DIMENSION = 84;
	
	/**
	 * Default Constructor
	 */
	public Paddle()
	{
		super(WIDTH, HEIGHT);
		
		//create new lasers object
		this.lasers = new Lasers();
		
		//reset paddle
		reset();
	}
	
	/**
	 * Get the lasers object
	 * @return Lasers object for this paddle that contains all lasers it has fired
	 */
	public Lasers getLasers()
	{
		return this.lasers;
	}
	
	/**
	 * Flag the laser capability
	 * @param laser True if we want the paddle to fire lasers, false otherwise
	 */
	public void setLaser(final boolean laser)
	{
		this.laser = laser;
		
		//if lasers are enabled, reset frames
		if (hasLaser())
		{
			//reset laser frame counts
			this.framesLaser = 0;
			this.framesLaserCurrent = FRAMES_LASER_DELAY;
		}
	}
	
	/**
	 * Does this paddle have laser firing capability?
	 * @return true if the paddle can fire lasers, false otherwise
	 */
	public boolean hasLaser()
	{
		return this.laser;
	}
	
	/**
	 * Set the magnet
	 * @param magnet true = the paddle can catch the balls, false otherwise
	 */
	public void setMagnet(final boolean magnet)
	{
		this.magnet = magnet;
		
		//if we have magnet, reset frame count
		if (hasMagnet())
			this.framesMagnet = 0;
	}
	
	/**
	 * Do we have a magnet?
	 * @return true if the paddle can catch the balls, false otherwise
	 */
	public boolean hasMagnet()
	{
		return this.magnet;
	}
	
	/**
	 * Expand the size of the paddle
	 */
	public void expand()
	{
		//increase width
		super.setWidth(super.getWidth() + WIDTH_CHANGE);
		
		//make sure we don't exceed the max
		if (super.getWidth() > MAX_WIDTH)
			super.setWidth(MAX_WIDTH);
	}
	
	/**
	 * Shrink the size of the paddle
	 */
	public void shrink()
	{
		//decrease width
		super.setWidth(super.getWidth() - WIDTH_CHANGE);
		
		//make sure we don't exceed the minimum
		if (super.getWidth() < MIN_WIDTH)
			super.setWidth(MIN_WIDTH);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		if (this.lasers != null)
		{
			this.lasers.dispose();
			this.lasers = null;
		}
	}
	
	@Override
	public void reset() 
	{
		//remove any lasers
		getLasers().reset();
		
		//expire laser time
		this.framesLaser = FRAMES_LASER_LIMIT;
		
		//expire magnet time
		this.framesMagnet = FRAMES_MAGNET_LIMIT;
		
		//stop the paddle from moving
		setLeft(false);
		setRight(false);
		
		//assign width
		super.setWidth(WIDTH);
		
		//set start location
		super.setX(START_X);
		super.setY(START_Y);
	}
	
	/**
	 * Flag the paddle to move left
	 * @param left true = yes, false = otherwise
	 */
	public void setLeft(final boolean left)
	{
		this.left = left;
	}
	
	/**
	 * Flag the paddle to move right
	 * @param right true = yes, false = otherwise
	 */
	public void setRight(final boolean right)
	{
		this.right = right;
	}
	
	/**
	 * Is the paddle flagged to move left?
	 * @return true = yes, false = no
	 */
	public boolean hasLeft()
	{
		return this.left;
	}
	
	/**
	 * Is the paddle flagged to move right?
	 * @return true = yes, false = no
	 */
	public boolean hasRight()
	{
		return this.right;
	}
	
    /**
     * Flag the user touching the screen.<br>
     * If touch is false the touchX is not stored
     * @param touchX Where did the user touch
     * @param touch Did the user touch?
     * @param touchPower the % of power we applied to the touch 0% - 100%
     */
    public void touch(final float touchX, final boolean touch, final float touchPower)
    {
    	//flag touch true
    	this.touch = touch;
    	
    	//store the touch power ratio
		this.touchPower = touchPower;
		
		//if we are touching store the destination x
    	if (this.touch)
    		this.touchX = touchX;
    }
	
	@Override
	public void update(GameActivity activity)
	{
		//difference between location and touchX
		double xdiff = 0;
		
		//if we touched
		if (this.touch)
		{
			//get middle x-coordinate of the paddle
			final double mx = getX() + (getWidth() / 2);
			
			//determine which direction we move
			if (this.touchX < mx)
			{
				//calculate difference
				xdiff = mx - this.touchX;
				
				//assign moving direction
				setLeft(true);
				setRight(false);
			}
			else if (this.touchX > mx)
			{
				//calculate difference
				xdiff = this.touchX - mx;

				//assign moving direction
				setLeft(false);
				setRight(true);
			}
		}
		else
		{
			setLeft(false);
			setRight(false);
		}
		
		//if moving and close enough to the destination we can place at the location
		if ((hasRight() || hasLeft()) && xdiff < MOVE_VELOCITY * this.touchPower)
		{
			//place at coordinate
			this.setX(touchX - (getWidth() / 2));
			
			//stop moving
			setLeft(false);
			setRight(false);
		}
		else
		{
			if (hasLeft())
				this.setX(getX() - (MOVE_VELOCITY * this.touchPower));
			if (hasRight())
				this.setX(getX() + (MOVE_VELOCITY * this.touchPower));
		}

		//flag false first
		soundPaddleCollision = false;
		soundPaddleCollisionMagnet = false;
		soundLaserFire = false;
		
		//check each ball for paddle collision
		for (Ball ball : MANAGER.getBalls().getBalls())
		{
			//only check balls that are moving south
			if (ball.getDY() < 0)
				continue;
			
			//don't check balls that are frozen
			if (ball.isFrozen())
				continue;
			
			//check first for collision with the ball
			if (hasCollision(ball))
			{
				//if the ball has moved past the paddle
				if (ball.getY() > getY() + (getHeight() / 2))
					continue;
				
				//play sound effect
				soundPaddleCollision = true;
				
				//switch ball y-velocity
				ball.setDY(-ball.getDY());
				
				//the middle x-coordinate
				double x = ball.getX() + (ball.getWidth() * .5);
				
				//determine how to adjust the ball velocity
				for (int i = 0; i < PADDLE_COLLISION_RATIOS.length; i++)
				{
					//check if these edges where part of collision
					if (x <= getX() + (getWidth() * PADDLE_COLLISION_RATIOS[i]))
					{
						//set the velocity adjustment accordingly
						ball.setXRatio(PADDLE_COLLISION_VELOCITY[i]);
						
						//if heading east, switch directions
						if (ball.getDX() > 0)
							ball.setDX(-ball.getDX());
						
						//no need to continue, since there was collision
						break;
					}
					else if (x >= getX() + (getWidth() * (1.0f - PADDLE_COLLISION_RATIOS[i])))
					{
						//set the velocity adjustment accordingly
						ball.setXRatio(PADDLE_COLLISION_VELOCITY[i]);
						
						//if heading west, switch directions
						if (ball.getDX() < 0)
							ball.setDX(-ball.getDX());
						
						//no need to continue, since there was collision
						break;
					}
				}
				
				//if the paddle is a magnet then we freeze the ball
				if (hasMagnet())
				{
					//flag to play sound effect
					soundPaddleCollisionMagnet = true;
					
					//freeze the ball
					ball.setFrozen(true);
					
					//set x-offset
					ball.setOffsetX(ball.getX() - this.getX());
				}
				else
				{
					//flag to play sound effect
					soundPaddleCollision = true;
					
					//vibrate when a ball hits the paddle
					activity.vibrate();
				}
			}
		}
		
		//update the lasers object
		getLasers().update(activity);
		
		//track how long we have magnetism
		if (hasMagnet())
		{
			//increase frames
			this.framesMagnet++;
			
			//if we reached the limit, time over
			if (this.framesMagnet > FRAMES_MAGNET_LIMIT)
				setMagnet(false);
		}
		
		//if we have the capability to fire
		if (hasLaser())
		{
			//increase frames
			this.framesLaser++;
			
			//increase frames since last laser fire
			this.framesLaserCurrent++;
			
			//if we have exceeded the limit stop firing lasers
			if (this.framesLaser > FRAMES_LASER_LIMIT)
			{
				this.setLaser(false);
			}
			else
			{
				//if enough time has lapsed fire more lasers
				if (this.framesLaserCurrent >= FRAMES_LASER_DELAY)
				{
					//flag true to play sound effect
					soundLaserFire = true;
					
					//reset current count
					this.framesLaserCurrent = 0;
					
					//fire more lasers
					getLasers().addLasers(this);
				}
			}
		}
		
		//play sound effects accordingly
		if (soundPaddleCollision)
			activity.playSound(R.raw.paddlecollision);
		if (soundPaddleCollisionMagnet)
			activity.playSound(R.raw.paddlecatch);
		if (soundLaserFire)
			activity.playSound(R.raw.laser);
	}
	
	@Override
	public void setX(final double x)
	{
		//calculate the new x-coordinate
		double nx = x; //- (getWidth() / 2);
		
		//keep the paddle in bounds
		if (nx < Wall.WIDTH)
			nx = Wall.WIDTH;
		if (nx > OpenGLSurfaceView.WIDTH - Wall.WIDTH - getWidth())
			nx = OpenGLSurfaceView.WIDTH - Wall.WIDTH - getWidth();
		
		//update x-coordinate
		super.setX(nx);
		
		//check if we need to update any frozen balls
		for (Ball ball : MANAGER.getBalls().getBalls())
		{
			//if the ball is frozen we will update the x-coordinate
			if (ball.isFrozen())
				ball.setX(this.getX() + ball.getOffsetX());
		}
	}
	
	@Override
	public void render(GL10 openGL)
	{
		//render the paddle
		super.render(openGL);
		
		//render any lasers
		getLasers().render(openGL);
	}
}