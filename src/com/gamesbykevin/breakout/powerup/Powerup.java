package com.gamesbykevin.breakout.powerup;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.panel.GamePanel;

import android.graphics.Canvas;

public class Powerup extends Entity implements ICommon
{
	//different animations for the power ups
	public enum Key
	{
		Magnet, Expand, Shrink, Laser, ExtraLife, ExtraBalls, SpeedUp, SpeedDown, Fireball
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
	public static final double Y_VELOCITY = (Ball.SPEED_MAX * .15);
	
	//the animation key of the power up
	private Key key;
	
	/**
	 * Constructor
	 * @param key Animation key for the power up
	 */
	public Powerup() throws Exception
	{
		super(null, WIDTH, HEIGHT);
		
		//set animation coordinates
		final int x = 0;
		int y = 0;
		
		//map the power up animations
		for (Key key : Key.values())
		{
			switch (key)
			{
				case Magnet:
					y = 404;
					break;
					
				case Expand:
					y = 316;
					break;
					
				case Shrink:
					y = 338;
					break;
					
				case Laser:
					y = 448;
					break;
					
				case ExtraLife:
					y = 426;
					break;
					
				case ExtraBalls:
					y = 360;
					break;
					
				case SpeedUp:
					y = 294;
					break;
					
				case SpeedDown:
					y = 272;
					break;
					
				case Fireball:
					y = 250;
					break;
					
				default:
					throw new Exception("Key not found: " + key.toString());
			}
			
			//create new animation
			Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), x, y, ANIMATION_WIDTH, ANIMATION_HEIGHT, ANIMATION_COLS, ANIMATION_ROWS, ANIMATION_COLS * ANIMATION_ROWS);
			
			//the animation will always loop
			animation.setLoop(true);
			
			//assign the animation delay
			animation.setDelay(ANIMATION_DELAY);
			
			//now add animation to the sprite sheet
			super.getSpritesheet().add(key, animation);
		}
		
		//assign the y-velocity
		super.setDY(Y_VELOCITY);
		
		//reset the power up
		reset();
	}
	
	/**
	 * Get a random key
	 * @return pick a random power up
	 */
	private Key getRandomKey()
	{
		return (Key.values()[GamePanel.RANDOM.nextInt(Key.values().length)]);
	}
	
	/**
	 * Assign the key
	 * @param key The desired animation key
	 */
	public void setKey(final Key key)
	{
		this.key = key;
		
		//assign the proper animation key
		super.getSpritesheet().setKey(getKey());
	}
	
	/**
	 * Get the key
	 * @return The current assigned animation key
	 */
	public Key getKey()
	{
		return this.key;
	}
	
	@Override
	public void update() throws Exception 
	{
		//update animation
		super.getSpritesheet().update();
		
		//drop the power up
		super.setY(super.getY() + super.getDY());
		
		//if the power up fell off the screen we will hide it
		if (super.getY() > GamePanel.HEIGHT)
			setHidden(true);
	}

	@Override
	public void reset() 
	{
		//don't hide the power up
		setHidden(false);
		
		//assign random animation
		this.setKey(getRandomKey());
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//render power up
		super.render(canvas);
	}
}