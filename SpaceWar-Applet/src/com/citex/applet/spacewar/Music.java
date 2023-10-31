package com.citex.applet.spacewar;

import java.io.BufferedInputStream;
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
	
	private static String mPath;
	private static JLayerMp3Player player;

	public Music(String path) {
		mPath = path;
		try {
			if(Settings.Music)
				play(path);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void stopMusic() {
		if(player != null)
			player.pause();
	}

	public static void closeMusic() {
		if(player != null)
			player.close();
	}
	
	
	public static void startMusic() {
		if(player != null)
			player.start();
	}

	public void play(String filename) throws JavaLayerException, IOException {
		InfoListener lst = new InfoListener();
		playMp3(filename, lst);
	}

	public void showUsage() {
		System.out.println("Usage: jla <filename>");
		System.out.println("");
		System.out.println(" e.g. : java javazoom.jl.player.advanced.jlap localfile.mp3");
	}

	public static JLayerMp3Player playMp3(String path, PlaybackListener listener)
			throws IOException, JavaLayerException {
		return playMp3(path, 0, Integer.MAX_VALUE, listener);
	}

	public static JLayerMp3Player playMp3(String path, int start, int end,
			PlaybackListener listener) throws IOException, JavaLayerException {
    	InputStream is = Main.class.getResourceAsStream(path); 
		return playMp3(new BufferedInputStream(is),
				start, end, listener);
	}

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

	public class InfoListener extends PlaybackListener {
		public void playbackStarted(PlaybackEvent evt) {
			// System.out.println("Play started from frame " + evt.getFrame());
		}

		public void playbackFinished(PlaybackEvent evt) {
			InfoListener lst = new InfoListener();
			try {
				playMp3(mPath, lst);
			} catch (Exception e) {
			}
			// System.out.println("Play completed at frame " + evt.getFrame());
		}
	}
}