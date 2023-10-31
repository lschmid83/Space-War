package com.citex.spacewar;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.util.Log;

/**
    This class loads an image containing a sprite sheet and buffers 
    the frames of animation.
	
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

public class SpriteSheet {

	/** The sprite sheet containing the animation */
	private GLSprite mSpriteSheet;
	/** The buffered frames of animation */
    private GLSprite mFrame[];
    /** The frame dimensions */
    private int mFrameWidth, mFrameHeight;
    /** The sprite sheet dimensions */
    private float mSheetWidth, mSheetHeight;
    /** The number of rows and columns in the sprite sheet */
    private int mRows, mColumns;
    /** The number of frames in the sprite sheet */
    private int mFrameCount;
    /** Provides access to an application's raw asset files */
    private AssetManager mAssetManager;

    /**
     * Constructs the sprite sheet
     * @param gl The GL context
     * @param path The path of the image file
     * @param frameWidth The frame width
     * @param frameHeight The frame height
     * @param frameCount The frame count
     */
    public SpriteSheet(GL10 gl, String path, int frameWidth, int frameHeight, int frameCount, AssetManager assetManager) {
        this.mFrameWidth = frameWidth;
        this.mFrameHeight = frameHeight;
        this.mFrameCount = frameCount;
        mAssetManager = assetManager;

        mSpriteSheet = new GLSprite(gl, path, null, mAssetManager);
        if (!loadSpriteSheet(gl, path)) {
            Log.e("error loading sheet", path);
        }
    }

    /**
     * Loads a sprite sheet and buffers the frames of animation
     * @param gl The GL context
     * @param path The path of the image file
     * @return True if the sprite sheet has loaded successfully
     */
    public boolean loadSpriteSheet(GL10 gl, String path) {
        if (mSpriteSheet.getTextureID() != -1) {
            mSheetWidth = mSpriteSheet.width;
            mSheetHeight = mSpriteSheet.height;
            mRows = (int) (mSheetHeight / this.mFrameHeight);
            mColumns = (int) (mSheetWidth / this.mFrameWidth);

            //buffer frames from sheet
            mFrame = new GLSprite[mFrameCount];
            for (int i = 0; i < mFrameCount; i++) {
                mFrame[i] = getFrameFromSheet(gl, i); //store each frame in the array
            }
             return true;
        } else {
            Log.e("error loading sheet", "error");
            mSheetWidth = 0;
            mSheetHeight = 0;
            mRows = 0;
            mColumns = 0;
            this.mFrameCount = 1;
            return false;
        }
    }

    /**
     * Get the entire sprite sheet image data
     * @return The sprite sheet image data
     */
    public GLSprite getSheet()
    {
        return mSpriteSheet;
    }

    /**
     * Returns a frame of animation from the sprite sheet
     * @param gl The GL context
     * @param frameNumber The frame number
     * @return The frame of animation
     */
    public GLSprite getFrameFromSheet(GL10 gl, int frameNumber) {
        GLSprite frame = (GLSprite)mSpriteSheet.clone();
        try {
            if (mRows > 0 && mColumns > 0) {
                int col = frameNumber / mRows; //column
                int row = frameNumber - (col * mRows); //row
                float clip[] = {col * mFrameWidth, row * mFrameHeight, mFrameWidth, mFrameHeight};
                frame.setVertices(gl, clip);
                return frame;
            } else
            {
                float clip[] = {0, 0, mFrameWidth, mFrameHeight};
                frame.setVertices(gl, clip);
                return frame;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a buffered frame of animation from the sprite sheet
     * @param frameNumber The frame number
     * @return The buffered frame of animation
     */
    public GLSprite getFrame(int frameNumber) {
        return mFrame[frameNumber];
    }

    /**
     * Returns a buffered frame of animation from the sprite sheet which can be flipped horizontally
     * @param frameNumber The frame number
     * @param direction The horizontal direction of the frame
     * @return The buffered frame of animation
     */
    public GLSprite getFrame(int frameNumber, char direction) {
        try {
           return mFrame[frameNumber];
        } catch (Exception e) 
        {
            return null;
        }
    }
    
    /**
     * Returns the frame width of the sprite sheet
     * @return The frame width
     */
    public int getWidth()
    {
    	return mFrameWidth;
    }
    
    
    /**
     * Returns the frame height of the sprite sheet
     * @return The frame height
     */
    public int getHeight()
    {
    	return mFrameHeight;   	
    }
    
    /**
     * Destroys the textures and releases the hardware buffers
     * @param gl The GL context
     */
    public void destroy(GL10 gl) {
        if(mFrame!=null)
        {
            for (int i = 0; i < mFrameCount; i++) {
                mFrame[i].destroy(gl);
            }
        }
        mFrame = null;
        if(mSpriteSheet != null)
            mSpriteSheet.destroy(gl);
    }
}
