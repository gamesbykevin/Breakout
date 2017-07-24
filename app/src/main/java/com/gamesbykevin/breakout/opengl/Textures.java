package com.gamesbykevin.breakout.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.brick.Bricks;
import com.gamesbykevin.breakout.laser.Laser;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.powerup.Powerup;
import com.gamesbykevin.breakout.util.StatDescription;
import com.gamesbykevin.breakout.util.UtilityHelper;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 7/23/2017.
 */

public class Textures {

    //array containing all the texture ids
    public static int[] IDS;

    private final Context activity;

    //keep track of the current index
    private int index = 0;

    //how many things are we loading here
    private static final int TOTAL_BALLS = 6;
    private static final int TOTAL_BRICKS = Bricks.Key.values().length;
    private static final int TOTAL_NUMBERS = 10;
    private static final int TOTAL_PARTICLES = 7;
    private static final int TOTAL_LASERS = 1;
    private static final int TOTAL_PADDLES = 1;
    private static final int TOTAL_POWERUPS = Powerup.Key.values().length * 8;

    public Textures(Context activity) {

        this.activity = activity;

        //create array containing all the texture ids
        IDS = new int[
                TOTAL_BALLS + TOTAL_BRICKS + TOTAL_NUMBERS +
                TOTAL_PARTICLES + TOTAL_LASERS + TOTAL_PADDLES + TOTAL_POWERUPS];
    }

    /**
     * Load all the textures
     * @param openGL
     */
    public void loadTextures(GL10 openGL) {

        //reset index
        this.index = 0;

        //sprite sheet containing a lot of images
        Bitmap sheet = BitmapFactory.decodeResource(activity.getResources(), R.drawable.sheet);

        //get each ball from the sprite sheet
        for (int i = 0; i < TOTAL_BALLS; i++) {

            //get the current ball
            Bitmap ball = Bitmap.createBitmap(sheet, (Ball.DIMENSIONS * i), 0, Ball.DIMENSIONS, Ball.DIMENSIONS);

            //load the texture
            loadTexture(ball, openGL);
        }

        //get each brick from the sprite sheet
        for (Bricks.Key key : Bricks.Key.values()) {

            //get the current brick
            Bitmap brick = Bitmap.createBitmap(sheet, key.getX(), key.getY(), Brick.WIDTH_ANIMATION, Brick.HEIGHT_ANIMATION);

            //load the texture
            loadTexture(brick, openGL);
        }

        //load the laser texture
        loadTexture(Bitmap.createBitmap(sheet, 0, 0, Laser.WIDTH, Laser.HEIGHT), openGL);

        //load the paddle texture
        loadTexture(Bitmap.createBitmap(sheet, 80, 64, Paddle.WIDTH, Paddle.HEIGHT), openGL);

        //load all of the power ups
        for (Powerup.Key key : Powerup.Key.values()) {

            //each power up has 8 animations
            for (int count = 0; count < 8; count++) {

                //calculate current x-coordinate
                int x = (count * Powerup.ANIMATION_WIDTH);

                //get the current animation
                Bitmap powerup = Bitmap.createBitmap(sheet, x, key.getY(), Powerup.ANIMATION_WIDTH, Powerup.ANIMATION_HEIGHT);

                //load the texture
                loadTexture(powerup, openGL);
            }
        }

        //load the texture for the particles
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.particle1), openGL);
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.particle2), openGL);
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.particle3), openGL);
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.particle4), openGL);
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.particle5), openGL);
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.particle6), openGL);
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.particle7), openGL);

        //load the textures for each number display
        for (int i = 0; i < 10; i++) {

            //calculate current x-coordinate
            int x = (i * StatDescription.ANIMATION_WIDTH);

            //get the current animation
            Bitmap powerup = Bitmap.createBitmap(sheet, x, 0, StatDescription.ANIMATION_WIDTH, StatDescription.ANIMATION_HEIGHT);

            //load the texture
            loadTexture(powerup, openGL);
        }
    }


    /**
     * Load a single texture
     * @param bitmap Image we want to convert into a texture
     * @param openGL Open GL Context
     * @return texture id from generating a texture
     */
    public int loadTexture(Bitmap bitmap, GL10 openGL) {

        try {

            //our container to generate the textures
            openGL.glGenTextures(1, IDS, index);

            //bind the texture id
            openGL.glBindTexture(GL10.GL_TEXTURE_2D, IDS[index]);

            if (false) {
                //we want smoother images
                openGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                openGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
            } else {
                //not smoother images
                openGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
                openGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            }

            //allow any size texture
            openGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);

            //allow any size texture
            openGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            //add bitmap to texture
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            //we no longer need the resource
            bitmap.recycle();

            if (IDS[index] == 0) {
                throw new Exception("Error loading texture: " + index);
            } else {
                //display texture id
                UtilityHelper.logEvent("Texture loaded id: " + IDS[index]);
            }

        } catch (Exception e) {
            UtilityHelper.handleException(e);
        }

        //get texture id
        int value = IDS[index];

        //keep increasing the index
        index++;

        //return our value
        return value;
    }
}