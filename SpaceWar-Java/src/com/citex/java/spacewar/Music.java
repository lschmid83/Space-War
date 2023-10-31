package com.citex.java.spacewar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;

/**
	This class loads and plays an mp3 file using the jLayer player.
	Based on sample code from: http://www.javazoom.net/javalayer/javalayer.html
	
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

public class Music {
	
	/** The file path of the mp3 file */
	private static String mPath;
	/** The jLayer mp3 player */
	private static JLayerMp3Player player;

	/**
	 * Constructs the Music
 	 * @param path The mp3 file path
	 */
	public Music(String path) {
		mPath = path;
		try {
			play(path);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** 
	 * Stops the playing music 
	 */
	public static void stopMusic() {
		if(player != null)
			player.pause();
	}

	/**
	 * Closes the mp3 player
	 */
	public static void closeMusic() {
		if(player != null)
			player.close();
	}
	
	/**
	 * Start the music	
	 */
	public static void startMusic() {
		if(player != null)
			player.start();
	}

	/**
	 * Play a mp3 file
	 * @param filename The file path of the mp3 file
	 * @throws JavaLayerException JLayer exception
	 * @throws IOException IO Exception
	 */
	public void play(String filename) throws JavaLayerException, IOException {
		InfoListener lst = new InfoListener();
		playMp3(new File(filename), lst);
	}

	/**
	 * Displays jLayer player usage
	 */
	public void showUsage() {
		System.out.println("Usage: jla <filename>");
		System.out.println("");
		System.out.println(" e.g. : java javazoom.jl.player.advanced.jlap localfile.mp3");
	}

	/**
	 * 
	 * @param mp3
	 * @param listener
	 * @return
	 * @throws IOException
	 * @throws JavaLayerException
	 */
	public static JLayerMp3Player playMp3(File mp3, PlaybackListener listener)
			throws IOException, JavaLayerException {
		return playMp3(mp3, 0, Integer.MAX_VALUE, listener);
	}

	/**
	 * 
	 * @param mp3
	 * @param start
	 * @param end
	 * @param listener
	 * @return
	 * @throws IOException
	 * @throws JavaLayerException
	 */
	public static JLayerMp3Player playMp3(File mp3, int start, int end,
			PlaybackListener listener) throws IOException, JavaLayerException {
		return playMp3(new BufferedInputStream(new FileInputStream(mp3)),
				start, end, listener);
	}

	/**
	 * 
	 * @param is
	 * @param start
	 * @param end
	 * @param listener
	 * @return
	 * @throws JavaLayerException
	 */
	public static JLayerMp3Player playMp3(final InputStream is, final int start,
			final int end, PlaybackListener listener) throws JavaLayerException {
		
		player = new JLayerMp3Player(is);
		player.setPlayBackListener(listener);
		// run in new thread
		new Thread() {
			public void run() {
				try {
					player.play(start, end);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}.start();
		return player;

	}

	/**
	 * PLayback listener class detects start and end of mp3 play back
	 * @author jLayer Sample Code - http://www.javazoom.net/javalayer/javalayer.html
	 *
	 */
	public class InfoListener extends PlaybackListener {
		public void playbackStarted(PlaybackEvent evt) {
			// System.out.println("Play started from frame " + evt.getFrame());
		}
		public void playbackFinished(PlaybackEvent evt) {
			InfoListener lst = new InfoListener();
			try {
				playMp3(new File(mPath), lst);
			} catch (Exception e) {
			}
			// System.out.println("Play completed at frame " + evt.getFrame());
		}
	}
}