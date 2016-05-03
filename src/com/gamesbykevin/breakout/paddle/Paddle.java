package com.gamesbykevin.breakout.paddle;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.panel.GamePanel;
import com.gamesbykevin.breakout.wall.Wall;

import android.graphics.Canvas;

public class Paddle extends Entity implements ICommon
{
	/**
	 * Dimensions of paddle
	 */
	public static final int WIDTH = 104;
	
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
	public static final int START_Y = GamePanel.HEIGHT - (GamePanel.HEIGHT / 8);
	
	public Paddle() 
	{
		super(WIDTH, HEIGHT);
		
		//set start location
		super.setX(START_X);
		super.setY(START_Y);
		
		//create animation
		Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), 80, 64, WIDTH, HEIGHT);
		
		//now add animation to the sprite sheet
		super.getSpritesheet().add(DEFAULT, animation);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void reset() 
	{
		
	}
	
	@Override
	public void update()
	{
		
	}
	
	@Override
	public void setX(final double x)
	{
		//calculate the new x-coordinate
		double nx = x - (getWidth() / 2);
		
		//keep the paddle in bounds
		if (nx < Wall.WIDTH)
			nx = Wall.WIDTH;
		if (nx > GamePanel.WIDTH - Wall.WIDTH - getWidth())
			nx = GamePanel.WIDTH - Wall.WIDTH - getWidth();
		
		//update x-coordinate
		super.setX(nx);
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}