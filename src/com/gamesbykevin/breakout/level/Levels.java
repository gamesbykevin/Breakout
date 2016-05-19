package com.gamesbykevin.breakout.level;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Files;
import com.gamesbykevin.androidframework.resources.Text;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Bricks;

public class Levels 
{
	//the list of levels
	private ArrayList<Level> levels;

	//the current level
	private int levelIndex = 0;
	
	public Levels() 
	{
		//create list of levels
		this.levels = new ArrayList<Level>();
	}

	/**
	 * Load the levels from what is found in the text file
	 */
	private void load()
	{
		//check every line in our text file
		for (String line : Files.getText(Assets.TextKey.Levels).getLines())
		{
			
		}
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
				ssss
			}
		}
	}
	
	/**
	 * Get the current level
	 * @return The level object containing the level key
	 */
	private Level get()
	{
		return this.levels.get(levelIndex);
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