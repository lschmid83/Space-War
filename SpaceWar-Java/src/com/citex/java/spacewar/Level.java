package com.citex.java.spacewar;

import java.io.*;
import java.util.ArrayList;

/**
	This class opens and saves level files which contain header, enemy
	and scenery tile information.
	
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

public class Level {

	/** The levels header information */
    private Header mHeader;
    /** The tile information */
    private ArrayList<Tile> mTiles;

    /**
     * Constructs the Level 
     * @param path The file input path
     */
    public Level(String path) {
        loadLevel(path);
    } 
    
    /**
     * Default constructor
     */
    public Level() {
    }

    /**
     * Returns the level header information
     * @return The level header
     */
    public Header getHeader() {
        return mHeader;
    }

    /**
     * Sets the level header information
     * @param header The level header
     */
    public void setHeader(Header header) {
        mHeader = header;
    }

    /**
     * Removes a tile from the level
     * @param index The index of the tile
     */
    public void removeTile(int index) {
        mTiles.remove(index);
        mHeader.objectCount = mTiles.size();
    }

    /**
     * Adds a tile to the level
     * @param tileIndex The index for the tile
     * @param tilesetNumber The folder containing the tiles 0-block, 16-terrain, 32-scenery
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void addTile(int tile, int tileset, int x, int y) { 	
    	Tile obj = new Tile();
        obj.x = x;
        obj.y = y;
        obj.tile = tile;
        obj.tileset = tileset;
        if(tileset == 1)
        {
           obj.collision = 1;
           if(obj.tile <= 3)            
        	   obj.health = 3;
           else
        	   obj.health = 5;
           if(obj.tile == 8)
        	   obj.weapon = 2;
           else
           {
        	   if(obj.tile >= 6)
        		   obj.weapon = 1;     	   
           }
        	   
           obj.powerup = 0;        
        }
        else
        	obj.collision = 0;
    
        mTiles.add(obj);
        mHeader.objectCount = mTiles.size();
    }

 
    /**
     * Returns the number of tiles in the level
     * @return The number of tiles in the level
     */
    public int getTileCount() {
        return mTiles.size();
    }
    
    /** 
     * Returns the tile array list
     * @return The tiles in the level map
     */ 
    public ArrayList<Tile> getTiles()
    {
    	return mTiles;  	
    }

    /** 
     * Returns the tile array list
     * @return The tiles in the level map
     */ 
    public Tile getTile(int index)
    {
    	return mTiles.get(index);  	
    }    
    
    /**
     * Initialises a new level
     */
    public void newLevel() {
        mHeader.file = 1;
        mHeader.background = 1;
        mHeader.width = 2720;
        mHeader.music = 1;
    } 
  
    /**
     * Loads the level header, tile and entity information
     * @param world The world number
     * @param level The level number
     * @param area The area number
     * @return True if the level exists
     */
    public boolean loadLevel(String path) {
    	
        boolean result = false;
        mHeader = new Header();
        mTiles = new ArrayList<Tile>();

        try {
            String line;
            BufferedReader input = new BufferedReader(new FileReader(path));
            mHeader.file = Integer.parseInt(input.readLine());
        	mHeader.background = Integer.parseInt(input.readLine());
        	mHeader.width = Integer.parseInt(input.readLine()) * 16;
        	mHeader.music =  Integer.parseInt(input.readLine());
        	mHeader.objectCount = Integer.parseInt(input.readLine());
        	
        	//read object data
        	if(mHeader.objectCount > 0)
        	{
	            Tile obj;           
	            for (int i = 0; i < mHeader.objectCount; i++) {
	                obj = new Tile();                 
	                line = input.readLine();              
	                obj.tile = Integer.parseInt(line.split(",")[0]);
	                obj.tileset = Integer.parseInt(line.split(",")[1]);
	                obj.x = Integer.parseInt(line.split(",")[2]);
	                obj.y = Integer.parseInt(line.split(",")[3]);
	                obj.collision = Integer.parseInt(line.split(",")[4]);  
	                obj.health = Integer.parseInt(line.split(",")[5]);
                	obj.weapon = Integer.parseInt(line.split(",")[6]);
	                obj.powerup = Integer.parseInt(line.split(",")[7]);
	                mTiles.add(obj);
	            }
        	}
        	result = true;
        	input.close();

	    } catch (FileNotFoundException e) {
	        newLevel();
	        return result;
	    } catch (IOException e) {
	        newLevel();
	        return result;
	    } 
        
	    return result;	
	}

    /**
     * Saves the level header, tile and entity information
     * @param path The file output path
     */
    public void saveLevel(String path) {
        try {
        	PrintWriter pw = new PrintWriter(new FileWriter(path));
        	pw.println(mHeader.file);
        	pw.println(mHeader.background);
        	pw.println(mHeader.width / 16);
        	pw.println(mHeader.music);
        	pw.println(mHeader.objectCount);
            //write tile data
            Tile obj;
            for (int i = 0; i < mTiles.size(); i++) {
                obj = mTiles.get(i);
                pw.println(obj.tile + "," + obj.tileset + "," + obj.x + "," + obj.y + "," + obj.collision + "," + obj.health  + "," + obj.weapon  + "," + obj.powerup);
            }        	
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  	
    }
}

/**
    This class stores level header information such as the background, music
    and tilesets associated with the level.

	@version 1.0
	@modified 27/02/2012
	@author Lawrence Schmid
*/
class Header {
    int file;
    int background;
    int width; 
    int music;
    int objectCount;
}

/**
    This class stores tile information.
 
	@version 1.0
	@modified 27/02/2012
	@author Lawrence Schmid
*/
class Tile {
    int x;
    int y;
    int tile;
    int tileset;
    int collision;
    int health;    
    int weapon;
    int powerup;
};
