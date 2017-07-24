package com.gamesbykevin.breakout.game;

import android.view.MotionEvent;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.ball.Balls;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.activity.GameActivity.Screen;
import com.gamesbykevin.breakout.level.Levels;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.powerup.Powerups;
import com.gamesbykevin.breakout.util.StatDescription;
import com.gamesbykevin.breakout.util.UtilityHelper;
import com.gamesbykevin.breakout.wall.Wall;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.STATISTICS;
import static com.gamesbykevin.breakout.game.GameHelper.GAME_OVER_FRAMES_DELAY;
import static com.gamesbykevin.breakout.game.GameHelper.STAT_DESCRIPTION;
import static com.gamesbykevin.breakout.opengl.OpenGLRenderer.LOADED;

/**
 * Created by Kevin on 7/19/2017.
 */

public class Manager implements ICommon {

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

    public Manager(GameActivity activity) {

        //store activity reference
        this.activity = activity;

        //create new bricks container
        this.bricks = new Bricks();

        //create new wall
        this.wall = new Wall();

        //create new paddle
        this.paddle = new Paddle();

        //create the balls
        this.balls = new Balls();

        //create the power ups
        this.powerups = new Powerups();

        //create and load the levels
        this.levels = new Levels(activity);
    }

    @Override
    public void reset() {
        GameHelper.resetLevel(this);
    }

    @Override
    public void update(GameActivity activity) {

        switch (STEP) {

            //we are loading
            case Loading:

                //if the textures have finished loading
                if (LOADED) {

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

                    //if there are no more bricks left, we won
                    if (getBricks().getCount() <= 0) {

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

                        //deduct 1 life from our total
                        STAT_DESCRIPTION.setDescription(STAT_DESCRIPTION.getStatValue() - 1);

                        if (STAT_DESCRIPTION.getStatValue() <= 0) {
                            //play sound
                            activity.playSound(R.raw.gameover);

                        }
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

                //keep counting if enough time has not yet passed
                if (frames < GAME_OVER_FRAMES_DELAY) {

                    //keep track of frames elapsed
                    frames++;

                    //if we are now ready to display go ahead and do it
                    if (frames >= GAME_OVER_FRAMES_DELAY)
                        activity.setScreen(Screen.GameOver);
                }
                break;
        }
    }

    @Override
    public void render(GL10 openGL) {

        //render everything on screen
        GameHelper.render(openGL, this);
    }

    @Override
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
        wall = null;
        paddle = null;
        balls = null;
        powerups = null;
        bricks = null;
    }

    public boolean onTouchEvent(final int action, final float x, final float y) {

        //don't continue if we aren't ready yet
        if (STEP != Step.Updating)
            return true;

        //if the control is tilt, we can't continue
        if (!activity.getBooleanValue(R.string.control_file_key))
            return true;

        if (action == MotionEvent.ACTION_UP)
        {
            //un freeze any frozen balls here
            if (this.press)
                getBalls().setFrozen(false);

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
}