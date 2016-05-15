package com.gamesbykevin.breakout.powerup;

import com.gamesbykevin.breakout.powerup.Powerups.Key;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.panel.GamePanel;

public class Powerup extends Entity implements ICommon
{
	//which power up is this?
	private Key key;
	
	/**
	 * Default width of a power up
	 */
	public static final int WIDTH = 20;
	
	/**
	 * Default height of a power up
	 */
	public static final int HEIGHT = 10;

	/**
	 * The rate at which the power up falls
	 */
	public static final double Y_VELOCITY = (Ball.SPEED_MAX * .33);
	
	/**
	 * Constructor
	 * @param key Animation key for the power up
	 */
	public Powerup(final Key key) 
	{
		super(null, WIDTH, HEIGHT);
		
		//assign power up key
		setKey(key);
		
		//assign the y-velocity
		super.setDY(Y_VELOCITY);
		
		//reset the power up
		reset();
	}

	/**
	 * Assign the power up animation key 
	 * @param powerupKey Assign the animation key if a power up
	 */
	public void setKey(final Key key)
	{
		this.key = key;
	}
	
	/**
	 * Get the power up animation key
	 * @return The power up animation key
	 */
	public Key getKey()
	{
		return this.key;
	}
	
	@Override
	public void update() throws Exception 
	{
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
	}
}