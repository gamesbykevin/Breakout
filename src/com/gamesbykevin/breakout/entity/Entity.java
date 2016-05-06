package com.gamesbykevin.breakout.entity;

import com.gamesbykevin.breakout.game.Game;

import android.graphics.Canvas;

public abstract class Entity extends com.gamesbykevin.androidframework.base.Entity 
{
	/**
	 * Default animation
	 */
	protected static final String DEFAULT = "Default";
	
	//our game reference object
	private final Game game;
	
	/**
	 * Default constructor
	 */
	public Entity(final Game game, final int width, final int height) 
	{
		super();
		
		//our game reference object
		this.game = game;
		
		//assign dimensions
		super.setWidth(width);
		
		//assign dimensions
		super.setHeight(height);
	}
	
	/**
	 * Get the game reference object
	 * @return Object containing all game elements
	 */
	protected Game getGame()
	{
		return this.game;
	}
	
	/**
	 * Each child should have logic to update
	 */
	public abstract void update() throws Exception;
	
	/**
	 * Is there collision?
	 * @param entity The entity we want to check collision with
	 * @return true if the entities intersect, false otherwise
	 */
	public boolean hasCollision(final Entity entity)
	{
		//if this entity is not located in range of the other entity we can't have collision 
		if (getX() + getWidth() < entity.getX())
			return false;
		if (getX() > entity.getX() + entity.getWidth())
			return false;
		if (getY() + getHeight() < entity.getY())
			return false;
		if (getY() > entity.getY() + entity.getHeight())
			return false;
		
		//we have collision
		return true;
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}