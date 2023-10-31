package com.citex.spacewar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.view.KeyEvent;

/**
	This class draw screen the user interface including the title
	screen and main menu.
	
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

public class UserInterface {

	/** The title screen graphic */
    private GLSprite mTitle;
	/** The energy bar background */
    private GLSprite mEnergyBar;
	/** The energy bar segment */
    private GLSprite mEnergy;
    /** The main game font used in the title and pause screens */
    private GameFont mGameFont;
    /** The level opening background image */
    private GLSprite mLevelOpening;
    /** The player icon displayed on the level opening screen */
    private GLSprite mCharacterIcon;
    /** The number of options in a menu */
    private int mNumberOfOptions;
    /** The currently selected option in a menu */
    private int mSelectedOption;
    /** The pause options background */
    private GLSprite mPauseBackground;
    /** The amount of time that has elapsed since displaying the title or game over screens  */
    private int mUITimer;
    /** The text displayed in the credits screen */
    private ArrayList<String> mCreditsText;
    /** The text displayed in the credits screen */
    private ArrayList<String> mEndCreditsText;
    /** The position of the credits text on the y axis */
    private float mCreditScrollY;
    /** Store the current time values. */
    private int mKeyPressTimer;

    /**
     * Constructs the UserInterface
     * @param gl The GL context
     */
	public UserInterface(GL10 gl, AssetManager assetManager) {

		mGameFont = new GameFont(gl, "fnt/main/", assetManager);
		mTitle = new GLSprite(gl, "gui/title.png", null, assetManager);
		mEnergyBar = new GLSprite(gl, "gui/energy bar.png", null, assetManager);
		mEnergy = new GLSprite(gl, "gui/energy.png", null, assetManager);
		mPauseBackground = new GLSprite(gl, "gui/pause.png", null, assetManager);
        mLevelOpening = new GLSprite(gl, "gui/level.png", null, assetManager);
        mCharacterIcon = new GLSprite(gl, "gui/player.png", null, assetManager);
        
        readSettings();  
        Settings.setDifficulty(Settings.Difficulty);
        BufferedReader br;
        InputStreamReader is;
        mCreditsText = new ArrayList<String>();
        
        mCreditsText.add("Credits");
        mCreditsText.add("");
        mCreditsText.add("Programmer - Lawrence Schmid");     
        
        mEndCreditsText = new ArrayList<String>();
        mEndCreditsText.add("Well done you completed space war");
        mEndCreditsText.add("");
        mEndCreditsText.add("You have unlocked level select");
        mEndCreditsText.add("touch the score to skip levels");  
        mEndCreditsText.add("");
        mEndCreditsText.add("Programmer - Lawrence Schmid");         

        try //read the credits 
        {
            is = new InputStreamReader(assetManager.open("gui/credits.txt"));
            br = new BufferedReader(is);
            String ln = null;
            while ((ln = br.readLine()) != null) {
                mCreditsText.add(ln);
                mEndCreditsText.add(ln);
            }
        } catch (IOException ex) {}
	}
	
    /**
     * Draws the energy bar at the top of the game screen 
     * @param g The GL context
     * @param x The x coordinate
     * @param y The y coordinate
     * @param life The amount of life
     */   
    public void drawEnergybar(GL10 gl, float x, float y, int life) {
    	mEnergyBar.draw(gl, x, y);
    	for(int i = 0; i < life; i++)
    	{
    		mEnergy.draw(gl, x + 15 + (i * 9) + i, y + 6);	 
    		if(i == 5)
    			break;
    	}
    }    
    
    /**
     * Draws the score
     * @param gl The GL context
     * @param x The x coordinate
     * @param y The y coordinate
     * @param score The score count
     */
    public void drawScore(GL10 gl, float x, float y, int score)
    {  	
    	mGameFont.drawString(gl, 0, "Score " + score, x, y);
    }
    
    /**
     * Draws a high score with preceding zeros for the high score table
     * @param gl The GL context
     * @param x The x coordinate
     * @param y The y coordinate
     * @param score The score count
     */
    public void drawHighScore(GL10 gl, float x, float y, int score)
    {
	    int tmp = score;
	    int digit[] = new int[8];
	    for (int i = 7; i >= 0; i--) {
	        digit[i] = tmp % 10;
	        tmp = tmp / 10;
	    }
		String str = "";
		for(int i = 0; i < 8; i++)
			str += digit[i];
	
		mGameFont.drawString(gl, 0, str, x, y);	
	}
    
    /**
     * Draws the level opening screen with world, level number and players remaining lives
     * @param gl The GL context
     * @param level The level number
     * @param lives The remaining lives
     */
    public void drawLevelOpen(GL10 gl, int level, int lives) {

        gl.glClearColor(235, 235, 235, 0);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT |  GL10.GL_DEPTH_BUFFER_BIT);

        mLevelOpening.draw(gl, 75, 100);
        mCharacterIcon.draw(gl, 175, 138);    
        mGameFont.drawString(gl, 0, "x " + lives, 204, 145);
        mGameFont.drawString(gl, 0, "Level - " + level, 163, 111);
        
        //MainActivity.mShowAd = true;
    }   
    
    /**
     * Draws the game over screen
     * @param gl The GL context
     * @param score The score count
     */
    public void drawGameOver(GL10 gl, int score)
    {  
    	mUITimer++;
    	if(score > 0) //new high score
    	{
    		if(mUITimer < 50)
    			mGameFont.drawString(gl, 0, "Game Over", 200 - (mGameFont.getStringWidth("Game Over") / 2), 85);       
	        mGameFont.drawString(gl, 0, "New High Score", 200 - (mGameFont.getStringWidth("New High Score") / 2), 125);   
	        mGameFont.drawString(gl, 0, String.valueOf(score), 200 - (mGameFont.getStringWidth(String.valueOf(score)) / 2), 145);
    	}
    	else
    	{
        	if(mUITimer < 50)
        		mGameFont.drawString(gl, 0, "Game Over", 200 - (mGameFont.getStringWidth("Game Over") / 2), 115);			
    	}    	
    	
    	if(mUITimer > 100)
    		mUITimer = 0;        
	}   

    /**
     * Sets the selected menu item
     * @param option The selected menu item
     */
    public void setSelectedOption(int option)
    {
    	mSelectedOption = option;  	
    }
    
    /**
     * Resets the UI timer which is the amount of time that has elapsed since a screen was displayed
     */
    public void resetUITimer()
    {
    	mUITimer = 0;
    }

    /**
     * Sets the position of the credits text on the y axis
     * @param y The y coordinate
     */  
    public void setCreditScrollY(int y)
    {
    	mCreditScrollY = y;	
    }
 
    /**
     * Reads the settings from a file
     */
	public void readSettings()
	{	
		//http://developer.android.com/guide/topics/data/data-storage.html#filesInternal
		try
		{
			FileInputStream fis = MainActivity.mContext.openFileInput("spacewar.dat");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis)); 
			//music
			if(br.readLine().equals("true"))
				Settings.Music = true;
			else
				Settings.Music = false;
			//sound
			if(br.readLine().equals("true"))
				Settings.Sound = true;
			else
				Settings.Sound = false;
			//difficulty
			Settings.Difficulty = Integer.valueOf(br.readLine());	
			//level select
			if(br.readLine().equals("true"))
				Settings.LevelSelect = true;
			else
				Settings.LevelSelect = false;

			Settings.HighScore[0] = Integer.valueOf(br.readLine());	
			Settings.HighScore[1] = Integer.valueOf(br.readLine());
			Settings.HighScore[2] = Integer.valueOf(br.readLine());	

			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
			Settings.Music = true;
			Settings.Sound = true;
			Settings.Difficulty = 1;
			Settings.Lives = 3;
			Settings.HighScore = new int[3];
			Settings.HighScore[0] = 0;	
			Settings.HighScore[1] = 0;
			Settings.HighScore[2] = 0;		
			Settings.LevelSelect = false;
			writeSettings(); 		
		}			
	}
		
	/**
	 * Writes the settings to a file
	 */
	public void writeSettings()
	{
		try
		{
			FileOutputStream fos = MainActivity.mContext.openFileOutput("spacewar.dat", Context.MODE_PRIVATE);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write(String.valueOf(Settings.Music) + "\n");
			bw.write(String.valueOf(Settings.Sound) + "\n");
			bw.write(Settings.Difficulty + "\n");
			bw.write(Settings.LevelSelect + "\n");
			bw.write(Settings.HighScore[0] + "\n");
			bw.write(Settings.HighScore[1] + "\n");
			bw.write(Settings.HighScore[2] + "\n");
			bw.close();		
		}
		catch(Exception e){}			
	}    
    	
    /**
     * Draws the title, load game, options, erase game, pause, options, save game, credits
     * level opening, level editor settings etc..
     * @param gl The graphics context
     * @param dt The delta time between frame updates
     */   
    public void draw(GL10 gl, float dt) {

        if (Settings.State.equals("title")) //title screen / main menu
        {
        	mTitle.draw(gl, 78, 35);
            mNumberOfOptions = 3;
            for (int i = 0; i < 3; i++) {
                int sel = 0;
                if (i == mSelectedOption) {
                    sel = 1;
                }
                if (i == 0) {
                    mGameFont.drawString(gl, sel, "Start Game", 149, 120);
                } else if (i == 1) {
                    mGameFont.drawString(gl, sel, "High scores", 149, 140);
                } else if (i == 2) {
                    mGameFont.drawString(gl, sel, "Settings", 149, 160);
                } 
            }        	
            mUITimer++;
        } else if (Settings.State.equals("high scores")) //high scores
        {
        	mTitle.draw(gl, 78, 35);
        	mGameFont.drawString(gl, 0, "High Scores", 149, 120);
        	
        	mGameFont.drawString(gl, 0, "1", 144, 150);
        	mGameFont.drawString(gl, 0, "-", 159, 150);
        	drawHighScore(gl, 174, 150, Settings.HighScore[0]);
        	
        	mGameFont.drawString(gl, 0, "2", 144, 170);
        	mGameFont.drawString(gl, 0, "-", 159, 170);
        	drawHighScore(gl, 174, 170, Settings.HighScore[1]);
        	
        	mGameFont.drawString(gl, 0, "3", 144, 190);
        	mGameFont.drawString(gl, 0, "-", 159, 190);
        	drawHighScore(gl, 174, 190, Settings.HighScore[2]);
        	
        } else if (Settings.State.equals("options")) //options
        {
        	mTitle.draw(gl, 78, 35);
            mNumberOfOptions = 4;
            for (int i = 0; i < 4; i++) {
                int sel = 0;
                if (i == mSelectedOption) {
                    sel = 1;
                }
                if (i == 0) {
                    if (Settings.Music) {
                        mGameFont.drawString(gl, sel, "Music - On", 140, 120);
                    } else {
                        mGameFont.drawString(gl, sel, "Music - Off", 140, 120);
                    }

                } else if (i == 1) {
                    if (Settings.Sound) {
                        mGameFont.drawString(gl, sel, "Sound - On", 140, 140);
                    } else {
                        mGameFont.drawString(gl, sel, "Sound - Off", 140, 140);
                    }
                    
                } else if (i == 2) {
                	if(Settings.Difficulty == 0)
                		mGameFont.drawString(gl, sel, "Mode - Easy", 140, 160);
                	else if(Settings.Difficulty == 1)
                		mGameFont.drawString(gl, sel, "Mode - Normal", 140, 160);
                	else if(Settings.Difficulty == 2)
                	mGameFont.drawString(gl, sel, "Mode - Hard", 140, 160);
                }                 
                else if (i == 3) {
                        mGameFont.drawString(gl, sel, "Credits", 140, 180);
                }           
            }
        } else if (Settings.State.equals("pause")) {
			
        	mPauseBackground.draw(gl, 89, 63);
        	mNumberOfOptions = 3;
            for (int i = 0; i < 3; i++) {
                int sel = 0;
                if (i == mSelectedOption) {
                    sel = 1;
                }
                if (i == 0) {
                    mGameFont.drawString(gl, sel, "Continue", 165, 95);
                }  else if (i == 1) {
                    mGameFont.drawString(gl, sel, "Options", 165, 115);
                } else if (i == 2) {
                    mGameFont.drawString(gl, sel, "Quit", 165, 135);
                }
            }  
        } else if (Settings.State.equals("pause options")) {
        	mPauseBackground.draw(gl, 89, 63);
            mNumberOfOptions = 2;
            for (int i = 0; i < 4; i++) {
                int sel = 0;
                if (i == mSelectedOption) {
                    sel = 1;
                }
                if (i == 0) {
                    if (Settings.Music) {
                        mGameFont.drawString(gl, sel, "Music On", 160, 105);
                    } else {
                        mGameFont.drawString(gl, sel, "Music Off", 160, 105);
                    }

                } else if (i == 1) {
                    if (Settings.Sound) {
                        mGameFont.drawString(gl, sel, "Sound On", 160, 125);
                    } else {
                        mGameFont.drawString(gl, sel, "Sound Off", 160, 125);
                    }
                }
            } 
        } else if (Settings.State.equals("credits")) {
       		mCreditScrollY -= 0.04 * dt;
            for (int i = 0; i < mCreditsText.size(); i++) {
                int x = 200 - (mGameFont.getStringWidth(mCreditsText.get(i)) / 2); 
                float y = mCreditScrollY + i * 20;
                if(y > -20 && y < 240)
                	mGameFont.drawString(gl, 0, mCreditsText.get(i), x, y);
                if (i == mCreditsText.size() - 1 && y < 0) 
                {
                    mSelectedOption = 0;
                    Settings.State = "options";
                }
            }      	
        } else if (Settings.State.equals("end credits")) {          	
        	mCreditScrollY -= 0.04 * dt;
            for (int i = 0; i < mEndCreditsText.size(); i++) {
                int x = 200 - (mGameFont.getStringWidth(mEndCreditsText.get(i)) / 2); 
                float y = mCreditScrollY + i * 20;
                if(y > -20 && y < 240)
                	mGameFont.drawString(gl, 0, mEndCreditsText.get(i), x, y);
                if (i == mEndCreditsText.size() - 1 && y < 0) 
                {
                    mSelectedOption = 0;                   
                    Settings.State = "exit";
                }
            }
        }    
    }

	/**
	 * Invoked when a key has been pressed or a touch screen event occurs
	 * @param key The key / touch event code 
	 */
    public void keyPress(String key)
    {
        if(key.equals("UP") ||key.equals("DOWN") || key.equals("LEFT") || key.equals("RIGHT"))
        {
            mKeyPressTimer++;
            if(mKeyPressTimer < 5)
                return;
            else
                mKeyPressTimer = 0;
        }

    	if(key.equals("UP"))
    	{
            if (mSelectedOption > 0)
            	mSelectedOption--;
    	}
    	else if (key.equals("DOWN"))
    	{
	        if (mSelectedOption < mNumberOfOptions - 1)
	        	mSelectedOption++;
    	}
    	else if (key.equals("LEFT") || key.equals("RIGHT"))
    	{
    		if (Settings.State.equals("options") || Settings.State.equals("pause options")) {
                if (mSelectedOption == 0) {
                    if (Settings.Music) {
                        Settings.Music = false;
                        Music.pauseMusic();
                    } else {
                        Settings.Music = true;
                        Music.startMusic();
                    }
                    writeSettings();
                }
                if (mSelectedOption == 1) {
                    Settings.Sound = !Settings.Sound;
                    writeSettings();
                }
            } 	
      		
    		if (Settings.State.equals("options") && mSelectedOption == 2)
    		{
    			if (key.equals("LEFT")) {
   					if(Settings.Difficulty > 0)
                		Settings.Difficulty--;
                	else 
                		Settings.Difficulty = 2; 					
    			}
    			else
    			{
   					if(Settings.Difficulty < 2)
                		Settings.Difficulty++;
                	else 
                		Settings.Difficulty = 0;				
    			}				
    		} 		
    	} 	
    	else if (key.equals("ENTER"))
    	{
    		if (Settings.State.equals("options") || Settings.State.equals("pause options")) {
                 if (mSelectedOption == 0) {
                     if (Settings.Music) {
                         Settings.Music = false;
                         Music.pauseMusic();
                     } else {
                         Settings.Music = true;
                         Music.startMusic();
                     }
                     writeSettings();
                 }
                 if (mSelectedOption == 1) {
                     Settings.Sound = !Settings.Sound;
                     writeSettings();
                 }
             } 		

    		if (Settings.State.equals("title") && mUITimer > Settings.MenuDelay) //title
            {
                if (mSelectedOption == 0) {
                	Settings.State = "load level";
                } else if (mSelectedOption == 1) {
                	Settings.State = "high scores";
                } else if (mSelectedOption == 2) {
                	Settings.State = "options";
                } 
                mSelectedOption = 0;
                SoundEffect.play("select.wav");
            }  
            else if(Settings.State.equals("high scores"))
            {
            	Settings.State = "title";
            	SoundEffect.play("select.wav");
            }
            else if(Settings.State.equals("game over high score") || Settings.State.equals("game over"))
            {
            	mSelectedOption = 0;                   
                Settings.State = "exit";
                SoundEffect.play("select.wav");       	   	
            }
            else if (Settings.State.equals("credits"))
            {
            	Settings.State = "options";
            	SoundEffect.play("select.wav");
            }
	    	else if (Settings.State.equals("end credits")) {
				mSelectedOption = 0;
				Settings.State = "exit";
				SoundEffect.play("select.wav");
			}
            else if(Settings.State.equals("options"))
            {
            	if(mSelectedOption == 2)
            	{
            		if(Settings.Difficulty < 2)
            			Settings.Difficulty++;
            		else 
            			Settings.Difficulty = 0;

            		SoundEffect.play("option.wav");
            		Settings.setDifficulty(Settings.Difficulty);
            		writeSettings();
            	}
            	else if(mSelectedOption == 3)
            	{
                    setCreditScrollY(100);
                    Settings.State = "credits";
                    SoundEffect.play("select.wav");
            	}	
            }
            else if (Settings.State.equals("pause")) {
                if (mSelectedOption == 0) {
                    Settings.State = "game";
                    Settings.Paused = false;
                }  else if (mSelectedOption == 1) {
                    Settings.State = "pause options";
                    mSelectedOption = 0;
                } else if (mSelectedOption == 2) {
                    Settings.State = "exit";
                    Settings.Paused = false;
                    mSelectedOption = 0;
                }
                SoundEffect.play("select.wav");
            } 
    	} 	
    	else if (key.equals("BACK"))
    	{ 	
           	if(Settings.State.equals("game"))
           	{		
           		Settings.Paused = true;
           		Settings.State = "pause";
           		SoundEffect.play("select.wav");
           	}           	
           	else if(Settings.State.equals("pause"))
           	{		
           		Settings.State = "game";
           		Settings.Paused = false;
           		SoundEffect.play("select.wav");
           	}
           	else if(Settings.State.equals("pause options"))
           	{		
           		Settings.State = "pause";
           		SoundEffect.play("select.wav");
           	}
            else if (Settings.State.equals("credits"))
            {  
            	mSelectedOption = 3;                   
                Settings.State = "options";
                SoundEffect.play("select.wav");
            }           	
            else if (Settings.State.equals("end credits"))
            {  
            	mSelectedOption = 0;                   
                Settings.State = "exit";
                SoundEffect.play("select.wav");
            }         	  	
           	else
           	{
           		if(Settings.State.equals("title") && mUITimer > Settings.MenuDelay)
	    		{
		    		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.mContext);
		    		dialog.setIcon(android.R.drawable.ic_dialog_alert);       
		    		dialog.setTitle(R.string.quit_title);
		    		dialog.setMessage(R.string.quit_message);
		    		dialog.setPositiveButton(R.string.quit_yes, new DialogInterface.OnClickListener() {             
		    			public void onClick(DialogInterface dialog, int which) {                 
		    				//Stop the activity                 
		    				MainActivity.mActivity.finish();
		    			}         
		    		});
		    		dialog.setNegativeButton(R.string.quit_no, null); 
		    		dialog.show(); 	    		   		
	    		}
           		else if(!Settings.State.equals("title") && !Settings.State.equals("license") && !Settings.State.equals("title") && !Settings.State.equals("load level") && !Settings.State.equals("pause"))
	    		{
	           		Settings.State = "title";
		            mSelectedOption = 0;
		            SoundEffect.play("select.wav");
	    		}
	    		
           	}
    	}
    }

	/**
	 * Invoked when a key has been pressed
	 * @param e Event which indicates that a keystroke occurred
	 */
    public void onKeyDown(KeyEvent e) {

    	 int kc = e.getKeyCode();

         if (kc == KeyEvent.KEYCODE_DPAD_UP) {
        	 keyPress("UP");
         }
         if (kc == KeyEvent.KEYCODE_DPAD_DOWN) {
        	 keyPress("DOWN");
         }  
         if (kc == KeyEvent.KEYCODE_DPAD_LEFT) {
        	 keyPress("LEFT");
         }
         if (kc == KeyEvent.KEYCODE_DPAD_RIGHT) {
        	 keyPress("RIGHT");
         }           
         if (kc == KeyEvent.KEYCODE_A || kc == KeyEvent.KEYCODE_ENTER) {
        	 keyPress("ENTER");
         }    
         if (kc == KeyEvent.KEYCODE_DEL) {       
            keyPress("BACK");     
         }
    }

    /**
     * Destroys the textures and releases the hardware buffers
     * @param gl The GL context
     */
    public void destroy(GL10 gl)
    {
    	if(mTitle != null)
    		mTitle.destroy(gl);

    	if(mPauseBackground != null)
    		mPauseBackground.destroy(gl);

    	if(mGameFont != null)
    		mGameFont.destroy(gl);
    }
}
