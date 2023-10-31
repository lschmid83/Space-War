package com.citex.spacewar;

import javax.microedition.khronos.opengles.GL10;

/**
	This class draws an animated explosion.
	
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

public class Explosion extends Sprite {

	/** The current frame of animation */
    private int mFrame;
    
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
    }
	
    /**
	 * Draws the explosion on the graphics surface
	 * @param gl The graphics context
	 * @param cam The camera coordinates
	*/  
	public void draw(GL10 gl, Camera cam, SpriteSheet explosion) {  
		
		if(mFrame < 7)
			mFrame++;

		explosion.getFrame(mFrame,'l').draw(gl, mX - cam.x, mY + 1 - cam.y);
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
