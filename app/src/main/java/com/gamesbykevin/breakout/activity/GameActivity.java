package com.gamesbykevin.breakout.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.game.Game;
import com.gamesbykevin.breakout.game.Game.Step;
import com.gamesbykevin.breakout.level.Levels;
import com.gamesbykevin.breakout.level.Statistics;
import com.gamesbykevin.breakout.opengl.OpenGLSurfaceView;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.ui.CustomAdapter;
import com.gamesbykevin.breakout.util.StatDescription;
import com.gamesbykevin.breakout.util.UtilityHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

import javax.microedition.khronos.opengles.GL10;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.gamesbykevin.breakout.game.GameHelper.WIN;
import static com.gamesbykevin.breakout.game.Game.STEP;
import static com.gamesbykevin.breakout.game.GameHelper.getStatDescription;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.WIDTH;

public class GameActivity extends BaseActivity implements AdapterView.OnItemClickListener, SensorEventListener {

    //our open GL surface view
    private GLSurfaceView glSurfaceView;

    /**
     * Create a random object which the seed as the current time stamp
     */
    private static Random RANDOM;

    //Our game manager class
    private static Game GAME;

    //keep track of our level progress
    public static Statistics STATISTICS;

    //our list of levels
    public static Levels LEVELS;

    //has the activity been paused
    private boolean paused = false;

    //our layout parameters
    private LinearLayout.LayoutParams layoutParams;

    //a list of layouts on the game screen, separate from opengl layout
    private List<ViewGroup> layouts;

    /**
     * Different steps in the game
     */
    public enum Screen {
        Loading,
        Ready,
        GameOver,
        LevelSelect
    }

    //current screen we are on
    private Screen screen = Screen.Loading;

    //our custom adapter that we bind to GridView
    private CustomAdapter customAdapter;

    //manage all the phones sensors
    private SensorManager sensorManager;

    //sensor to detect tilt
    private Sensor accelerometer;

    //the values from our accelerometer sensor
    private float[] gravity;

    //the minimum tilt needed to be considered valid
    private static final float TILT_MIMINUM = 1.75f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //call parent
        super.onCreate(savedInstanceState);

        //all the levels in the game
        if (LEVELS == null)
            LEVELS = new Levels(this);

        //track our level progress
        STATISTICS = new Statistics(this, LEVELS.getSize());

        //create our game manager
        GAME = new Game(this);

        //set the content view
        setContentView(R.layout.activity_game);

        //obtain our open gl surface view object for reference
        this.glSurfaceView = (OpenGLSurfaceView)findViewById(R.id.openglView);

        //add the layouts to our list
        this.layouts = new ArrayList<>();
        this.layouts.add((LinearLayout)findViewById(R.id.gameOverLayoutDefault));
        this.layouts.add((LinearLayout)findViewById(R.id.loadingScreenLayout));
        this.layouts.add((TableLayout)findViewById(R.id.levelSelectLayout));

        //if we want tilt control, make sure our settings match the same
        if (!super.getBooleanValue(R.string.control_file_key)) {

            //get the sensor manager that manages all sensors
            this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            //get the tilt sensor
            if (this.sensorManager != null)
                this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        //update level select screen
        refreshLevelSelect();
    }

    public Sensor getAccelerometer() {
        return this.accelerometer;
    }

    public static Game getGame() {
        return GAME;
    }

    /**
     * Update the level select screen with the current data
     */
    public void refreshLevelSelect() {

        //get the grid view reference and assign on click
        GridView levelSelectGrid = (GridView)findViewById(R.id.levelSelectGrid);
        levelSelectGrid.setOnItemClickListener(this);

        //create the custom adapter using the level selection layout and data to populate it
        this.customAdapter = new CustomAdapter(this, R.layout.level_selection, STATISTICS.getList());

        //set our adapter to the grid view
        levelSelectGrid.setAdapter(this.customAdapter);

        //show current position, without having to scroll
        levelSelectGrid.setSelection(STATISTICS.getIndex());
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        //assign the level selected
        STATISTICS.setIndex(position);

        //show loading screen while we reset
        setScreen(Screen.Loading);

        //reset the game board
        STEP = Step.Reset;
    }

    /**
     * Get our random object.<br>
     * If object is null a new instance will be instantiated
     * @return Random object used to generate random events
     */
    public static Random getRandomObject() {

        //create the object if null
        if (RANDOM == null) {

            //get the current timestamp
            final long time = System.nanoTime();

            //create our Random object
            RANDOM = new Random(time);
        }

        return RANDOM;
    }

    @Override
    protected void onStart() {

        //call parent
        super.onStart();
    }

    @Override
    protected void onDestroy() {

        //call parent
        super.onDestroy();

        //cleanup resources
        if (GAME != null) {
            try {
                GAME.dispose();
            } catch (Exception e) {
                UtilityHelper.handleException(e);
            }
        }
    }

    @Override
    protected void onPause() {

        //call parent
        super.onPause();

        if (this.sensorManager != null && this.accelerometer != null)
            this.sensorManager.unregisterListener(this);

        //pause the game
        getGame().pause();

        //flag paused true
        this.paused = true;

        //pause the game view
        glSurfaceView.onPause();

        //flag for recycling
        glSurfaceView = null;

        //stop all sound
        stopSound();
    }

    @Override
    protected void onResume() {

        //call parent
        super.onResume();

        //register the listener upon resume
        if (this.sensorManager != null && this.accelerometer != null)
            this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_UI);

        //resume sound playing
        super.playSong(R.raw.theme);

        //if the game was previously paused create a new view
        if (this.paused) {

            //flag paused false
            this.paused = false;

            //create a new OpenGL surface view
            glSurfaceView = new OpenGLSurfaceView(this);

            //resume the game view
            glSurfaceView.onResume();

            //remove layouts from the parent view
            for (int i = 0; i < layouts.size(); i++) {
                ((ViewGroup)layouts.get(i).getParent()).removeView(layouts.get(i));
            }

            //set the content view for our open gl surface view
            setContentView(glSurfaceView);

            //add the layouts to the current content view
            for (int i = 0; i < layouts.size(); i++) {
                super.addContentView(layouts.get(i), getLayoutParams());
            }

        } else {

            //resume the game view
            glSurfaceView.onResume();
        }

        //determine what screen(s) are displayed
        setScreen(getScreen());
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void setScreen(final Screen screen) {

        //assign step
        this.screen = screen;

        //default all layouts to hidden
        for (int i = 0; i < layouts.size(); i++) {
            setLayoutVisibility(layouts.get(i), false);
        }

        //only display the correct screens
        switch (getScreen()) {

            //show loading screen
            case Loading:
                setLayoutVisibility((ViewGroup)findViewById(R.id.loadingScreenLayout), true);
                break;

            //decide which game over screen is displayed
            case GameOver:
                setLayoutVisibility((ViewGroup)findViewById(R.id.gameOverLayoutDefault), true);
                break;

            //show level select screen
            case LevelSelect:
                setLayoutVisibility((ViewGroup)findViewById(R.id.levelSelectLayout), true);
                break;

            //don't re-enable any
            case Ready:
                break;
        }
    }

    private LinearLayout.LayoutParams getLayoutParams() {

        if (this.layoutParams == null)
            this.layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT);

        return this.layoutParams;
    }

    public void setLayoutVisibility(final ViewGroup layoutView, final boolean visible) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //assign visibility accordingly
                layoutView.setVisibility(visible ? VISIBLE : INVISIBLE);

                //if the layout is visible, make sure it is displayed
                if (visible) {
                    layoutView.invalidate();
                    layoutView.bringToFront();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        //if not on level select screen, go to it
        if (getScreen() != Screen.LevelSelect) {

            //update list so it displays correct information
            refreshLevelSelect();

            //go to level select screen
            setScreen(Screen.LevelSelect);

            //go back to start step
            STEP = Step.Start;

            //no need to continue here
            return;
        }

        //call parent
        super.onBackPressed();
    }

    /**
     * Update the next/restart button text that is displayed depending on if the game is over or level complete
     */
    public void updateActionButtonText() {

        //determine what text is showing
        if (WIN) {
            ((Button)findViewById(R.id.ButtonNext)).setText(getString(R.string.button_text_next));
        } else {
            ((Button)findViewById(R.id.ButtonNext)).setText(getString(R.string.button_text_restart));
        }
    }

    public void onClickNext(View view) {

        if (WIN) {
            //since we won, move to the next level
            STATISTICS.setIndex(STATISTICS.getIndex() + 1);
        } else {
            //if we lost, reset the lives
            getStatDescription().setDescription(StatDescription.DEFAULT_LIVES);
        }

        //play song
        super.playSong(R.raw.theme);

        //show loading screen while we reset
        setScreen(Screen.Loading);

        //reset the game board
        STEP = Step.Reset;
    }

    public void onClickLevelSelect(View view) {

        //resume sound playing
        super.playSong(R.raw.theme);

        //go back to level select screen
        setScreen(Screen.LevelSelect);

        //update level select
        refreshLevelSelect();
    }

    public void onClickMenu(View view) {

        //go back to the main game menu
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //if no values exist, do nothing
        if (event.values == null)
            return;

        //make sure we have the correct sensor
        switch (event.sensor.getType()) {

            //if accelerometer, get the values
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values;
                break;
        }

        //values should have been retrieved
        if (gravity == null) {
            UtilityHelper.logEvent("gravity is null");
            return;
        } else {

            //make sure the game exists before we try to update it
            if (getGame() != null) {

                //check our thresh-hold to ensure the tilt is valid
                if (gravity[0] <= -TILT_MIMINUM) {
                    //we are tilting right
                    getGame().updateTilt(WIDTH, true, Paddle.TOUCH_POWER_50);
                } else if (gravity[0] >= TILT_MIMINUM) {
                    //we are tilting left
                    getGame().updateTilt(0, true, Paddle.TOUCH_POWER_50);
                } else {
                    //no tilt, stop the paddle if it exists
                    getGame().updateTilt(0, false);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}