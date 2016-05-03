package com.gamesbykevin.breakout.ball;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.breakout.assets.Assets;
import com.gamesbykevin.breakout.common.ICommon;
import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.panel.GamePanel;
import com.gamesbykevin.breakout.wall.Wall;

import android.graphics.Canvas;

public class Balls extends Entity implements ICommon
{
	//list of balls in play
	private ArrayList<Ball> balls;
	
	/**
	 * The different animations for each ball
	 */
	public enum Key
	{
		Yellow, Blue, Green, Orange, Red, White
	}
	
	public Balls() throws Exception 
	{
		//call parent constructor
		super(Ball.WIDTH, Ball.HEIGHT);
		
		//create new list of balls
		this.balls = new ArrayList<Ball>();
		
		//where animation is located
		final int y = 0;
		
		for (Key key : Key.values())
		{
			int x;
			
			//locate y-coordinate
			switch (key)
			{
				case Yellow:
					x = (0 * Ball.DIMENSIONS);
					break;
					
				case Blue:
					x = (1 * Ball.DIMENSIONS);
					break;
					
				case Green:
					x = (2 * Ball.DIMENSIONS);
					break;
					
				case Orange:
					x = (3 * Ball.DIMENSIONS);
					break;
					
				case Red:
					x = (4 * Ball.DIMENSIONS);
					break;
					
				case White:
					x = (5 * Ball.DIMENSIONS);
					break;
					
				default:
					throw new Exception("Key not found: " + key.toString());
			}
			
			//create new animation
			Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Sheet), x, y, Ball.DIMENSIONS, Ball.DIMENSIONS);
			
			//now add animation to the sprite sheet
			super.getSpritesheet().add(key, animation);
		}
	}

	@Override
	public void dispose() 
	{
		if (this.balls != null)
		{
			for (int i=0; i < this.balls.size(); i++)
			{
				if (this.balls.get(i) != null)
				{
					this.balls.get(i).dispose();
					this.balls.set(i, null);
				}
			}
			
			this.balls.clear();
			this.balls = null;
		}
	}

	/**
	 * Get the list of balls
	 * @return The list of balls in play
	 */
	public ArrayList<Ball> getBalls()
	{
		return this.balls;
	}
	
	/**
	 * Add ball to collection
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void add(final int x, final int y)
	{
		//create a new ball
		Ball ball = new Ball(Balls.Key.Blue);

		//position the ball
		ball.setX(x);
		ball.setY(y);
		
		//choose random velocity
		ball.setDX(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
		ball.setDY(GamePanel.RANDOM.nextBoolean() ? Ball.SPEED_MIN : -Ball.SPEED_MIN);
		
		//add to list
		getBalls().add(ball);
	}
	
	@Override
	public void update() throws Exception 
	{
		if (getBalls() != null)
		{
			//update all balls
			for (int i = 0; i < getBalls().size(); i++)
			{
				//get the current ball
				Ball ball = getBalls().get(i);
				
				if (ball != null)
				{
					//update ball
					ball.update();
					
					//make sure the ball stays in bounds
					if (ball.getDX() > 0)
					{
						if (ball.getX() + ball.getWidth() >= GamePanel.WIDTH - Wall.WIDTH)
							ball.setDX(-ball.getDX());
					}
					else if (ball.getDX() < 0)
					{
						if (ball.getX() <= Wall.WIDTH)
							ball.setDX(-ball.getDX());
					}
					
					//make sure the ball stays in bounds
					if (ball.getDY() > 0)
					{
						if (ball.getY() + ball.getHeight() >= GamePanel.HEIGHT - Wall.HEIGHT)
							ball.setDY(-ball.getDY());
					}
					else if (ball.getDY() < 0)
					{
						if (ball.getY() <= Wall.HEIGHT)
							ball.setDY(-ball.getDY());
					}
				}
				else
				{
					//remove the ball since it is null
					getBalls().remove(i);
					
					//decrease index
					i--;
				}
			}
		}
	}
	
	@Override
	public void reset() 
	{
		if (getBalls() != null)
			getBalls().clear();
	}

	@Override
	public void render(Canvas canvas) throws Exception 
	{
		if (getBalls() != null)
		{
			//render all balls
			for (int i = 0; i < getBalls().size(); i++)
			{
				//get the current ball
				final Ball ball = getBalls().get(i);

				//assign values
				setX(ball);
				setY(ball);
				setWidth(ball);
				setHeight(ball);
				getSpritesheet().setKey(ball.getKey());
				
				//render the current ball
				render(canvas);
			}
		}
	}
}