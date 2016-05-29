package com.gamesbykevin.breakout.game;

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
			
			//go to the game over screen
			game.getScreen().setState(ScreenManager.State.GameOver);
		}
		else if (game.getBalls().getBalls().isEmpty())
		{
			//reset frames
			FRAMES = 0;
			
			//flag lose true
			LOSE = true;
			
			//take a life away
			LIVES--;
		}
	}
    
    /**
     * Update game
     * @throws Exception 
     */
    public static final void update(final Game game) throws Exception
    {
    	if (isReady())
    	{
    		if (GameHelper.LOSE)
    		{
    			//do we need to do anything here
    			GameHelper.LOSE = false;
    			
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
    	}
    }
    
    /**
     * Is the game ready?
     * @return true if the number of frames elapsed the limit
     */
    protected static boolean isReady()
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
    		
    		//render lives here for now
    		canvas.drawText(LIVES + "", 50, 75, game.getPaint());
    		
			//render image
			if (WIN)
			{
    			//darken background
    			ScreenManager.darkenBackground(canvas, TRANSITION_ALPHA_TRANSPARENCY);
				
				//render image
    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.LevelComplete), 70, 364, null);
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