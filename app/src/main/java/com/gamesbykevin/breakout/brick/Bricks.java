package com.gamesbykevin.breakout.brick;

import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;

import javax.microedition.khronos.opengles.GL10;

public class Bricks extends Entity implements ICommon
{
	//array list of bricks
	private Brick[][] bricks;
	
	/**
	 * The different animations for each brick
	 */
	public enum Key
	{
		Yellow(40,100, "Y"), Black(80,100, "B"), Grey(120,100, "G"), 
		Red(40,120, "R"),	Orange(80,120, "O"), Brown(120, 120, "N"), 
		Purple(40,140, "U"), Pink(80,140, "P"), Peach(120,140, "H"),  
		Silver(40,160, "S"), White(80,160, "W"),
		Green(40,180, "C"),  DarkGreen(80,180, "D"),
		Blue(40,200, "E"), 	DarkBlue(80,200, "F");
		
		//location of animation coordinates
		private final int x, y;
		
		//the code that we can match to this key
		private final String code;

		private int textureId = 0;

		private Key(final int x, final int y, final String code)
		{
			//assign animation coordinates
			this.x = x;
			this.y = y;
			
			//the code to compare
			this.code = code;
		}

		public void setTextureId(int textureId) {
			this.textureId = textureId;
		}

		public int getTextureId() {
			return this.textureId;
		}

		/**
		 * Do we have a matching code?
		 * @param code The code we want to check
		 * @return true if the codes match, false otherwise or if any values checked are null
		 */
		public boolean hasCode(final String code)
		{
			//if any is null we can't match
			if (code == null)
				return false;
			if (getCode() == null)
				return false;
			
			//if values match return true
			if (getCode().equalsIgnoreCase(code))
				return true;
			
			//no match was found
			return false;
		}
		
		/**
		 * Get the code
		 * @return The character code to match to this key
		 */
		public String getCode()
		{
			return this.code;
		}
		
		/**
		 * Get X coordinate
		 * @return The starting x-coordinate on the sprite sheet
		 */
		public int getX()
		{
			return this.x;
		}
		
		/**
		 * Get Y coordinate
		 * @return The starting y-coordinate on the sprite sheet
		 */
		public int getY()
		{
			return this.y;
		}
	}
	
	/**
	 * Dimensions of bricks board with normal size bricks
	 */
	public static final int COLS_NORMAL = 11;

	/**
	 * Dimensions of bricks board with smaller bricks
	 */
	public static final int COLS_SMALL = 22;
	
	/**
	 * Dimensions of bricks board with xtra smaller bricks
	 */
	public static final int COLS_XSMALL = 44;
	
	/**
	 * Dimensions of bricks board with normal size bricks
	 */
	public static final int ROWS_NORMAL = 18;

	/**
	 * Dimensions of bricks board with smaller bricks
	 */
	public static final int ROWS_SMALL = 36;
	
	/**
	 * Dimensions of bricks board with xtra smaller bricks
	 */
	public static final int ROWS_XSMALL = 72;
	
	/**
	 * The starting x-coordinate
	 */
	public static final int START_X = 20;
	
	/**
	 * The starting y-coordinate
	 */
	public static final int START_Y = 20;
	
	public Bricks()
	{
		super(Brick.WIDTH_NORMAL, Brick.HEIGHT_NORMAL);
	}
	
	@Override
	public final void reset() 
	{
		//create a new array list for the bricks
		this.bricks = new Brick[(int)getRow()][(int)getCol()];
		
		//the size of the bricks
		int width, height;
		
		//determine the size of the bricks
		if (getBricks()[0].length == COLS_NORMAL)
		{
			width = Brick.WIDTH_NORMAL;
			height = Brick.HEIGHT_NORMAL;
		}
		else if (getBricks()[0].length == COLS_SMALL)
		{
			width = Brick.WIDTH_SMALL;
			height = Brick.HEIGHT_SMALL;
		}
		else
		{
			width = Brick.WIDTH_XSMALL;
			height = Brick.HEIGHT_XSMALL;
		}
		
		//assign dimensions
		super.setWidth(width);
		super.setHeight(height);
		
		//create a brick at every position and mark dead (to start)
		for (int row = 0; row < getBricks().length; row++)
		{
			for (int col = 0; col < getBricks()[0].length; col++)
			{
				//if a brick does not exist we will create it
				if (getBricks()[row][col] == null)
				{
					//create brick
					getBricks()[row][col] = new Brick();
					
					//assign correct position
					getBricks()[row][col].setX(START_X + (col * width));
					getBricks()[row][col].setY(START_Y + (row * height));
				}
				
				//ensure correct dimensions are assigned
				getBricks()[row][col].setWidth(width);
				getBricks()[row][col].setHeight(height);
				
				//reset brick
				getBricks()[row][col].reset();
				
				//make sure all bricks are dead
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
	public void update(GameActivity activity)
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
				getBricks()[row][col].update(activity);
			}
		}
	}
	
	@Override
	public void render(final GL10 openGL)
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

				//get the bricks texture
				super.setTextureId(brick.getTextureId());

				//is the brick dead?
				if (!brick.isDead())
				{
					//position brick
					super.setX(brick.getX());
					super.setY(brick.getY());

					//if the brick is solid apply transparency
					if (brick.isSolid())
					{
						//assign the correct transparency
						//make brick transparent

						//render the brick
						super.render(openGL);
					}
					else
					{
						//render brick
						super.render(openGL);
					}
				}
				else
				{
					//render brick particles (if exist)
					brick.render(openGL);
				}
			}
		}
	}
}