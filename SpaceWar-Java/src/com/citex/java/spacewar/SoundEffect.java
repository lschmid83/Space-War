package com.citex.java.spacewar;

import javax.sound.sampled.*;
import java.io.*;

/**
	This class loads and plays wav sounds effects.
	
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

public class SoundEffect extends Thread {

	/** The file path of the wav sound effect */
    private String mFilePath;

    /**
     * Constructs the SoundEffect
     * @param path The file path of the wav file
     */
    public SoundEffect(String path) {
        mFilePath = path;
    }

    /**
     * Run thread and play sound effect
     */
    public void run() {
        if(Settings.Sound)
        {
	    	try {
	            //Open an audio input stream.
	            File file = new File(mFilePath);
	            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
	            // Get a sound clip resource.
	            Clip clip = AudioSystem.getClip();
	            // Open audio clip and load samples from the audio input stream.
	            clip.open(audioIn);
	            clip.start();
	        } catch (UnsupportedAudioFileException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (LineUnavailableException e) {
	            e.printStackTrace();
	        }
        }
    }
}
