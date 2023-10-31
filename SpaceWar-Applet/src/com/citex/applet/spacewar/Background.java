package com.citex.applet.spacewar;

import java.awt.*;
import java.awt.image.*;

/**
	This class draws a scrolling background with three layers.
	
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

public class Background {

	/** The file number of each background layer */
	private int[] mFileNumber;
	/** The image data for each background layer */
	private BufferedImage[] mLayerImage;
	/** The x coordinate of each layer */
	private float mLayerX[];
    /** The scrolling speed of each layer */
    private float mLayerSpeed[];
    /** The width of the background */
    public int mWidth;
    /** The height of the background */
    private int mHeight;
    /** The time elapsed between scrolling the background */
	private int mScrollTimer;
    /** The x coordinate of the background */
    private int bgX;
   
    /**
     * Constructs the Background
     * @param fileNumber The file number of the image for the near, middle and far layers [0]-near, [1]-middle [2]-far
     * @param width The width of the level background
     * @param height The height of the level background
     * @param layerSpeed The scrolling speed of each background layer
     */
    public Background(int fileNumber[], int width, int height, float layerSpeed[]) {
    	mFileNumber = new int[3];
    	mFileNumber = fileNumber;
    	mLayerX = new float[3];
        mWidth = width;
        mHeight = height;
        mLayerSpeed = new float[3];
        mLayerSpeed = layerSpeed;
        mLayerImage = new BufferedImage[3];   
        try 
        {
            mLayerImage[0] = new Image("res/bgr/near/" + mFileNumber[0] + ".bmp").getImage(); //near
            mLayerImage[1] = new Image("res/bgr/middle/" + mFileNumber[1] + ".bmp").getImage(); //middle
            mLayerImage[2] = new Image("res/bgr/far/" + mFileNumber[2] + ".bmp").getImage(); //far       
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
		if (cam.x >= mWidth - 400) {
			cam.x = mWidth - 400;
		}
		if (cam.x <= 0) {
			cam.x = 0;
		}
		if (cam.y >= mHeight - 240) {
			cam.y = mHeight - 240;
		}
		if (cam.y <= 0) {
			cam.y = 0;
		}

		bgX = (int) cam.x - ((int) cam.x / 512 * 512);
		if (!Settings.Paused) {
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
     * @param g The graphics context
     * @param cam The camera coordinates
     */
    public void draw(Graphics g, Camera cam) {
		g.drawImage(mLayerImage[2], -bgX - (int) mLayerX[2], 0, null);
		g.drawImage(mLayerImage[2], -bgX - (int) mLayerX[2] + 508, 0, null);
		g.drawImage(mLayerImage[2], -bgX - (int) mLayerX[2] + 1016, 0, null);

		g.drawImage(mLayerImage[1], -bgX - (int) mLayerX[1], 0, null);
		g.drawImage(mLayerImage[1], -bgX - (int) mLayerX[1] + 508, 0, null);
		g.drawImage(mLayerImage[1], -bgX - (int) mLayerX[1] + 1016, 0, null);

		g.drawImage(mLayerImage[0], -bgX - (int) mLayerX[0], 0, null);
		g.drawImage(mLayerImage[0], -bgX - (int) mLayerX[0] + 508, 0, null);
		g.drawImage(mLayerImage[0], -bgX - (int) mLayerX[0] + 1016, 0, null);
    }
}
