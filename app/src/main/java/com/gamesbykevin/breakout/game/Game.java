package com.gamesbykevin.breakout.game;

import android.view.MotionEvent;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.ball.Balls;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.activity.GameActivity.Screen;
import com.gamesbykevin.breakout.level.Levels;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.powerup.Powerups;
import com.gamesbykevin.breakout.util.StatDescription;
import com.gamesbykevin.breakout.util.UtilityHelper;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.STATISTICS;
import static com.gamesbykevin.breakout.game.GameHelper.GAME_OVER_FRAMES_DELAY;
import static com.gamesbykevin.breakout.game.GameHelper.GET_READY_FRAMES_LIMIT;
import static com.gamesbykevin.breakout.game.GameHelper.TAP_START;
import static com.gamesbykevin.breakout.game.GameHelper.WIN;
import static com.gamesbykevin.breakout.game.GameHelper.getStatDescription;
import static com.gamesbykevin.breakout.game.GameHelper.restartLevel;
import static com.gamesbykevin.breakout.opengl.OpenGLRenderer.LOADED;

/**
 * Created by Kevin on 7/19/2017.
 */
public class Game {

    //the collection of bricks
    private Bricks bricks;

    //the paddle in the game
    private Paddle paddle;

    //the balls in the game
    private Balls balls;

    //the power ups in the game
    private Powerups powerups;

    //the object containing the levels
    private Levels levels;

    //object for rendering a number
    private StatDescription stat;

    private final GameActivity activity;

    //are we pressing on the screen
    private boolean press = false;

    /**
     * The list of steps in the game
     */
    public enum Step {
        Start,
        Reset,
        Loading,
        GameOver,
        Updating
    }

    //what is the current step that we are on
    public static Step STEP = Step.Loading;

    //keep track so we know when to display the game over screen
    private int frames = 0;

    public Game(GameActivity activity) {

        //store activity reference
        this.activity = activity;

        //default to loading
        STEP = Step.Loading;
    }

    /**
     * Pause the game
     */
    public void pause() {

        //flag display to show
        TAP_START = true;

        //go here so nothing is done
        STEP = Step.Start;

        //freeze the balls etc....
        getBalls().setFrozen(true);
    }

    public void resume() {

        //un freeze the balls
        getBalls().setFrozen(false);

        //remove tap display
        GameHelper.TAP_START = false;

        //continue updating the game
        STEP = Step.Updating;
    }

    public void reset() {
        GameHelper.resetLevel();
    }

    public void update() {

        switch (STEP) {

            //we are loading
            case Loading:

                //if the textures have finished loading
                if (LOADED) {

                    //create new bricks container
                    if (this.bricks == null)
                        this.bricks = new Bricks();

                    //create new paddle
                    if (this.paddle == null)
                        this.paddle = new Paddle();

                    //create the balls
                    if (this.balls == null)
                        this.balls = new Balls();

                    //create the power ups
                    if (this.powerups == null)
                        this.powerups = new Powerups();

                    //create and load the levels
                    if (this.levels == null)
                        this.levels = new Levels(activity);

                    //if loaded display level select screen
                    activity.setScreen(Screen.LevelSelect);

                    //go to start step
                    STEP = Step.Start;
                }
                break;

            //don't do anything
            case Start:
                break;

            //we are resetting the board
            case Reset:

                //reset level
                reset();

                //after resetting, next step is updating
                STEP = Step.Updating;

                //we can go to ready now
                activity.setScreen(Screen.Ready);
                break;

            case Updating:

                //update the bricks
                getBricks().update(activity);

                //update the balls
                getBalls().update(activity);

                //update the paddle
                getPaddle().update(activity);

                //update the power ups
                getPowerups().update(activity);

                //if the game is over, move to the next step
                if (GameHelper.isGameOver()) {

                    //did we win?
                    if (WIN) {

                        //save the index of the current level completed
                        STATISTICS.update(true);
                        STATISTICS.save();

                        //play sound
                        activity.playSound(R.raw.complete);

                        //display message
                        UtilityHelper.logEvent("GAME OVER WIN!!!");

                    } else {

                        //display message
                        UtilityHelper.logEvent("GAME OVER LOSE!!!");

                        //take away one of our lives
                        long lives = (getStatDescription().getStatValue() - 1);

                        //update the image displayed
                        getStatDescription().setDescription(lives);

                        //no more lives
                        if (getStatDescription().getStatValue() <= 0)
                            activity.playSound(R.raw.gameover);
                    }

                    //move to game over step
                    STEP = Step.GameOver;

                    //vibrate the phone
                    activity.vibrate();

                    //reset frames timer
                    frames = 0;
                }
                break;

            case GameOver:

                //the next step will vary if we win/lose
                if (WIN) {
                    //keep counting if enough time has not yet passed
                    if (frames < GAME_OVER_FRAMES_DELAY) {

                        //keep track of frames elapsed
                        frames++;

                        //if we are now ready to display go ahead and do it
                        if (frames >= GAME_OVER_FRAMES_DELAY)
                            activity.setScreen(Screen.GameOver);
                    }
                } else {

                    //if there are no more lives, the game is over
                    if (getStatDescription().getStatValue() <= 0) {
                        //keep counting if enough time has not yet passed
                        if (frames < GAME_OVER_FRAMES_DELAY) {

                            //keep track of frames elapsed
                            frames++;

                            //if we are now ready to display go ahead and do it
                            if (frames >= GAME_OVER_FRAMES_DELAY)
                                activity.setScreen(Screen.GameOver);
                        }
                    } else {
                        //keep counting if enough time has not yet passed
                        if (frames < GET_READY_FRAMES_LIMIT) {

                            //keep track of frames elapsed
                            frames++;

                            if (frames >= GET_READY_FRAMES_LIMIT) {

                                //keep displaying the game
                                activity.setScreen(Screen.Ready);

                                //add a ball back to the game
                                restartLevel();

                                //go back to updating
                                STEP = Step.Updating;
                            }
                        }
                    }
                }
                break;
        }
    }

    public void dispose() {

        if (levels != null)
            levels.dispose();
        if (bricks != null)
            bricks.dispose();
        if (paddle != null)
            paddle.dispose();
        if (balls != null)
            balls.dispose();
        if (powerups != null)
            powerups.dispose();
        if (bricks != null)
            bricks.dispose();

        levels = null;
        bricks = null;
        paddle = null;
        balls = null;
        powerups = null;
        bricks = null;
    }

    public boolean onTouchEvent(final int action, final float x, final float y) {

        //don't continue if we aren't ready yet
        if (STEP != Step.Updating && STEP != Step.Start)
            return true;

        //if the control is tilt, we can't continue
        if (!activity.getBooleanValue(R.string.control_file_key))
            return true;

        if (action == MotionEvent.ACTION_UP)
        {
            //un-pause the game
            if (this.press)
                resume();

            //un flag press
            this.press = false;

            //update the paddle
            getPaddle().touch(x, false, Paddle.TOUCH_POWER_0);
        }
        else if (action == MotionEvent.ACTION_DOWN)
        {
            //flag that we pressed down
            this.press = true;

            //update the paddle
            getPaddle().touch(x, true, Paddle.TOUCH_POWER_100);
        }
        else if (action == MotionEvent.ACTION_MOVE)
        {
            //flag press
            this.press = true;

            //update the paddle
            getPaddle().touch(x, true, Paddle.TOUCH_POWER_100);
        }

        //return true to keep receiving events
        return true;
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
     * Get the paddle
     * @return The paddle object the player interacts with
     */
    public Paddle getPaddle()
    {
        return this.paddle;
    }

    public void render(GL10 openGL) {

        //don't display if we are still loading
        if (STEP == Step.Loading)
            return;

        //render everything on screen
        GameHelper.render(openGL);
    }
}