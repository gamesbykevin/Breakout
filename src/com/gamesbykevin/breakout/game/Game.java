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
        
        //populate the bricks accordingly
        this.levels.populate(getBricks());
        
        //add test ball
        this.balls.add();
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
    		if (this.press)
    		{
	    		//un-freeze any frozen balls here
	    		getBalls().setFrozen(false);
    		}
    		
    		//un-flag press
    		this.press = false;
    	}
    	else if (action == MotionEvent.ACTION_DOWN)
		{
    		//flag that we pressed down
    		this.press = true;
		}
		else if (action == MotionEvent.ACTION_MOVE)
    	{
			//update the paddle location
			getPaddle().setX(x);
			
    		//un-flag press
    		this.press = false;
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
    	}
    }
    
    @Override
    public void dispose()
    {
        this.paint = null;
    }
}