package com.citex.applet.spacewar;

import java.awt.Container;
import java.net.URL;
import javax.swing.JApplet;

/**
	This class creates the main application window.
	
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

public class Main extends JApplet
{
	/** The applet code base */
    public static URL cb;
	/** The game panel containing the main game loop */
    public static GamePanel mGamePanel;	
    public final static long serialVersionUID = 1000000;
	
    /**
     * Applet initialization
     */
    public void init () {
    	setSize(400, 239);
      	cb = getCodeBase();
    	Container content_pane = getContentPane ();
        mGamePanel = new GamePanel();        
    	content_pane.add (mGamePanel);
    	mGamePanel.start(); 
    } 
} 