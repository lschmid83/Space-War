package com.citex.spacewar;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

/**
	This class loads and plays wav sounds effects.
	
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

public class SoundEffect {

	/** Controls the playback of audio files */
	private static MediaPlayer[] mPlayer;
	/** Provides access to an application's raw asset files */
	public static AssetManager mAssetManager;
	/**
	 * Load the sound effects
	 */

	public SoundEffect(AssetManager assetManager) {
	 mAssetManager = assetManager;
	}

	public static void loadSounds(AssetManager assetManager)
	{
		mPlayer = new MediaPlayer[14];
		loadSoundEffect("sfx/die.wav", 0, assetManager);
		loadSoundEffect("sfx/explosion.wav", 1, assetManager);
		loadSoundEffect("sfx/hit.wav", 2, assetManager);
		loadSoundEffect("sfx/powerup.wav", 3, assetManager);
		loadSoundEffect("sfx/select.wav", 4, assetManager);
	}
		
	/**
	 * Loads a wav file from the assets folder
	 * @param file The path of the wave file
	 * @param id The id of the sound effect
	 */
	public static void loadSoundEffect(String file, int id, AssetManager assetManager) {
		try {
			mPlayer[id] = new MediaPlayer();
			AssetFileDescriptor afd = assetManager.openFd(file);
			mPlayer[id].setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
			mPlayer[id].prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Plays a sound effect 
	 * @param file The name of the wav sound file
	 */
	public static void play(String file) {
		
		if (Settings.Sound) {
			if (file.equals("die.wav"))
				mPlayer[0].start();
			else if (file.equals("explosion.wav"))
				mPlayer[1].start();			
			else if (file.equals("hit.wav"))
				mPlayer[2].start();
			else if (file.equals("powerup.wav"))
				mPlayer[3].start();
			else if (file.equals("select.wav"))
				mPlayer[4].start();
		}
	}

    /**
     * Destroys the media player
     */
	public static void destroy() {
		for (int i = 0; i < 13; i++)
		{
			if(mPlayer[i] !=null)
				mPlayer[i].release();
		}
	}
}
