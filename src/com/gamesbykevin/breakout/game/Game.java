package com.gamesbykevin.breakout.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.breakout.ball.Balls;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.level.Levels;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.powerup.Powerups;
import com.gamesbykevin.breakout.screen.OptionsScreen;
import com.gamesbykevin.breakout.screen.ScreenManager;
import com.gamesbykevin.breakout.screen.ScreenManager.State;
import com.gamesbykevin.breakout.wall.Wall;

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
    
    //the duration we want to vibrate the phone for
    private static final long VIBRATION_DURATION = 500L;
    
    /**
     * Our value to identify if vibrate is enabled
     */
	public static final int VIBRATE_ENABLED = 0;
	
	//the collection of bricks
	private Bricks bricks;
	
	//the wall around the game
	private Wall wall;
	
	//the paddle in the game
	private Paddle paddle;
	
	//the balls in the game
	private Balls balls;
	
	//the power ups in the game
	private Powerups powerups;
	
	//the object containing the levels
	private Levels levels;
	
	//did we press the screen
	private boolean press = false;
	
    /**
     * Create our game object
     * @param screen The main screen
     * @throws Exception
     */
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new bricks container
        this.bricks = new Bricks(this);
        
        //create new wall
        this.wall = new Wall();
        
        //create new paddle
        this.paddle = new Paddle(this);
        
        //create the balls
        this.balls = new Balls(this);
        
        //create the power ups
        this.powerups = new Powerups(this);
        
        //create and load the levels
        this.levels = new Levels();
    }
    
    /**
     * Get the levels object
     * @return The object containing every level layout in the game
     */
    public Levels getLevels()
    {
    	return this.levels;
    }
    
    /**
     * Get the power ups object
     * @return The object containing all power ups in the game
     */
    public Powerups getPowerups()
    {
    	return this.powerups;
    }
    
    /**
     * Get the balls object
     * @return The object containing all balls in the game
     */
    public Balls getBalls()
    {
    	return this.balls;
    }
    
    /**
     * Get the bricks object
     * @return Object containing collection of bricks
     */
    public Bricks getBricks()
    {
    	return this.bricks;
    }
    
    /**
     * Get the wall
     * @return Object that renders the wall
     */
    public Wall getWall()
    {
    	return this.wall;
    }
    
    /**
     * Get the paddle
     * @return The paddle object the player interacts with
     */
    public Paddle getPaddle()
    {
    	return this.paddle;
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
     * Reset the game
     */
    private void reset()
    {
    	//make sure we have notified first
    	if (GameHelper.NOTIFY)
    	{
        	//flag reset false
    		GameHelper.RESET = false;
    		GameHelper.WIN = false;
    		GameHelper.LOSE = false;
    		GameHelper.GAMEOVER = false;
    		
    		//reset level
    		GameHelper.resetLevel(this);
    		
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
    	//if we can't interact, we can't continue
    	if (!GameHelper.canInteract())
    		return;
    	
		if (action == MotionEvent.ACTION_UP)
    	{
    		//un freeze any frozen balls here
    		if (this.press)
	    		getBalls().setFrozen(false);
    		
    		//un flag press
    		this.press = false;
    		
    		//flag touch false, depending on controls setting
    		if (GameHelper.canTouch(this))
    			getPaddle().touch(x, false);
    	}
    	else if (action == MotionEvent.ACTION_DOWN)
		{
    		//flag that we pressed down
    		this.press = true;
    		
    		//flag touch true, depending on controls setting
    		if (GameHelper.canTouch(this))
    			getPaddle().touch(x, true);
		}
		else if (action == MotionEvent.ACTION_MOVE)
    	{
    		//un-flag press
    		this.press = false;
    		
    		//flag touch true, depending on controls setting
    		if (GameHelper.canTouch(this))
    			getPaddle().touch(x, true);
    	}
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
    	//if the game is over
    	if (GameHelper.GAMEOVER)
    	{
    		//switch to a different screen
    		getScreen().setState(State.GameOver);
    		
    		//no need to continue
    		return;
    	}
    	else if (GameHelper.RESET)
        {
        	//reset the game
        	reset();
        }
        else
        {
        	GameHelper.update(this);
        }
    }
    
    /**
     * Vibrate the phone for the default duration
     */
    public void vibrate()
    {
    	this.vibrate(VIBRATION_DURATION);
    }
    
    /**
     * Vibrate the phone if the vibrate feature is enabled
     * @param duration The duration to vibrate for milliseconds
     */
    public void vibrate(final long duration)
    {
		//make sure vibrate option is enabled
		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Vibrate) == VIBRATE_ENABLED)
		{
    		//get our vibrate object
    		Vibrator v = (Vibrator) getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    		 
			//vibrate for a specified amount of milliseconds
			v.vibrate(duration);
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
    	GameHelper.render(canvas, this);
    }
    
    @Override
    public void dispose()
    {
        this.paint = null;
        
        if (levels != null)
        {
        	levels.dispose();
        	levels = null;
        }
        
        if (bricks != null)
        {
        	bricks.dispose();
        	bricks = null;
        }
        
    	if (wall != null)
    		wall = null;
    	
    	if (paddle != null)
    	{
    		paddle.dispose();
    		paddle = null;
    	}
    	
    	if (balls != null)
    	{
    		balls.dispose();
    		balls = null;
    	}
    	
    	if (powerups != null)
    	{
    		powerups.dispose();
    		powerups = null;
    	}
    	
    	if (bricks != null)
    	{
    		bricks.dispose();
    		bricks = null;
    	}
    }
}