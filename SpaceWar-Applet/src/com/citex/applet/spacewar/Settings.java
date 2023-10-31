package com.citex.applet.spacewar;

/**
	This class stores global settings variables for the program.
	
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

public class Settings {

    /** The width of the screen */
	public static int ScreenWidth = 400;
	/** The height of the screen */
    public static int ScreenHeight = 240;
    /** Enable music playback */
    public static boolean Music = true;
    /** Enable sound effects */
    public static boolean Sound = true;
    /** The difficulty 0 - easy, 1 - normal, 2 - hard */
    public static int Difficulty = 1;
    /** The number of lives to start the game with */
    public static int Lives = 3;
    /** The high scores */
    public static int HighScore[]; 
    /** Is the game paused */
    public static boolean Paused = false;
    /** The current game state */
    public static String State = "license";
    /** The camera scroll speed */
    public static float CameraSpeed = 0.065f;
    /** The delay between enemies firing bullets */
    public static int EnemyAutoFireDelay = 400;
    /** The strength of the players bullets */    
    public static float PlayerBulletStrength = 1.0f;
    /** Draw a bounding rectangle around sprite graphics */
    public static boolean DebugMode = false;
    /** The game version id */
    public static String GameVersion = "1.0";
    /** Is the level editor running */
    public static boolean LevelEditor = false; 
    /** Is level select enabled */
    public static boolean LevelSelect = false;
    
    /**
     * Sets the difficult level of the game
     * @param difficulty The difficulty level 0-easy, 1-normal, 2-hard
     */
    public static void setDifficulty(int difficulty)
    {
    	Difficulty = difficulty;
    	if(Difficulty == 0) // easy
    	{
    	    CameraSpeed = 0.060f;
    	    EnemyAutoFireDelay = 425;
    	    PlayerBulletStrength = 1.2f;
    	}
    	else if (Difficulty == 1) //normal
    	{
    		CameraSpeed = 0.065f;
    		EnemyAutoFireDelay = 400;
    		PlayerBulletStrength = 1.0f;
    	}
    	else if (Difficulty == 2) //hard
    	{
    		CameraSpeed = 0.070f;
    		EnemyAutoFireDelay = 375;
    		PlayerBulletStrength = 1.0f;
    	}	   	
    }    
}
