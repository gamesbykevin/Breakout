package com.gamesbykevin.breakout.wall;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.panel.GamePanel;

import android.graphics.Canvas;

public class Wall extends Entity 
{
	/**
	 * The dimensions of a wall
	 */
	public static final int WIDTH = 20;
	
	/**
	 * The dimensions of a wall
	 */
	public static final int HEIGHT = 20;

	/**
	 * Number of columns 
	 */
	private static final int COLS = (GamePanel.WIDTH / WIDTH);
	
	/**
	 * Number of rows
	 */
	private static final int ROWS = (GamePanel.HEIGHT / HEIGHT);
	
	public enum Key
	{
		Green, Blue, Red, Purple
	}
	
	/**
	 * Default constructor
	 */
	public Wall() throws Exception
	{
		super(WIDTH, HEIGHT);
		
		//x-coordinate
		final int x = 0;
		
		for (Key key : Key.values())
		{
			int y;
			
			switch (key)
			{
				case Green:
					y = 64;
					break;
					
				case Blue:
					y = 84;
					break;
					
				case Red:
					y = 104;
					break;
					
				case Purple:
					y = 124;
					break;
					
				default:
					throw new Exception("Key not found: " + key.toString());
			}
			
			//create new animation
			Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), x, y, WIDTH, HEIGHT);
			
			//now add animation to the sprite sheet
			super.getSpritesheet().add(key, animation);
		}
		
		//pick random wall
		super.getSpritesheet().setKey(Key.values()[GamePanel.RANDOM.nextInt(Key.values().length)]);
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void update()
	{
		
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		for (int col = 0; col < COLS; col++)
		{
			//render top wall
			super.setX(col * WIDTH);
			super.setY(0);
			super.render(canvas);
			
			//render bottom wall
			//super.setX(col * WIDTH);
			//super.setY(GamePanel.HEIGHT - HEIGHT);
			//super.render(canvas);
		}
		
		for (int row = 1; row < ROWS; row++)
		{
			//render left wall
			super.setX(0);
			super.setY(row * HEIGHT);
			super.render(canvas);
			
			//render right wall
			super.setX(GamePanel.WIDTH - WIDTH);
			super.setY(row * HEIGHT);
			super.render(canvas);
		}
	}
}