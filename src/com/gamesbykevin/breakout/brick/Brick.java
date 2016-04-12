package com.gamesbykevin.breakout.brick;

import com.gamesbykevin.breakout.entity.Entity;

public class Brick extends Entity 
{
	//is the brick dead?
	private boolean dead = false;
	
	//is this brick not meant to be destroyed?
	private boolean solid = false;
	
	public Brick() 
	{
		
	}
}