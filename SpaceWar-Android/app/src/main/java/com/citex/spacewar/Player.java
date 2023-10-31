package com.citex.spacewar;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

/**
	This class draws a user controllable sprite and detects collisions 
	between enemies, bullets and power-ups.
	
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

public class Player extends Sprite {

    /** The current frame of animation */
    private int mFrameNum;
    /** The time elapsed between advancing the frame of animation */
    private int mFrameTimer;
    /** The time elapsed between firing bullets */
    private int mBulletTimer;
    /** The controller state of the player */ 
    public Control mControls;
    /** The array list containing the player bullets */
    private ArrayList<PlayerBullet> mBulletList;
    /** Has the player collected the special power-up */
    private boolean mCollectedSpecial;
    /** Has the player fired the special weapon */
    private boolean mSpecialFired;
    /** The time elapsed since firing the special weapon */
    private int mSpecialTimer;
    /** The number level of the players weapon power-up */
    public static int mPowerupLevel;
    /** The amount of life remaining */
    public static int mLife;
    /** The players score */
    public static int mScore;
    /** Is the player invincible */
    private boolean mInvincible;
    /** The time elapsed since player became invincible */
    private int mInvincibleTimer;
    
    /**
     * Constructs the Player
     */
	public Player()
	{
		mSpecialTimer = 0;
		mX = 30;
		mY = 95;
		mFrameNum = 0;
		mFrameTimer = 0;
        mControls = new Control();
        mBulletList = new ArrayList<PlayerBullet>();
        mPowerupLevel = 0;
        if(Settings.Difficulty == 0)
        	mLife = 5;
        else
        	mLife = 4;
        mInvincible = false;
        mInvincibleTimer = 0;	
	}	
	
	/** 
	 * Add a bullet to the bullet list
	 */
	public void addBullet()
	{		
		mBulletList.add(new PlayerBullet(mPowerupLevel, mX + 40, mY + 7));
	}
	
	/**
	 * Add an amount to the current score
	 * @param amount The amount to add to the score
	 */
	public void addScore(int amount)
	{
		mScore+=amount;	
	}

	/**
	 * Returns the current score 
	 * @return The current score
	 */
	public int getScore()
	{
		return mScore;		
	}
	
    /**
     * Returns the amount of remaining life for the player
     * @return The amount of life
     */
    public int getLife()
    {
    	return mLife;
    }	
    
    /**
     * Returns the player collected special power-up flag
     * @return True if the player has collected the special weapon power-up
     */
    public boolean hasCollectedSpecial()
    {
    	return mCollectedSpecial;
    }
    
    /**
     * Moves the player in the direction that is pressed
     * @param cam The camera coordinates
	 * @param dt The delta time between frame updates
     */
    public void move(Camera cam, float dt)
    {	
    	float amount;
    	if(dt>=17)
    		amount = 0.10f;
    	else
    		amount = 0.11f; 	
    	
    	if(mControls.mUpPressed)
    	{
    		if(mY > -9)
    			mY -= amount * dt;
    	}
    	
    	if(mControls.mDownPressed)
    	{
    		if(mY < 200 - mHeight)
    			mY += amount * dt;
    	}
    	
    	if(mControls.mLeftPressed)
    	{
    		if(mX - cam.x > -20)
    			mX -= amount * dt; 
    	}
    	
    	if(mControls.mRightPressed)
    	{
    		if(mX - cam.x < 380)
    			mX += amount * dt;
    	}
    	
    	int time;
    	if(dt>=17)
    		time=21;
    	else
    		time=25; 	  	
    	
    	//fire bullet
    	if(mControls.mFire1Pressed && !mSpecialFired)
    	{
    		if(mBulletTimer > time)
    		{
    			addBullet();
    			mBulletTimer = 0;
    		}
    		else 
    			mBulletTimer++;	
    	}  	
    	
    	for(int i = 0; i < mBulletList.size(); i++)
    	{
    		mBulletList.get(i).move(dt);
    	} 	
    }
	
	/**
     * Draws the player on the graphics surface
     * @param g The graphics context
     * @param cam The camera coordinates
     */    
    public void draw(GL10 gl, Camera cam, SpriteSheet player, SpriteSheet bullet,  PlayerSpecialWeapon special) {

    	//advance frame of animation
    	if(mFrameTimer > 10)
    	{
	    	if(mFrameNum < 2) 
	    		mFrameNum++;
	    	else
	    		mFrameNum = 0;
	    	mFrameTimer = 0;
    	}
    	else
    		mFrameTimer++;     
   	
    	//fire special weapon
    	if(mControls.mFire2Pressed && mCollectedSpecial)
    	{
    		if(!mSpecialFired)
    		{
	    		mSpecialFired = true;
	    		mSpecialTimer = 0;
	    		mCollectedSpecial = false;
    		}		
    	}
    	
    	//draw special weapon
    	if(mSpecialFired)
    	{
    		if(mSpecialTimer < 220)
    		{
    			special.mX = mX + 45;
    			special.mY = mY - 8;
    			special.draw(gl, cam);	
    		}
    		else
    		{
    			mSpecialFired = false;
    			mSpecialTimer = 0;
    		}
    		mSpecialTimer++;	
    	}
	
    	//limit amount of time player is invincible after being hit
    	if(mInvincible)
    	{
    		if(mInvincibleTimer > 50)
    		{
    			mInvincible= false;
    			mInvincibleTimer = 0;
    		}
    		mInvincibleTimer++;
    	}
    	
    	//draw player (flicker if invincible)
    	if(mInvincible && mInvincibleTimer % 5 == 0 || !mInvincible)
    	{
	    	if(mControls.mUpPressed)
	    		player.getFrame(3 + mFrameNum,'l').draw(gl, mX - cam.x, mY + 1 - cam.y);
	    	else if(mControls.mDownPressed)
	    		player.getFrame(6 + mFrameNum,'l').draw(gl, mX - cam.x, mY + 1 - cam.y);
	    	else    		
	    		player.getFrame(0 + mFrameNum,'l').draw(gl, mX - cam.x, mY + 1 - cam.y);
    	}

    	//draw bullets
    	for(int i = 0; i < mBulletList.size(); i++)
    	{
        	bullet.getFrame(mBulletList.get(i).getType(),'l').draw(gl, mBulletList.get(i).mX - cam.x, mBulletList.get(i).mY + 1 - cam.y);
    		if(mBulletList.get(i).mX > cam.x + 400)
    			mBulletList.remove(i);
    	}	
    }
    
    
    /** 
     * Detects collisions between the player and power-ups
     * @param powerups The array list of power-ups
     * @return True if a collision has occurred
     */
    public boolean detectPowerupCollision(ArrayList<Powerup> powerups)
    {
    	boolean collision = false;  	
    	for(int i = 0; i<powerups.size(); i++)
    	{
    		if(getBounds().intersect(powerups.get(i).getBounds()))
    		{
    			if(powerups.get(i).getType() == 1) //weapon power-up
    			{
    				if(mPowerupLevel < 2)
    					mPowerupLevel++;    
    				powerups.remove(i);		
    			}
    			else if(powerups.get(i).getType() == 2) //health power-up
    			{
    				if(mLife < 6)
    					mLife++;    
    				powerups.remove(i);		
    			}  	
    			else if(powerups.get(i).getType() == 3) //special weapon power-up
    			{
    				mCollectedSpecial = true;
    				powerups.remove(i);		
    			}  		
    			collision = true;
    			SoundEffect.play("powerup.wav");
    			addScore(500);
    			break;
    		} 		
    	}	
    	return collision;
    }
        
    /**
     * Detects collisions between the player and enemies
     * @param enemies The enemy array list
     * @return True if a collision occurs
     */  
    public boolean detectEnemyCollision(GL10 gl, Camera cam, ArrayList<Enemy> enemies, PlayerSpecialWeapon special)
    {
    	boolean collision = false;
    	
    	for(int i = 0; i<enemies.size(); i++)
    	{   		
    		if(getBounds().intersect(enemies.get(i).getBounds())) //player hit enemy
    		{
    			if(!enemies.get(i).getHit() && !mInvincible) 
    			{		 
    				enemies.get(i).removeLife(1);	
    				enemies.get(i).setHit(true);
    				mLife-=enemies.get(i).getStrenth();
    				mInvincible = true;
    				SoundEffect.play("hit.wav");
    			}
    			collision = true;	
    		} 
    		
    		for(int b = 0; b < mBulletList.size(); b++)
    		{
    			if(mBulletList.get(b).getBounds().intersect(enemies.get(i).getBounds()) && !enemies.get(i).getDestroyed()) //player bullet hit enemy
    			{
    				enemies.get(i).removeLife(mBulletList.get(b).getStrength());	
    				enemies.get(i).setHit(true);
    				mBulletList.remove(b);
    			} 			
    		}
    		
    		//player special weapon hit enemy
    		if(special.getBounds().intersect(enemies.get(i).getBounds()) && mSpecialFired)
    		{
    			if(enemies.get(i).mX > cam.x && enemies.get(i).mX < cam.x + 400)
    			{
    				if(enemies.get(i).getType() != 10)
    					enemies.get(i).removeLife(special.getStrength());	
    				else //end of level boss
    					enemies.get(i).removeLife(0.01f);	
					enemies.get(i).setHit(true);	
    			}
    		}
    	    		
    		for(int c = 0; c < enemies.get(i).getBulletList().size(); c++) 
    		{
    			EnemyBullet eb = enemies.get(i).getBulletList().get(c);
    			
    			if(getBounds().intersect(eb.getBounds()) && !mInvincible) //enemy bullet hit player
    			{
    				mLife-=eb.getStrength();
    				mInvincible = true;
    				SoundEffect.play("hit.wav");				
    			}	
    		}
    	}	
    	return collision;
    }
    
    /**
     * Returns the bounding box for the player sprite
     * @return The bounding box for the sprite
     */ 
    public Rectangle getBounds()
    {	
    	return new Rectangle(mX + 12, mY + 10, 17, 28);
    }      
}
