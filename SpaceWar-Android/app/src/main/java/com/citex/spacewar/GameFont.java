package com.citex.spacewar;

import android.content.res.AssetManager;

import javax.microedition.khronos.opengles.GL10;
import java.io.*;

/**
	This class loads two sprite sheets for a font character set and draws 
	and strings of text.
	
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

public class GameFont {

	/** The game font sprite sheets */
    private SpriteSheet[] mSpriteSheet;
    /** The character width of each letter */
    private int[][] mCharacterWidth;

    /**
     * Constructs the GameFont
     * @param gl The GL context
     * @param path The folder containing the fonts sprite sheets
     */
    public GameFont(GL10 gl, String path, AssetManager assetManager) {

    	mSpriteSheet = new SpriteSheet[2];
        mSpriteSheet[0] = new SpriteSheet(gl, path + "0.png", 16, 16, 40, assetManager);
        mSpriteSheet[1] = new SpriteSheet(gl, path + "1.png", 16, 16, 40, assetManager);

        BufferedReader br;
        InputStreamReader is;
        mCharacterWidth = new int[2][40];
        try {
            for (int i = 0; i < 2; i++) {
                is = new InputStreamReader(assetManager.open(path + i + ".ini"));
                br = new BufferedReader(is);
                String value;
                try {
                    for (int a = 0; a < 40; a++) {
                        value = br.readLine();
                        mCharacterWidth[i][a] = Integer.parseInt(value.split(",")[1]);
                    }
                } catch (Exception e) {
                    br.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Draws a string of text at the x,y coordinates in a character set
     * @param gl - The GL context
     * @param charset - The character set e.g. lowercase or capital letters
     * @param string - The string of text to draw
     * @param x - The x drawing coordinate
     * @param y - The y drawing coordinate
     */
    public void drawString(GL10 gl, int charset, String string, float x, float y) {

        int spacing = 0;
        for (int i = 0; i < string.length(); i++) {
            int frameNumber = getLetterFrame(string.charAt(i));
            if (frameNumber != 40) {
                mSpriteSheet[charset].getFrame(frameNumber).draw(gl, x + spacing, y);
                spacing += mCharacterWidth[charset][frameNumber] + 1;
            } else {
                spacing += 5;
            }
        }
    }

    /**
     * Draws a string of text at the x,y coordinates detecting the character set
     * @param gl - The GL context
     * @param string - The string of text to draw
     * @param x - The x drawing coordinate
     * @param y - The y drawing coordinate
     */
    public void drawString(GL10 gl, String string, float x, float y) {

        int spacing = 0;
        int characterSet = 0;
        for (int i = 0; i < string.length(); i++) {
            int frameNumber = getLetterFrame(string.charAt(i));
            if (Character.isLowerCase(string.charAt(i))) {
                characterSet = 0;
            } else {
                characterSet = 1;
            }

            if (frameNumber != 40) {
                mSpriteSheet[characterSet].getFrame(frameNumber).draw(gl, x + spacing, y);
                spacing += mCharacterWidth[characterSet][frameNumber] + 1;
            } else {
                spacing += 5;
            }
        }
    }

    /**
     * Returns the width of text given by the specified string for the font charset in pixels
     * @param string The string to be tested
     * @return The width of the text given by the specified string
     */
    public int getStringWidth(String string) {
        int spacing = 0;
        for (int i = 0; i < string.length(); i++) {
            int frameNumber = getLetterFrame(string.charAt(i));
            if (frameNumber != 40) {
                spacing += mCharacterWidth[0][frameNumber] + 1;
            } else {
                spacing += 5;
            }
        }
        return spacing;
    }

    /**
     * Returns the frame index of a character in the sprite sheet
     * @param letter The character
     * @return The index of the frame in the sprite sheet
     */
    public int getLetterFrame(char letter) {
        letter = Character.toUpperCase(letter);
        switch (letter) {
            case '0': return 0;
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            case 'A': return 10;
            case 'B': return 11;
            case 'C': return 12;
            case 'D': return 13;
            case 'E': return 14;
            case 'F': return 15;
            case 'G': return 16;
            case 'H': return 17;
            case 'I': return 18;
            case 'J': return 19;
            case 'K': return 20;
            case 'L': return 21;
            case 'M': return 22;
            case 'N': return 23;
            case 'O': return 24;
            case 'P': return 25;
            case 'Q': return 26;
            case 'R': return 27;
            case 'S': return 28;
            case 'T': return 29;
            case 'U': return 30;
            case 'V': return 31;
            case 'W': return 32;
            case 'X': return 33;
            case 'Y': return 34;
            case 'Z': return 35;
            case '-': return 36;
            case '.': return 37;
            case '@': return 38;
            case '/': return 39;
            default: return 40;              
        }
    }

    /**
     * Destroys the textures and releases the hardware buffers
     * @param gl The GL context
     */
    public void destroy(GL10 gl)
    {
        mSpriteSheet[0].destroy(gl);
        mSpriteSheet[1].destroy(gl);
    }
}
