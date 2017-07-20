package com.gamesbykevin.breakout.game;

import android.view.MotionEvent;

import com.gamesbykevin.androidframework.level.Select;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.ball.Balls;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.level.Levels;
import com.gamesbykevin.breakout.number.Number;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.powerup.Powerups;
import com.gamesbykevin.breakout.score.Score;
import com.gamesbykevin.breakout.wall.Wall;

import javax.microedition.khronos.opengles.GL10;

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

    //the game score card
    private Score score;

    //object for rendering a number
    private Number number;

    private final GameActivity activity;

    private boolean press = false;

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
        this.levels = new Levels();

        //create new number object
        this.number = new Number();

        //create our score card
        this.score = new Score(screen.getPanel().getActivity());
    }

    @Override
    public void reset() {
        //flag reset false
        GameHelper.RESET = false;
        GameHelper.WIN = false;
        GameHelper.LOSE = false;

        //reset level
        GameHelper.resetLevel(this);
    }

    @Override
    public void update() {
        //if the game is over
        if (GameHelper.RESET)
        {
            //reset the game
            reset();
        }
        else
        {
            GameHelper.update(this);
        }
    }

    @Override
    public void render(GL10 openGL) {
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
        number = null;
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
                getPaddle().touch(x, false, Paddle.TOUCH_POWER_0);
        }
        else if (action == MotionEvent.ACTION_DOWN)
        {
            //flag that we pressed down
            this.press = true;

            //flag touch true, depending on controls setting
            if (GameHelper.canTouch(this))
                getPaddle().touch(x, true, Paddle.TOUCH_POWER_100);
        }
        else if (action == MotionEvent.ACTION_MOVE)
        {
            //flag press
            this.press = true;

            //flag touch true, depending on controls setting
            if (GameHelper.canTouch(this))
                getPaddle().touch(x, true, Paddle.TOUCH_POWER_100);
        }
    }

    /**
     * Get the number object
     * @return Our number object reference for rendering lives
     */
    public Number getNumber()
    {
        return this.number;
    }

    /**
     * Get our score card
     * @return Our score reference object to track completed levels
     */
    public Score getScore()
    {
        return this.score;
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