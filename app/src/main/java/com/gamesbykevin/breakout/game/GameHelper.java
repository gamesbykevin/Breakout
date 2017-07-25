package com.gamesbykevin.breakout.game;

import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.opengl.Textures;
import com.gamesbykevin.breakout.util.StatDescription;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.MANAGER;
import static com.gamesbykevin.breakout.game.Manager.STEP;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.FPS;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.HEIGHT;
import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.WIDTH;

/**
 * Game helper methods
 * @author GOD
 */
public final class GameHelper 
{
	/**
	 * The duration we wait until we show the game over screen
	 */
	protected static final int GAME_OVER_FRAMES_DELAY = (FPS * 2);

	/**
	 * The number of frames to display get ready text
	 */
	protected static final int GET_READY_FRAMES_LIMIT = FPS;

	/**
	 * Our object that will keep track of our lives
	 */
	public static StatDescription STAT_DESCRIPTION = new StatDescription();

	//did we win?
	public static boolean WIN = false;


	private static Entity ENTITY = new Entity(0, 0);

    /**
     * Start the current assigned level all over 
     * @param game Our game reference object
     */
    protected final static void resetLevel(final Manager game)
	{
		//reset balls
		game.getBalls().reset();
		
        //reset paddle and ball(s)
		game.getBalls().add(game.getPaddle());
    	
        //hide power ups
		game.getPowerups().reset();
		
        //populate the bricks accordingly
        game.getLevels().populate(game.getBricks());
	}

    /**
     * Restart the same level because the player lost a life
     */
    protected final static void restartLevel()
    {
        //reset paddle and ball(s)
		MANAGER.getBalls().add(MANAGER.getPaddle());
    }

    /**
     * Check if the player lost a life or if the level has been completed
     */
    protected final static boolean isGameOver()
	{
		//if there are no more bricks to be broken the game is over
		if (MANAGER.getBricks().getCount() <= 0) {
			WIN = true;
			return true;
		}

		//if there are no more balls in play, the game is over
		if (MANAGER.getBalls().getCount() < 1) {
			WIN = true;
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
		//render the bricks
		MANAGER.getBricks().render(openGL);

		//render the power ups
		MANAGER.getPowerups().render(openGL);

		//render the balls
		MANAGER.getBalls().render(openGL);

		//render the paddle
		MANAGER.getPaddle().render(openGL);

		//render number of lives
		STAT_DESCRIPTION.render(openGL);

		//if game over step
		if (STEP == Manager.Step.GameOver) {
			if (WIN) {
				//if we win display "Level Complete" text
				ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_WORD_LEVEL_COMPLETED);
			} else {
				if (STAT_DESCRIPTION.getStatValue() <= 0) {
					//if no more lives, the game is over
					ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_WORD_GAMEOVER);
				} else {
					//if we lose display "Ready" text
					ENTITY.render(openGL, 0, 0, WIDTH, HEIGHT, Textures.TEXTURE_ID_WORD_READY);
				}
			}
		}
    }
}