package com.gamesbykevin.breakout.brick;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.brick.Bricks.Key;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.panel.GamePanel;
import com.gamesbykevin.breakout.thread.MainThread;

import android.graphics.Canvas;

public final class Brick extends Entity implements ICommon
{
	//is the brick dead?
	private boolean dead = false;
	
	//is this brick not meant to be destroyed?
	private boolean solid = false;
	
	//will this brick become a power up
	private boolean powerup = false;
	
	//how many frames has the brick been dead
	private int frames = FRAMES_PARTICLE_LIMIT + 1;
	
	/**
	 * The number of frames to show the particles before we hide
	 */
	private static final int FRAMES_PARTICLE_LIMIT = (MainThread.FPS / 4);
	
	/**
	 * The size of a single particle
	 */
	private static final int PARTICLE_DIMENSION = 10;

	/**
	 * How fast the particles move
	 */
	private static final int PARTICLE_SPEED = (PARTICLE_DIMENSION / 2);
	
	/**
	 * Default width of a brick
	 */
	public static final int WIDTH = 40;
	
	/**
	 * Default height of a brick
	 */
	public static final int HEIGHT = 20;
	
	//animation for the brick
	private Key key;
	
	//animation for the particle
	private Assets.ImageGameKey particleKey = Assets.ImageGameKey.Particle1;
	
	protected Brick(final Key key)
	{
		super(null, WIDTH, HEIGHT);
		
		//assign animation key
		setKey(key);
	}
	
	@Override
	public final void reset() 
	{
		//flag dead false
		setDead(false);
		
		//flag solid false
		setSolid(false);

		//make sure it doesn't have any particles (yet :))
		removeParticles();
	}
	
	/**
	 * Assign random particle key
	 */
	private void assignParticleKey()
	{
		switch (GamePanel.RANDOM.nextInt(7))
		{
			case 0:
				particleKey = Assets.ImageGameKey.Particle1;
				break;
			
			case 1:
				particleKey = Assets.ImageGameKey.Particle2;
				break;
				
			case 2:
				particleKey = Assets.ImageGameKey.Particle3;
				break;
				
			case 3:
				particleKey = Assets.ImageGameKey.Particle4;
				break;
				
			case 4:
				particleKey = Assets.ImageGameKey.Particle5;
				break;
				
			case 5:
				particleKey = Assets.ImageGameKey.Particle6;
				break;
				
			case 6:
				particleKey = Assets.ImageGameKey.Particle7;
				break;
				
			default:
				particleKey = Assets.ImageGameKey.Particle1;
				break;
		}
	}
	
	/**
	 * Assign the animation key
	 * @param key Animation key for the brick
	 */
	public final void setKey(final Key key)
	{
		this.key = key;
	}
	
	/**
	 * Get the animation key
	 * @return The assigned animation key
	 */
	public Key getKey()
	{
		return this.key;
	}
	
	/**
	 * Add particle effects
	 */
	public void addParticles()
	{
		//reset frame count
		this.frames = 0;
		
		//pick random particle key
		assignParticleKey();
	}
	
	/**
	 * Remove the particles for this brick
	 */
	public void removeParticles()
	{
		//exceed frame limit
		this.frames = FRAMES_PARTICLE_LIMIT;
		
		//remove particle animation key
		this.particleKey = null;
	}
	
	/**
	 * Flag the brick dead
	 * @param dead true if dead, false otherwise
	 */
	public void setDead(final boolean dead)
	{
		this.dead = dead;
	}
	
	/**
	 * Is the brick dead?
	 * @return true if dead, false otherwise
	 */
	public boolean isDead()
	{
		return this.dead;
	}
	
	public void setPowerup(final boolean powerup)
	{
		this.powerup = powerup;
	}
	
	/**
	 * Is this brick a power up?
	 * @return true = yes, false = no;
	 */
	public boolean hasPowerup()
	{
		return this.powerup;
	}
	
	/**
	 * Flag if the brick can be broken
	 * @param solid true if solid and can't be broken, false otherwise
	 */
	public void setSolid(final boolean solid)
	{
		this.solid = solid;
	}
	
	/**
	 * Is the brick solid?
	 * @return true if solid and can't be broken, false otherwise
	 */
	public boolean isSolid()
	{
		return this.solid;
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public void update() throws Exception 
	{
		//if dead keep count of frames
		if (isDead())
			this.frames++;
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		//if dead render particles
		if (isDead())
		{
			//only render the particles for a limited number of frames
			if (frames <= FRAMES_PARTICLE_LIMIT && this.particleKey != null)
			{
				//store values
				final double x = getX();
				final double y = getY();
				final double w = getWidth();
				final double h = getHeight();
				
				//the middle coordinate
				final double mx = x + (w / 2);
				final double my = y + (h / 2);
				
				//assign dimension for the particle
				super.setWidth(PARTICLE_DIMENSION);
				super.setHeight(PARTICLE_DIMENSION);
				
				//place particle and then render (nw)
				super.setX(mx - (frames * PARTICLE_SPEED));
				super.setY(my - (frames * PARTICLE_SPEED));
				super.render(canvas, Images.getImage(this.particleKey));
				
				//place particle and then render (ne)
				super.setX(mx + (frames * PARTICLE_SPEED));
				super.setY(my - (frames * PARTICLE_SPEED));
				super.render(canvas, Images.getImage(this.particleKey));
				
				//place particle and then render (sw)
				super.setX(mx - (frames * PARTICLE_SPEED));
				super.setY(my + (frames * PARTICLE_SPEED));
				super.render(canvas, Images.getImage(this.particleKey));
				
				//place particle and then render (se)
				super.setX(mx + (frames * PARTICLE_SPEED));
				super.setY(my + (frames * PARTICLE_SPEED));
				super.render(canvas, Images.getImage(this.particleKey));
				
				//restore values
				super.setX(x);
				super.setY(y);
				super.setWidth(w);
				super.setHeight(h);
			}
		}
	}
}