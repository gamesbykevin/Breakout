package com.gamesbykevin.breakout.paddle;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.ball.Ball;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.Game;
import com.gamesbykevin.breakout.panel.GamePanel;
import com.gamesbykevin.breakout.wall.Wall;

import android.graphics.Canvas;

public class Paddle extends Entity implements ICommon
{
	/**
	 * Dimensions of paddle
	 */
	public static final int WIDTH = 104;
	
	/**
	 * The amount we can increase/decrease the size of the paddle
	 */
	public static final int WIDTH_CHANGE = 13;
	
	/**
	 * Minimum dimensions of paddle
	 */
	public static final int MIN_WIDTH = 52;
	
	/**
	 * Maximum dimensions of paddle
	 */
	public static final int MAX_WIDTH = 208;
	
	/**
	 * Dimensions of paddle
	 */
	public static final int HEIGHT = 24;

	/**
	 * Default starting coordinate
	 */
	public static final int START_X = (GamePanel.WIDTH / 2) - (WIDTH / 2);
	
	/**
	 * Default starting coordinate
	 */
	public static final int START_Y = GamePanel.HEIGHT - (GamePanel.HEIGHT / 6);
	
	/**
	 * Adjust the x-velocity based on where the paddle is
	 */
	public static final double PADDLE_COLLISION_FAR = 1.25;
	
	/**
	 * Adjust the x-velocity based on where the paddle is
	 */
	public static final double PADDLE_COLLISION_CLOSE = 0.75;
	
	/**
	 * Adjust the x-velocity based on where the paddle is
	 */
	public static final double PADDLE_COLLISION_MIDDLE = 0.0;
	
	//does this paddle have magnet capabilities
	private boolean magnet = false;
	
	public Paddle(final Game game)
	{
		super(game, WIDTH, HEIGHT);
		
		//set start location
		super.setX(START_X);
		super.setY(START_Y);
		
		//create animation
		Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), 80, 64, WIDTH, HEIGHT);
		
		//now add animation to the sprite sheet
		super.getSpritesheet().add(DEFAULT, animation);
	}
	
	/**
	 * Set the magnet
	 * @param magnet true = the paddle can catch the balls, false otherwise
	 */
	public void setMagnet(final boolean magnet)
	{
		this.magnet = magnet;
	}
	
	/**
	 * Do we have a magnet?
	 * @return true if the paddle can catch the balls, false otherwise
	 */
	public boolean hasMagnet()
	{
		return this.magnet;
	}
	
	/**
	 * Expand the size of the paddle
	 */
	public void expand()
	{
		//calculate the middle
		final double mx = getX() + (getWidth() / 2);
		
		//increase width
		super.setWidth(getWidth() + WIDTH_CHANGE);
		
		//make sure we don't exceed the max
		if (super.getWidth() > MAX_WIDTH)
			super.setWidth(MAX_WIDTH);
		
		//position the paddle so it is in the middle
		this.setX(mx - (getWidth() / 2));
	}
	
	/**
	 * Shrink the size of the paddle
	 */
	public void shrink()
	{
		//calculate the middle
		final double mx = getX() + (getWidth() / 2);
		
		//decrease width
		super.setWidth(getWidth() - WIDTH_CHANGE);
		
		//make sure we don't exceed the min
		if (super.getWidth() > MIN_WIDTH)
			super.setWidth(MIN_WIDTH);
		
		//position the paddle so it is in the middle
		this.setX(mx - (getWidth() / 2));
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void reset() 
	{
		
	}
	
	@Override
	public void update()
	{
		//check each ball for paddle collision
		for (Ball ball : getGame().getBalls().getBalls())
		{
			//only check balls that are moving south
			if (ball.getDY() < 0)
				continue;
			
			//check first for collision with the ball
			if (hasCollision(ball))
			{
				//if the ball has moved past the paddle
				if (ball.getY() > getY() + (getHeight() / 2))
					continue;
				
				//switch ball y-velocity
				ball.setDY(-ball.getDY());
				
				//the middle x-coordinate
				double x = ball.getX() + (ball.getWidth() * .5);
				
				//now check where the collision occurred
				if (x < getX() + (getWidth() * .2))
				{
					//move max west
					ball.setXRatio(PADDLE_COLLISION_FAR);
					
					//make sure correct direction
					if (ball.getDX() > 0)
						ball.setDX(-ball.getDX());
				}
				else if (x >= getX() + (getWidth() * .8))
				{
					//move max east
					ball.setXRatio(PADDLE_COLLISION_FAR);
					
					//make sure correct direction
					if (ball.getDX() < 0)
						ball.setDX(-ball.getDX());
				}
				else if (x >= getX() + (getWidth() * .6))
				{
					//move slightly east
					ball.setXRatio(PADDLE_COLLISION_CLOSE);
					
					//make sure correct direction
					if (ball.getDX() < 0)
						ball.setDX(-ball.getDX());
				}
				else if (x >= getX() + (getWidth() * .4))
				{
					//move straight
					ball.setXRatio(PADDLE_COLLISION_MIDDLE);
				}
				else if (x >= getX() + (getWidth() * .2))
				{
					//move slightly west
					ball.setXRatio(PADDLE_COLLISION_CLOSE);
					
					//make sure correct direction
					if (ball.getDX() > 0)
						ball.setDX(-ball.getDX());
				}
			}
		}
	}
	
	@Override
	public void setX(final double x)
	{
		//calculate the new x-coordinate
		double nx = x - (getWidth() / 2);
		
		//keep the paddle in bounds
		if (nx < Wall.WIDTH)
			nx = Wall.WIDTH;
		if (nx > GamePanel.WIDTH - Wall.WIDTH - getWidth())
			nx = GamePanel.WIDTH - Wall.WIDTH - getWidth();
		
		//update x-coordinate
		super.setX(nx);
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		super.render(canvas);
	}
}