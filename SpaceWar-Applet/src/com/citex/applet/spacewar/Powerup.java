package com.citex.applet.spacewar;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

/**
	This class draws the different types of collectable power-ups.
	
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

public class Powerup extends Sprite {

	/** The type of power-up */
    private int mType;	
		
    /**
     * Constructs the power-up
     * @param type The type of power-up
     * @param x The x coordinate
     * @param y The y coordinate
     */
	public Powerup(int type, float x, float y)
	{
		mType = type;
		mX = x;
		mY = y;	
	}
		
	/**
	 * Draws the power-up on the graphics surface
	 * @param g The graphics context
	 * @param cam The camera coordinates
	*/    
	public void draw(Graphics g, Camera cam, SpriteSheet powerup) {

		g.drawImage(powerup.getFrame(mType-1,'l'), (int)mX - (int)cam.x, (int)mY + 1 - (int)cam.y, null);
		if(Settings.DebugMode)
		{
			g.setColor(new Color(255,0,0));
			g.drawRect(getBounds().x - (int)cam.x, getBounds().y - (int)cam.y, getBounds().width, getBounds().height);
			
		}			
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
		return new Rectangle((int)mX, (int)mY, 8, 10);
	}		
}
