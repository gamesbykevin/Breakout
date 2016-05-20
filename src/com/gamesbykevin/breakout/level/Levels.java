package com.gamesbykevin.breakout.level;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Files;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Bricks;

public class Levels 
{
	//the list of levels
	private ArrayList<Level> levels;

	//the current level
	private int levelIndex = 0;
	
	/**
	 * Character representing empty space
	 */
	private static final String BRICK_EMPTY = "_";
	
	/**
	 * Character representing a breakable brick
	 */
	private static final String BRICK_BREAKABLE = "B";
	
	/**
	 * Character representing an un-breakable brick
	 */
	private static final String BRICK_UNBREAKABLE = "A";
	
	/**
	 * Character that indicates new level begins
	 */
	private static final String LEVEL_SEPARATOR = "#";
	
	public Levels() 
	{
		//create list of levels
		this.levels = new ArrayList<Level>();
		
		//load the levels
		load();
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
				this.levels.add(level);
				
				//now create a new object
				level = new Level();
			}
			else
			{
				if (level == null)
					level = new Level();
				
				//add line to level
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
				if (character == null || character.equals(BRICK_EMPTY))
				{
					//if empty flag dead true
					bricks.getBricks()[row][col].setDead(true);
					
					//remove any particles
					bricks.getBricks()[row][col].removeParticles();
				}
				else if (character.equals(BRICK_BREAKABLE))
				{
					//assign animation
					bricks.getBricks()[row][col].setKey(Bricks.Key.Purple);
					
					//flag not dead
					bricks.getBricks()[row][col].reset();
				}
				else if (character.equals(BRICK_UNBREAKABLE))
				{
					//assign animation
					bricks.getBricks()[row][col].setKey(Bricks.Key.Silver);
					
					//flag not dead
					bricks.getBricks()[row][col].reset();
				}
				else
				{
					//anything else flag dead true
					bricks.getBricks()[row][col].setDead(true);
					
					//remove any particles
					bricks.getBricks()[row][col].removeParticles();
				}
			}
		}
	}
	
	/**
	 * Get the current level
	 * @return The level object containing the level key
	 */
	private Level get()
	{
		return this.levels.get(getLevelIndex());
	}
	
	private class Level
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
	}
}