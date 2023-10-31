package com.citex.java.spacewar;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
    This class draws all of the graphics for the game, handles game logic and 
    collision detection between the player, objects and entities.
	
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

public class GamePanel extends JPanel implements KeyListener, MouseListener,
        MouseMotionListener, MouseWheelListener, Runnable {

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
	/** The d-pad graphic */
    private BufferedImage mDPad;
	/** The button graphic */
    private BufferedImage mButton1;
	/** The button graphic */
    private BufferedImage mButton2;
	/** The undo graphic */
    private BufferedImage mUndo;
	/** The company logo graphic */
	private BufferedImage mCompanyLogo;
	/** The time between frame updates */
	private float mDeltaTime; 
    /** The main game thread */    
    private Thread thread;
	/** The double buffer graphics surface */
    private BufferedImage back_buffer;
    /** The double buffer graphics context */
    private Graphics2D back_buffer_graphics;   
    /** Distinguishes the class when it is serialized and deserialized */
    public final static long serialVersionUID = 3000000;
   
    /**
	 * Constructs the GamePanel
	 */
	public GamePanel() {
		
		//create double buffer graphics
        back_buffer = new BufferedImage(400, 240, BufferedImage.TYPE_INT_RGB);
        back_buffer_graphics = (Graphics2D) back_buffer.getGraphics();
       
        //add event listeners
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setFocusable(true);
        requestFocus();
        
        //preload the the graphics
        preloadGraphics();
		mUserInterface = new UserInterface();
		mLevelLoadingTimer = 0;
		if(mCompanyLogo == null)
			Settings.State = "error";
		else		
			Settings.State = "license";
		Settings.Paused = false;
		mCamera = new Camera(0, 0);
	}

	/**
	 * Preload enemy, player, power-up and scenery sprite sheets
	 */
	public void preloadGraphics()
	{
		//company logo
		mCompanyLogo = new Image("res/gui/logo.bmp").getImage();

		//controls
		mDPad = new Image("res/gui/d-pad.png").getImage();
		mButton1 = new Image("res/gui/button1.png").getImage();
		mButton2 = new Image("res/gui/button2.png").getImage();
		mUndo = new Image("res/gui/undo.png").getImage();	

        //enemies
		mEnemySprite = new SpriteSheet[12];
   		mEnemySprite[0] = new SpriteSheet("res/spr/0.bmp", 16, 16, 2);
    	mEnemySprite[1] = new SpriteSheet("res/spr/1.bmp", 32, 32, 2);
   		mEnemySprite[2] = new SpriteSheet("res/spr/2.bmp", 32, 32, 2);
  		mEnemySprite[3] = new SpriteSheet("res/spr/3.bmp", 32, 32, 2);
    	mEnemySprite[4] = new SpriteSheet("res/spr/4.bmp", 64, 64, 2);
    	mEnemySprite[5] = new SpriteSheet("res/spr/5.bmp", 64, 64, 2);  
    	mEnemySprite[6] = new SpriteSheet("res/spr/6.bmp", 32, 32, 2);   
    	mEnemySprite[7] = new SpriteSheet("res/spr/7.bmp", 32, 32, 2);  
    	mEnemySprite[8] = new SpriteSheet("res/spr/8.bmp", 32, 32, 2);    
      	mEnemySprite[9] = new SpriteSheet("res/spr/9.bmp", 32, 32, 2);   
      	mEnemySprite[10] = new SpriteSheet("res/spr/10.bmp", 64, 64, 2);   	
		mEnemyBulletSprite = new SpriteSheet("res/spr/enemy_bullet.bmp", 32, 32, 3);	
		
    	//explosions
		mExplosionSprite = new SpriteSheet("res/spr/explosion.bmp", 64, 64, 8); 	
    	
    	//player 
		mPlayerSprite = new SpriteSheet("res/spr/player.bmp", 48, 48, 9);
		mPlayerBulletSprite = new SpriteSheet("res/spr/player_bullet.bmp", 32, 32, 3);
		mSpecialWeapon = new PlayerSpecialWeapon(0,0);

		//power-up
		mPowerupSprite = new SpriteSheet("res/spr/powerup.bmp", 16, 16, 3);
		
		//scenery
		mScenerySprite = new SpriteSheet[3];
		mScenerySprite[0] = new SpriteSheet("res/obj/0.bmp", 617, 240, 1);
		mScenerySprite[1] = new SpriteSheet("res/obj/1.bmp", 65, 65, 1);
		mScenerySprite[2] = new SpriteSheet("res/obj/2.bmp", 118, 112, 1);		
	}    
    
	/**
	 * Shows the title screen
	 */
	public void showTitleScreen() {	
		Settings.State = "title";
        int bgFile[] = {1,1,1};
        float bgSpeed[] = {0.12f,0.06f,0.0f};
        mUserInterface.setSelectedOption(0);
        mPlayer = null;
        mBackground = new Background(bgFile, 400, 240, bgSpeed);
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
		mLevelLoadingTimer = 0;
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
     * @param type The type of power-up 0-weapon 1-life 2-special weapon
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void addPowerup(int type, float x, float y){
    	mPowerupList.add(new Powerup(type,x,y));
    }
    
    /**
     * Adds an explosion to the level
     * @param x The x coordinate
     * @param y The y coordinate
     */
	public void addExplosion(float x, float y)
	{		
		mExplosionList.add(new Explosion(x, y));
		new SoundEffect("res/sfx/explosion.wav").run();
	}	
	
    /**
   	 * Loads the level 
   	 * @param path The folder containing the levels
     * @return True if the level exists
     */
    public boolean loadLevel(String path) {

    	mLevelInfo = new Level();
        if (mLevelInfo.loadLevel(path)) {	
            mLevelHeader = mLevelInfo.getHeader();
            //mCamera = new Camera(2200, 0);
            mCamera = new Camera(0, 0);
            int bgFile[] = {1,1, mLevelHeader.background};
            float bgSpeed[] = {0.12f,0.06f,0.0f};
            mBackground = new Background(bgFile, mLevelHeader.width, 240, bgSpeed);
            mPlayer = new Player();
            //mCamera = new Camera(2200, 0);
            //mPlayer.mX = 2300;
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
    	if(mLevel < UserInterface.getFileCount("res/lvl/"))
    	{
	    	mLevel++;
			Settings.State = "load level";
    	}
    	else
    	{
    		UserInterface.mCreditScrollY = 180;
    		Settings.State = "end credits";
    		Settings.LevelSelect = true;
    		mUserInterface.writeSettings();
    	}
		mLevelLoadingTimer = 0;
		mCompletedLevel = false;		
    }   
    
    /**
     * Gets the current level number
     */
    public int getLevel()
    {
    	return mLevel;  	
    }
   
    /**
     * Sets the level music
     * @param file The mp3 file number
     */
	public void setMusic(int file) {

		String path = System.getProperty("user.dir") + "\\res\\snd\\" + file + ".mp3"; 
		
		if (mMusic != null)
			Music.closeMusic();

		mMusic = new Music(path);

		if (!Settings.Music) {
			Music.stopMusic();
		}
	}     
    
    /**
     * Starts the main game thread
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Repaint the game panel in a thread
     */
	public void run() {
		
		//time variables
		long t1 = System.currentTimeMillis(); 
		long t2;		
    	while (true) {
	    	t2 = System.currentTimeMillis(); //get the current time
			mDeltaTime = t2 - t1; //calculate delta time between frame updates
	    	repaint();		        
	        t1 = t2; //update time variables      
	        try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}	
		}
	}
	
	/**
	 * Draws the player, background, enemies, scenery, handles collision 
	 * detection and control state of the game
	 * @param gl The graphics context
	 */
    public void paintComponent(Graphics g) {
	
    	if(!Settings.State.equals("license") && !Settings.State.equals("error"))
		{	
			//draw background
    		mCamera = mBackground.move(mCamera, mDeltaTime); 
			mBackground.draw(back_buffer_graphics, mCamera); 
			
			if((Settings.State.equals("game") && !Settings.Paused) || isGameOver()) {
				
				//move camera and player at same speed
				mCamera.y = 0;
				if (mCamera.x + 400 < mLevelHeader.width && !isGameOver()) {
					mCamera.x+=Settings.CameraSpeed * mDeltaTime;  
					mPlayer.mX+=Settings.CameraSpeed * mDeltaTime; 
				}			
				
				//draw scenery
				for (int i = 0; i < mSceneryList.size(); i++) {
					Scenery s = mSceneryList.get(i);
					if (s.mX > mCamera.x - (mScenerySprite[s.getType()].getWidth() * 2) && s.mX < mCamera.x + 400) {
						s.draw(back_buffer_graphics, mCamera, mScenerySprite[s.getType()]);
					}
				}
				
				//draw power-ups
		    	for(int i = 0; i < mPowerupList.size(); i++)
		    	{
		    		mPowerupList.get(i).draw(back_buffer_graphics, mCamera, mPowerupSprite);
		    		if(mPowerupList.get(i).mX > mCamera.x + 400)
		    			mPowerupList.remove(i);
		    	}	
		    	
		    	//draw enemies
				for (int i = 0; i < mEnemyList.size(); i++) {
					Enemy e = mEnemyList.get(i);

					if (e.mX > mCamera.x - mEnemySprite[e.getType()].getWidth() && e.mX < mCamera.x + 400) //enemy is in screen area
					{
						if(!isGameOver())
							e.move(mCamera, mDeltaTime, mLevel); //move enemy
						e.draw(back_buffer_graphics, mCamera, mEnemySprite[e.getType()], mEnemyBulletSprite);
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
		    			addExplosion(mEnemyList.get(i).mX + (mEnemySprite[e.getType()].getWidth() / 2) - 32 , mEnemyList.get(i).mY + (mEnemySprite[e.getType()].getHeight() / 2) - 32);		
		    			if(mEnemyList.get(i).getPowerup() > 0) //if enemy contains a power-up
		    				addPowerup(mEnemyList.get(i).getPowerup(), mEnemyList.get(i).mX + (mEnemySprite[e.getType()].getWidth() / 2) - 4  , mEnemyList.get(i).mY + (mEnemySprite[e.getType()].getHeight() / 2) - 5);		    			
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
		    		mExplosionList.get(i).draw(back_buffer_graphics, mCamera, mExplosionSprite);
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
						Settings.State = "load level";	
						mCompletedLevel = false; 
						mLevelLoadingTimer = 0;
						Player.mLife = 4;				
					}
					else //end game
					{	
						if(checkHighScore(mPlayer.getScore()))
							Settings.State = "game over high score";
						else
							Settings.State = "game over";
					}
					mCompletedLevel = false; 
					new SoundEffect("res/sfx/die.wav").run();
				}  		
					
				//draw player
				if(mPlayer != null && mPlayer.getLife() > 0)
				{
					mPlayer.move(mCamera, mDeltaTime); 
					mPlayer.draw(back_buffer_graphics, mCamera, mPlayerSprite, mPlayerBulletSprite, mSpecialWeapon);
					mPlayer.detectPowerupCollision(mPowerupList);
					if(mEnemyList != null)
						mPlayer.detectEnemyCollision(mCamera, mEnemyList, mSpecialWeapon);
					mUserInterface.drawEnergyBar(back_buffer_graphics, 250, 19, mPlayer.getLife());
					mUserInterface.drawScore(back_buffer_graphics, 15, 24, mPlayer.getScore());
				}				
			}	
			else {
				
	        	if (Main.mFrame.mEditorPanel != null) //level editor
	            {
	        		Main.mFrame.mEditorPanel.paintComponent(back_buffer_graphics);
	            }   
				mUserInterface.draw(back_buffer_graphics, mDeltaTime); //draw user interface 
			}		
		}
		
    	if (Settings.State.equals("license")) //draw license screen
		{
			if (mMusic != null && Settings.Music) {
				Music.closeMusic();
				mMusic = null;
			}
			
			back_buffer_graphics.setColor(new Color(255, 255, 255));
			back_buffer_graphics.fillRect(0, 0, Settings.ScreenWidth, Settings.ScreenHeight);
			back_buffer_graphics.drawImage(mCompanyLogo,  Settings.ScreenWidth/2 - mCompanyLogo.getWidth()/2, Settings.ScreenHeight/2 - mCompanyLogo.getHeight()/2, null);
			mLevelLoadingTimer++;
			if (mLevelLoadingTimer == 5) {
				preloadGraphics();
				mUserInterface = new UserInterface();
			}
			else if (mLevelLoadingTimer == 60) { //120 for youtube video
				showTitleScreen();	
			}
		}
    	else if (Settings.State.equals("load level")) //draw level loading screen
		{
			mUserInterface.drawLevelOpen(back_buffer_graphics, mLevel, mLives);	
			if (mLevelLoadingTimer == 0) {
				//set music
				if (mMusic != null && Settings.Music) {
					Music.closeMusic();
					mMusic = null;
				}			
			} 
			else if (mLevelLoadingTimer > 70) //load level 
			{
				loadLevel("res/lvl/" + mLevel + ".lvl");
				Settings.State = "game";
			}
			mLevelLoadingTimer++;
		}		
		else if(Settings.State.equals("error")) //error cannot find resources
		{
			back_buffer_graphics.setColor(new Color(255, 255, 255));
			back_buffer_graphics.fillRect(0, 0, Settings.ScreenWidth, Settings.ScreenHeight);
			back_buffer_graphics.setColor(Color.BLACK);
			back_buffer_graphics.drawString("Error loading resources", 8, 17);
			back_buffer_graphics.drawString("Please check <game path>/res folder exists", 8, 32);
			back_buffer_graphics.drawString("Re-download Space War from www.citexsoftware.co.uk/spacewar", 8, 47);
		}
		else //all other screens
		{
			if(Settings.ShowOnScreenControls)
			{
				//overlay d-pad, fire and undo button images on game window
				back_buffer_graphics.drawImage(mDPad, 15, 150, this);
				back_buffer_graphics.drawImage(mButton1, 340, 180, this);			
				if(mPlayer != null && mPlayer.hasCollectedSpecial() && !Settings.State.equals("end credits"))
					back_buffer_graphics.drawImage(mButton2, 290, 180, this);
				if(!Settings.State.equals("title") && !Settings.State.equals("pause"))
				{
					back_buffer_graphics.drawImage(mUndo, 350, 15, this);
				}
			}	
			
			if(Settings.State.equals("exit")) //exit game show title screen	
			{	
				if(mPlayer != null)
					setHighScore(mPlayer.getScore());
				showTitleScreen();
			}	
			
			if(Settings.State.equals("game over")) //exit game show title screen
			{
				mUserInterface.drawGameOver(back_buffer_graphics, 0);
			}
			
			if(Settings.State.equals("game over high score")) //exit game show title screen
			{
				mUserInterface.drawGameOver(back_buffer_graphics, mPlayer.getScore());
			}						
		}
		
        g.drawImage(back_buffer, 0, 0, getWidth() + 2, getHeight() + 1, this); //draw back buffer
        
        if(Settings.DebugMode == true)
        {
	        g.setColor(Color.RED);
	        if(mPlayer != null)
	        	g.drawString("Memory = " + Runtime.getRuntime().totalMemory() / 1024 + "kb" + " x = " + (int)mPlayer.mX + " y = " + (int)mPlayer.mY, 8, 16);
	        else
	        	g.drawString("Memory = " + Runtime.getRuntime().totalMemory() / 1024 + "kb", 8, 16);
        }
    }

    /**
     * Invoked when a key has been pressed<BR>
     * Sets the player controls, pause the game or pass event the level editor panel
     * @param e event which indicates that a keystroke occurred in a component
    */
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_F7)
        {
        	Settings.ShowOnScreenControls = !Settings.ShowOnScreenControls;  	
        }    	
    	
        if(e.getKeyCode() == KeyEvent.VK_F8)
        {
        	Settings.DebugMode = !Settings.DebugMode;  	
        }
      
        if(e.getKeyCode() == KeyEvent.VK_END && Settings.LevelSelect && Settings.State.equals("game") && !Settings.Paused)
        {
        	advanceLevel();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
	        if (Settings.State.equals("game") || Settings.State.equals("level editor")) {
	        	if(Settings.LevelEditor)
	        	{
		            if(!Main.mFrame.mEditorPanel.isEditingEnemy())
		            {
			        	new SoundEffect("res/sfx/select.wav").start();
			            Settings.Paused = true;
			            Settings.State = "pause";
		            }
	        	}
	        	else
	        	{
		        	new SoundEffect("res/sfx/select.wav").start();
		            Settings.Paused = true;
		            Settings.State = "pause";
	        		
	        	}
	        } else {
                mUserInterface.keyPressed(e);
            }    
        }
        else
        {
	        if (Settings.State.equals("game") || Settings.State.equals("level editor"))
	            setControl(e, true);
	        else
	        	mUserInterface.keyPressed(e);
        }   
    
        if (Settings.State.equals("level editor")) {
            Main.mFrame.mEditorPanel.keyPressed(e);
        }    
    }

    /**
     * Invoked when a key has been released
     * @param e event which indicates that a keystroke occurred in a component
    */
    public void keyReleased(KeyEvent e) {
        setControl(e, false);
    }

    /**
     * Invoked when a key has been typed
     * @param e event which indicates that a keystroke occurred in a component
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
	 * Sets the controller flags for the player
     * @param e event which indicates that a keystroke occurred in a component
     * @param pressed True if a key is pressed
     */
    public void setControl(KeyEvent e, boolean pressed) {
        int keyCode = e.getKeyCode();
        if (mPlayer != null) {
            if (keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_SPACE) {
                mPlayer.mControls.mFire1Pressed = pressed;
            } 
            if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_CONTROL) {
                mPlayer.mControls.mFire2Pressed = pressed;
            } 
            if (keyCode == KeyEvent.VK_UP) {
                mPlayer.mControls.mUpPressed = pressed;
            }
            if (keyCode == KeyEvent.VK_LEFT) {
                mPlayer.mControls.mLeftPressed = pressed;
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                mPlayer.mControls.mDownPressed = pressed;
            }
            if (keyCode == KeyEvent.VK_RIGHT) {
                mPlayer.mControls.mRightPressed = pressed;
            }
            
        }
    }

    /**
     * Invoked when the mouse enters a component
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component
     * @param e An event which indicates that a mouse action occurred in a component
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been pressed on a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseMoved(MouseEvent e) {
  	
        if (Settings.State.equals("level editor")) {
            Main.mFrame.mEditorPanel.mouseMoved(e);
        }
    }

    /**
     * Invoked when the mouse exits a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseExited(MouseEvent e) {
    	
    	if (Settings.State.equals("level editor")) {
            Main.mFrame.mEditorPanel.mouseExited(e);
        }
    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseDragged(MouseEvent e) {	
    	
        if (Settings.State.equals("level editor")) {
            Main.mFrame.mEditorPanel.mouseDragged(e);
        }
    }

    /**
     * Invoked when a mouse button has been released on a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseReleased(MouseEvent e) {
    	
        if (Settings.State.equals("level editor")) {
            Main.mFrame.mEditorPanel.mouseReleased(e);
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mousePressed(MouseEvent e) {
             
        if (Settings.State.equals("level editor")) {
            Main.mFrame.mEditorPanel.mousePressed(e);
        }       
    }

    /**
     * Invoked when the mouse wheel is rotated<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that the mouse wheel was rotated in a component
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (Settings.State.equals("level editor")) {
            Main.mFrame.mEditorPanel.mouseWheelMoved(e);
        }
    }
    
    /**
     * Gets a enemy sprite
     * @param index The index of the sprite
     * @return The enemy sprite
     */
    public SpriteSheet getEnemySprite(int index)
    {   	
    	return mEnemySprite[index];   	
    }
    
    /**
     * Gets a scenery sprite
     * @param index The index of the sprite
     * @return The scenery sprite
     */
    public SpriteSheet getScenerySprite(int index)
    {
    	return mScenerySprite[index];   	
    }   
    
    /**
     * Gets a power-up sprite
     * @return The power-up sprite
     */
    public SpriteSheet getPowerupSprite()
    {
    	return mPowerupSprite;
    }      
}
