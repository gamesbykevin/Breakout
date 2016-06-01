package com.gamesbykevin.breakout.powerup;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;
import com.gamesbykevin.breakout.game.GameHelper;
import com.gamesbykevin.breakout.thread.MainThread;

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
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		if (this.powerups != null)
		{
			for (int i = 0; i < this.powerups.size(); i++)
			{
				if (this.powerups.get(i) != null)
				{
					this.powerups.get(i).dispose();
					this.powerups.set(i, null);
				}
			}
			
			this.powerups.clear();
			this.powerups = null;
		}
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
		//possible sound effects to play
		boolean soundPowerup = false;
		boolean soundFireball = false;
		boolean soundNewlife = false;
		
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
				
				if (MainThread.DEBUG)
					System.out.println("Key + " + powerup.getKey().toString());
				
				//determine which power up to apply
				switch (powerup.getKey())
				{
					case Magnet: 
						getGame().getPaddle().setMagnet(true);
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Expand:
						getGame().getPaddle().expand();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Shrink:
						getGame().getPaddle().shrink();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Laser:
						getGame().getPaddle().setLaser(true);
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case ExtraLife:
						GameHelper.addLife(getGame());
						
						//play sound effect
						soundNewlife = true;
						break;
						
					case ExtraBalls:
						getGame().getBalls().add();
						getGame().getBalls().add();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case SpeedUp:
						getGame().getBalls().speedUp();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case SpeedDown:
						getGame().getBalls().speedDown();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Fireball:
						getGame().getBalls().setFire(true);
						
						//play sound effect
						soundFireball = true;
						break;
				
					default:
						throw new Exception("Key not found here: " + powerup.getSpritesheet().getKey().toString());
				}
			}
			else
			{
				//update power up location etc...
				powerup.update();
			}
		}
		
		//play sound effects accordingly
		if (soundPowerup)
			Audio.play(Assets.AudioGameKey.Powerup);
		if (soundFireball)
			Audio.play(Assets.AudioGameKey.FirePowerup);
		if (soundNewlife)
			Audio.play(Assets.AudioGameKey.NewLife);
	}

	@Override
	public void reset() 
	{
		//reset all power ups
		for (Powerup powerup : getPowerups())
		{
			//hide any existing power ups
			powerup.setHidden(true);
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