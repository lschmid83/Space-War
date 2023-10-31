package com.citex.java.spacewar;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Rectangle;

/**
	This class is used to create new and edit existing levels by adding, 
	removing enemy and background tiles.
	
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

public class EditorPanel extends JPanel {

	/** The level background */
    private Background mBackground;
	/** The camera position */
    private Camera mCamera;
    /** The mp3 music */
    private Music mMusic;
    /** The enemy sprite list */
    private Enemy mEnemy[];
    /** The scenery sprite list */
    private Scenery mScenery[];    
    /** The level information */
    private Level mLevelInfo;
    /** The level header information */
    private Header mLevelHeader;
    /** The mouse x coordinate */
    private int mMouseX;
    /** The mouse y coordinate */
    private int mMouseY;   
    /** The mouse cursor rectangle */
    private Rectangle mMouseCursorRect;
    /** The selected enemy sprite */
    private int mSelectedTile;
    /** The level editor state */
    private int mEditorState;
    /** The number of available enemy sprites */
    private int mNumEnemies;
    /** The number of available object sprites */
    private int mNumObjects;
    /** Is the enemy settings screen displayed */
    private boolean mEditEnemy;
    /** The currently selected enemy */
    private int mCurrentEnemy;
    /** The currently selected enemy setting option */
    private int mSelectedOption;
    /** The main game font used in the title and pause screens */
    private GameFont mGameFont;
    /** Distinguishes the class when it is serialized and deserialized */
    public final static long serialVersionUID = 1000000;

    /**
     * Constructs the EditorPanel
     */
    public EditorPanel() {

    	mCamera = new Camera(0,0);
        mMouseCursorRect = new Rectangle(-10, -10, 5, 5);       
        mNumEnemies = 11;
        mEnemy = new Enemy[mNumEnemies];
        for(int i = 0; i<mNumEnemies; i++)
        {
        	mEnemy[i] = new Enemy(i,0,0,0,0,0);  	
        	mEnemy[i].mWidth = Main.mFrame.mGamePanel.getEnemySprite(i).getFrame(0).getWidth();
        	mEnemy[i].mHeight = Main.mFrame.mGamePanel.getEnemySprite(i).getFrame(0).getHeight(); 	
        }
        mNumObjects = 3;
        mScenery = new Scenery[mNumObjects];
        for(int i = 0; i<mNumObjects; i++)
        {
        	mScenery[i] = new Scenery(i,0,0);  	
        	mScenery[i].mWidth = Main.mFrame.mGamePanel.getScenerySprite(i).getFrame(0).getWidth();
        	mScenery[i].mHeight = Main.mFrame.mGamePanel.getScenerySprite(i).getFrame(0).getHeight(); 	        	   	
        }        
        
        mSelectedTile = 0;
        mEditorState = 0;    
        mEditEnemy = false;
        mSelectedOption = 0;
        mCurrentEnemy = 0;
        mGameFont = new GameFont("res/fnt/main/");
    }
 
    /**
     * Loads a level file for editing
     * @param path The level file path e.g "res/lvl/Main Game/0.0.0.lvl" (title screen)
     */
    public void loadLevel(String path) {
        mLevelInfo = new Level(path);      
        mLevelHeader = mLevelInfo.getHeader();
        setBackground(mLevelHeader.background);  
        setMusic(mLevelHeader.music);
        repaint();

    }
   
    /**
     * Saves a level which has been created using the level editor
     * @param path The file output path e.g "res/lvl/Main Game/0.0.0.lvl" (title screen)
     */
    public void saveLevel(String path) {
        mLevelInfo.saveLevel(path + mLevelHeader.file + ".lvl");
    }   
    
    /**
     * Sets the background and update the level header
     * @param new_bgImg[] The background image resource (res/bgr) 0-near, 1-middle, 2-far
     */
    public void setBackground(int fileNumber) {
    	int bgFile[] = {1, 1, fileNumber};
        float bgSpeed[] = {2,1,0};
        mBackground = new Background(bgFile, mLevelHeader.width, 240, bgSpeed);
    }  
    
    /**
     * Change the background in the level editor and update the level header
     * @param new_bgImg[] The background image resource (res/bgr) 0-near, 1-middle, 2-far
     */
    public void changeBackground(int fileNumber) {
    	
        if(mBackground.getFileNumber()[2] != fileNumber)
        {
	    	int bgFile[] = {1, 1,fileNumber};
	        float bgSpeed[] = {2,1,0};
	        mBackground = new Background(bgFile, mLevelHeader.width, 240, bgSpeed);
        }
    }  
    
    /**
     * Sets the width of the background
     * @param width The width of the background
     */
    public void setBackgroundWidth(int width) {
        mBackground.setWidth(width);
    }
   
    /**
     * Sets the level header information 
     * @param header The level header 
     */
    public void setHeader(Header header) {
        mLevelInfo.setHeader(header);
        mLevelHeader = mLevelInfo.getHeader();
        mBackground.setWidth(mLevelHeader.width);
    }    
    
    /**
     * Returns the level header information
     * @return The level header
     */
    public Header getHeader() {
        return mLevelInfo.getHeader();
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
     * Invoked when a key has been typed
     * @param e Event which indicates that a keystroke occurred in a component
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key has been released
     * @param e Event which indicates that a keystroke occurred in a component
    */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Invoked when a key has been pressed<BR>
     * Moves the camera around the map area, scroll through object/sprite selection
     * @param e Event which indicates that a keystroke occurred in a component
    */
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
            
        if(mEditEnemy)
        {
	        if (kc == KeyEvent.VK_UP) {
	        	if(!Settings.State.equals("edit level"))
	        	{
		            if (mSelectedOption > 0) {
		                mSelectedOption--;
		                new SoundEffect("res/sfx/select.wav").start();
		            }
	        	}
	        }
	        if (kc == KeyEvent.VK_DOWN) {
	        	if(!Settings.State.equals("edit level"))
	        	{
		            if (mSelectedOption < 3) {
		                mSelectedOption++;
		                new SoundEffect("res/sfx/select.wav").start();
		            }
	        	}
	        }  
        }
        
        if(mEditEnemy)
        {
        	if(mSelectedOption == 0) //health
        	{
        		if (kc == KeyEvent.VK_LEFT && mLevelInfo.getTile(mCurrentEnemy).health > 0) {
       				mLevelInfo.getTile(mCurrentEnemy).health--;
    	        } else if (kc == KeyEvent.VK_RIGHT) {
    	        	mLevelInfo.getTile(mCurrentEnemy).health++;
    	        }	
        	}
        	else if(mSelectedOption == 1) //weapon
        	{
        		if (kc == KeyEvent.VK_LEFT && mLevelInfo.getTile(mCurrentEnemy).weapon > 0) {
       				mLevelInfo.getTile(mCurrentEnemy).weapon--;
    	        } else if (kc == KeyEvent.VK_RIGHT && mLevelInfo.getTile(mCurrentEnemy).weapon < 2) {
    	        	mLevelInfo.getTile(mCurrentEnemy).weapon++;
    	        }   		
        	}
        	else if(mSelectedOption == 2) //power-up
        	{
        		if (kc == KeyEvent.VK_LEFT && mLevelInfo.getTile(mCurrentEnemy).powerup > 0) {
        			mLevelInfo.getTile(mCurrentEnemy).powerup--;
    	        } else if (kc == KeyEvent.VK_RIGHT && mLevelInfo.getTile(mCurrentEnemy).powerup < 3) {
    	        	mLevelInfo.getTile(mCurrentEnemy).powerup++;
    	        }   		
        	}
        	if (kc == KeyEvent.VK_ENTER) {
        		if(mEditEnemy && mSelectedOption == 3)
        		{
        			mEditEnemy = false;	
	                new SoundEffect("res/sfx/select.wav").start();
        		}
        	}  	
        }
        else
        {
	        if (kc == KeyEvent.VK_LEFT) {
	            mCamera.x -= 16;
	        } else if (kc == KeyEvent.VK_RIGHT) {
	            mCamera.x += 16;
	        } else if (kc == KeyEvent.VK_UP) {
	            mCamera.y -= 16;
	        } else if (kc == KeyEvent.VK_DOWN) {
	            mCamera.y += 16;
	        } 
        }
    }

    /**
     * Invoked when the mouse enters a component
     * @param e Event which indicates that a mouse action occurred in a component
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component
     * @param e Event which indicates that a mouse action occurred in a component
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component<BR>
     * Sets the mouse and cursor rectangles x,y coordinates
     * @param e Event which indicates that a mouse action occurred in a component
     */
    public void mouseMoved(MouseEvent e) {
        mMouseX = e.getX() + (int)mCamera.x;
        mMouseY = e.getY() + (int)mCamera.y;  
        mMouseCursorRect.setLocation(e.getX(), e.getY());    
    }

    /**
     * Invoked when the mouse exits a component<BR>
     * Sets the mouse and cursor rectangles x,y coordinates outside of the screen area
     * @param e Event which indicates that a mouse action occurred in a component
     */
    public void mouseExited(MouseEvent e) {
       
    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged<BR>
     * Sets the size of the selection rectangle
     * @param e Event which indicates that a mouse action occurred in a component
     */
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Invoked when a mouse button has been released on a component<BR>
     * Adds an object to the map and sets the size of selection rectangle
     * @param e Event which indicates that a mouse action occurred in a component
     */
    public void mouseReleased(MouseEvent e) {	
    }

    /**
     * Invoked when a mouse button has been pressed on a component<BR>
     * Button1 - Adds / Selects an object or sprite and set position<BR>
     * Button2 - Changes the editor state 0-Select 1-Object 2-Sprite<BR>
     * Button3 - Removes an object or sprite</p>
     * @param e Event which indicates that a mouse action occurred in a component
     */
    public void mousePressed(MouseEvent e) {
    	
   		if(mEditorState > 0 && e.getButton() == 1)
   		{	
   			if(mEditorState == 1)
   				mLevelInfo.addTile(mSelectedTile, mEditorState, mMouseCursorRect.x - ((int)mEnemy[mSelectedTile].mWidth/2) + (int)mCamera.x, mMouseCursorRect.y - ((int)mEnemy[mSelectedTile].mHeight/2) + (int)mCamera.y);
   			else if(mEditorState == 2)
   				mLevelInfo.addTile(mSelectedTile, mEditorState, mMouseCursorRect.x - ((int)mScenery[mSelectedTile].mWidth/2) + (int)mCamera.x, mMouseCursorRect.y - ((int)mScenery[mSelectedTile].mHeight/2) + (int)mCamera.y);
   		}
   		else if(mEditorState == 0 && e.getButton() == 1)
   		{
    		Rectangle mouse = new Rectangle(mMouseX, mMouseY, 5, 5);
    		for(int i = 0; i < mLevelInfo.getTileCount(); i++)
    		{
    			Tile t = mLevelInfo.getTile(i);    		
    			Rectangle tile = new Rectangle(0,0,0,0);
    			if(t.tileset == 1)
    			{
    				tile = new Rectangle(t.x, t.y, (int)mEnemy[t.tile].mWidth, (int)mEnemy[t.tile].mHeight);
    			}
    				    			
    			if(mouse.intersects(tile))
    			{
    				mEditEnemy = true;
    				mSelectedOption = 0;
    				mCurrentEnemy = i;		
    			}
    		}   				
   		}
	
    	if (e.getButton() == 2) //change editor state
        {
    		if(mEditorState < 2)
    		{
    			mEditorState++;
    			if(mEditorState == 2)
    				mSelectedTile = 1;
    			else
    				mSelectedTile = 0;
    		}
            else
            	mEditorState = 0;
        }  
    	else if(e.getButton() == 3) //remove tile
    	{
    		Rectangle mouse = new Rectangle(mMouseX, mMouseY, 5, 5);
    		for(int i = 0; i < mLevelInfo.getTileCount(); i++)
    		{
    			Tile t = mLevelInfo.getTile(i);    		
    			Rectangle tile = new Rectangle(0,0,0,0);
    			if(t.tileset == 1)
    			{
    				tile = new Rectangle(t.x, t.y, (int)mEnemy[t.tile].mWidth, (int)mEnemy[t.tile].mHeight);
    			}
    			else if(t.tileset == 2)
    			{
    				tile = new Rectangle(t.x, t.y, (int)mScenery[t.tile].mWidth, (int)mScenery[t.tile].mHeight);
    			}
    				    			
    			if(mouse.intersects(tile))
    			{
    				mLevelInfo.removeTile(i);   			
    			}
    		}   		
    	}
    }

    /**
     * Invoked when the mouse wheel is rotated<BR>
     * Scrolls through object or sprite selection
     * @param e Event which indicates that the mouse wheel was rotated in a component
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        if (wheelRotation < 0) //scroll up
        {
        	if(mEditorState == 1)
        	{
	        	if(mSelectedTile < mNumEnemies-1)
	        		mSelectedTile++;
        	}
        	else if (mEditorState == 2)
        	{
	        	if(mSelectedTile < mNumObjects-1)
	        		mSelectedTile++;      		
            }
        } else //scroll down
        {
        	if(mSelectedTile > 0)
        		mSelectedTile--;
        }
    }

    /**
     * Draws the level editor graphics object/entity collection and background
     * @param g The graphics context
     */
    protected void paintComponent(Graphics2D g) {

        mCamera = mBackground.move(mCamera, 1); 
        mBackground.draw(g, mCamera);
		
        if(mEditorState == 1)
        {
        	g.drawImage(Main.mFrame.mGamePanel.getEnemySprite(mSelectedTile).getFrame(0), mMouseCursorRect.x - ((int)mEnemy[mSelectedTile].mWidth/2), mMouseCursorRect.y - ((int)mEnemy[mSelectedTile].mHeight/2), null);
	
        } 
        else if(mEditorState == 2) 
        {
        	g.drawImage(Main.mFrame.mGamePanel.getScenerySprite(mSelectedTile).getFrame(0), mMouseCursorRect.x - ((int)mScenery[mSelectedTile].mWidth/2), mMouseCursorRect.y  - ((int)mScenery[mSelectedTile].mHeight/2), null);
        } 
        
        for(int i = 0; i<mLevelInfo.getTiles().size(); i++) //draw background objects
        {
        	Tile t = mLevelInfo.getTile(i);
        	if (t.tileset == 2)
         	{
         		if(t.tile < mNumObjects)
         		{
         			g.drawImage(Main.mFrame.mGamePanel.getScenerySprite(t.tile).getFrame(0), t.x - (int)mCamera.x, t.y - (int)mCamera.y, null);         			
         		}
         	} 
        	else if(t.tileset == 1)
        	{
        		if(t.tile < mNumEnemies)
        		{
        			g.drawImage(Main.mFrame.mGamePanel.getEnemySprite(t.tile).getFrame(0), t.x - (int)mCamera.x, t.y - (int)mCamera.y, null);
        			if(t.powerup>0)
        			{
            			g.drawImage(Main.mFrame.mGamePanel.getPowerupSprite().getFrame(t.powerup-1), t.x - (int)mCamera.x, t.y - (int)mCamera.y, null);
        				
        			}
        		}
        	}
        }       
                 
        if (Settings.State.equals("level editor")) {        	
            g.setColor(new Color(255, 1, 1, 120));
            g.fillRect(mMouseCursorRect.x - 2, mMouseCursorRect.y - 1, 5, 5);   
        }
        
        if(mEditEnemy)
        {        	
        	 mGameFont.drawString(g, 0, "Edit Enemy", 153, 70);     	 
        	 for (int i = 0; i < 4; i++) {
                 int sel = 0;
                 if (i == mSelectedOption) {
                     sel = 1;
                 }
                 if (i == 0) {
                	 mGameFont.drawString(g, sel, "Health", 145, 100);
                	 mGameFont.drawString(g, sel, String.valueOf(mLevelInfo.getTile(mCurrentEnemy).health), 245, 100);
                 } else if (i == 1) {
                	 mGameFont.drawString(g, sel, "Weapon", 145, 120);
                	 mGameFont.drawString(g, sel, String.valueOf(mLevelInfo.getTile(mCurrentEnemy).weapon), 245, 120);
                 } else if (i == 2) {
                	 mGameFont.drawString(g, sel, "Powerup", 145, 140);
                	 mGameFont.drawString(g, sel, String.valueOf(mLevelInfo.getTile(mCurrentEnemy).powerup), 245, 140);
                 } else if (i == 3) {
                	 mGameFont.drawString(g, sel, "OK", 185, 170);
                 }
             }      	 
        }   
        
        if (Settings.State.equals("level editor") && mCamera.x < 50 && !Settings.DebugMode) {       	
	        g.setColor(new Color(255,0,0,200));	
	        g.drawString("Left Button - Add Object", 8, 16);
	        g.drawString("Middle Button - Select Tileset", 8, 32);
	        g.drawString("Right Button  - Remove Object", 8, 48);    
        }         
    }  
    
    /**
     * Is the edit enemy screen displayed
     * @return True if the edit enemy screen displayed
     */
    public boolean isEditingEnemy()
    {
    	return mEditEnemy;
    }
}
