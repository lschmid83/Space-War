package com.citex.spacewar;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

/**
	This class loads and plays an mp3 file using the Android MediaPlayer.
	
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
	
	Copyright 2012 Lawrence Schmid.
*/

public class Music {

	/** Controls the playback of audio files */
	private static MediaPlayer mPlayer;
	/** Provides access to an application's raw asset files */
	private AssetManager mAssetManager;

	/**
	 * Constructs the Music
	 * @param path The path of the mp3 file
	 */
    public Music(String path, AssetManager assetManager) {
    	try {
			mPlayer = new MediaPlayer();
			AssetFileDescriptor afd = assetManager.openFd(path);
			mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mPlayer.prepare();
			mPlayer.setLooping(true);
			mPlayer.setVolume(1.0f, 1.0f);
			if(Settings.Music)
				mPlayer.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * Pauses playback of the music
     */
    public static void pauseMusic() {
    	if(mPlayer != null)
    		mPlayer.pause();
    }

    /**
     * Starts playback of the music
     */
    public static void startMusic() {
    	if(mPlayer != null)
    		mPlayer.start();
    }

    /**
     * Destroys the music player
     */
    public void destroy()
    {
    	if(mPlayer != null)
    	{
    		mPlayer.release();
    	}
    }
}
