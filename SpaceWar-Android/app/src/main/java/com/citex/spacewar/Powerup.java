package com.citex.spacewar;

import javax.microedition.khronos.opengles.GL10;

/**
	This class draws the different types of collectable power-ups.
	
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

public class Powerup extends Sprite {

    /** The type of power-up */
    private int mType;	
	
    /**
     * Constructs the power-up
     * @param gl The GL context
     * @param type The type of power-up
     * @param x The x coordinate
     * @param y The y coordinate
     */
	public Powerup(GL10 gl, int type, float x, float y)
	{
		mType = type;
		mX = x;
		mY = y;	
	}
		
	/**
	 * Draws the power-up on the graphics surface
     * @param gl The GL context
	 * @param cam The camera coordinates
	*/    
	public void draw(GL10 gl, Camera cam, SpriteSheet powerup) {
		powerup.getFrame(mType-1,'l').draw(gl,  mX - cam.x, mY + 1 - cam.y);
	}
		
	/**
	 * Returns the type of power-up
	 * @return The type of power-up
	 */
	public int getType()
	{
		return mType;
	}
	
    /**
     * Returns the bounding box for the power-up
     * @return The bounding box for the sprite
     */ 
	public Rectangle getBounds()
	{		
		return new Rectangle(mX + 4, mY + 4, 8, 10);
	}	
}
