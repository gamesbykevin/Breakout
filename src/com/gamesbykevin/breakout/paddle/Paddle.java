package com.gamesbykevin.breakout.paddle;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;
import com.gamesbykevin.breakout.laser.Lasers;
import com.gamesbykevin.breakout.panel.GamePanel;
import com.gamesbykevin.breakout.thread.MainThread;
import com.gamesbykevin.breakout.wall.Wall;

import android.graphics.Canvas;

public class Paddle extends Entity implements ICommon
{
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
	public static final int START_X = (GamePanel.WIDTH / 2) - (WIDTH / 2);
	
	/**
	 * Default starting coordinate
	 */
	public static final int START_Y = GamePanel.HEIGHT - (GamePanel.HEIGHT / 6);
	
	/**
	 * Adjust the x-velocity based on where the paddle is
	 */
	public static final double PADDLE_COLLISION_FAR = 1.25;
	
	/**
	 * Adjust the x-velocity based on where the paddle is
	 */
	public static final double PADDLE_COLLISION_CLOSE = 0.75;
	
	/**
	 * Adjust the x-velocity based on where the paddle is
	 */
	public static final double PADDLE_COLLISION_MIDDLE = 0.0;
	
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
	private static final int FRAMES_LASER_DELAY = (MainThread.FPS / 2);
	
	/**
	 * How long we can shoot lasers for
	 */
	private static final int FRAMES_LASER_LIMIT = (MainThread.FPS * 4);
	
	//how many frames have elapsed total
	private int framesMagnet = 0;
	
	/**
	 * How long do we have magnetism for?
	 */
	private static final int FRAMES_MAGNET_LIMIT = (MainThread.FPS * 30);
	
	//is the paddle moving left
	private boolean left = false;
	
	//is the paddle moving right
	private boolean right = false;
	
	/**
	 * The speed at which the paddle can move
	 */
	private static final double MOVE_VELOCITY = 8.5;
	
	//did we touch the screen to move the paddle
	private boolean touch = false;
	
	//the x-coordinate touched
	private float touchX = 0;
	
	/**
	 * Default Constructor
	 * @param game Game reference object
	 */
	public Paddle(final Game game)
	{
		super(game, WIDTH, HEIGHT);
		
		//create new lasers object
		this.lasers = new Lasers(game);
		
		//set start location
		super.setX(START_X);
		super.setY(START_Y);
		
		//create animation
		Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), 80, 64, WIDTH, HEIGHT);
		
		//now add animation to the sprite sheet
		super.getSpritesheet().add(DEFAULT, animation);
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
		getLasers().reset();
		
		//stop the paddle from moving
		setLeft(false);
		setRight(false);
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
     */
    public void touch(final float touchX, final boolean touch)
    {
    	this.touch = touch;
    	
    	if (this.touch)
    		this.touchX = touchX;
    }
	
	@Override
	public void update() throws Exception
	{
		
		//if we touched
		if (this.touch)
		{
			//get middle x-coordinate of the paddle
			final double mx = getX() + (getWidth() / 2);
			
			//determine which direction we move
			if (this.touchX < mx)
			{
				setLeft(true);
				setRight(false);
			}
			else if (this.touchX > mx)
			{
				setLeft(false);
				setRight(true);
			}
		}
		else
		{
			setLeft(false);
			setRight(false);
		}
		
		if (hasLeft())
			this.setX(getX() - MOVE_VELOCITY);
		if (hasRight())
			this.setX(getX() + MOVE_VELOCITY);
		
		//check each ball for paddle collision
		for (Ball ball : getGame().getBalls().getBalls())
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
				
				//switch ball y-velocity
				ball.setDY(-ball.getDY());
				
				//the middle x-coordinate
				double x = ball.getX() + (ball.getWidth() * .5);
				
				//now check where the collision occurred
				if (x < getX() + (getWidth() * .2))
				{
					//move max west
					ball.setXRatio(PADDLE_COLLISION_FAR);
					
					//make sure correct direction
					if (ball.getDX() > 0)
						ball.setDX(-ball.getDX());
				}
				else if (x >= getX() + (getWidth() * .8))
				{
					//move max east
					ball.setXRatio(PADDLE_COLLISION_FAR);
					
					//make sure correct direction
					if (ball.getDX() < 0)
						ball.setDX(-ball.getDX());
				}
				else if (x >= getX() + (getWidth() * .6))
				{
					//move slightly east
					ball.setXRatio(PADDLE_COLLISION_CLOSE);
					
					//make sure correct direction
					if (ball.getDX() < 0)
						ball.setDX(-ball.getDX());
				}
				else if (x >= getX() + (getWidth() * .4))
				{
					//move straight
					ball.setXRatio(PADDLE_COLLISION_MIDDLE);
				}
				else if (x >= getX() + (getWidth() * .2))
				{
					//move slightly west
					ball.setXRatio(PADDLE_COLLISION_CLOSE);
					
					//make sure correct direction
					if (ball.getDX() > 0)
						ball.setDX(-ball.getDX());
				}
				
				//if the paddle is a magnet then we freeze the ball
				if (hasMagnet())
				{
					//freeze the ball
					ball.setFrozen(true);
					
					//set x-offset
					ball.setOffsetX(ball.getX() - this.getX());
				}
			}
		}
		
		//update the lasers object
		getLasers().update();
		
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
					//reset current count
					this.framesLaserCurrent = 0;
					
					//fire more lasers
					getLasers().addLasers(this);
				}
			}
		}
	}
	
	@Override
	public void setX(final double x)
	{
		//calculate the new x-coordinate
		double nx = x; //- (getWidth() / 2);
		
		//keep the paddle in bounds
		if (nx < Wall.WIDTH)
			nx = Wall.WIDTH;
		if (nx > GamePanel.WIDTH - Wall.WIDTH - getWidth())
			nx = GamePanel.WIDTH - Wall.WIDTH - getWidth();
		
		//update x-coordinate
		super.setX(nx);
		
		//check if we need to update any frozen balls
		for (Ball ball : getGame().getBalls().getBalls())
		{
			//if the ball is frozen we will update the x-coordinate
			if (ball.isFrozen())
				ball.setX(this.getX() + ball.getOffsetX());
		}
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//render the paddle
		super.render(canvas);
		
		//render any lasers
		getLasers().render(canvas);
	}
}