package com.gamesbykevin.breakout.score;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.breakout.thread.MainThread;

import android.app.Activity;

public class Score extends Internal 
{
	/**
	 * The name of our internal storage file
	 */
	public static final String FILE_NAME = "scorecard";

    /**
     * New score separator string
     */
    private static final String NEW_SCORE = ";";
	
	//list of all levels completed
	private ArrayList<Integer> levels;
	
	/**
	 * Default Constructor
	 */
	public Score(final Activity activity) 
	{
		super(FILE_NAME, activity, MainThread.DEBUG);
		
		//create new list of levels
		this.levels = new ArrayList<Integer>();
		
        //if content exists we will load it
        if (super.getContent().toString().trim().length() > 0)
        {
            //each index indicates a level completed
            final String[] scores = super.getContent().toString().split(NEW_SCORE);
            
            for (String score : scores)
            {
            	this.levels.add(Integer.parseInt(score));
            }
        }
	}
	
	/**
	 * Do we have this level?
	 * @param levelIndex The desired level index
	 * @return true if the specified level index exists in our score list, false otherwise
	 */
	public boolean hasLevel(final int levelIndex)
	{
		for (int i = 0; i < this.levels.size(); i++)
		{
			//if the parameter equals the int in the list, we have the level
			if (this.levels.get(i) == levelIndex)
				return true;
		}
		
		//we do not have the level index
		return false;
	}
	
	/**
	 * Add the level to the score list
	 * @param levelIndex The desired levelIndex
	 * @return true if addition was successful, false otherwise
	 */
	public boolean update(final int levelIndex)
	{
		//check the entire list
		for (int i = 0; i < this.levels.size(); i++)
		{
			//the value already exists, so no need to update
			if (this.levels.get(i) == levelIndex)
				return false;
		}
		
		//add to list
		this.levels.add(levelIndex);
		
		//save the levels
		save();
		
		//we were successful updating
		return true;
	}
	
    /**
     * Save the levels to the internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        for (int i = 0; i < this.levels.size(); i++)
        {
            //if content exists, add delimiter to separate each level
            if (super.getContent().length() > 0)
                super.getContent().append(NEW_SCORE);
            
            //write level, size, and time
            super.getContent().append(this.levels.get(i));
        }
        
        //save the content to physical internal storage location
        super.save();
    }
    
    @Override
    public void dispose()
    {
    	super.dispose();
    	
    	if (this.levels != null)
    	{
    		this.levels.clear();
    		this.levels = null;
    	}
    }
}