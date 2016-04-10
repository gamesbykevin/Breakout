package com.gamesbykevin.breakout.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.screen.OptionsScreen;
import com.gamesbykevin.breakout.screen.ScreenManager;
import com.gamesbykevin.breakout.screen.ScreenManager.State;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final ScreenManager screen;
    
    //paint object to draw text
    private Paint paint;
    
    //is the game being reset
    private boolean reset = false;
    
    //has the player been notified (has the user seen the loading screen)
    private boolean notify = false;
    
    //is the game over?
    private boolean gameover = false;
    
    //the duration we want to vibrate the phone for
    private static final long VIBRATION_DURATION = 500L;
    
    /**
     * Our value to identify if vibrate is enabled
     */
	public static final int VIBRATE_ENABLED = 0;
	
    /**
     * Create our game object
     * @param screen The main screen
     * @throws Exception
     */
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public ScreenManager getScreen()
    {
        return this.screen;
    }
    
    /**
     * Is the game over?
     * @return true = yes, false = no
     */
    private boolean hasGameover()
    {
    	return this.gameover;
    }
    
    /**
     * Flag the game over
     * @param gameover true = yes, false = no
     */
    public void setGameover(final boolean gameover)
    {
    	this.gameover = gameover;
    	
    	//if the game is flagged as over 
    	if (hasGameover())
    		getScreen().setState(State.GameOver);
    }
    
    /**
     * Reset the game
     */
    private void reset() 
    {
    	//make sure we have notified first
    	if (hasNotify())
    	{
        	//flag reset false
        	setReset(false);
        	
        	//flag game over false
        	setGameover(false);
        	
        	/*
    		switch (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Difficulty))
    		{
    			default:
	    			break;
    		}
    		*/
    	}
    }
    
    /**
     * Flag reset, we also will flag notify to false if reset is true
     * @param reset true to reset the game, false otherwise
     */
    @Override
    public void setReset(final boolean reset)
    {
    	this.reset = reset;
    	
    	//flag that the user has not been notified, since we are resetting
    	if (hasReset())
    		setNotify(false);
    }
    
    /**
     * Do we have reset flagged?
     * @return true = yes, false = no
     */
    public boolean hasReset()
    {
    	return this.reset;
    }
    
    /**
     * Flag notify
     * @param notify True if we notified the user, false otherwise
     */
    private void setNotify(final boolean notify)
    {
    	this.notify = notify;
    }
    
    /**
     * Do we have notify flagged?
     * @return true if we notified the user, false otherwise
     */
    protected boolean hasNotify()
    {
    	return this.notify;
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
    	//if the object has not been created yet
    	if (this.paint == null)
    	{
            //create new paint object
            this.paint = new Paint();
            //this.paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
            this.paint.setTextSize(48f);
            this.paint.setColor(Color.WHITE);
            this.paint.setLinearText(false);
    	}
    	
        return this.paint;
    }
    
    @Override
    public void update(final int action, final float x, final float y) throws Exception
    {
    	//if reset we can't continue
    	if (hasReset())
    		return;
    	
    	//if the game is over, we can't continue
    	if (hasGameover())
    		return;
    	
    	if (action == MotionEvent.ACTION_UP)
    	{
    		
    	}
    	else if (action == MotionEvent.ACTION_DOWN)
		{
    		
		}
		else if (action == MotionEvent.ACTION_MOVE)
    	{
			
    	}
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //if we are to reset the game
        if (hasReset())
        {
        	//reset the game
        	reset();
        }
        else
        {
        	
        }
    }
    
    /**
     * Vibrate the phone if the setting is enabled
     */
    public void vibrate()
    {
		//make sure vibrate option is enabled
		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Vibrate) == VIBRATE_ENABLED)
		{
    		//get our vibrate object
    		Vibrator v = (Vibrator) getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    		 
			//vibrate for a specified amount of milliseconds
			v.vibrate(VIBRATION_DURATION);
		}
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
    	if (hasReset())
    	{
			//render loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
			
			//flag that the user has been notified
			setNotify(true);
    	}
    	else
    	{
    		
    	}
    }
    
    @Override
    public void dispose()
    {
        this.paint = null;
    }
}