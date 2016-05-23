package com.gamesbykevin.breakout.level;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Files;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.panel.GamePanel;

public class Levels implements Disposable
{
	//the list of levels
	private ArrayList<Level> levels;

	//the current level
	private int levelIndex = 0;
	
	//list of locations used for the bonus bricks
	private ArrayList<Location> locations;
	
	/**
	 * Character representing empty space
	 */
	private static final String BRICK_EMPTY = "_";
	
	/**
	 * Character representing a breakable brick
	 */
	private static final String BRICK_BREAKABLE = "A";
	
	/**
	 * Character representing an un-breakable brick
	 */
	private static final String BRICK_UNBREAKABLE = "B";
	
	/**
	 * Character that indicates new level begins
	 */
	private static final String LEVEL_SEPARATOR = "#";
	
	/**
	 * How many of the bricks should we flag as a bonus
	 */
	private static final float BONUS_RATIO = .3f;
	
	public Levels() 
	{
		//create list of levels
		this.levels = new ArrayList<Level>();
		
		//create a list of possible bonus bricks
		this.locations = new ArrayList<Location>();
		
		//load the levels
		load();
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
	private void load()
	{
		Level level = null;
		
		//check every line in our text file
		for (String line : Files.getText(Assets.TextKey.Levels).getLines())
		{
			//if the line has the level separator add to the list
			if (line.contains(LEVEL_SEPARATOR))
			{
				//add level to array list
				if (!level.getKey().isEmpty() && level.getKey().size() > 1)
					this.levels.add(level);
				
				//now create a new object
				level = new Level();
			}
			else
			{
				if (level == null)
					level = new Level();
				
				//add line to level
				if (line.length() > 0)
					level.getKey().add(line);
			}
		}
		
		if (level != null && !level.getKey().isEmpty())
			this.levels.add(level);
	}
	
	/**
	 * Set the level index
	 * @param levelIndex The desired level of play
	 */
	public void setLevelIndex(final int levelIndex)
	{
		this.levelIndex = levelIndex;
	}
	
	/**
	 * Get the level index
	 * @return The desired level of play
	 */
	public int getLevelIndex()
	{
		return this.levelIndex;
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
		
		//check every row in the level
		for (int row = 0; row < level.getKey().size(); row++)
		{
			//get the current line
			final String line = level.getKey().get(row);
			
			//check every column in the row
			for (int col = 0; col < line.length(); col++)
			{
				//check the current character
				final String character = line.substring(col, col + 1); 
				
				//now determine if there is a brick here
				if (character == null || character.equalsIgnoreCase(BRICK_EMPTY))
				{
					//if empty flag dead true
					bricks.getBricks()[row][col].setDead(true);
				}
				else if (character.equalsIgnoreCase(BRICK_BREAKABLE))
				{
					//assign animation
					bricks.getBricks()[row][col].setKey(Bricks.Key.Purple);
					
					//flag not dead
					bricks.getBricks()[row][col].reset();
					
					//add place as possible location 
					getLocations().add(new Location(col, row));
				}
				else if (character.equalsIgnoreCase(BRICK_UNBREAKABLE))
				{
					//assign animation
					bricks.getBricks()[row][col].setKey(Bricks.Key.Silver);
					
					//flag not dead
					bricks.getBricks()[row][col].reset();
					
					//flag the brick as solid so it can't be broken
					bricks.getBricks()[row][col].setSolid(true);
				}
				else
				{
					//anything else flag dead true
					bricks.getBricks()[row][col].setDead(true);
				}
			}
		}
		
		//add random bonuses to the bricks
		populateBonuses(bricks);
	}
	
	/**
	 * Flag a random number of bricks that are not dead to contain a bonus item.<br>
	 * The number will be a ratio of the total number of bricks not dead and not solid
	 * @param bricks Object containing bricks in play
	 */
	private void populateBonuses(final Bricks bricks)
	{
		//calculate how many bricks should be flagged as a bonus
		final int limit = (int)(bricks.getCount() * BONUS_RATIO);
		
		//track the count
		int count = 0;
		
		//continue to loop as long as there are locations to add a bonus
		while (!getLocations().isEmpty())
		{
			//pick random index
			final int index = GamePanel.RANDOM.nextInt(getLocations().size());
			
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
		return this.levels.get(getLevelIndex());
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
	
	private class Location
	{
		//the location of the location, lol
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