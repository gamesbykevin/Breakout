package com.gamesbykevin.breakout.brick;

import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.brick.Bricks.Key;
import com.gamesbykevin.breakout.common.ICommon;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.opengl.OpenGLSurfaceView.FPS;

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
	private static final int FRAMES_PARTICLE_LIMIT = (FPS / 4);
	
	/**
	 * The size of a single particle
	 */
	private static final int PARTICLE_DIMENSION = 10;

	/**
	 * How fast the particles move
	 */
	private static final int PARTICLE_SPEED = (PARTICLE_DIMENSION / 2);
	
	/**
	 * Animation width of a brick
	 */
	public static final int WIDTH_ANIMATION = 40;
	
	/**
	 * Animation height of a brick
	 */
	public static final int HEIGHT_ANIMATION = 20;
	
	/**
	 * Default width of a brick
	 */
	public static final int WIDTH_NORMAL = 40;
	
	/**
	 * Default height of a brick
	 */
	public static final int HEIGHT_NORMAL = 20;
	
	/**
	 * Small width of a brick
	 */
	public static final int WIDTH_SMALL = 20;
	
	/**
	 * Small height of a brick
	 */
	public static final int HEIGHT_SMALL = 10;
	
	/**
	 * Xtra small width of a brick
	 */
	public static final int WIDTH_XSMALL = 10;
	
	/**
	 * Xtra small height of a brick
	 */
	public static final int HEIGHT_XSMALL = 5;

	/**
	 * The number of times a solid brick can be hit before it is dead
	 */
	private static final int COLLISIONS_LIMIT_SOLID = 3;
	
	/**
	 * The number of times a non-solid brick can be hit before it is dead
	 */
	private static final int COLLISIONS_LIMIT = 1;
	
	
	//the number of collisions required before the brick is dead
	private int collisions;
	
	/**
	 * Default Constructor
	 */
	protected Brick()
	{
		super(WIDTH_NORMAL, HEIGHT_NORMAL);
	}
	
	@Override
	public final void reset() 
	{
		//flag dead false
		setDead(false);
		
		//flag solid false
		setSolid(false);

		//flag power up false
		setPowerup(false);
		
		//make sure it doesn't have any particles (yet :))
		removeParticles();
	}
	
	/**
	 * Add particle effects
	 */
	public void addParticles()
	{
		//reset frame count
		this.frames = 0;
	}
	
	/**
	 * Remove the particles for this brick
	 */
	public void removeParticles()
	{
		//exceed frame limit
		this.frames = FRAMES_PARTICLE_LIMIT;
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
		
		//if the brick is solid assign the collisions
		if (isSolid())
		{
			setCollisions(COLLISIONS_LIMIT_SOLID);
		}
		else
		{
			setCollisions(COLLISIONS_LIMIT);
		}
	}
	
	/**
	 * Is the brick solid?
	 * @return true if solid and can't be broken, false otherwise
	 */
	public boolean isSolid()
	{
		return this.solid;
	}

	/**
	 * Assign the collisions
	 * @param collisions The number of collisions remaining before the brick is dead
	 */
	public void setCollisions(final int collisions)
	{
		this.collisions = collisions; 
	}
	
	/**
	 * Get the collisions
	 * @return The number of collisions remaining
	 */
	public int getCollisions()
	{
		return this.collisions;
	}
	
	/**
	 * Mark collision for the ball
	 */
	public void markCollision()
	{
		setCollisions(getCollisions() - 1);
		
		//if there are no more collisions remaining
		if (getCollisions() <= 0)
		{
			//flag dead
			setDead(true);
			
			//add particles
			addParticles();
		}
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public void update(GameActivity activity)
	{
		//if dead keep count of frames
		if (isDead())
			this.frames++;
	}

	@Override
	public void render(GL10 openGL)
	{
		//if dead render particles
		if (isDead())
		{
			//only render the particles for a limited number of frames
			if (frames <= FRAMES_PARTICLE_LIMIT)
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
				super.render(openGL);
				
				//place particle and then render (ne)
				super.setX(mx + (frames * PARTICLE_SPEED));
				super.setY(my - (frames * PARTICLE_SPEED));
				super.render(openGL);
				
				//place particle and then render (sw)
				super.setX(mx - (frames * PARTICLE_SPEED));
				super.setY(my + (frames * PARTICLE_SPEED));
				super.render(openGL);
				
				//place particle and then render (se)
				super.setX(mx + (frames * PARTICLE_SPEED));
				super.setY(my + (frames * PARTICLE_SPEED));
				super.render(openGL);
				
				//restore values
				super.setX(x);
				super.setY(y);
				super.setWidth(w);
				super.setHeight(h);
			}
		}
	}
}