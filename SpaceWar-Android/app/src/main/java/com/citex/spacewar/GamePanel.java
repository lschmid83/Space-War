package com.citex.spacewar;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

import android.content.res.AssetManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
	This class draws all of the graphics for the game and handles
	collision detection between the player, enemies and power-ups.
	
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

public class GamePanel {

	/** The user controllable player */
    private Player mPlayer;
    /** The level background */
    private Background mBackground;
    /** The user interface */
    private UserInterface mUserInterface;
    /** The camera position */ 
    private Camera mCamera;
    /** The mp3 music */
    private Music mMusic;
    /** The enemy list */
    private ArrayList<Enemy> mEnemyList;  
    /** The background object list */
    private ArrayList<Scenery> mSceneryList;      
    /** The destroyed enemy explosion list */
    private ArrayList<Explosion> mExplosionList;
    /** The list of power ups */
    private ArrayList<Powerup> mPowerupList;    
	/** The player sprite sheet */
    private SpriteSheet mPlayerSprite;
	/** The player bullet sprite sheet */
	private SpriteSheet mPlayerBulletSprite;
    /** The special weapon sprite sheet */  
    private PlayerSpecialWeapon mSpecialWeapon;
    /** The enemies sprite sheet */
    private SpriteSheet mEnemySprite[];
	/** The enemy bullet sprite sheet */
	private SpriteSheet mEnemyBulletSprite;    
	/** The explosions sprite sheet */
    private SpriteSheet mExplosionSprite;
	/** The power-up sprite sheet */
    private SpriteSheet mPowerupSprite;    
    /** The scenery sprite sheet */
    private SpriteSheet mScenerySprite[];
    /** The number of lives the player has remaining */
    private int mLives;
    /** The current level */
    private int mLevel;
    /** Has the player defeated level boss */
    private boolean mCompletedLevel;
    /** The level information */ 
    private Level mLevelInfo;
    /** The level header information */
    private Header mLevelHeader;
	/** The time elapsed while loading the level */
	private int mLevelLoadingTimer;
	/** The time elapsed between selecting options */
	private int mOptionTimer;
	/** The d-pad graphic */
    private GLSprite mDPad;
	/** The button graphic */
    private GLSprite mButton1;
	/** The button graphic */
    private GLSprite mButton2;
	/** The undo graphic */
    private GLSprite mUndo;
	/** The company logo graphic */
	private GLSprite mCompanyLogo;
	/** Has the player pressed the score area */
	private boolean mPressedLevelSelect;
	/** Provides access to an application's raw asset files */
	public static AssetManager mAssetManager;
	
	/**
	 * Constructs the GamePanel
	 * @param gl The GL context
	 */
	public GamePanel(GL10 gl, AssetManager assetManager) {
		mAssetManager = assetManager;
		mCompanyLogo = new GLSprite(gl, "gui/logo.png", null, assetManager);
		mLevelLoadingTimer = 0;
		Settings.State = "license";
		Settings.Paused = false;
		mOptionTimer = 0;
	}

	/**
	 * Preload enemy, player, power-up and scenery sprite sheets
	 * @param gl The GL context
	 */
	public void preloadGraphics(GL10 gl)
	{
		//controls
		mDPad = new GLSprite(gl, "gui/d-pad.png", null, mAssetManager);
		mButton1 = new GLSprite(gl, "gui/button1.png", null, mAssetManager);
		mButton2 = new GLSprite(gl, "gui/button2.png", null, mAssetManager);
		mUndo = new GLSprite(gl, "gui/undo.png", null, mAssetManager);
				
        //enemies
		mEnemySprite = new SpriteSheet[12];
   		mEnemySprite[0] = new SpriteSheet(gl,"spr/0.png", 16, 16, 2, mAssetManager);
    	mEnemySprite[1] = new SpriteSheet(gl,"spr/1.png", 32, 32, 2, mAssetManager);
   		mEnemySprite[2] = new SpriteSheet(gl,"spr/2.png", 32, 32, 2, mAssetManager);
  		mEnemySprite[3] = new SpriteSheet(gl,"spr/3.png", 32, 32, 2, mAssetManager);
    	mEnemySprite[4] = new SpriteSheet(gl,"spr/4.png", 64, 64, 2, mAssetManager);
    	mEnemySprite[5] = new SpriteSheet(gl,"spr/5.png", 64, 64, 2, mAssetManager);
    	mEnemySprite[6] = new SpriteSheet(gl,"spr/6.png", 32, 32, 2, mAssetManager);
    	mEnemySprite[7] = new SpriteSheet(gl,"spr/7.png", 32, 32, 2, mAssetManager);
    	mEnemySprite[8] = new SpriteSheet(gl,"spr/8.png", 32, 32, 2, mAssetManager);
      	mEnemySprite[9] = new SpriteSheet(gl,"spr/9.png", 32, 32, 2, mAssetManager);
      	mEnemySprite[10] = new SpriteSheet(gl,"spr/10.png", 64, 64, 2, mAssetManager);
		mEnemyBulletSprite = new SpriteSheet(gl, "spr/enemy_bullet.png", 32, 32, 3, mAssetManager);
		
    	//explosions
		mExplosionSprite = new SpriteSheet(gl, "spr/explosion.png", 64, 64, 8, mAssetManager);
    	
    	//player 
		mPlayerSprite = new SpriteSheet(gl, "spr/player.png", 48, 48, 9, mAssetManager);
		mPlayerBulletSprite = new SpriteSheet(gl, "spr/player_bullet.png", 32, 32, 3, mAssetManager);
		mSpecialWeapon = new PlayerSpecialWeapon(gl, 0,0, mAssetManager);

		//power-up
		mPowerupSprite = new SpriteSheet(gl, "spr/powerup.png", 16, 16, 3, mAssetManager);
		
		//scenery
		mScenerySprite = new SpriteSheet[3];
		mScenerySprite[0] = new SpriteSheet(gl,"obj/0.png", 617, 240, 1, mAssetManager);
		mScenerySprite[1] = new SpriteSheet(gl,"obj/1.png", 65, 65, 1, mAssetManager);
		mScenerySprite[2] = new SpriteSheet(gl,"obj/2.png", 118, 112, 1, mAssetManager);
	}
	
	/**
	 * Shows the title screen
	 */
	public void showTitleScreen(GL10 gl) {	
		Settings.State = "title";
        int bgFile[] = {1,1,1};
        float bgSpeed[] = {0.12f,0.06f,0.0f};
        mUserInterface.setSelectedOption(0);
        mUserInterface.resetUITimer();
        if(mBackground != null)
        	mBackground.destroy(gl);
        mPlayer = null;
        mBackground = new Background(gl, bgFile, 400, 240, bgSpeed, mAssetManager);
        mCamera = new Camera(0, 0);
        Player.mScore = 0;
		setMusic(0);
        mLevel = 1;
        mLives = Settings.Lives;
        mCompletedLevel = false;
        mEnemyList = new ArrayList<Enemy>();
        mSceneryList = new ArrayList<Scenery>();
        mExplosionList = new ArrayList<Explosion>();
        mPowerupList = new ArrayList<Powerup>(); 
        mLevelLoadingTimer=0;
	}
	
	/**
	 * Sets a new high score and stores it in the settings if the amount is greater than any of the top 3 scores
	 * @param amount The score amount
	 * @return True if a new high score has been set
	 */
	public boolean setHighScore(int amount)
	{
		if(amount > Settings.HighScore[0])
		{
			Settings.HighScore[2] = Settings.HighScore[1];
			Settings.HighScore[1] = Settings.HighScore[0];
			Settings.HighScore[0] = amount;
			mUserInterface.writeSettings();
			return true;
		}
		else if(amount > Settings.HighScore[1])
		{
			Settings.HighScore[2] = Settings.HighScore[1];
			Settings.HighScore[1] = amount;
			mUserInterface.writeSettings();
			return true;
		}
		else if(amount > Settings.HighScore[2]) 
		{	
			Settings.HighScore[2] = amount;
			mUserInterface.writeSettings();
			return true;
		}	
		else
			return false;	
	}
		
	/**
	 * Checks if the amount is a new high score
	 * @param amount The score amount
	 * @return True if a new high score has been set
	 */
	public boolean checkHighScore(int amount)
	{
		if(amount > Settings.HighScore[0] || amount > Settings.HighScore[1] || amount > Settings.HighScore[2])
			return true;
		else
			return false;					
	}
	
	/**
	 * Checks if the game is over 
	 * @return True if the game over or high score screen is displayed
	 */
	private boolean isGameOver()
	{
		if(Settings.State.equals("game over") || Settings.State.equals("game over high score"))
			return true;
		else
			return false;			
	}
	
	/**
     * Adds a power-up item to the level
     * @param gl The GL context
     * @param type The type of power-up 0-weapon 1-life 2-special weapon
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void addPowerup(GL10 gl, int type, float x, float y){
    	mPowerupList.add(new Powerup(gl, type,x,y));
    }
    
    /**
     * Adds an explosion to the level
     * @param gl The GL context
     * @param x The x coordinate
     * @param y The y coordinate
     */
	public void addExplosion(GL10 gl, float x, float y)
	{		
		mExplosionList.add(new Explosion(x, y));
		SoundEffect.play("explosion.wav");
	}	

    /**
   	 * Loads the level 
   	 * @param path The folder containing the levels
     * @return True if the level exists
     */
    public boolean loadLevel(GL10 gl, String path) {

        mLevelInfo = new Level(mAssetManager);
        if (mLevelInfo.loadLevel(path)) {	
            mLevelHeader = mLevelInfo.getHeader();
            mCamera = new Camera(0, 0);
            int bgFile[] = {1,1, mLevelHeader.background};
            float bgSpeed[] = {0.12f,0.06f,0.0f};
            if(mBackground != null)
            	mBackground.destroy(gl);
            mBackground = new Background(gl, bgFile, mLevelHeader.width, 240, bgSpeed, mAssetManager);
			int powerupLevel = Player.mPowerupLevel;
			int playerLife = 0;
			if(Settings.Difficulty == 0)
				playerLife = 5;
			else
				playerLife = 4;
			if(Player.mLife != 0)
				playerLife = Player.mLife;
			mPlayer = new Player();
			Player.mPowerupLevel = powerupLevel;
			Player.mLife = playerLife;
			mPowerupList = new ArrayList<Powerup>();
            mEnemyList = new ArrayList<Enemy>();
            mSceneryList = new ArrayList<Scenery>();
            mExplosionList = new ArrayList<Explosion>();
        	for(int i = 0; i < mLevelInfo.getTiles().size(); i++)
        	{
        		Tile t = mLevelInfo.getTile(i);
        		if(t.tileset == 1) //enemy sprite
        		{
        			mEnemyList.add(new Enemy(t.tile, t.x, t.y, t.health, t.weapon, t.powerup));      			
        		} 
        		else if(t.tileset == 2) //scenery
        		{
        			mSceneryList.add(new Scenery(t.tile, t.x, t.y));       			
        		}
        	}                        
			setMusic(mLevelHeader.music);
            return true;
        } else //level not found
        {
            return false;
        }
    }
    
    /**
     * Advances to the next level or show the end credits if game is complete
     */
    public void advanceLevel()
    {
    	if(mLevel < 5)
    	{
	    	mLevel++;
			Settings.State = "load level";	
    	}
    	else
    	{
    		mUserInterface.setCreditScrollY(180);
    		Settings.State = "end credits";
    		Settings.LevelSelect = true;
    		mUserInterface.writeSettings();
    	}
    	mLevelLoadingTimer = 0;
		mCompletedLevel = false;
    }   
    
	/**
	 * Sets the the level music
	 * @param file The mp3 file number
	 */
	public void setMusic(int file) {
		
		if (mMusic != null) {
			mMusic.destroy();
		}
		String path = "snd/" + file + ".mp3";
		mMusic = new Music(path, mAssetManager);
	}

	/**
	 * Draws the player, background, tileset and entities, handles collision detection
	 * and control the state of the game
	 * @param gl The GL context
	 * @param dt The delta time between frame updates
	 */
	public void paintComponent(GL10 gl, float dt) {	
		
		if(!Settings.State.equals("license"))
		{
			//draw background
			mCamera = mBackground.move(mCamera, dt); 
			mBackground.draw(gl, mCamera); 
		
			if((Settings.State.equals("game") && !Settings.Paused) || isGameOver()) {
				
				//move camera and player at same speed
				mCamera.y = 0;
				if (mCamera.x + 400 < mLevelHeader.width && !isGameOver()) {
					mCamera.x+=Settings.CameraSpeed * dt;  
					mPlayer.mX+=Settings.CameraSpeed * dt; 
				}			
				
				//draw scenery
				for (int i = 0; i < mSceneryList.size(); i++) {
					Scenery s = mSceneryList.get(i);
					if (s.mX > mCamera.x - (mScenerySprite[s.getType()].getWidth() * 2) && s.mX < mCamera.x + 400) {
						s.draw(gl, mCamera, mScenerySprite[s.getType()]);
					}
				}
				
				//draw power-ups
		    	for(int i = 0; i < mPowerupList.size(); i++)
		    	{
		    		mPowerupList.get(i).draw(gl, mCamera, mPowerupSprite);
		    		if(mPowerupList.get(i).mX > mCamera.x + 400)
		    			mPowerupList.remove(i);
		    	}	
		    	
		    	//draw enemies
				for (int i = 0; i < mEnemyList.size(); i++) {
					Enemy e = mEnemyList.get(i);

					if (e.mX > mCamera.x - mEnemySprite[e.getType()].getWidth() && e.mX < mCamera.x + 400) //enemy is in screen area
					{
						if(!isGameOver())
							e.move(mCamera, dt, mLevel); //move enemy
						
						e.draw(gl, mCamera, mEnemySprite[e.getType()], mEnemyBulletSprite);
					}
					
					//enemies life <= 0 set as destroyed and add to players score
					if(mEnemyList.get(i).getLife() <= 0 && !mEnemyList.get(i).getDestroyed())
		    		{
		    			if(mEnemyList.get(i).getType() <= 5)
		    				mPlayer.addScore(200);
		    			if(mEnemyList.get(i).getType() >= 5 && mEnemyList.get(i).getType() < 10)
		    				mPlayer.addScore(500);
		    			if(mEnemyList.get(i).getType() == 10) //end of level boss
		    			{
		    				mPlayer.addScore(1000); 
		    				mCompletedLevel = true; //set completed level 
		    				mLevelLoadingTimer = 0;
		    			}
		    			//add explosion
		    			addExplosion(gl, mEnemyList.get(i).mX + (mEnemySprite[e.getType()].getWidth() / 2) - 32 , mEnemyList.get(i).mY + (mEnemySprite[e.getType()].getHeight() / 2) - 32);		
		    			if(mEnemyList.get(i).getPowerup() > 0) //if enemy contains a power-up
		    				addPowerup(gl, mEnemyList.get(i).getPowerup(), mEnemyList.get(i).mX + (mEnemySprite[e.getType()].getWidth() / 2) - 4  , mEnemyList.get(i).mY + (mEnemySprite[e.getType()].getHeight() / 2) - 5);		    			
		    			mEnemyList.get(i).setDestroyed(true);
		    		}
		    		
		    		//remove enemy 
	    			if(mEnemyList.get(i).getBulletList().size() < 1 && mEnemyList.get(i).getDestroyed() || e.mX < mCamera.x - mEnemySprite[e.getType()].getWidth())
	    			{
	    				mEnemyList.remove(i);
	    				i--;
	    			}				
				}
			
				//draw explosions
		    	for(int i = 0; i < mExplosionList.size(); i++)
		    	{
		    		mExplosionList.get(i).draw(gl, mCamera, mExplosionSprite);
		    		if(mExplosionList.get(i).getFrame() == 7)
		    			mExplosionList.remove(i);
		    	} 
					
		    	//if level is complete advance to next level
		    	if(mCompletedLevel)
		    	{
		    		mLevelLoadingTimer++;
		    		if(mLevelLoadingTimer > 200)
		    		{
		    			advanceLevel();	
		    		}
		    	}	
		    	
		    	//player died
				if(mPlayer.getLife() <= 0 && !isGameOver())
				{
					if(mLives > 1) //reset level
					{
						mLives--;
						Player.mPowerupLevel = 0;
						Settings.State = "load level";	
						mLevelLoadingTimer = 0;
					}
					else //end game
					{	
						if(checkHighScore(mPlayer.getScore()))
							Settings.State = "game over high score";
						else
							Settings.State = "game over";
					}
					mCompletedLevel = false; 
					SoundEffect.play("die.wav");	
				}
					
				//draw player
				if(mPlayer != null && mPlayer.getLife() > 0)
				{
					mPlayer.move(mCamera, dt);
					mPlayer.draw(gl, mCamera, mPlayerSprite, mPlayerBulletSprite, mSpecialWeapon);
					mPlayer.detectPowerupCollision(mPowerupList);
					mPlayer.detectEnemyCollision(gl, mCamera, mEnemyList, mSpecialWeapon);
					mUserInterface.drawEnergybar(gl, 250, 19, mPlayer.getLife());
					mUserInterface.drawScore(gl, 15, 24, mPlayer.getScore());
				}
				mUndo.draw(gl, 350, 15);		
			}	
			else {
				mUserInterface.draw(gl, dt); //draw user interface				
			}
		}
		
		if (Settings.State.equals("license")) //draw license screen
		{
			gl.glClearColor(235, 235, 235, 0);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			mCompanyLogo.draw(gl, 53, 78);
			mLevelLoadingTimer++;
			if (mLevelLoadingTimer == 5) {
				preloadGraphics(gl);
				mUserInterface = new UserInterface(gl, mAssetManager);
				SoundEffect.loadSounds(mAssetManager);
			}
			else if(mLevelLoadingTimer == 30) //25
			{
				showTitleScreen(gl);	
			}
		}
		else if (Settings.State.equals("load level")) //draw level loading screen
		{
			mUserInterface.drawLevelOpen(gl, mLevel, mLives);	
			if (mLevelLoadingTimer == 0) {
				//set music
				if (mMusic != null) {
					if(Settings.Music)
						Music.pauseMusic();
					mMusic.destroy();
					mMusic = null;
				}			
			} 
			else if (mLevelLoadingTimer > 60) //load level  //40
			{
				loadLevel(gl,"lvl/" + mLevel + ".lvl");
				Settings.State = "game";
				mPressedLevelSelect =false;
			}
			mLevelLoadingTimer++;
		}
		else //all other screens
		{
			//overlay d-pad, fire and undo button images on game window
			mDPad.draw(gl, 15, 150);
			mButton1.draw(gl, 340, 180);
			if(mPlayer != null && mPlayer.hasCollectedSpecial() && !Settings.State.equals("end credits"))
				mButton2.draw(gl, 290, 180);
			mUndo.draw(gl, 350, 15);
			
			if(Settings.State.equals("exit")) //exit game show title screen	
			{	
				if(mPlayer != null)
					setHighScore(mPlayer.getScore());
				showTitleScreen(gl);
			}
			
			if(Settings.State.equals("game over")) //exit game show title screen
			{
				mUserInterface.drawGameOver(gl, 0);
			}
			if(Settings.State.equals("game over high score")) //exit game show title screen
			{
				mUserInterface.drawGameOver(gl, mPlayer.getScore());
			}
			
		}
		mOptionTimer++;
	}

	/**
	 * Invoked when a key has been pressed
	 * @param e Event which indicates that a keystroke occurred
	 */
	public void onKeyDown(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.KEYCODE_BACK) 
		{
			if(mUserInterface!= null)
				mUserInterface.keyPress("BACK");
		}
		else if(e.getKeyCode() == KeyEvent.KEYCODE_P)
		{
			advanceLevel();
			//Settings.State = "loading new level";
			//mLevelLoadingTimer = 0;
		}
		else if (e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			if (Settings.State.equals("game")) {
				Settings.Paused = true;
				Settings.State = "pause";
				SoundEffect.play("select.wav");
			} else {
				mUserInterface.onKeyDown(e);
			}
		}
		else
		{
			if (Settings.State.equals("game"))
				setControl(e, true);
			else {
				if (!Settings.State.equals("license"))
					mUserInterface.onKeyDown(e);
			}
		}
		
		
	}
	
	/**
	 * Invoked when a key has been released
	 * @param e Event which indicates that a keystroke occurred
	 */
	public void onKeyUp(KeyEvent e) {
		setControl(e, false);
	}
	      
	/**
     * Called when a touch screen motion event occurs
     * @param event The motion event that occurred 
     */
    public void onTouchEvent(final MotionEvent event) {

		if(!Settings.State.equals("license") && !Settings.State.equals("level open"))
		{
			String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
					"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
			StringBuilder sb = new StringBuilder();
			int action = event.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			sb.append(names[actionCode]);
			if (actionCode == MotionEvent.ACTION_POINTER_DOWN
					|| actionCode == MotionEvent.ACTION_POINTER_UP) {
				sb.append("(pid ").append(
						action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
				sb.append(")");
			}

			String ac = sb.toString();
			if (ac.equals("DOWN")) {
				setTouchControls(event.getX(), event.getY(), true);
			}else if (ac.equals("UP")) {
				setTouchControls(event.getX(), event.getY(), false);
			}	
	    	else if (ac.equals("MOVE")) {
	    		setTouchControls(event.getX(), event.getY(), true);
	    	}
			else if (ac.equals("POINTER_DOWN(pid 1)")) {
				setTouchControls(event.getX(1), event.getY(1), true);
			} else if (ac.equals("POINTER_DOWN(pid 0)")) {
				setTouchControls(event.getX(0), event.getY(0), true);
			}
			else if (ac.equals("POINTER_UP(pid 1)")) {
				setTouchControls(event.getX(1), event.getY(1), false);
			} else if (ac.equals("POINTER_UP(pid 0)")) {
				clearControls();
				setTouchControls(event.getX(0), event.getY(0), false);
			}			
    	}
    }
    
    /**
     * Clears all of the player controls variables to false values
     */
    public void clearControls()
    {
    	if(mPlayer != null)
    	{
			mPlayer.mControls.mUpPressed = false;
			mPlayer.mControls.mLeftPressed = false;
			mPlayer.mControls.mDownPressed = false;
			mPlayer.mControls.mRightPressed = false; 
    	}
	}

    /**
     * Sets a control action based on the coordinates of the touch event
     * @param x The x coordinate of the touch event
     * @param y The y coordinate of the touch event
     * @param pressed Is the button pressed or released
     */
	public void setTouchControls(float x, float y, boolean pressed) {

		double x1, x2, y1, y2;    	
    	float w = (float)GLSurfaceViewRenderer.mWidth;
    	float h =  (float)GLSurfaceViewRenderer.mHeight;

		x = (float)(x / w * 400.0);
		y = (float)(y / h * 240.0);

		//Log.i("Controls", "x="+x + " y=" + y);
		
		//up
		x1 = 28;
		x2 = 74;
		y1 = 134;
		y2 = 176;
		if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
			if (Settings.State.equals("game") && !Settings.Paused
					&& mPlayer != null) {
				clearControls();
				mPlayer.mControls.mUpPressed = pressed;
			} else {
				if (pressed)
					mUserInterface.keyPress("UP");
			}
		}

		//down
		x1 = 28;
		x2 = 74;
		y1 = 196;
		y2 = 240;
		if (x >= x1 && x < x2 && y > y1 && y <= y2) {
			if (Settings.State.equals("game") && !Settings.Paused
					&& mPlayer != null) {
				clearControls();
				mPlayer.mControls.mDownPressed = pressed;
			} else {
				if (pressed)
					mUserInterface.keyPress("DOWN");
			}
		}

		//left
		x1 = 0;
		x2 = 41;
		y1 = 163;
		y2 = 207;
		if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
			if (Settings.State.equals("game") && !Settings.Paused
					&& mPlayer != null) {
				clearControls();
				mPlayer.mControls.mLeftPressed = pressed;
			} else {
				if (pressed)
					mUserInterface.keyPress("LEFT");
			}
		}

		//right
		x1 = 62;
		x2 = 100;
		y1 = 163;
		y2 = 207;
		if (x >= x1 && x <= x2 && y > y1 && y < y2) {
			if (Settings.State.equals("game") && !Settings.Paused
					&& mPlayer != null) {
				clearControls();
				mPlayer.mControls.mRightPressed = pressed;
			} else {
				if (pressed)
					mUserInterface.keyPress("RIGHT");
			}
		}

		//fire 1
		x1 = 325;
		x2 = 400;
		y1 = 150;
		y2 = 240;
		if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
			if (Settings.State.equals("game") && !Settings.Paused
					&& mPlayer != null) {
				// clearControls();
				mPlayer.mControls.mFire1Pressed = pressed;
			} else {
				if (pressed)
					mUserInterface.keyPress("ENTER");
			}
		}

		//fire 2
		x1 = 290;
		x2 = 320;
		y1 = 150;
		y2 = 240;
		if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
			if (Settings.State.equals("game") && !Settings.Paused
					&& mPlayer != null) {
				// clearControls();
				mPlayer.mControls.mFire2Pressed = pressed;
			}
		}

		//back
		x1 = 325;
		x2 = 500;
		y1 = 0;
		y2 = 70;
		if (x >= x1 && x <= x2 && y > y1 && y <= y2) {	
			if(isGameOver())
			{
				Settings.State = "exit";
				SoundEffect.play("select.wav");
			}
			else
			{
				if(mOptionTimer > Settings.MenuDelay && pressed)
				{
					mUserInterface.keyPress("BACK");
					mOptionTimer=0;
				}
			}		
		}			

		//title screen
		if (Settings.State.equals("title") && mOptionTimer > Settings.MenuDelay) {
			// new game
			x1 = 140;
			x2 = 260;
			y1 = 115;
			y2 = 135;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.State = "load level";
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//high scores
			x1 = 140;
			x2 = 260;
			y1 = 140;
			y2 = 170;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.State = "high scores";
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//settings
			x1 = 140;
			x2 = 260;
			y1 = 175;
			y2 = 205;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.State = "options";
				mUserInterface.setSelectedOption(0);
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}
		} else if (Settings.State.equals("options") && mOptionTimer > Settings.MenuDelay) {
			//music
			x1 = 125;
			x2 = 265;
			y1 = 115;
			y2 = 130;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				if (Settings.Music) {
					Settings.Music = false;
					Music.pauseMusic();
				} else {
					Settings.Music = true;
					Music.startMusic();
				}
				mUserInterface.setSelectedOption(0);
				mUserInterface.writeSettings();
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//sound effects
			x1 = 125;
			x2 = 265;
			y1 = 140;
			y2 = 155;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.Sound = !Settings.Sound;
				mUserInterface.setSelectedOption(1);
				mUserInterface.writeSettings();
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//difficulty
			x1 = 125;
			x2 = 265;
			y1 = 160;
			y2 = 185;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				if (Settings.Difficulty < 2)
					Settings.Difficulty++;
				else
					Settings.Difficulty = 0;
				mUserInterface.setSelectedOption(2);
				Settings.setDifficulty(Settings.Difficulty);
				mUserInterface.writeSettings();
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//game credits
			x1 = 125;
			x2 = 265;
			y1 = 180;
			y2 = 205;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				mUserInterface.setSelectedOption(3);
				mUserInterface.setCreditScrollY(100);
				Settings.State = "credits";
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}
		} else if (Settings.State.equals("pause") && mOptionTimer > Settings.MenuDelay) {
			//continue
			x1 = 155;
			x2 = 255;
			y1 = 100;
			y2 = 115;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.State = "game";
				Settings.Paused = false;
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//options
			x1 = 155;
			x2 = 255;
			y1 = 120;
			y2 = 135;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.State = "pause options";
				mUserInterface.setSelectedOption(0);
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//quit
			x1 = 155;
			x2 = 255;
			y1 = 140;
			y2 = 155;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.State = "exit";
				Settings.Paused = false;
				mUserInterface.setSelectedOption(0);
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}
		} else if (Settings.State.equals("pause options") && mOptionTimer > Settings.MenuDelay) {
			//music
			x1 = 150;
			x2 = 255;
			y1 = 110;
			y2 = 125;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				if (Settings.Music) {
					Settings.Music = false;
					Music.pauseMusic();
				} else {
					Settings.Music = true;
					Music.startMusic();
				}
				mUserInterface.setSelectedOption(0);
				mUserInterface.writeSettings();
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}

			//sound
			x1 = 150;
			x2 = 255;
			y1 = 130;
			y2 = 145;
			if (x >= x1 && x <= x2 && y > y1 && y <= y2) {
				Settings.Sound = !Settings.Sound;
				mUserInterface.setSelectedOption(1);
				mUserInterface.writeSettings();
				SoundEffect.play("select.wav");
				mOptionTimer = 0;
			}
		}
	}

    /**
	 * Sets the controller flags for the player
     * @param e event which indicates that a keystroke occurred in a component
     * @param pressed True if a key is pressed
     */
    public void setControl(KeyEvent e, boolean pressed) {
        int keyCode = e.getKeyCode();
        if (mPlayer != null) {
            if (keyCode == KeyEvent.KEYCODE_A || keyCode == KeyEvent.KEYCODE_SPACE) {
                mPlayer.mControls.mFire1Pressed = pressed;
            } 
            if (keyCode == KeyEvent.KEYCODE_S || keyCode == KeyEvent.KEYCODE_ALT_LEFT) {
                mPlayer.mControls.mFire2Pressed = pressed;
            } 
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            	mPlayer.mControls.mRightPressed = pressed;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            	mPlayer.mControls.mUpPressed = pressed;
               
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            	mPlayer.mControls.mLeftPressed = pressed;          
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            	mPlayer.mControls.mDownPressed = pressed;
            }
        }
    }
    
    /**
     * Destroys the resources and releases the hardware buffers
     * @param gl The GL context
     */
	public void destroy(GL10 gl) {

		//user interface
		if(mDPad != null)
			mDPad.destroy(gl);
		if(mButton1 != null)
			mButton1.destroy(gl);
		if(mButton2 != null)
			mButton2.destroy(gl);
		if(mUndo != null)
			mUndo.destroy(gl);
		if(mUserInterface != null)
			mUserInterface.destroy(gl);	
		
		//background
		if (mBackground != null)
			mBackground.destroy(gl);
		
		//player
		if(mPlayerSprite != null)
			mPlayerSprite.destroy(gl);
		if(mPlayerBulletSprite != null)
			mPlayerBulletSprite.destroy(gl);
		if(mSpecialWeapon != null)
			mSpecialWeapon.destroy(gl);
			
		//power-ups
    	if(mPowerupSprite != null)
   	 		mPowerupSprite.destroy(gl);
		if(mPowerupList != null)
			mPowerupList.clear();
			
		//enemies
    	for(int i = 0; i < mEnemySprite.length; i++)
    	{
    		if(mEnemySprite[i] != null)
    			mEnemySprite[i].destroy(gl);
    	}			
		if(mEnemyList != null)
			mEnemyList.clear();

		//scenery
    	for(int i = 0; i < mScenerySprite.length; i++)
    	{
    		if(mScenerySprite[i] != null)
    			mScenerySprite[i].destroy(gl);
    	}		
		if(mSceneryList != null)
			mSceneryList.clear();
	
		//sound effects
		SoundEffect.destroy();		
		
		//music
		if (mMusic != null)
			mMusic.destroy();

		//explosions
		if(mExplosionList != null)
			mExplosionList.clear();	
		 
		if(mExplosionSprite != null)
			mExplosionSprite.destroy(gl);	

        //run the GC
        Runtime r = Runtime.getRuntime();
        r.gc();
	}
}
