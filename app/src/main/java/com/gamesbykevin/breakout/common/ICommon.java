package com.gamesbykevin.breakout.common;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.breakout.activity.GameActivity;

import javax.microedition.khronos.opengles.GL10;

public interface ICommon extends Disposable
{
	/**
	 * Update the entity
	 */
	public void update(GameActivity activity);

	/**
	 * Logic to reset
	 */
	public void reset();
	
	/**
	 * Render the entity
	 * @param openGL Object used to render pixels
	 */
	public void render(final GL10 openGL);
}