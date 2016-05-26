package com.gamesbykevin.breakout.brick;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
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
	public static final int COLS = 11;
	
	/**
	 * Dimensions of bricks board
	 */
	public static final int ROWS = 18;

	/**
	 * The starting x-coordinate
	 */
	public static final int START_X = 20;
	
	/**
	 * The starting y-coordinate
	 */
	public static final int START_Y = 20;
	
	//did the player eliminate the bricks in play
	private boolean win = false;
	
	public Bricks(final Game game) throws Exception
	{
		super(game, Brick.WIDTH, Brick.HEIGHT);
		
		//create a new array list for the bricks
		this.bricks = new Brick[ROWS][COLS];
		
		//reset all bricks
		reset();
		
		//where animation is located
		int x = 40;
		int y = 0;
		
		for (Key key : Key.values())
		{
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
	public final void reset() 
	{
		//create a brick at every position and mark dead (to start)
		for (int row = 0; row < getBricks().length; row++)
		{
			for (int col = 0; col < getBricks()[0].length; col++)
			{
				//if a brick does not exist we will create it
				if (getBricks()[row][col] == null)
				{
					//create brick
					getBricks()[row][col] = new Brick(Key.Blue);
					
					//assign correct position
					getBricks()[row][col].setX(START_X + (col * Brick.WIDTH));
					getBricks()[row][col].setY(START_Y + (row * Brick.HEIGHT));
				}
				
				//flag every brick as dead to start
				getBricks()[row][col].setDead(true);
			}
		}
	}

	/**
	 * Get the bricks array
	 * @return The array of bricks in play
	 */
	public Brick[][] getBricks()
	{
		return this.bricks;
	}
	
	/**
	 * Count the number of bricks
	 * @return The total number of breakable bricks that are not flagged dead
	 */
	public int getCount()
	{
		//track the count
		int count = 0;
		
		for (int row = 0; row < getBricks().length; row++)
		{
			for (int col = 0;  col < getBricks()[0].length; col++)
			{
				if (getBricks()[row][col] != null)
				{
					//if the brick is not dead and not solid, add to the count
					if (!getBricks()[row][col].isDead() && !getBricks()[row][col].isSolid())
						count++;
				}
			}
		}
		
		//return our result
		return count;
	}
	
    /**
     * Flag if the player won the level
     * @param win true = yes, false = no
     */
    public void setWin(final boolean win)
    {
    	this.win = win;
    }
    
    /**
     * Has the player won?
     * @return true if the bricks were eliminated, false otherwise
     */
    public boolean hasWin()
    {
    	return this.win;
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
		//if there are no more bricks to be broken
		if (getCount() <= 0)
		{
			//flag win true
			setWin(true);
			
			//reset frames count
			getGame().setFrames(0);
			
			//no need to continue
			return;
		}
		
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
				//get the current brick
				final Brick brick = getBricks()[row][col]; 
				
				//skip if it does not exist
				if (brick == null)
					continue;
				
				//is the brick dead?
				if (!brick.isDead())
				{
					//position brick
					super.setX(brick.getX());
					super.setY(brick.getY());
					
					//assign the appropriate animation
					super.getSpritesheet().setKey(brick.getKey());
					
					//render brick
					super.render(canvas);
				}
				else
				{
					//render brick particles (if exist)
					brick.render(canvas);
				}
			}
		}
	}
}