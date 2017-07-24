package com.gamesbykevin.breakout.game;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.util.StatDescription;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.MANAGER;
import static com.gamesbykevin.breakout.activity.GameActivity.STATISTICS;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.FPS;

/**
 * Game helper methods
 * @author GOD
 */
public final class GameHelper 
{
	/**
	 * The duration we wait until we show the game over screen
	 */
	protected static final int GAME_OVER_FRAMES_DELAY = (FPS * 2);

	/**
	 * The number of frames to display get ready text
	 */
	private static final int GET_READY_FRAMES_LIMIT = FPS;

	/**
	 * Our object that will keep track of our lives
	 */
	public static StatDescription STAT_DESCRIPTION = new StatDescription();

    /**
     * Start the current assigned level all over 
     * @param game Our game reference object
     */
    public final static void resetLevel(final Manager game)
	{
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
    protected final static void restartLevel(final Manager game)
    {
        //reset paddle and ball(s)
		game.getBalls().add(game.getPaddle());
    }

    /**
     * Check if the player lost a life or if the level has been completed
     */
    protected final static boolean isGameOver()
	{
		//if there are no more bricks to be broken the game is over
		if (MANAGER.getBricks().getCount() <= 0)
			return true;

		//if there are no more balls in play, the game is over
		if (MANAGER.getBalls().getCount() < 1)
			return true;

		//game is not yet over
		return false;
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
     * Render the game accordingly
     * @param openGL Place to write pixel data
     * @param game Our game reference object
     * @throws Exception
     */
    public static final void render(final GL10 openGL, final Manager game) throws Exception
    {
		/*
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
				if (MainThread.DEBUG)
				{
					if (MainThread.AI_ASSISTANCE)
					{
		    			//darken background
		    			ScreenManager.darkenBackground(canvas, TRANSITION_ALPHA_TRANSPARENCY);
		    			
						//render image
		    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.GetReady), 120, 446, null);
					}
				}
				else
				{
	    			//darken background
	    			ScreenManager.darkenBackground(canvas, TRANSITION_ALPHA_TRANSPARENCY);
	    			
					//render image
	    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.GetReady), 120, 446, null);
				}
			}
    	}
    	*/
    }
}