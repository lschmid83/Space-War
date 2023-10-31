package com.citex.java.spacewar;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
	This class creates the main application window containing the game and 
	level editor drawing panels.
	
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

public class GameFrame extends JFrame {

	/** The game panel containing the main game loop */
    public GamePanel mGamePanel;
    /** The level editor panel containing the level editor code */
    public EditorPanel mEditorPanel;
    /** Distinguishes the class when it is serialized and deserialized */
    public final static long serialVersionUID = 2000000;

    /**
     * Constructs the GameFrame
     */
    public GameFrame() {
        
    	setTitle("Space War");
    	setSize(415, 277);
    	//setSize(830, 554);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mGamePanel = new GamePanel();        
        mGamePanel.start(); 
        add(mGamePanel, BorderLayout.CENTER);    
        centerScreen();
        setResizable(true);
        setVisible(true);
        
        // transparent 16 x 16 pixel cursor image
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);  
        // create a new blank cursor 
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor( cursorImg, new Point(0, 0), "blank cursor");  
        // set the blank cursor to the JFrame
        getContentPane().setCursor(blankCursor);               
      }

    /**
     * Centres the main game frame
     */
    public void centerScreen() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = ((toolkit.getScreenSize().width - getWidth()) / 2);
        int y = ((toolkit.getScreenSize().height - getHeight()) / 2);
        setLocation(x, y);
    }
    
    /**
     * Starts the level editor
     */
    public void startEditor() {
        mEditorPanel = new EditorPanel();
        Settings.LevelEditor = true;
    }

    /**
     * Stops the level editor
     */
    public void stopEditor() {
        mEditorPanel = null;
        Settings.LevelEditor = false;
    }    
}
