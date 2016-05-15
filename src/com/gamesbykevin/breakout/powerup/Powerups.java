package com.gamesbykevin.breakout.powerup;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;
import com.gamesbykevin.breakout.panel.GamePanel;

import android.graphics.Canvas;

public class Powerups extends Entity implements ICommon
{
	//list of power ups in the game
	private ArrayList<Powerup> powerups;
	
	//different animations for the power ups
	public enum Key
	{
		Magnet, Expand, Shrink, Laser, ExtraLife, ExtraBalls, SpeedUp, SpeedDown, Fireball
	}
	
	/**
	 * Default width of a power up animation
	 */
	public static final int ANIMATION_WIDTH = 44;
	
	/**
	 * Default height of a power up animation
	 */
	public static final int ANIMATION_HEIGHT = 22;

	/**
	 * The number of columns we need to map the animation
	 */
	private static final int ANIMATION_COLS = 8;
	
	/**
	 * The number of rows we need to map the animation
	 */
	private static final int ANIMATION_ROWS = 1;
	
	/**
	 * The animation delay
	 */
	private static final long ANIMATION_DELAY = 75L;
	
	/**
	 * Constructor
	 * @param game
	 * @throws Exception
	 */
	public Powerups(Game game) throws Exception
	{
		super(game, Powerup.WIDTH, Powerup.HEIGHT);

		//create list of power ups
		this.powerups = new ArrayList<Powerup>();
		
		//set animation coordinates
		final int x = 0;
		int y = 0;
		
		//map the power up animations
		for (Key key : Key.values())
		{
			switch (key)
			{
				case Magnet:
					y = 404;
					break;
					
				case Expand:
					y = 316;
					break;
					
				case Shrink:
					y = 338;
					break;
					
				case Laser:
					y = 448;
					break;
					
				case ExtraLife:
					y = 426;
					break;
					
				case ExtraBalls:
					y = 360;
					break;
					
				case SpeedUp:
					y = 294;
					break;
					
				case SpeedDown:
					y = 272;
					break;
					
				case Fireball:
					y = 250;
					break;
					
				default:
					throw new Exception("Key not found: " + key.toString());
			}
			
			//create new animation
			Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), x, y, ANIMATION_WIDTH, ANIMATION_HEIGHT, ANIMATION_COLS, ANIMATION_ROWS, ANIMATION_COLS * ANIMATION_ROWS);
			
			//the animation will always loop
			animation.setLoop(true);
			
			//assign the animation delay
			animation.setDelay(ANIMATION_DELAY);
			
			//now add animation to the sprite sheet
			super.getSpritesheet().add(key, animation);
		}
	}
	
	/**
	 * Get a random key
	 * @return pick a random power up
	 */
	private Key getRandomKey()
	{
		return (Key.values()[GamePanel.RANDOM.nextInt(Key.values().length)]);
	}
	
	/**
	 * Get the list of power ups
	 * @return The list of power ups that exist
	 */
	public ArrayList<Powerup> getPowerups()
	{
		return this.powerups;
	}
	
	/**
	 * Add power up
	 * @param brick The brick whose location we want to place the power up
	 */
	public void add(final Brick brick)
	{
		//lets see if we can re-use an existing power up
		for (Powerup powerup : getPowerups())
		{
			//if this is hidden we can re-use
			if (powerup.isHidden())
			{
				//assign dimensions
				powerup.setWidth(Powerup.WIDTH);
				powerup.setHeight(Powerup.HEIGHT);
				
				//assign position at the brick
				powerup.setX(brick.getX() + (brick.getWidth() / 2) - (powerup.getWidth() / 2));
				powerup.setY(brick.getY());
				
				//stop hiding it so we can interact/display
				powerup.setHidden(false);
				
				//no need to continue
				return;
			}
		}
		
		//we weren't able to re-use a power up so let's add one
		Powerup powerup = new Powerup(getRandomKey());
		
		//assign position at the brick
		powerup.setX(brick.getX() + (brick.getWidth() / 2) - (powerup.getWidth() / 2));
		powerup.setY(brick.getY());
		
		//add power up to the list
		getPowerups().add(powerup);
	}
	
	@Override
	public void update() throws Exception 
	{
		//check all power ups
		for (Powerup powerup : getPowerups())
		{
			//skip the power up if it is hidden
			if (powerup.isHidden())
				continue;
			
			//check for paddle collision
			if (getGame().getPaddle().hasCollision(powerup))
			{
				//flag the power up as hidden
				powerup.setHidden(true);
				
				switch (powerup.getKey())
				{
					case Magnet: 
						getGame().getPaddle().setMagnet(true);
						break;
						
					case Expand:
						getGame().getPaddle().expand();
						break;
						
					case Shrink:
						getGame().getPaddle().shrink();
						break;
						
					case Laser:
						flag lasers for a few seconds and then paddle will add them etc..Powerups.Key.
						break;
						
					case ExtraLife:
						
						break;
						
					case ExtraBalls:
						
						break;
						
					case SpeedUp:
						getGame().getBalls().speedUp();
						break;
						
					case SpeedDown:
						getGame().getBalls().speedDown();
						break;
						
					case Fireball:
						getGame().getBalls().setFire(true);
						break;
				
					default:
						throw new Exception("Key not found here: " + powerup.getKey().toString());
				}
			}
			
			//update power up location etc...
			powerup.update();
		}
		
		//update the animation
		super.getSpritesheet().update();
	}

	@Override
	public void reset() 
	{
		//reset all power ups
		for (Powerup powerup : getPowerups())
		{
			//hide all power ups
			powerup.setHidden(true);
		}
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//render all power ups
		for (Powerup powerup : getPowerups())
		{
			//skip the power up if it is hidden
			if (powerup.isHidden())
				continue;
			
			//assign coordinates
			super.setX(powerup.getX());
			super.setY(powerup.getY());
			
			//assign dimensions
			super.setWidth(powerup.getWidth());
			super.setHeight(powerup.getHeight());
			
			//assign animation
			super.getSpritesheet().setKey(powerup.getKey());
			
			//render the power up
			super.render(canvas);
		}
	}
}