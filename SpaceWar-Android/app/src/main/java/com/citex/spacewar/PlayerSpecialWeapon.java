package com.citex.spacewar;

import android.content.res.AssetManager;

import javax.microedition.khronos.opengles.GL10;

/**
	This class draws the animated laser beam weapon.
	
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
     * @param gl The GL context
     * @param x The start x coordinate
     * @param y The start y coordinate
     */
	public PlayerSpecialWeapon(GL10 gl, float x, float y, AssetManager assetManager)
	{
		mSpecialWeapon = new SpriteSheet(gl, "spr/player_special_weapon.png", 448, 64, 2, assetManager);
		mStrength = Settings.PlayerBulletStrength;
		mX = x;
		mY = y;	
	}
	
	/**
     * Draws the player special weapon on the graphics surface
     * @param gl The GL context
     * @param cam The camera coordinates
     */    
    public void draw(GL10 gl, Camera cam) {
   	
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

    	mSpecialWeapon.getFrame(mFrame,'l').draw(gl, mX - cam.x, mY + 1 - cam.y);   	    	  	
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
   		return new Rectangle(mX + 1, mY + 27, 448, 11);
    } 

    /**
     * Destroys the textures and releases the hardware buffers
     * @param gl The GL context
     */    
    public void destroy(GL10 gl)
    {
    	mSpecialWeapon.destroy(gl);
    }	
}
