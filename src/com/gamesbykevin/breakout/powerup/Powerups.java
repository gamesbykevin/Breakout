package com.gamesbykevin.breakout.powerup;

import java.util.ArrayList;

import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;

import android.graphics.Canvas;

public class Powerups extends Entity implements ICommon
{
	//list of power ups in the game
	private ArrayList<Powerup> powerups;
	
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
	public void add(final Brick brick) throws Exception
	{
		//lets see if we can re-use an existing power up
		for (Powerup powerup : getPowerups())
		{
			//if this is hidden we can re-use
			if (powerup.isHidden())
			{
				//reset
				powerup.reset();
				
				//assign dimensions
				powerup.setWidth(Powerup.WIDTH);
				powerup.setHeight(Powerup.HEIGHT);
				
				//assign position at the brick
				powerup.setX(brick.getX() + (brick.getWidth() / 2) - (powerup.getWidth() / 2));
				powerup.setY(brick.getY());
				
				//no need to continue
				return;
			}
		}
		
		//we weren't able to re-use a power up so let's add one
		Powerup powerup = new Powerup();
		
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
				
				//determine which power up to apply
				switch ((Powerup.Key)powerup.getSpritesheet().getKey())
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
						getGame().getPaddle().setLaser(true);
						break;
						
					case ExtraLife:
						//add extra life here
						getGame().setLives(getGame().getLives() + 1);
						break;
						
					case ExtraBalls:
						getGame().getBalls().add();
						getGame().getBalls().add();
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
						throw new Exception("Key not found here: " + powerup.getSpritesheet().getKey().toString());
				}
			}
			
			//update power up location etc...
			powerup.update();
		}
	}

	@Override
	public void reset() 
	{
		//reset all power ups
		for (Powerup powerup : getPowerups())
		{
			//hide all power ups
			powerup.reset();
		}
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//render all power ups
		for (Powerup powerup : getPowerups())
		{
			powerup.render(canvas);
		}
	}
}