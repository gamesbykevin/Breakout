package com.gamesbykevin.breakout.laser;

import java.util.ArrayList;

import com.gamesbykevin.breakout.activity.GameActivity;
import com.gamesbykevin.breakout.brick.Brick;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.paddle.Paddle;
import com.gamesbykevin.breakout.util.UtilityHelper;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.activity.GameActivity.getGame;

public class Lasers extends Entity implements ICommon 
{
	//the list of lasers
	private ArrayList<Laser> lasers;

	public Lasers()
	{
		super(Laser.WIDTH, Laser.HEIGHT);
		
		//create new list of lasers
		this.lasers = new ArrayList<Laser>();
	}

	@Override
	public void reset() 
	{
		//hide all existing lasers
		for (int i = 0; i < getLasers().size(); i++)
		{
			Laser laser = getLasers().get(i);
			laser.setHidden(true);
		}
	}

	@Override
	public void dispose()
	{
		super.dispose();
		
		if (this.lasers != null)
		{
			for (int i = 0; i < this.lasers.size(); i++)
			{
				if (this.lasers.get(i) != null)
				{
					this.lasers.get(i).dispose();
					this.lasers.set(i, null);
				}
			}
			
			this.lasers.clear();
			this.lasers = null;
		}
	}
	
	@Override
	public void update(GameActivity activity)
	{
		if (getLasers() != null)
		{
			for (int i = 0; i < getLasers().size(); i++)
			{
				Laser laser = getLasers().get(i);

				if (laser.isHidden())
					continue;
				
				//update location of laser
				laser.update();
				
				//set the length
				final int rowMax = getGame().getBricks().getBricks().length;
				final int colMax = getGame().getBricks().getBricks()[0].length;
				
				//check if it hit any bricks etc.....
				for (int row = 0; row < rowMax; row++)
				{
					for (int col = 0; col < colMax; col++)
					{
						//get the current brick
						final Brick brick = getGame().getBricks().getBricks()[row][col];
						
						if (!brick.isDead())
						{
							//if the laser hit the brick
							if (laser.hasCollision(brick))
							{
								//flag laser hidden
								laser.setHidden(true);
								
								//mark the collision
								brick.markCollision();

								//if the brick has been destroyed, track it
								if (!brick.isSolid() && brick.isDead())
									getGame().getBricks().setDestroyed(getGame().getBricks().getDestroyed() + 1);

								//if the brick contains a power up we will add it
								if (brick.hasPowerup())
									getGame().getPowerups().add(brick);
								
								//move to the end
								row = rowMax;
								col = colMax;
									
								//no need to check the other bricks since the ball already hit
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Add lasers to the mix
	 * @param paddle The paddle which is the starting location of the lasers
	 */
	public void addLasers(final Paddle paddle)
	{
		//count the number of lasers re-used
		int count = 0;
		
		//the starting point of the laser
		final int laserY = (int)(paddle.getY() - Laser.HEIGHT);

		for (int i = 0; i < getLasers().size(); i++)
		{
			//get the current laser
			Laser laser = getLasers().get(i);

			//hidden lasers can be re-used, just make sure we didn't already re-use 2
			if (laser.isHidden() && count < 2)
			{
				//increase count
				count++;
				
				//flag false
				laser.setHidden(false);
				
				//y-coordinate will be same
				laser.setY(laserY);
				
				if (count == 1)
				{
					laser.setX(paddle.getX());
				}
				else if (count == 2)
				{
					laser.setX(paddle.getX() + paddle.getWidth() - Laser.WIDTH);
				}
			}
		}
		
		//no lasers were found so let's add the lasers to the list
		if (count < 1)
		{
			getLasers().add(new Laser(paddle.getX(), laserY));
			getLasers().add(new Laser(paddle.getX() + paddle.getWidth() - Laser.WIDTH, laserY));
		}
		else if (count < 2)
		{
			getLasers().add(new Laser(paddle.getX() + paddle.getWidth() - Laser.WIDTH, laserY));
		}
	}
	
	private ArrayList<Laser> getLasers()
	{
		return this.lasers;
	}

	public void render(GL10 openGL) {

		if (getLasers() != null)
		{
			for (int i = 0; i < getLasers().size(); i++)
			{
				try {
					if (i >= getLasers().size())
						continue;

					//get the current laser
					Laser laser = getLasers().get(i);

					//only render what is not hidden
					if (laser.isHidden())
						continue;

					super.setX(laser);
					super.setY(laser);
					super.setTextureId(laser.getTextureId());
					super.render(openGL);

					//render the laser
					//laser.render(openGL);

				} catch (Exception e) {
					UtilityHelper.handleException(e);
				}
			}
		}
	}
}