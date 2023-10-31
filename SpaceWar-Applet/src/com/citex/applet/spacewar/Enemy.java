package com.citex.applet.spacewar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
	This class draws a non-playable enemy sprite that fires bullets 
	and moves in different patterns such as circles and sine waves.
	
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

public class Enemy extends Sprite {
	
	/** The enemy type */
    private int mType;	
    /** The enemy life */
    private float mHealth;
    /** The power-up left when the enemy is destroyed */
    private int mPowerup;
    /** The start coordinates */
    private float mStartX, mStartY;
    /** The level of the enemy power-up */
    private int mWeapon;
    /** The time elapsed after enemy fires a bullet */
    private int mBulletTimer;
    /** The list of enemy bullets */
    private ArrayList<EnemyBullet> mBulletList;    
    /** The radius determining the sine wave */
    private float mSineRadius;
    /** The angle determining the sine wave */
    private float mSineAngle;
    /** The speed of the sine wave */
    private float mSineSpeed;    
    /** The angle of the circular movement */
    private float mCircleAngle;
    /** Has the enemy hit by a bullet */
    private boolean mHit;
    /** The time elapsed since the enemy was hit */
    private int mHitTimer;
    /** The amount of life to subtract if the player and enemy collide */
    private int mStrength;
    /** The time elapsed between enemy shooting bullets */
    private int mAutofireTimer;
    /** Has the enemy been destroyed */
    private boolean mDestroyed;  
    
    /**
     * Constructs the Enemy
     * @param type The type of enemy 1 to 5 = asteroid, 6 = static enemy, 7 = move in sine wave,
     * 8 = static enemy, 9 = move in circle, 10 = end of level boss moves up and down
     * @param x The start x coordinate
     * @param y The start y coordinate
     * @param health The amount of life the enemy has
     * @param weapon The weapon the enemy has
     * @param powerup The power-up left when the enemy is destroyed
     */
    public Enemy(int type, int x, int y, int health, int weapon, int powerup)
    {
    	mX = x;
    	mY = y;
    	mStartX = mX;
    	mStartY = mY;
    	mType = type;  	
		
   		mHealth = health;
   		mWeapon = weapon;
   		mPowerup = powerup;

    	mBulletList = new ArrayList<EnemyBullet>();
    	mBulletTimer = 300;
    	mAutofireTimer = 0;
    	mDestroyed = false;

    	mHit = false;
    	mHitTimer = 0;
    	mStrength = 1;
    	mCircleAngle = 0;
    	
    	if(mType == 7)
    	{
    	    mSineRadius = 40.0f;
    	    mSineAngle = 0.0f;
    	    mSineSpeed = 0.002f;
    	}
    	else if(mType == 10)
    	{
    	    mSineRadius = 75.0f;
    	    mSineAngle = 0.0f;
    	    mSineSpeed = 0.0009f; 		
    	} 
    }
    
    /**
     * Add an enemy bullet to the bullet list
     * @param x The start x coordinate
     * @param y The start y coordinate
     * @param type The type of bullet
     * @param sine Is the bullet moving in a sine wave pattern
     */
	private void addBullet(float x, float y, int type, boolean sine)
	{
		if(type > 0)
			mBulletList.add(new EnemyBullet(type, (int)x, (int)y, sine));   
	}  

	/**
	 * Move to next position based on the type of enemy 1 to 5 = asteroid, 6 = static enemy, 
	 * 7 = move in sine wave, 8 = static enemy, 9 = move in circle, 10 = end of level boss
	 * @param cam The camera coordinates
	 * @param dt The delta time between frame updates
	 * @param level The current level
	 */
	public void move(Camera cam, float dt, int level)
	{
		if(!mDestroyed)
		{
			if (mType == 6 || mType == 8)  //static enemy 
			{
				if (mBulletTimer > Settings.EnemyAutoFireDelay && mX < cam.x + 380) {
					addBullet(mX-25, mY, mWeapon, false);
					mBulletTimer = 0;
				}
				mBulletTimer += 5;
			}
	
			if (mType == 7) // move in sine wave pattern (http://gamedev.stackexchange.com/questions/9198/oscillating-sprite-movement-in-xna)
			{
				mX -= 0.06 * dt;
				mY = (int) (mStartY + Math.sin(mSineAngle) * mSineRadius);
				mSineAngle += mSineSpeed * dt;
				if (mBulletTimer > Settings.EnemyAutoFireDelay && mX < cam.x + 380) {
					addBullet(mX-25, mY, mWeapon, false);
					mBulletTimer = 0;
				}
				mBulletTimer += 5;
			}		

			if (getType() == 9 && mX < cam.x + 400)  //move in circular motion (http://www.coderanch.com/t/504164/GUI/java/Circular-motion)
			{
				double degrees = mCircleAngle * Math.PI * 2 / 360.0; // convert degree into radians
	
				if (mCircleAngle < 360)
					mCircleAngle +=  0.10 * dt;
				else
					mCircleAngle = 0;
	
				// find X and Y on the circle
				int X = (int) (mStartX + (40) * java.lang.Math.cos(degrees));
				int Y = (int) (mStartY + (40) * java.lang.Math.sin(degrees));
	
				mX = X;
				mY = Y;
	
				if (mBulletTimer > Settings.EnemyAutoFireDelay && mX < cam.x + 400) {
					addBullet(mX-25, mY+4, mWeapon, false);
					mBulletTimer = 0;
				}
				mBulletTimer += 5;
			}
		
			if(getType() == 10) //end of level boss
			{	
				mY = (int) (mStartY + Math.sin(mSineAngle) * mSineRadius);
				mSineAngle += mSineSpeed * dt;
		    	int time1, time2; 
		    	float delay;
	    		time1 = 550;
	    		time2 = 600;
	    		if(Settings.Difficulty == 0)
	    			delay = 400 / 1.1f;
	    		else
	    			delay = 400 / 1.2f;
				if (mAutofireTimer < time1) {
					if (mBulletTimer > delay && mX < cam.x + 380) {					
						boolean sine = false;
						if(level > 1)
							sine = true;		
						if(level == 4 || level == 5)
						{
							addBullet(mX - 12, mY - 10, mWeapon, sine);
							addBullet(mX - 12, mY + 15, mWeapon, sine);		
							addBullet(mX - 12, mY + 43, mWeapon, sine);	
						}
						else
						{					
							addBullet(mX - 12, mY - 10, mWeapon, sine);
							addBullet(mX - 12, mY - 3, mWeapon, sine);	
							addBullet(mX - 12, mY + 36, mWeapon, sine);
							addBullet(mX - 12, mY + 43, mWeapon, sine);			
						}	
						mBulletTimer = 0;
					}
					mBulletTimer += 5;
				} else if (mAutofireTimer >= time2) {
					mAutofireTimer = 0;
				}
				mAutofireTimer++;		
			}
			
			if (mHit) {
				mHitTimer++;
			}
			if (mHitTimer > 5) {
				mHit = false;
				mHitTimer = 0;
			}
		}
		
    	for(int i = 0; i < mBulletList.size(); i++)
    	{
    		if(!Settings.State.equals("game over") && !Settings.State.equals("game over high score"))   		
    			mBulletList.get(i).move(dt);
    	}	
	}	

	
	/**
     * Draws the enemy on the graphics surface
     * @param gl The GL context
     * @param cam The camera coordinates
     */
    public void draw(Graphics g, Camera cam, SpriteSheet mEnemySprite, SpriteSheet mBullet) {
    	
		if(!mDestroyed)
		{
			if(mHit)
				g.drawImage(mEnemySprite.getFrame(1,'l'), (int)mX - (int)cam.x, (int)mY + 1 - (int)cam.y, null);
			else
				g.drawImage(mEnemySprite.getFrame(0,'l'), (int)mX - (int)cam.x, (int)mY + 1 - (int)cam.y, null);
		}
		
		g.setColor(new Color(255,0,0));
    	for(int i = 0; i < mBulletList.size(); i++)
    	{	
    		g.drawImage(mBullet.getFrame(mBulletList.get(i).getType() - 1, 'l'), (int)mBulletList.get(i).mX - (int)cam.x, (int)mBulletList.get(i).mY + 1 - (int)cam.y, null); 
    		
    		if(Settings.DebugMode)
    		{		
    			g.drawRect(mBulletList.get(i).getBounds().x - (int)cam.x, mBulletList.get(i).getBounds().y - (int)cam.y, mBulletList.get(i).getBounds().width, mBulletList.get(i).getBounds().height);	
    		}	
    	}	
    	
		if(Settings.DebugMode && !mDestroyed)
		{
			g.drawRect(getBounds().x - (int)cam.x, getBounds().y - (int)cam.y, getBounds().width, getBounds().height);	
		}		
    }
    
	/**
	 * Draws the enemy sprite at the x,y coordinates
	 * @param g The graphics context
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void drawXY(Graphics g, int x, int y, SpriteSheet mEnemySprite)
	{
		g.drawImage(mEnemySprite.getFrame(0,'l'), x, y, null);	
	}
    
    	
	
	/** 
	 * Returns the enemies bullet list
	 * @return The arraylist containing the enemy bullets
	 */
	public ArrayList<EnemyBullet> getBulletList()
	{
		return mBulletList;	
	}
	
	/**
	 * Returns the power-up left when the enemy is killed 
	 * @return The power-up left when the enemy is killed
	 */
	public int getPowerup()
	{		return mPowerup;
	}
	
	/**
	 * Returns the amount of life the enemy has remaining
	 * @return The amount of life
	 */
	public float getLife()
	{
		return mHealth;
	}
	
	/**
	 * Sets the enemy as destroyed
	 * @param destroyed Is the enemy destroyed
	 */
	public void setDestroyed(boolean destroyed)
	{
		mDestroyed = destroyed;
	}
	
	/**
	 * Returns the enemy destroyed flag
	 * @return Is the enemy destroyed
	 */
	public boolean getDestroyed()
	{
		return mDestroyed;
	}
	
	/**
	 * Removes an amount of life from the enemy
	 * @param amount The amount of life to remove
	 */
	public void removeLife(float amount)
	{
		mHealth-=amount;	
	}	

	/**
	 * Returns the type of enemy
	 * @return The type of enemy
	 */
	public int getType()
	{
		return mType;
	}	
	
	/**
	 * Sets the hit state of the enemy
	 * @param hit The hit state of the enemy
	 */
    public void setHit(boolean hit)
    {
    	mHit = hit;	
    }
    
    /**
     * Returns the hit state of the enemy
     * @return The hit state of the enemy
     */
    public boolean getHit()
    {
    	return mHit;
    }
    
    /**
     * Returns the strength of the enemy
     * @return The amount of life to remove when the player collides with the enemy
     */
    public int getStrenth()
    {
    	return mStrength;
    }
    
    /**
     * Returns the bounding box for the enemy sprite
     * @return The bounding box for the sprite
     */ 
	public Rectangle getBounds()
	{
    	if(mType == 0)
    		return new Rectangle((int)mX + 2, (int)mY + 3, 12, 12);
    	else if(mType == 1)
    		return new Rectangle((int)mX + 5, (int)mY + 6, 21, 21);
    	else if(mType == 2)
    		return new Rectangle((int)mX + 7, (int)mY + 4, 18, 26);
    	else if(mType == 3)
    		return new Rectangle((int)mX + 1, (int)mY + 2, 30, 31);
    	else if(mType == 4)
    		return new Rectangle((int)mX + 8, (int)mY + 11, 48, 42);
    	else if(mType == 5)
    		return new Rectangle((int)mX + 5, (int)mY + 8, 55, 53);
    	else if(mType == 6)
    		return new Rectangle((int)mX, (int)mY + 4, 32, 26);
    	else if(mType == 7)
    		return new Rectangle((int)mX + 3, (int)mY + 3, 26, 28);
    	else if(mType == 8)
    		return new Rectangle((int)mX, (int)mY, 32, 32);
    	else if(mType == 9)
    		return new Rectangle((int)mX + 3, (int)mY + 3, 26, 28);
    	else if(mType == 10)
    		return new Rectangle((int)mX + 13, (int)mY + 5, 47, 57);
    	else
    		return new Rectangle((int)mX, (int)mY, 0, 0);	
	}      
}
