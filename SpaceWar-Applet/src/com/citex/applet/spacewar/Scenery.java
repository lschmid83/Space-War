package com.citex.applet.spacewar;

import java.awt.Graphics;

/**
	This class draws the different types of background scenery.
	
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

public class Scenery extends Sprite {

	/** The type of scenery */
    private int mType;	

    /**
     * Constructs the scenery
     * @param type The type of scenery
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Scenery(int type, int x, int y)
    {
    	mX = x;
    	mY = y;
    	mType = type;  	
    }
    	
	/**
	 * Draws the scenery on the graphics surface
	 * @param g The graphics context
	 * @param cam The camera coordinates
	 */    
	public void draw(Graphics g, Camera cam, SpriteSheet scenery) {
		g.drawImage(scenery.getFrame(0,'l'), (int)mX - (int)cam.x, (int)mY + 1 - (int)cam.y, null);	
	}
	
	/**
	 * Draws the scenery at the x,y coordinate
	 * @param g The graphics context
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void drawXY(Graphics g, int x, int y, SpriteSheet scenery)
	{
		g.drawImage(scenery.getFrame(0,'l'), x, y, null);	
	}

	/**
	 * Returns the type of scenery
	 * @return The type of scenery
	 */
	public int getType()
	{
		return mType;
	}	
}
