package com.gamesbykevin.breakout.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.ball.Balls;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.level.Levels;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.powerup.Powerups;
import com.gamesbykevin.breakout.screen.OptionsScreen;
import com.gamesbykevin.breakout.screen.ScreenManager;
import com.gamesbykevin.breakout.screen.ScreenManager.State;
import com.gamesbykevin.breakout.thread.MainThread;
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
	 * The default starting # of lives
	 */
	public static final int DEFAULT_LIVES = 5;
	
	//default # of lives
	private int lives;
	
	//the number of elapsed frames
	private int frames = 0;
	
	/**
	 * The number of frames to display get ready text
	 */
	private static final int GET_READY_FRAMES_LIMIT = MainThread.FPS;
	
	/**
	 * The number of frames to display win text
	 */
	private static final int WIN_FRAMES_LIMIT = MainThread.FPS;
	
	/**
	 * Darken the background accordingly
	 */
	private static final int TRANSITION_ALPHA_TRANSPARENCY = 95;
	
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
        
        //set default # of lives
        setLives(DEFAULT_LIVES);
        
        //populate the bricks accordingly
        getLevels().populate(getBricks());
    }
    
    /**
     * Set the number of lives
     * @param lives The desired # of lives
     */
    public void setLives(final int lives)
    {
    	this.lives = lives;
    }
    
    /**
     * Get the number of lives
     * @return The desired # of lives
     */
    public int getLives()
    {
    	return this.lives;
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
    		//reset the frames count
    		setFrames(0);
    		
        	//flag reset false
        	setReset(false);
        	
        	//flag game over false
        	setGameover(false);
            
        	//reset paddle back to middle etc...
        	getPaddle().reset();
        	
        	//remove all existing balls
        	getBalls().getBalls().clear();
        	
            //add default ball
            getBalls().add(getPaddle());
        	
            //reset power ups
            getPowerups().reset();
            
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
     * Is the game ready?
     * @return true if the number of frames elapsed the limit
     */
    private boolean isReady()
    {
    	if (getBricks().hasWin())
    	{
    		return (this.frames > WIN_FRAMES_LIMIT);
    	}
    	else
    	{
    		return (this.frames > GET_READY_FRAMES_LIMIT);
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
    }
    
    /**
     * Do we have reset flagged?
     * @return true = yes, false = no
     */
    public boolean hasReset()
    {
    	return this.reset;
    }
    
    public void setFrames(final int frames)
    {
    	this.frames = frames;
    }
    
    /**
     * Flag notify
     * @param notify True if we notified the user, false otherwise
     */
    public void setNotify(final boolean notify)
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
		
    	//if the game is not ready don't continue
    	if (!isReady())
    		return;
    	
		if (action == MotionEvent.ACTION_UP)
    	{
    		//un freeze any frozen balls here
    		if (this.press)
	    		getBalls().setFrozen(false);
    		
    		//un flag press
    		this.press = false;
    		
    		//flag touch false, depending on controls setting
    		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Controls) == 1 || getScreen().getPanel().getSensor() == null)
    			getPaddle().touch(x, false);
    	}
    	else if (action == MotionEvent.ACTION_DOWN)
		{
    		//flag that we pressed down
    		this.press = true;
    		
    		//flag touch true, depending on controls setting
    		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Controls) == 1 || getScreen().getPanel().getSensor() == null)
    			getPaddle().touch(x, true);
		}
		else if (action == MotionEvent.ACTION_MOVE)
    	{
    		//un-flag press
    		this.press = false;
    		
    		//flag touch true, depending on controls setting
    		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Controls) == 1 || getScreen().getPanel().getSensor() == null)
    			getPaddle().touch(x, true);
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
        	if (isReady())
        	{
        		if (getBricks().hasWin())
        		{
    				//move to the next level
    				getLevels().setLevelIndex(getLevels().getLevelIndex() + 1);
    				
    		        //flag win false
    		        getBricks().setWin(false);
    		        
    		        //populate the bricks accordingly
    		        getLevels().populate(getBricks());
    		        
    		        //reset
    		        setReset(true);
        		}
        		else
        		{
		    		//update the bricks
		    		getBricks().update();
		    		
		    		//update the balls
		    		getBalls().update();
		    		
		    		//update the paddle
		    		getPaddle().update();
		    		
		    		//update the power ups
		    		getPowerups().update();
        		}
        	}
        	else
        	{
        		//keep track of the frames count
        		setFrames(this.frames + 1);
        	}
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
    	if (!hasNotify())
    	{
			//render loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
			
			//flag that the user has been notified
			setNotify(true);
    	}
    	else
    	{
    		//render the wall
    		getWall().render(canvas);
    		
    		//render the bricks
    		getBricks().render(canvas);
    		
    		//render the power ups
    		getPowerups().render(canvas);
    		
    		//render the balls
    		getBalls().render(canvas);
    		
    		//render the paddle
    		getPaddle().render(canvas);
    		
    		//
    		canvas.drawText(getLives() + "", 50, 75, getPaint());
    		
    		//if not ready yet
    		if (!isReady())
    		{
    			//darken background
    			ScreenManager.darkenBackground(canvas, TRANSITION_ALPHA_TRANSPARENCY);
    			
    			//render image
    			if (getBricks().hasWin())
    			{
	    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.LevelComplete), 70, 364, null);
    			}
    			else
    			{
	    			canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.GetReady), 120, 446, null);
    			}
    		}
    	}
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