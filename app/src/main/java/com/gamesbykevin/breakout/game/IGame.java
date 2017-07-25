package com.gamesbykevin.breakout.game;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Disposable;

import javax.microedition.khronos.opengles.GL10;

/**
 * Game interface methods
 * @author GOD
 */
public interface IGame extends Disposable
{
    /**
     * Logic to update element
     */
    public void update() throws Exception;
    
    /**
     * Update the game based on a motion event
     * @param action The action of the MotionEvent
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void update(final int action, final float x, final float y) throws Exception;
}
