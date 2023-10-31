package com.citex.java.spacewar;

import java.awt.Graphics;

/**
	This class draws an animated explosion.
	
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

public class Explosion extends Sprite {

	/** The current frame of animation */
    private int mFrame;
    /** The time elapsed between frame updates */
    private int mTimer;
    
    /**
     * Constructs the explosion
     * @param x The start x coordinate
     * @param y The start y coordinate
     */
    public Explosion(float x, float y)
    {
    	mX = x;
    	mY = y;
    	mFrame = 0;
    	mTimer = 0;
    }
	
    /**
	 * Draws the explosion on the graphics surface
	 * @param g The graphics context
	 * @param cam The camera coordinates
	*/   
	public void draw(Graphics g, Camera cam, SpriteSheet explosion) {  
		
		if(mFrame < 7)
		{
			if(mTimer % 3 == 0)
			{
				mFrame++;
			}
			mTimer++;
		}
		g.drawImage(explosion.getFrame(mFrame,'l'), (int)mX - (int)cam.x, (int)mY + 1 - (int)cam.y, null);	
	}   
    
	/** 
	 * Returns the current frame number of the animation
	 * @return The frame number of the animation
	 */
	public int getFrame()
	{
		return mFrame;
	}
}
