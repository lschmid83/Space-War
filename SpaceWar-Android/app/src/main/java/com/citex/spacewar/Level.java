package com.citex.spacewar;

import android.content.res.AssetManager;

import java.io.*;
import java.util.ArrayList;

/**
	This class opens and saves level files which contain header, enemy
	and scenery tile information.
	
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

public class Level {

	/** The levels header information */
	private Header mHeader;
	/** The tile information */
	private ArrayList<Tile> mTiles;
	/** Provides access to an application's raw asset files */
	private AssetManager mAssetManager;

	/**
	 * Default constructor
	 */
	public Level(AssetManager assetManager) {
		mAssetManager = assetManager;
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
     * @param tile The index for the tile
     * @param tileset The folder containing the tiles 0-block, 16-terrain, 32-scenery
     * @param x The x coordinate
     * @param y The y coordinate
     */
	public void addTile(int tile, int tileset, int x, int y) {
		Tile obj = new Tile();
		obj.x = x;
		obj.y = y;
		obj.tile = tile;
		obj.tileset = tileset;
		if (tileset == 1) {
			obj.collision = 1;
			if (obj.tile <= 3)
				obj.health = 3;
			else
				obj.health = 5;

			obj.weapon = 0;
			obj.powerup = 0;
		} else
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
	public ArrayList<Tile> getTiles() {
		return mTiles;
	}

    /** 
     * Returns the tile array list
     * @return The tiles in the level map
     */ 
	public Tile getTile(int index) {
		return mTiles.get(index);
	}

	/**
	 * Initialises a new level
	 */
	public void newLevel() {
        mHeader.file = 1;
        mHeader.background = 1;
        mHeader.width = 800;
        mHeader.music = 0;
	}

    /**
     * Loads the level header, tile and entity information
     * @param path The level file path
     * @return True if the level exists
     */
	public boolean loadLevel(String path) {

		boolean result = false;
		mHeader = new Header();
		mTiles = new ArrayList<Tile>();

		InputStream is;  
        try {
            String line;  
            is = mAssetManager.open(path); // open the input stream for reading
            BufferedReader input = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
	
			mHeader.file = Integer.parseInt(input.readLine());
			mHeader.background = Integer.parseInt(input.readLine());
			mHeader.width = Integer.parseInt(input.readLine()) * 16;
			mHeader.music = Integer.parseInt(input.readLine());
			mHeader.objectCount = Integer.parseInt(input.readLine());
			// read object data
			if (mHeader.objectCount > 0) {
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

		} catch (FileNotFoundException e) {
			//newLevel();
			return result;
		} catch (IOException e) {
			//newLevel();
			return result;
		}
		return result;
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
