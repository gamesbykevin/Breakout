package com.gamesbykevin.breakout.game;

import com.gamesbykevin.breakout.game.Game.Step;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.opengl.Textures;
import com.gamesbykevin.breakout.util.StatDescription;
import com.gamesbykevin.breakout.wall.Wall;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.getGame;
import static com.gamesbykevin.breakout.game.Game.STEP;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.FPS;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.HEIGHT;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.WIDTH;
import static com.gamesbykevin.breakout.util.StatDescription.STAT_WIDTH;

/**
 * Game helper methods
 * @author GOD
 */
public final class GameHelper 
{
	/**
	 * The duration we wait until we show the game over screen
	 */
	protected static final int GAME_OVER_FRAMES_DELAY = (int)(FPS * 2.5);

	/**
	 * The number of frames to display get ready text
	 */
	protected static final int GET_READY_FRAMES_LIMIT = (int)(FPS * 1.5);

	/**
	 * Our object that will keep track of our lives
	 */
	private static StatDescription STAT_DESCRIPTION, LEVEL_DESCRIPTION;

	//where do we render the stat description
	public static final int STAT_X = Wall.WIDTH;
	public static final int STAT_Y = HEIGHT - (int)(HEIGHT * .07);

	//where do we render the word "lives"
	public static int LIVES_X = STAT_X + (STAT_WIDTH * 2);
	public static final int LIVES_Y = STAT_Y;
	public static final int LIVES_W = 113;
	public static final int LIVES_H = 50;

	public static final int LEVEL_TEXT_X = STAT_X;
	public static final int LEVEL_TEXT_Y = STAT_Y - (int)(LIVES_H * 1.5);
	public static final int LEVEL_TEXT_W = 109;
	public static final int LEVEL_TEXT_H = 50;

	public static final int LEVEL_STAT_X = LEVEL_TEXT_X + LEVEL_TEXT_W + (int)(STAT_WIDTH * .5);
	public static final int LEVEL_STAT_Y = LEVEL_TEXT_Y;

	//did we win?
	public static boolean WIN = false;

	//do we show tap start image
	public static boolean TAP_START = false;

	public static StatDescription getStatDescription() {

		if (STAT_DESCRIPTION == null)
			STAT_DESCRIPTION = new StatDescription();

		STAT_DESCRIPTION.setX(STAT_X);
		STAT_DESCRIPTION.setY(STAT_Y);

		return STAT_DESCRIPTION;
	}

	public static StatDescription getLevelDescription() {

		if (LEVEL_DESCRIPTION == null)
			LEVEL_DESCRIPTION = new StatDescription();

		LEVEL_DESCRIPTION.setX(LEVEL_STAT_X);
		LEVEL_DESCRIPTION.setY(LEVEL_STAT_Y);

		return LEVEL_DESCRIPTION;
	}

	private static Entity ENTITY = new Entity(0, 0);

    /**
     * Start the current assigned level all over 
     */
    protected final static void resetLevel()
	{
		//flag tap start so we display the notification
		TAP_START = true;

		//reset balls
		getGame().getBalls().reset();
		
        //reset paddle and ball(s)
		getGame().getBalls().add(getGame().getPaddle());
    	
        //hide power ups
		getGame().getPowerups().reset();
		
        //populate the bricks accordingly
		getGame().getLevels().populate(getGame().getBricks());
	}

    /**
     * Restart the same level because the player lost a life
     */
    protected final static void restartLevel()
    {
        //reset paddle and ball(s)
		getGame().getBalls().add(getGame().getPaddle());
    }

    /**
     * Check if the player lost a life or if the level has been completed
     */
    protected final static boolean isGameOver()
	{
		//if there are no more bricks to be broken the game is over
		if (getGame().getBricks().getCount() <= 0) {
			WIN = true;
			return true;
		}

		//if we lost all of our balls the game is over (temporarily)
		if (getGame().getBalls().getCount() < 1) {
			WIN = false;
			return true;
		}

		//flag win false
		WIN = false;

		//game is not yet over
		return false;
	}

    /**
     * Render the game accordingly
     * @param openGL Place to write pixel data
     * @throws Exception
     */
    public static final void render(final GL10 openGL)
    {
		//make sure we are supporting alpha for transparency
		openGL.glEnable(GL10.GL_BLEND);
		openGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		//render the background elements
		ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_BACKGROUND);
		ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_BORDER);

		//render the lives information
		getStatDescription().render(openGL);
		ENTITY.render(openGL, LIVES_X, LIVES_Y, LIVES_W, LIVES_H, Textures.TEXTURE_ID_WORD_LIVES);

		//render the current level #
		getLevelDescription().render(openGL);
		ENTITY.render(openGL, LEVEL_TEXT_X, LEVEL_TEXT_Y, LEVEL_TEXT_W, LEVEL_TEXT_H, Textures.TEXTURE_ID_WORD_LEVEL);

		//render the bricks
		getGame().getBricks().render(openGL);

		//render the power ups
		getGame().getPowerups().render(openGL);

		//render the balls
		getGame().getBalls().render(openGL);

		//render the paddle
		getGame().getPaddle().render(openGL);

		//if game over step
		if (STEP == Step.GameOver) {
			if (WIN) {
				//if we win display "Level Complete" text
				ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_WORD_LEVEL_COMPLETED);
			} else {
				if (getStatDescription().getStatValue() <= 0) {
					//if no more lives, the game is over
					ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_WORD_GAMEOVER);
				} else {
					//if we lose display "Ready" text
					ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_WORD_READY);
				}
			}
		} else {
			if (TAP_START)
				ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_WORD_TAP_START);
		}
    }
}