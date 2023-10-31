package com.citex.applet.spacewar;

import java.awt.Rectangle;

/**
	This class draws the different types of player bullets flying towards
	the right hand side of the screen.
	
	@version 1.0
	@modified 27/02/2012
	@author Lawrence Schmid<BR><BR>
	
	This file is part of Space War.<BR><BR>
	
	Space War is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.<BR><BR>
	
	Space War is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.<BR><BR>
	
	You should have received a copy of the GNU General Public License
	along with Space War.  If not, see http://www.gnu.org/licenses/.<BR><BR>
	
	Copyright 2012 Lawrence Schmid
*/

public class PlayerBullet extends Sprite {

	/** The type of bullet */
    private int mType;
    /** The strength of the bullet */
    private float mStrength;
    
    /**
     * Constructs the PlayerBullet
     * @param type The type of bullet
     * @param x The start x coordinate
     * @param y The start y coordinate
     */
	public PlayerBullet(int type, float x, float y)
	{
		mType = type;
		if(mType == 0)
			mStrength = Settings.PlayerBulletStrength;
		else if(mType == 1)
			mStrength = Settings.PlayerBulletStrength + 0.2f;
		else if(mType == 2)
			mStrength = Settings.PlayerBulletStrength + 0.5f;
		mX = x;
		mY = y;
	}
        
    /**
     * Move the bullet right across the screen
     * @param dt The delta time between frame updates
     */
    public void move(float dt)
    {
    	mX += 0.35 * dt;
    }
        
    /**
     * Returns the strength of the bullet
     * @return The amount of life to remove when the enemy collides with the player bullet
     */
    public float getStrength()
    {
    	return mStrength;
    }
    
    /**
     * Returns the type of bullet
     * @return The type of bullet
     */
    public int getType()
    {
    	return mType;	
    } 
    
    /**
     * Returns the bounding box for the player bullet sprite
     * @return The bounding box for the sprite
     */ 
    public Rectangle getBounds()
    {
    	if(mType == 0)
    		return new Rectangle((int)mX, (int)mY + 15, 10, 5);
    	else if(mType == 1)
    		return new Rectangle((int)mX, (int)mY + 3, 10, 26);
    	else if(mType == 2)
    		return new Rectangle((int)mX, (int)mY + 5, 15, 21);
    	else
    		return new Rectangle((int)mX, (int)mY, 0, 0);
    }        
}
