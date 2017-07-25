package com.gamesbykevin.breakout.powerup;

import java.util.ArrayList;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.GameHelper;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.MANAGER;

public class Powerups extends Entity implements ICommon
{
	//list of power ups in the game
	private ArrayList<Powerup> powerups;
	
	/**
	 * Default constructor
	 */
	public Powerups()
	{
		super(Powerup.WIDTH, Powerup.HEIGHT);

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
	public void add(final Brick brick)
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
	public void update(GameActivity activity)
	{
		//possible sound effects to play
		boolean soundPowerup = false;
		boolean soundFireball = false;
		boolean soundNewlife = false;
		
		//check all power ups
		for (int i = 0; i < getPowerups().size(); i++)
		{
			//get the current power up
			Powerup powerup = getPowerups().get(i);

			//skip the power up if it is hidden
			if (powerup.isHidden())
				continue;
			
			//check for paddle collision
			if (MANAGER.getPaddle().hasCollision(powerup))
			{
				//flag the power up as hidden
				powerup.setHidden(true);

				//determine which power up to apply
				switch (powerup.getKey())
				{
					case Magnet:
						MANAGER.getPaddle().setMagnet(true);
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Expand:
						MANAGER.getPaddle().expand();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Shrink:
						MANAGER.getPaddle().shrink();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Laser:
						MANAGER.getPaddle().setLaser(true);
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case ExtraLife:
						GameHelper.STAT_DESCRIPTION.setDescription(GameHelper.STAT_DESCRIPTION.getStatValue() + 1);

						//play sound effect
						soundNewlife = true;
						break;
						
					case ExtraBalls:
						MANAGER.getBalls().add();
						MANAGER.getBalls().add();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case SpeedUp:
						MANAGER.getBalls().speedUp();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case SpeedDown:
						MANAGER.getBalls().speedDown();
						
						//play sound effect
						soundPowerup = true;
						break;
						
					case Fireball:
						MANAGER.getBalls().setFire(true);
						
						//play sound effect
						soundFireball = true;
						break;
				}
			}
			else
			{
				//update power up location etc...
				powerup.update(activity);
			}
		}

		//play sound effects accordingly
		if (soundPowerup)
			activity.playSound(R.raw.powerup);
		if (soundFireball)
			activity.playSound(R.raw.firepickup);
		if (soundNewlife)
			activity.playSound(R.raw.newlife);
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
	public void render(final GL10 openGL)
	{
		//render all power ups
		for (int i = 0; i < getPowerups().size(); i++) {
			getPowerups().get(i).render(openGL);
		}
	}
}