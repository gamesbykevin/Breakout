package com.gamesbykevin.breakout.game;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.screen.OptionsScreen;
import com.gamesbykevin.breakout.screen.ScreenManager;
import com.gamesbykevin.breakout.thread.MainThread;

import android.graphics.Canvas;

/**
 * Game helper methods
 * @author GOD
 */
public final class GameHelper 
{
	/**
	 * Did we win?
	 */
	public static boolean WIN = false;
	
	/**
	 * Did we lose?
	 */
	public static boolean LOSE = false;
	
	/**
	 * Do we reset the game?
	 */
	public static boolean RESET = false;
	
	/**
	 * Should we display the loading screen to the user
	 */
	public static boolean NOTIFY = false;
	
	/**
	 * Is the game over?
	 */
	public static boolean GAMEOVER = false;
	
	/**
	 * Darken the background accordingly
	 */
	private static final int TRANSITION_ALPHA_TRANSPARENCY = 95;
	
	/**
	 * The default starting # of lives
	 */
	public static final int DEFAULT_LIVES = 5;
	
	/**
	 * The # of lives we have
	 */
	public static int LIVES = 0;
	
	/**
	 * The number of frames to display get ready text
	 */
	private static final int GET_READY_FRAMES_LIMIT = MainThread.FPS;
	
	/**
	 * Keep track of frames elapsed
	 */
	private static int FRAMES = 0;
	
	/**
	 * Can the user touch the screen to move the paddle?
	 * @param game Our game reference object
	 * @return true if the user can touch the screen to move the paddle, false otherwise
	 */
    protected final static boolean canTouch(final Game game)
    {
    	return (game.getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Controls) == 1 || game.getScreen().getPanel().getSensor() == null);
    }
	
    /**
     * Start the current assigned level all over 
     * @param game Our game reference object
     */
    public final static void resetLevel(final Game game)
	{
		//reset frames count
		GameHelper.FRAMES = 0;
    	
		//reset balls
		game.getBalls().reset();
		
        //reset paddle and ball(s)
		game.getBalls().add(game.getPaddle());
    	
        //hide power ups
		game.getPowerups().reset();
		
        //populate the bricks accordingly
        game.getLevels().populate(game.getBricks());
	}
	
    /**
     * Restart the same level because the player lost a life
     * @param game Our game reference object
     */
    protected final static void restartLevel(final Game game)
    {
        //reset paddle and ball(s)
		game.getBalls().add(game.getPaddle());
    }
    
    /**
     * Update our level select screens
     * @param game Our game reference object
     */
    protected final static void updateSelect(final Game game)
    {
        //load the saved data checking every level
        for (int levelIndex = game.getSelect().getTotal() - 1; levelIndex >= 0; levelIndex--)
        {
        	//if the level exists then we already completed it
        	if (game.getScore().hasLevel(levelIndex))
        	{
        		//mark this level as completed
        		game.getSelect().setCompleted(levelIndex, true);
        		
        		//mark this level as not locked
        		game.getSelect().setLocked(levelIndex, false);
        		
        		//also make sure the next level is not locked as well
        		if (levelIndex < game.getSelect().getTotal() - 1)
        			game.getSelect().setLocked(levelIndex + 1, false);
        	}
        	else
        	{
        		//mark this level as locked
        		//game.getSelect().setLocked(levelIndex, true);
        		
        		//don't lock any levels
        		game.getSelect().setLocked(levelIndex, false);
        		
        		//mark this level as not completed
        		game.getSelect().setCompleted(levelIndex, false);
        	}
        	
        	//if debugging unlock every level
        	if (MainThread.DEBUG)
            	game.getSelect().setLocked(levelIndex, false);
        }
        
    	//the first level can never be locked
    	game.getSelect().setLocked(0, false);
    }
	
    /**
     * Check if the player lost a life or if the level has been completed
     * @param game Our game reference object
     */
    protected final static void check(final Game game)
	{
		//if there are no more bricks to be broken
		if (game.getBricks().getCount() <= 0)
		{
			//reset frames count
			FRAMES = 0;
			
			//flag that we won
			WIN = true;
			
			//save the index of the level completed
			game.getScore().update(game.getLevels().getLevelIndex());
			
			//update the select screen
			updateSelect(game);
			
			//go to the game over screen
			game.getScreen().setState(ScreenManager.State.GameOver);
			
			//stop all previously playing sound
			Audio.stop();
			
			//play sound effect
			Audio.play(Assets.AudioMenuKey.LevelComplete, true);
		}
		else if (game.getBalls().getCount() < 1)
		{
			//reset frames
			FRAMES = 0;
			
			//flag lose true
			LOSE = true;
			
			//take a life away
			deductLife(game);
			
			//if no more lives, the game is over
    		if (LIVES <= 0)
    		{
				//flag game over true
				GAMEOVER = true;
				
				//go to the game over screen
				game.getScreen().setState(ScreenManager.State.GameOver);
				
				//stop all previously playing sound
				Audio.stop();
				
				//play sound effect
				Audio.play(Assets.AudioMenuKey.Gameover);
    		}
		}
	}
    
    /**
     * Update game
     * @throws Exception 
     */
    public static final void update(final Game game) throws Exception
    {
    	if (!game.getSelect().hasSelection())
    	{
    		//update the object
    		game.getSelect().update();
    		
    		//if we have a selection now, reset the board
    		if (game.getSelect().hasSelection())
    		{
    			//make sure the level is not locked, if it is locked play sound effect
    			if (game.getSelect().isLocked(game.getSelect().getLevelIndex()))
    			{
    				//flag selection as false
    				game.getSelect().setSelection(false);
    				
    				//play sound effect
    				Audio.play(Assets.AudioGameKey.Invalid);
    			}
    			else
    			{
    				//assign the appropriate level
    				game.getLevels().setLevelIndex(game.getSelect().getLevelIndex());
    				
    				//reset the board for the next level
    				RESET = true;
    			}
    		}
    		
    		//no need to continue
    		return;
    	}
    	
    	if (isReady())
    	{
    		if (LOSE)
    		{
    			//do we need to do anything here
    			LOSE = false;
    			
    			//reset level again
    			restartLevel(game);
    		}
    		else
    		{
	    		//update the bricks
    			game.getBricks().update();
	    		
	    		//update the balls
    			game.getBalls().update();
	    		
	    		//update the paddle
    			game.getPaddle().update();
	    		
	    		//update the power ups
    			game.getPowerups().update();
    			
    			//see if the player lost a life or completed a level
    			check(game);
    		}
    	}
    	else
    	{
    		//keep track of the frames count
    		FRAMES++;
    		
    		//if we are now ready, start playing the theme
    		if (isReady())
    			Audio.play(Assets.AudioMenuKey.Theme, true);
    	}
    }
    
    /**
     * Is the game ready?
     * @return true if the number of frames elapsed the limit
     */
    public static boolean isReady()
    {
		return (FRAMES > GET_READY_FRAMES_LIMIT);
    }
    
    /**
     * Can the user interact with the game?<br>
     * The user will not if game over, reset, win/lose, etc....
     * @return true if reset = false, notify = true, win = false, lose = false, isReady() = true
     */
    public static final boolean canInteract()
    {
    	return (!RESET && NOTIFY && !WIN && !LOSE && isReady());
    }
    
    public static final void resetLives(final Game game)
    {
    	//reset the number of lives
    	LIVES = DEFAULT_LIVES;
    	
    	//update number object
    	game.getNumber().setNumber(LIVES);
    }
    
    public static final void addLife(final Game game)
    {
    	//increase lives
    	LIVES++;
    	
    	//update number object
    	game.getNumber().setNumber(LIVES);
    }
    
    public static final void deductLife(final Game game)
    {
    	//decrease lives
    	LIVES--;
    	
    	//update number object
    	game.getNumber().setNumber(LIVES);
    }
    
    /**
     * Render the game accordingly
     * @param canvas Place to write pixel data
     * @param game Our game reference object
     * @throws Exception
     */
    public static final void render(final Canvas canvas, final Game game) throws Exception
    {
    	if (!NOTIFY)
    	{
			//render loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
			
			//flag that the user has been notified
			NOTIFY = true;
    	}
    	else
    	{
        	if (!game.getSelect().hasSelection())
        	{
        		//draw background
        		canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.Border), 0, 0, null);
        		
        		//render level select screen
        		game.getSelect().render(canvas, game.getPaint());
        		
        		//no need to continue
        		return;
        	}
    		
    		//render the wall
    		game.getWall().render(canvas);
    		
    		//render the bricks
    		game.getBricks().render(canvas);
    		
    		//render the power ups
    		game.getPowerups().render(canvas);
    		
    		//render the balls
    		game.getBalls().render(canvas);
    		
    		//render the paddle
    		game.getPaddle().render(canvas);
    		
			//render number of lives
			game.getNumber().render(canvas);
    		
			//render image
			if (WIN)
			{
    			//darken background
    			ScreenManager.darkenBackground(canvas, TRANSITION_ALPHA_TRANSPARENCY);
				
				//render image
    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.LevelCompleteText), 70, 364, null);
			}
			else if (GAMEOVER)
			{
    			//darken background
    			ScreenManager.darkenBackground(canvas, TRANSITION_ALPHA_TRANSPARENCY);
				
				//render image
    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.GameOver), 120, 446, null);
			}
			else if (LOSE || !isReady())
			{
    			//darken background
    			ScreenManager.darkenBackground(canvas, TRANSITION_ALPHA_TRANSPARENCY);
    			
				//render image
    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.GetReady), 120, 446, null);
			}
    	}
    }
}