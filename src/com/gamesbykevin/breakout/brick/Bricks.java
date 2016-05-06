package com.gamesbykevin.breakout.brick;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;

import android.graphics.Canvas;

public class Bricks extends Entity implements ICommon
{
	//array list of bricks
	private Brick[][] bricks;
	
	/**
	 * The different animations for each brick
	 */
	public enum Key
	{
		Yellow, Red, Purple, Silver, Green, Blue
	}
	
	/**
	 * Dimensions of bricks board
	 */
	private static final int COLS = 11;
	
	/**
	 * Dimensions of bricks board
	 */
	private static final int ROWS = 14;

	/**
	 * The starting x-coordinate
	 */
	public static final int START_X = 20;
	
	/**
	 * The starting y-coordinate
	 */
	public static final int START_Y = 20;
	
	public Bricks(final Game game) throws Exception
	{
		super(game, Brick.WIDTH, Brick.HEIGHT);
		
		//create a new array list
		this.bricks = new Brick[ROWS][COLS];
		
		for (int row = 0; row < getBricks().length; row++)
		{
			for (int col = 0; col < getBricks()[0].length; col++)
			{
				//create brick
				getBricks()[row][col] = new Brick(Key.Blue);
				
				//assign correct position
				getBricks()[row][col].setX(START_X + (col * Brick.WIDTH));
				getBricks()[row][col].setY(START_Y + (row * Brick.HEIGHT));
			}
		}
		
		//where animation is located
		final int x = 40;
		
		for (Key key : Key.values())
		{
			int y;
			
			//locate y-coordinate
			switch (key)
			{
				case Yellow:
					y = 64;
					break;
					
				case Red:
					y = 84;
					break;
					
				case Purple:
					y = 104;
					break;
					
				case Silver:
					y = 124;
					break;
					
				case Green:
					y = 144;
					break;
					
				case Blue:
					y = 164;
					break;
					
				default:
					throw new Exception("Key not found: " + key.toString());
			}
			
			//create new animation
			Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), x, y, Brick.WIDTH, Brick.HEIGHT);
			
			//now add animation to the sprite sheet
			super.getSpritesheet().add(key, animation);
		}
	}
	
	@Override
	public void reset() 
	{
		
	}

	/**
	 * Get the bricks array
	 * @return The array of bricks in play
	 */
	public Brick[][] getBricks()
	{
		return this.bricks;
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		for (int row = 0; row < getBricks().length; row++)
		{
			for (int col = 0; col < getBricks()[0].length; col++)
			{
				if (getBricks()[row][col] != null)
					getBricks()[row][col] = null;
			}
		}
		
		this.bricks = null;
	}
	
	@Override
	public void update() throws Exception
	{
		//update bricks?
		for (int row = 0; row < getBricks().length; row++)
		{
			for (int col = 0; col < getBricks()[0].length; col++)
			{
				//skip if it does not exist
				if (getBricks()[row][col] == null)
					continue;
				
				//update brick
				getBricks()[row][col].update();
				
				//if the brick is not dead check it for collision
				if (!getBricks()[row][col].isDead())
				{
					//check each ball for collision with a brick
					for (Ball ball : getGame().getBalls().getBalls())
					{
						//if this ball has collision with the current brick
						if (ball.hasCollision(getBricks()[row][col]))
						{
							//flip y-velocity
							ball.setDY(-ball.getDY());
							
							//assign the brick null
							getBricks()[row][col] = null;
							
							//no need to check the next ball
							break;
						}
					}
				}
			}
		}
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		for (int row = 0; row < getBricks().length; row++)
		{
			for (int col = 0; col < getBricks()[0].length; col++)
			{
				//skip if it does not exist
				if (getBricks()[row][col] == null)
					continue;
				
				//is the brick dead?
				if (!getBricks()[row][col].isDead())
				{
					//position brick
					super.setX(getBricks()[row][col].getX());
					super.setY(getBricks()[row][col].getY());
					
					//assign the appropriate animation
					super.getSpritesheet().setKey(getBricks()[row][col].getKey());
					
					//render brick
					super.render(canvas);
				}
			}
		}
	}
}