package com.citex.spacewar;

import android.content.res.AssetManager;

import javax.microedition.khronos.opengles.GL10;

/**
	This class draws a scrolling background with three layers.
	
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

public class Background {
	
	/** The file number of each background layer */
    private int[] mFileNumber;
    /** The image data for each background layer */
    private GLSprite mLayerImage[];
    /** The x coordinate of each layer */
    private float mLayerX[];
    /** The scrolling speed of each layer */
    private float mLayerSpeed[];
    /** The width of the background */
    private int mWidth;
    /** The height of the background */
    private int mHeight;
    /** The time elapsed between scrolling the background */
    private int mScrollTimer;
    /** The x coordinate of the background */
    private int bgX;

    /**
     * Constructs the Background
     * @param gl The GL context
     * @param fileNumber The file number of the image for the near, middle and far layers [0]-near, [1]-middle [2]-far
     * @param width The width of the level background
     * @param height The height of the level background
     * @param layerSpeed The scrolling speed of each background layer
     */
    public Background(GL10 gl, int fileNumber[], int width, int height, float layerSpeed[], AssetManager assetManager) {
        mFileNumber = fileNumber;
    	mLayerX = new float[3];
        mWidth = width;
        mHeight = height;
        mLayerSpeed = new float[3];
        mLayerSpeed = layerSpeed;
        mLayerImage = new GLSprite[3];
        try
        {
            mLayerImage[0] = new GLSprite(gl, "bgr/near/" + mFileNumber[0] + ".png", null, assetManager); //near
            mLayerImage[1] = new GLSprite(gl, "bgr/middle/" + mFileNumber[1] + ".png", null, assetManager); //middle
            mLayerImage[2] = new GLSprite(gl, "bgr/far/" + mFileNumber[2] + ".png", null, assetManager); //far
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves the scrolling background layers and limits camera coordinate to within background width and height
     * @param cam The camera coordinates
	 * @param dt The delta time between frame updates
     * @return The camera coordinates within the screen area
     */
    public Camera move(Camera cam, float dt)
    {
    	  if (cam.x >= mWidth - Settings.ScreenWidth) {
              cam.x = mWidth - Settings.ScreenWidth;
          }
          if (cam.y >= mHeight - Settings.ScreenHeight) {
              cam.y = mHeight - Settings.ScreenHeight;
          }

          bgX = (int)cam.x - ((int)cam.x / 512 * 512);

          if(!Settings.Paused)
          {
  	        if (mScrollTimer < 2) {
  	            mScrollTimer++;
  	        } else {
  	            for (int i = 0; i < 3; i++) {
  	                if (mLayerX[i] < 512) {
  	                    mLayerX[i] += mLayerSpeed[i] * dt;
  	                } else {
  	                    mLayerX[i] = 0;
  	                }
  	            }
  	            mScrollTimer = 0;
  	        }
          }    
          return cam; 
    }
    
    /**
     * Draws the background on the graphics surface at the camera position
     * @param gl The GL context
     * @param cam The camera coordinates
     */
    public void draw(GL10 gl, Camera cam) {
    	
    	mLayerImage[2].draw(gl, -bgX - mLayerX[2],0);
        mLayerImage[2].draw(gl, -bgX - mLayerX[2] + 512, 0);
        mLayerImage[2].draw(gl, -bgX - mLayerX[2] + 1024, 0);

        mLayerImage[1].draw(gl, -bgX - mLayerX[1], 0);
        mLayerImage[1].draw(gl, -bgX - mLayerX[1] + 512, 0);
        mLayerImage[1].draw(gl, -bgX - mLayerX[1] + 1024, 0);

        mLayerImage[0].draw(gl, -bgX - mLayerX[0], 0);
        mLayerImage[0].draw(gl, -bgX - mLayerX[0] + 512, 0);
        mLayerImage[0].draw(gl, -bgX - mLayerX[0] + 1024, 0);
    }

    /**
     * Destroys the textures and releases the hardware buffers
     * @param gl The GL context
     */
    public void destroy(GL10 gl) {
        mLayerImage[0].destroy(gl);
        mLayerImage[1].destroy(gl);
        mLayerImage[2].destroy(gl);
    }
}
