package com.gamesbykevin.breakout.level;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.brick.Bricks.Key;
import com.gamesbykevin.breakout.util.UtilityHelper;

import static com.gamesbykevin.breakout.activity.GameActivity.STATISTICS;

public class Levels implements Disposable
{
	//the list of levels
	private ArrayList<Level> levels;

	//list of locations used for the bonus bricks
	private ArrayList<Location> locations;
	
	/**
	 * Character representing empty space
	 */
	public static final String BRICK_EMPTY = "_";
	
	/**
	 * Character representing a breakable brick
	 */
	public static final String BRICK_BREAKABLE_NO_COLOR = "X";
	
	/**
	 * Character representing an un-breakable brick
	 */
	public static final String BRICK_UNBREAKABLE = "Z";
	
	/**
	 * Character that indicates new level begins
	 */
	public static final String LEVEL_SEPARATOR = "#";
	
	/**
	 * How many of the bricks should we flag as a bonus for normal sized bricks
	 */
	private static final float BONUS_RATIO_NORMAL = .25f;
	
	/**
	 * How many of the bricks should we flag as a bonus for small sized bricks
	 */
	private static final float BONUS_RATIO_SMALL = .10f;
	
	/**
	 * How many of the bricks should we flag as a bonus for xtra small sized bricks
	 */
	private static final float BONUS_RATIO_XSMALL = .065f;
	
	//list to choose random keys from
	private ArrayList<Bricks.Key> keys = new ArrayList<Bricks.Key>();

	//the name of the file containing all the levels
	private static final String FILE_NAME = "levels.txt";

	/**
	 * Default Constructor
	 */
	public Levels(Context context)
	{
		try {
			//create list of levels
			this.levels = new ArrayList<Level>();

			//create a list of possible bonus bricks
			this.locations = new ArrayList<Location>();

			//load the levels
			load(context);
		} catch (Exception e) {
			UtilityHelper.handleException(e);
		}
	}
	
	@Override
	public void dispose() 
	{
		if (this.locations != null)
		{
			this.locations.clear();
			this.locations = null;
		}
		
		if (this.levels != null)
		{
			for (Level level : this.levels)
			{
				if (level != null)
				{
					level.dispose();
					level = null;
				}
			}
			
			this.levels.clear();
			this.levels = null;
		}
	}

	/**
	 * Load the levels from what is found in the text file
	 */
	private void load(Context context) throws Exception
	{
		Level level = null;

		BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(FILE_NAME)));

		//this will contain the current line of the text file
		String line;

		//read every line in our text file
		while ((line = reader.readLine()) != null)
		{
			//if the line has the level separator add to the list
			if (line.contains(LEVEL_SEPARATOR))
			{
				//add level to array list
				if (!level.getKey().isEmpty() && level.getKey().size() > 1)
					getLevels().add(level);
				
				//now create a new object
				level = new Level();
			}
			else
			{
				if (level == null)
					level = new Level();
				
				//add line to level as long as it isn't big enough
				if (line.length() > 0 && line.length() < Bricks.COLS_XSMALL)
					level.getKey().add(line);
			}
		}

		//close the reader
		reader.close();

		if (level != null && !level.getKey().isEmpty())
			getLevels().add(level);
	}

	/**
	 * Get the size
	 * @return The number of levels that can be played
	 */
	public int getSize()
	{
		return getLevels().size();
	}
	
	protected ArrayList<Level> getLevels()
	{
		return this.levels;
	}

	/**
	 * Populate the bricks based on the current level setup
	 * @param bricks
	 */
	public void populate(final Bricks bricks)
	{
		//get the current level
		Level level = get();
		
		//clear the list of locations
		getLocations().clear();
		
		//are there brick with no assigned color?
		boolean noColor = false;
		
		//set bricks array size
		if (level.getKey().get(0).length() == Bricks.COLS_NORMAL)
		{
			bricks.setCol(Bricks.COLS_NORMAL);
			bricks.setRow(Bricks.ROWS_NORMAL);
			Ball.SPEED_MAX = Brick.HEIGHT_NORMAL;
		}
		else if (level.getKey().get(0).length() == Bricks.COLS_SMALL)
		{
			bricks.setCol(Bricks.COLS_SMALL);
			bricks.setRow(Bricks.ROWS_SMALL);
			Ball.SPEED_MAX = Brick.HEIGHT_SMALL;
		}
		else 
		{
			bricks.setCol(Bricks.COLS_XSMALL);
			bricks.setRow(Bricks.ROWS_XSMALL);
			Ball.SPEED_MAX = Brick.HEIGHT_XSMALL;
		}
		
		//reset bricks
		bricks.reset();
		
		//check every row in the level
		for (int row = 0; row < level.getKey().size(); row++)
		{
			//make sure we stay in bounds
			if (row >= level.getKey().size())
				break;
			
			//get the current line
			final String line = level.getKey().get(row);
			
			//check every column in the row
			for (int col = 0; col < line.length(); col++)
			{
				try
				{
					//check the current character
					final String character = line.substring(col, col + 1); 
					
					//now determine if there is a brick here
					if (character == null || character.equalsIgnoreCase(BRICK_EMPTY))
					{
						//if empty flag dead true
						bricks.getBricks()[row][col].setDead(true);
					}
					else if (character.equalsIgnoreCase(BRICK_UNBREAKABLE))
					{
						//assign animation
						bricks.getBricks()[row][col].setTextureId(Bricks.Key.Silver.getTextureId());
						
						//flag not dead
						bricks.getBricks()[row][col].reset();
						
						//flag the brick as solid so it can't be broken
						bricks.getBricks()[row][col].setSolid(true);
					}
					else if (character.equalsIgnoreCase(BRICK_BREAKABLE_NO_COLOR))
					{
						//there are bricks here with no specified color
						noColor = true;
						
						//assign animation
						bricks.getBricks()[row][col].setTextureId(Bricks.Key.Purple.getTextureId());
						
						//flag not dead
						bricks.getBricks()[row][col].reset();
						
						//add place as possible location 
						getLocations().add(new Location(col, row));
					}
					else
					{
						//check if we have a match
						boolean match = false;

						int size = Key.values().length;

						//check each color key to see if the character matches
						for (int i = 0; i < size; i++)
						{
							Key key = Key.values()[i];

							//if we have a match
							if (key.hasCode(character))
							{
								//flag match
								match = true;
							
								//assign animation
								bricks.getBricks()[row][col].setTextureId(key.getTextureId());
								
								//flag not dead
								bricks.getBricks()[row][col].reset();
								
								//add place as possible location 
								getLocations().add(new Location(col, row));
								
								//exit for loop
								break;
							}
						}
						
						//if there is no match, the brick is dead
						if (!match)
							bricks.getBricks()[row][col].setDead(true);
					}
				}
				catch (Exception e)
				{
					//handle error
					UtilityHelper.handleException(e);

					//print helpful info
					System.out.println("Level Index: " + STATISTICS.getIndex());
					
					//print current row where error occurred
					System.out.println(level.getKey().get(row));

					//end loop
					row = level.getKey().size();
					col = line.length();
					break;
				}
			}
		}
		
		//if there are bricks without an assigned color, populate all bricks with colors
		if (noColor)
			colorizeBricks(bricks);
		
		//add random bonuses to the bricks
		populateBonuses(bricks);
	}
	
	/**
	 * Populate the list of brick keys (not including "Silver")
	 */
	private void populateKeys()
	{
		keys.clear();

		int size = Key.values().length;

		for (int i = 0; i < size; i++)
		{
			Key key = Key.values()[i];

			if (key == Bricks.Key.Silver)
				continue;
			
			keys.add(key);
		}
	}
	
	/**
	 * Get the key
	 * @return A randomly chosen brick animation key from the remaining keys list
	 */
	private Bricks.Key getKey()
	{
		//if our keys list is empty, populate it
		if (this.keys.isEmpty())
			populateKeys();
		
		//pick random index

		final int index = GameActivity.getRandomObject().nextInt(this.keys.size());
		
		//pick random animation key
		Bricks.Key tmp = this.keys.get(index);
		
		//remove from list
		this.keys.remove(index);
		
		//return our result
		return tmp;
	}
	
	/**
	 * Change the color of the bricks to make the board more diverse
	 * @param bricks Object containing bricks in play
	 */
	private void colorizeBricks(final Bricks bricks)
	{
		//our key reference
		Bricks.Key key = null;
		
		//pick a random pattern to color the bricks
		switch (GameActivity.getRandomObject().nextInt(4))
		{
			//each row is a different color
			case 0:

				//check every row in the level
				for (int row = 0; row < bricks.getBricks().length; row++)
				{
					//check every column in the row
					for (int col = 0; col < bricks.getBricks()[0].length; col++)
					{
						if (key == null)
							key = getKey();
						
						//get the current brick
						Brick brick = bricks.getBricks()[row][col];
						
						//if the brick is not dead or hidden or solid, assign the key
						if (!brick.isDead() && !brick.isHidden() && !brick.isSolid())
							brick.setTextureId(key.getTextureId());
					}
					
					key = null;
				}
				break;
				
			//each column is a different color
			case 1:

				//check every column in the row
				for (int col = 0; col < bricks.getBricks()[0].length; col++)
				{
					//check every row in the level
					for (int row = 0; row < bricks.getBricks().length; row++)
					{
						if (key == null)
							key = getKey();
						
						//get the current brick
						Brick brick = bricks.getBricks()[row][col];
						
						//if the brick is not dead or hidden or solid, assign the key
						if (!brick.isDead() && !brick.isHidden() && !brick.isSolid())
							brick.setTextureId(key.getTextureId());
					}
					
					key = null;
				}
				break;
				
			//all bricks are the same
			case 2:
			default:
				//get animation key
				key = getKey();
				
				//check every column in the row
				for (int col = 0; col < bricks.getBricks()[0].length; col++)
				{
					//check every row in the level
					for (int row = 0; row < bricks.getBricks().length; row++)
					{
						//get the current brick
						Brick brick = bricks.getBricks()[row][col];
						
						//if the brick is not dead or hidden or solid, assign the key
						if (!brick.isDead() && !brick.isHidden() && !brick.isSolid())
							brick.setTextureId(key.getTextureId());
					}
				}
				break;
		}
	}
	
	/**
	 * Flag a random number of bricks that are not dead to contain a bonus item.<br>
	 * The number will be a ratio of the total number of bricks not dead and not solid
	 * @param bricks Object containing bricks in play
	 */
	private void populateBonuses(final Bricks bricks)
	{
		//# bricks that should be flagged as a bonus
		final int limit;
		
		//set bricks array size
		if (get().getKey().get(0).length() == Bricks.COLS_NORMAL)
		{
			limit = (int)(bricks.getCount() * BONUS_RATIO_NORMAL);
		}
		else if (get().getKey().get(0).length() == Bricks.COLS_SMALL)
		{
			limit = (int)(bricks.getCount() * BONUS_RATIO_SMALL);
		}
		else
		{
			limit = (int)(bricks.getCount() * BONUS_RATIO_XSMALL);
		}
		
		//track the count
		int count = 0;
		
		//continue to loop as long as there are locations to add a bonus
		while (!getLocations().isEmpty())
		{
			//pick random index
			final int index = GameActivity.getRandomObject().nextInt(getLocations().size());
			
			//get the random location
			final int col = getLocations().get(index).getCol(); 
			final int row = getLocations().get(index).getRow();
			
			//flag power up true
			bricks.getBricks()[row][col].setPowerup(true);
			
			//now that option is no longer available
			getLocations().remove(index);
			
			//add to count
			count++;
			
			//if we reached our limit, stop!!!!
			if (count >= limit)
				break;
		}
	}
	
	private ArrayList<Location> getLocations()
	{
		return this.locations;
	}
	
	/**
	 * Get the current level
	 * @return The level object containing the level key
	 */
	private Level get()
	{
		return getLevels().get(STATISTICS.getIndex());
	}
	
	private class Level implements Disposable
	{
		//the key that makes up the level
		private ArrayList<String> key;
		
		private Level()
		{
			//create new list for our level string
			this.key = new ArrayList<String>();
		}
		
		private ArrayList<String> getKey()
		{
			return this.key;
		}

		@Override
		public void dispose() 
		{
			this.key.clear();
			this.key = null;
		}
	}
	
	/**
	 * Location class, used to pick random locations for the bonuses
	 */
	private class Location
	{
		//the location of our location :)
		private int col, row;
		
		private Location(final int col, final int row)
		{
			setCol(col);
			setRow(row);
		}
		
		private void setCol(final int col)
		{
			this.col = col;
		}
		
		private void setRow(final int row)
		{
			this.row = row;
		}
		
		private int getCol()
		{
			return this.col;
		}
		
		private int getRow()
		{
			return this.row;
		}
	}
}