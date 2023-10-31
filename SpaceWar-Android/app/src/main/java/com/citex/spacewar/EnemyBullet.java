package com.citex.spacewar;

/**
	This class draws the different types of enemy bullet flying towards
	the left hand side of the screen.
	
	@version 1.0
 	@modified 28/10/2023
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

public class EnemyBullet extends Sprite {

	/** The type of bullet */
    private int mType;
    /** The strength of the bullet */
    private int mStrength;
    /** Is the bullet moving in a sine wave pattern */
    private boolean mSine; 
    /** The radius determining the sine wave */
    private float mSineRadius;
    /** The angle determining the sine wave */
    private float mSineAngle;
    /** The speed of the sine wave */
    private float mSineSpeed;  
    /** The start y coordinate */
    private float mStartY;
        
    /**
     * Constructs the EnemyBullet
     * @param type The type of bullet
     * @param x The start x coordinate
     * @param y The start y coordinate
     * @param sine Is the bullet moving in a sine wave pattern
     */
	public EnemyBullet(int type, float x, float y, boolean sine)
	{
		mType = type;
		mStrength = 1;
		mX = x;
		mY = y;
		mSine = sine;
    	mStartY = mY;	
  	    mSineRadius = 10.0f;
	    mSineAngle = 0.0f;
	    mSineSpeed = 0.006f;   			
	}
    
    /**
     * Move the bullet left across the screen
     * @param dt The delta time between frame updates
     */
    public void move(float dt)
    {
		if (mSine) // move in sine wave pattern (http://gamedev.stackexchange.com/questions/9198/oscillating-sprite-movement-in-xna)
		{
			mX -= 0.21 * dt;
			mY = (int) (mStartY + Math.sin(mSineAngle) * mSineRadius);
			mSineAngle += mSineSpeed * dt;
		}
		else   	
			mX-= 0.21 * dt;	
    }
    
    /**
     * Returns the strength of the bullet
     * @return The amount of life to remove when the player collides with the enemy bullet
     */
    public int getStrength()
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
     * Returns the bounding box for the enemy bullet sprite
     * @return The bounding box for the sprite
     */ 
    public Rectangle getBounds()
    {
    	if(mType == 1)
    		return new Rectangle(mX + 22, mY + 15, 11, 5);
    	else if(mType == 2)
    		return new Rectangle(mX + 22, mY + 4, 11, 26);	
    	else if(mType == 3)
    		return new Rectangle(mX + 18, mY + 6, 15, 21);	
    	else
    		return new Rectangle(mX, mY, 0, 0);
    } 
}