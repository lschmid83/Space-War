package com.citex.java.spacewar;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

/**
	This class draws the animated laser beam weapon.
	
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

public class PlayerSpecialWeapon extends Sprite {

	/** The player special weapon sprite sheet */
	private SpriteSheet mSpecialWeapon;
	/** The current frame of animation */
    private int mFrame; 
    /** The strength of the bullet */
    private float mStrength;
    /** The time elapsed between advancing the frame of animation */
    private int mFrameTimer;
    
    /**
     * Constructs the PlayerSpecialWeapon 
     * @param x The start x coordinate
     * @param y The start y coordinate
     */
	public PlayerSpecialWeapon(int x, int y)
	{
		mSpecialWeapon = new SpriteSheet("res/spr/player_special_weapon.bmp", 448, 64, 2);
		mStrength = Settings.PlayerBulletStrength;
		mX = x;
		mY = y;	
	}
	
	/**
     * Draws the player special weapon on the graphics surface
     * @param g The graphics context
     * @param cam The camera coordinates
     */    
    public void draw(Graphics g, Camera cam) {
   	
     	if(mFrameTimer > 5)
    	{    	
    		if(mFrame == 0)
    			mFrame = 1;
    		else
    			mFrame = 0;
	    	mFrameTimer = 0;
    	}
    	else
    		mFrameTimer++;     	
    	
    	g.drawImage(mSpecialWeapon.getFrame(mFrame,'l'), (int)mX - (int)cam.x, (int)mY + 1 - (int)cam.y, null);   	
		if(Settings.DebugMode)
		{
			g.setColor(new Color(255,0,0));
			g.drawRect(getBounds().x - (int)cam.x, getBounds().y - (int)cam.y, getBounds().width, getBounds().height);
			
		}	     	  	
    }
    
    /**
     * Returns the strength of the special weapon
     * @return The amount of life to remove when the enemy collides with the special weapon
     */
    public float getStrength()
    {
    	return mStrength;
    }
    
    /**
     * Returns the bounding box for the player special weapon
     * @return The bounding box for the sprite
     */ 
    public Rectangle getBounds()
    {
   		return new Rectangle((int)mX + 1, (int)mY + 27, 448, 11);
    }   
}