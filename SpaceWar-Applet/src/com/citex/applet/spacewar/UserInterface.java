package com.citex.applet.spacewar;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;

/**
    This class draw screen overlays such as the title screen, level 
    opening and game status.
	
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

public class UserInterface {

	/** The title screen graphic */
    private BufferedImage mTitle;
	/** The energy bar background */
    private BufferedImage mEnergyBar;
	/** The energy bar segment */
    private BufferedImage mEnergy;
    /** The main game font used in the title and pause screens */
    private GameFont mGameFont;
    /** The level opening background image */
    private BufferedImage mLevelOpening;
    /** The player icon displayed on the level opening screen */
    private BufferedImage mCharacterIcon;
    /** The number of options in a menu */
    private int mNumberOfOptions;
    /** The currently selected option in a menu */
    private int mSelectedOption;
    /** The pause options background */
    private BufferedImage mPauseBackground;
    /** The level list */
    private ArrayList<String> mLevelList;    
    /** The amount of time that has elapsed since displaying the game over screen  */
    public static int mUITimer;
    /** The text displayed in the credits screen */
    private ArrayList<String> mCreditsText;
    /** The text displayed in the credits screen */
    private ArrayList<String> mEndCreditsText;
    /** The amount the credits have scrolled in the y direction */
    public static float mCreditScrollY;	
    /** The time elapsed while the game is saving */
    public static int mSavingTimer;
	    
    /**
     * Constructs the UserInterface
     */   
    public UserInterface() {
    	
    	//load graphics
        mGameFont = new GameFont("res/fnt/main/");
       	mTitle = new Image("res/gui/title.png").getImage();
        mEnergyBar = new Image("res/gui/energy bar.png").getImage();
        mEnergy =  new Image("res/gui/energy.png").getImage();
        mPauseBackground = new Image("res/gui/pause.bmp").getImage();
        mLevelOpening = new Image("res/gui/level.bmp").getImage();
        mCharacterIcon = new Image("res/gui/player.bmp").getImage();  
        
        readSettings();  
        Settings.setDifficulty(Settings.Difficulty);
        mCreditsText = new ArrayList<String>();
        
        /*
        mCreditsText.add("-------------------------------------");
        mCreditsText.add(""); 
        mCreditsText.add("Space War is free software: you can");
        mCreditsText.add("redistribute it and/or modify it under");
        mCreditsText.add("the terms of the GNU General Public"); 
        mCreditsText.add("License as published by the Free"); 
        mCreditsText.add("Software Foundation"); 
        mCreditsText.add(""); 
        mCreditsText.add("Copyright 2012 Lawrence Schmid"); 
        mCreditsText.add(""); 
        mCreditsText.add("-------------------------------------"); 
        mCreditsText.add(""); 
        */  
       
        mCreditsText.add("Credits");
        mCreditsText.add("");
        mCreditsText.add("Programmer - Lawrence Schmid");     
        
        mEndCreditsText = new ArrayList<String>();
        mEndCreditsText.add("Well done you completed space war");
        mEndCreditsText.add("");
        mEndCreditsText.add("You have unlocked level select");
        mEndCreditsText.add("press the End key to skip levels");  
        mEndCreditsText.add("");
        mEndCreditsText.add("Programmer - Lawrence Schmid");  
        
        try //read the credits 
        {
        	InputStream is = Main.class.getResourceAsStream("res/gui/credits.txt"); 
            BufferedReader input = new BufferedReader(new InputStreamReader(is));   
            String ln = null;
            while ((ln = input.readLine()) != null) {
                mCreditsText.add(ln);
                mEndCreditsText.add(ln);
            }
        } catch (IOException ex) {}
     }
    
    /**
     * Draws the energy bar at the top of the game screen 
     * @param g The graphics context
     * @param x The x coordinate
     * @param y The y coordinate
     * @param life The amount of life
     */   
    public void drawEnergyBar(Graphics g, int x, int y, int life) {
    	g.drawImage(mEnergyBar, x, y, null);
    	for(int i = 0; i < life; i++)
    	{
    		g.drawImage(mEnergy, x + 15 + (i * 9) + i, y + 6, null);	   		
    	}
    } 
    
    /**
     * Draws the score
     * @param g The graphics context
     * @param x The x coordinate
     * @param y The y coordinate
     * @param score The current score
     */
    public void drawScore(Graphics g, int x, int y, int score)
    {
    	mGameFont.drawString(g,0, "Score " + score, x, y);
    }
    
    /**
     * Draws a high score with preceding zeros for the high score table
     * @param g The graphics context
     * @param x The x coordinate
     * @param y The y coordinate
     * @param score The score count
     */
    public void drawHighScore(Graphics g, float x, float y, int score)
    {
		//convert number into digits - http://stackoverflow.com/questions/5196186/split-int-value-into-seperate-digits
	    int tmp = score;
	    int digit[] = new int[8];
	    for (int i = 7; i >= 0; i--) {
	        digit[i] = tmp % 10;
	        tmp = tmp / 10;
	    }
		String str = "";
		for(int i = 0; i < 8; i++)
			str += digit[i];
	
		mGameFont.drawString(g, 0, str, (int)x, (int)y);	
	}     

    /**
     * Draws the level opening screen with world, level number and players remaining lives
     * @param g The graphics context
     * @param world The world number
     * @param level The level number
     * @param playerLives The remaining lives
     */
    public void drawLevelOpen(Graphics g, int level, int lives) {

    	g.setColor(new Color(255,255,255));
        g.fillRect(0, 0, 400, 240);

        g.drawImage(mLevelOpening, 75, 100, null);       
        g.drawImage(mCharacterIcon, 175, 138, null);     
             
        mGameFont.drawString(g, 0,  "x " + lives, 204, 145);
        mGameFont.drawString(g, 0,  "Level - " + level, 163, 111);
    }      
    
    /**
     * Draws the game over screen
     * @param g The graphics context
     * @param score The score count
     */
    public void drawGameOver(Graphics g, int score)
    {  
    	mUITimer++;
    	if(score > 0) //new high score
    	{
    		if(mUITimer < 50)
    			mGameFont.drawString(g, 0, "Game Over", 200 - (mGameFont.getStringWidth("Game Over") / 2), 85);       
	        mGameFont.drawString(g, 0, "New High Score", 200 - (mGameFont.getStringWidth("New High Score") / 2), 125);   
	        mGameFont.drawString(g, 0, String.valueOf(score), 200 - (mGameFont.getStringWidth(String.valueOf(score)) / 2), 145);
    	}
    	else
    	{
        	if(mUITimer < 50)
        		mGameFont.drawString(g, 0, "Game Over", 200 - (mGameFont.getStringWidth("Game Over") / 2), 115);			
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
     * Reads the settings from a file
     */
	public void readSettings()
	{			
		//http://developer.android.com/guide/topics/data/data-storage.html#filesInternal
		/*
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("res/spacewar.dat"));
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
			//high scores
			Settings.HighScore = new int[3];
			Settings.HighScore[0] = Integer.valueOf(br.readLine());	
			Settings.HighScore[1] = Integer.valueOf(br.readLine());
			Settings.HighScore[2] = Integer.valueOf(br.readLine());	
			
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		*/	
			//high scores
			Settings.HighScore = new int[3];
			Settings.HighScore[0] = 0;	
			Settings.HighScore[1] = 0;
			Settings.HighScore[2] = 0;		
			writeSettings(); 
			
		//}
	
	}
		
	/**
	 * Writes the settings to a file
	 */
	public void writeSettings()
	{
		/*
		try
		{		
			BufferedWriter bw = new BufferedWriter(new FileWriter("res/spacewar.dat"));
			bw.write(String.valueOf(Settings.Music) + "\n");
			bw.write(String.valueOf(Settings.Sound) + "\n");
			bw.write(Settings.Difficulty + "\n");
			bw.write(Settings.HighScore[0] + "\n");
			bw.write(Settings.HighScore[1] + "\n");
			bw.write(Settings.HighScore[2] + "\n");
			bw.close();		
		}
		catch(Exception e){}	
		*/	
	}      

	/**
     * Draws the title, load game, options, erase game, pause, options, save game, credits
     * level opening, level editor settings etc..
     * @param g The graphics context
     * @param dt The delta time between frame updates
     */   
    public void draw(Graphics g, float dt) {

        if (Settings.State.equals("title")) //title screen / main menu
        {
        	g.drawImage(mTitle, 87, 35, null);
            mNumberOfOptions = 3;
            for (int i = 0; i < 4; i++) {
                int sel = 0;
                if (i == mSelectedOption) {
                    sel = 1;
                }
                if (i == 0) {
                    mGameFont.drawString(g, sel, "Start Game", 150, 120);
                } else if (i == 1) {
                    mGameFont.drawString(g, sel, "High scores", 150, 140);
                } else if (i == 2) {
                    mGameFont.drawString(g, sel, "Settings", 150, 160);
                }
            }
        } else if (Settings.State.equals("high scores")) //high scores
        {
        	g.drawImage(mTitle, 87, 35, null);
        	mGameFont.drawString(g, 0, "High Scores", 149, 120);
        	
        	mGameFont.drawString(g, 0, "1", 144, 150);
        	mGameFont.drawString(g, 0, "-", 159, 150);
        	drawHighScore(g, 174, 150, Settings.HighScore[0]);
        	
        	mGameFont.drawString(g, 0, "2", 144, 170);
        	mGameFont.drawString(g, 0, "-", 159, 170);
        	drawHighScore(g, 174, 170, Settings.HighScore[1]);
        	
        	mGameFont.drawString(g, 0, "2", 144, 190);
        	mGameFont.drawString(g, 0, "-", 159, 190);
        	drawHighScore(g, 174, 190, Settings.HighScore[2]);
        	
        } else if (Settings.State.equals("options")) //options
        {
        	g.drawImage(mTitle, 87, 35, null);
            mNumberOfOptions = 3;
            for (int i = 0; i < 4; i++) {
                int sel = 0;
                if (i == mSelectedOption) {
                    sel = 1;
                }
                if (i == 0) {
                    if (Settings.Music) {
                        mGameFont.drawString(g, sel, "Music - On", 155, 120);
                    } else {
                        mGameFont.drawString(g, sel, "Music - Off", 155, 120);
                    }

                } else if (i == 1) {
                    if (Settings.Sound) {
                        mGameFont.drawString(g, sel, "Sound - On", 155, 140);
                    } else {
                        mGameFont.drawString(g, sel, "Sound - Off", 155, 140);
                    }
                    
                }            
                else if (i == 2) {
                        mGameFont.drawString(g, sel, "Credits", 155, 160);
                }           
            }
        }else if (Settings.State.equals("pause")) {
            g.drawImage(mPauseBackground, 200 - (mPauseBackground.getWidth() / 2), 120 - (mPauseBackground.getHeight() / 2), null);      
            if(!Settings.LevelEditor)
            {          
	            mNumberOfOptions = 3;
	            for (int i = 0; i < 3; i++) {
	                int sel = 0;
	                if (i == mSelectedOption) {
	                    sel = 1;
	                }
	                if (i == 0) {
	                    mGameFont.drawString(g, sel, "Continue", 165, 95);
	                }  else if (i == 1) {
	                    mGameFont.drawString(g, sel, "Options", 165, 115);
	                } else if (i == 2) {
	                    mGameFont.drawString(g, sel, "Quit", 165, 135);
	                }
	            }
            }
            else
            {
            	mNumberOfOptions = 4;
	            for (int i = 0; i < 4; i++) {
	                int sel = 0;
	                if (i == mSelectedOption) {
	                    sel = 1;
	                }
	                if (i == 0) {
	                    mGameFont.drawString(g, sel, "Continue", 165, 85);
	                }  else if (i == 1) {
	                    mGameFont.drawString(g, sel, "Save", 165, 105);
	                } else if (i == 2) {
	                    mGameFont.drawString(g, sel, "Options", 165, 125);
	                }else if (i == 3) {
	                    mGameFont.drawString(g, sel, "Quit", 165, 145);
	                }
	            }           	     	
            }
        } else if (Settings.State.equals("pause options")) {
            g.drawImage(mPauseBackground, 200 - (mPauseBackground.getWidth() / 2), 120 - (mPauseBackground.getHeight() / 2), null);
            mNumberOfOptions = 2;
            for (int i = 0; i < 4; i++) {
                int sel = 0;
                if (i == mSelectedOption) {
                    sel = 1;
                }

                if (i == 0) {
                    if (Settings.Music) {
                        mGameFont.drawString(g, sel, "Music On", 160, 105);
                    } else {
                        mGameFont.drawString(g, sel, "Music Off", 160, 105);
                    }

                } else if (i == 1) {
                    if (Settings.Sound) {
                        mGameFont.drawString(g, sel, "Sound On", 160, 125);
                    } else {
                        mGameFont.drawString(g, sel, "Sound Off", 160, 125);
                    }
                }
            }
        } else if (Settings.State.equals("saving")) {
            g.drawImage(mPauseBackground, 200 - (mPauseBackground.getWidth() / 2), 120 - (mPauseBackground.getHeight() / 2), null);
            mGameFont.drawString(g, 1, "Saving", 170, 115);
            mSavingTimer++;
            if (mSavingTimer > 100) {
                Settings.State = "pause";
                mSavingTimer = 0;
            }
        }  else if (Settings.State.equals("credits"))
        {
       		mCreditScrollY -= 0.04 * dt;

            for (int i = 0; i < mCreditsText.size(); i++) {
                int x = 200 - (mGameFont.getStringWidth(mCreditsText.get(i)) / 2); //centre text
                int y = (int)mCreditScrollY + i * 20;
                if(y > -20 && y < 240)
                	mGameFont.drawString(g, 0, mCreditsText.get(i), x, y);
                if (i == mCreditsText.size() - 1 && y < 0) 
                {
                    mSelectedOption = 0;
                    Settings.State = "options";
                }
            }      	
        }  
        else if (Settings.State.equals("end credits"))
        {          	
        	mCreditScrollY -= 0.04 * dt;
            for (int i = 0; i < mEndCreditsText.size(); i++) {
                int x = 200 - (mGameFont.getStringWidth(mEndCreditsText.get(i)) / 2); 
                int y = (int)mCreditScrollY + i * 20;
                if(y > -20 && y < 240)
                	mGameFont.drawString(g, 0, mEndCreditsText.get(i), x, y);
                if (i == mEndCreditsText.size() - 1 && y < 0) 
                {
                    mSelectedOption = 0;                   
                    Settings.State = "exit";
                }
            }
        }    
    }

    /**
	 * Invoked when a key has been pressed
	 * @param e Event which indicates that a keystroke occurred
	 */
    public void keyPressed(KeyEvent e) {

        int kc = e.getKeyCode();

        if (kc == KeyEvent.VK_UP) {
        	if(!Settings.State.equals("edit level") && !Settings.State.equals("load level") && !Settings.State.equals("license"))
        	{
	            if (mSelectedOption > 0) {
	                mSelectedOption--;
	                new SoundEffect("res/sfx/select.wav").start();
	            }
        	}
        }
        if (kc == KeyEvent.VK_DOWN) {
        	if(!Settings.State.equals("edit level") && !Settings.State.equals("load level") && !Settings.State.equals("license"))
        	{
	            if (mSelectedOption < mNumberOfOptions - 1) {
	                mSelectedOption++;
	                new SoundEffect("res/sfx/select.wav").start();
	            }
        	}
        }  

        if (kc == KeyEvent.VK_LEFT || kc == KeyEvent.VK_RIGHT) {
            if (Settings.State.equals("options") || Settings.State.equals("pause options")) {
                if (mSelectedOption == 0) //music            
                {
                    if (Settings.Music) {
                        Settings.Music = false;
                        Music.stopMusic();
                    } else {
                        Settings.Music = true;
                        Music.startMusic();
                    }
                }
                if (mSelectedOption == 1)  //sound effects
                {
                    Settings.Sound = !Settings.Sound;
                }
                if(Settings.State.equals("options") && mSelectedOption == 2) //difficulty
                {
                	if (kc == KeyEvent.VK_LEFT)
                	{
	                	if(Settings.Difficulty> 0)
	                		Settings.Difficulty--;
                	}
                	else 
                	{
                		if(Settings.Difficulty < 2)
                			Settings.Difficulty++;
                	}  		
                }
                
                new SoundEffect("res/sfx/select.wav").start();
                Settings.setDifficulty(Settings.Difficulty);
        		writeSettings();	
            }
            else if(Settings.State.equals("edit level"))
            {
            	if (kc == KeyEvent.VK_LEFT)
            	{
	            	if(mSelectedOption > 0)
	            		mSelectedOption--;
            	}
            	
            	if (kc == KeyEvent.VK_RIGHT)
            	{
	            	if(mSelectedOption < mLevelList.size()-1)
	            		mSelectedOption++;
            	}         	

            } 
        }
        
        if (kc == KeyEvent.VK_ENTER && !Settings.State.equals("load level") && !Settings.State.equals("license")) {
           setOption(); 
        }
        
        if (kc == KeyEvent.VK_ESCAPE) {
        	 if (!Settings.State.equals("title") && !Settings.State.equals("game") && !Settings.State.equals("pause") && !Settings.State.equals("pause options") && !Settings.State.equals("credits") && !Settings.State.equals("end credits") && !Settings.State.equals("game over") && !Settings.State.equals("game over high score")) {
            		Settings.State = "title";
            		mSelectedOption = 0;
             } else if (Settings.State.equals("pause options") || Settings.State.equals("save")) {
                 Settings.State = "pause";
             	mSelectedOption = 0;
             }
             else if(Settings.State.equals("credits"))
             {
             	Settings.State = "options";
	            	mSelectedOption = 3;      
	                new SoundEffect("res/sfx/select.wav").start();  	
             }
             else if (Settings.State.equals("end credits"))
             {  
             	mSelectedOption = 0;                   
                 Settings.State = "exit";
             } 
             else if(Settings.State.equals("game over") || Settings.State.equals("game over high score"))
             {	
					Settings.State = "exit";
					new SoundEffect("res/sfx/select.wav").start();			               	
             }
        }

    }

    /**
     * Select the menu option
     */
	public void setOption() {
		
		if (Settings.State.equals("title")) // title
		{
			if (mSelectedOption == 0) {
				Settings.State = "load level";
				mSelectedOption = 0;
			} else if (mSelectedOption == 1) {
				Settings.State = "high scores";
				mSelectedOption = 0;
			} else if (mSelectedOption == 2) {
				Settings.State = "options";
				mSelectedOption = 0;
			}
			new SoundEffect("res/sfx/select.wav").start();
		} else if (Settings.State.equals("high scores")) {
			Settings.State = "title";
			new SoundEffect("res/sfx/select.wav").start();
		} 
		else if(Settings.State.equals("game over") || Settings.State.equals("game over high score"))
        {	
			Settings.State = "exit";
			new SoundEffect("res/sfx/select.wav").start();			               	
        }			
		else if (Settings.State.equals("options")) {
			if (mSelectedOption == 0) {
				if (Settings.Music) {
					Settings.Music = false;
					Music.stopMusic();
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
			if (mSelectedOption == 2) {
				mCreditScrollY = 100;
				Settings.State = "credits";
				new SoundEffect("res/sfx/select.wav").start();
			}
			new SoundEffect("res/sfx/select.wav").start();
		} else if (Settings.State.equals("pause")) {

			if (mSelectedOption == 0) {
				Settings.State = "game";
				Settings.Paused = false;
			} else if (mSelectedOption == 1) {
				Settings.State = "pause options";
				mSelectedOption = 0;
			} else if (mSelectedOption == 2) {
				Settings.State = "exit";
				Settings.Paused = false;
				mSelectedOption = 0;
			}
			new SoundEffect("res/sfx/select.wav").start();
		} else if (Settings.State.equals("pause options")) {
			if (mSelectedOption == 0) {
				if (Settings.Music) {
					Settings.Music = false;
					Music.stopMusic();
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
			new SoundEffect("res/sfx/select.wav").start();
		} else if (Settings.State.equals("credits")) {
			Settings.State = "options";
			mSelectedOption = 2;
			new SoundEffect("res/sfx/select.wav").start();
		}
	}
    
    /**
     * Returns a list of the files at the specified path
     * @param path The file path
     * @return The list of files at the specified path
     */
	public static ArrayList<String> getFileList(String path) {
	    ArrayList<String> list = new ArrayList<String>();
	    File folder = new File(path);
	    File[] files = folder.listFiles();
	    if (files != null) {
	        for (int i = 0; i < files.length; i++) {
	            if (!files[i].isDirectory()) {
	                String str = files[i].getName();
                    list.add(str);
	            }
	        }
	        Collections.sort(list);
	        return list;
	    } else {
	        return null;
	    }
	}  
	
	/** 
	 * Returns the number of files at the specified path
	 * @param path The file path
	 * @return The number of files at the specified path
	 */
	public static int getFileCount(String path) {
	    File folder = new File(path);
	    File[] files = folder.listFiles();
	    return files.length;
	}	
}