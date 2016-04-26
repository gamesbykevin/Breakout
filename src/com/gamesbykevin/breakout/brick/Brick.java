package com.gamesbykevin.breakout.brick;

import com.gamesbykevin.breakout.brick.Bricks.Key;
import com.gamesbykevin.breakout.common.ICommon;

import android.graphics.Canvas;

public final class Brick implements ICommon
{
	//is the brick dead?
	private boolean dead = false;
	
	//is this brick not meant to be destroyed?
	private boolean solid = false;
	
	/**
	 * Default width of a brick
	 */
	public static final int WIDTH = 40;
	
	/**
	 * Default height of a brick
	 */
	public static final int HEIGHT = 20;
	
	//animation for the brick
	private Key key;
	
	protected Brick(final Key key) 
	{
		setKey(key);
	}
	
	@Override
	public void reset() 
	{
		
	}
	
	/**
	 * 
	 * @param key
	 */
	protected final void setKey(final Key key)
	{
		this.key = key;
	}
	
	/**
	 * 
	 * @return
	 */
	protected Key getKey()
	{
		return this.key;
	}
	
	/**
	 * Flag the brick dead
	 * @param dead true if dead, false otherwise
	 */
	public void setDead(final boolean dead)
	{
		this.dead = dead;
	}
	
	/**
	 * Is the brick dead?
	 * @return true if dead, false otherwise
	 */
	public boolean isDead()
	{
		return this.dead;
	}
	
	/**
	 * Flag if the brick can be broken
	 * @param solid true if solid and can't be broken, false otherwise
	 */
	public void setSolid(final boolean solid)
	{
		this.solid = solid;
	}
	
	/**
	 * Is the brick solid?
	 * @return true if solid and can't be broken, false otherwise
	 */
	public boolean isSolid()
	{
		return this.solid;
	}

	@Override
	public void dispose() 
	{
		
	}

	@Override
	public void update() throws Exception 
	{
		
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		
	}
}