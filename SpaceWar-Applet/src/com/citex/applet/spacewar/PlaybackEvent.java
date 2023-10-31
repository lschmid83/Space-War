/*
 *   This class stores a play back event action.
 *   
 *   Based on code from: http://www.javazoom.net/javalayer/javalayer.html
 * 
 *   11/19/04		1.0 moved to LGPL. 
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

package com.citex.applet.spacewar;

/**
 * An event which indicates a <code>Player</code> has performed an 'play back action'
 * @author Paul Stanton (http://wanto.f2o.org/)
 */
public class PlaybackEvent
{
	public static int STOPPED = 1;
	public static int STARTED = 2;

	private JLayerMp3Player source;
	private int frame;
	private int id;

	/**
	 * Construct the PlaybackEvent
	 * @param source The jLayer source
	 * @param id The id
	 * @param frame The frame
	 */
	public PlaybackEvent(JLayerMp3Player source, int id, int frame)
	{
		this.id = id;
		this.source = source;
		this.frame = frame;
	}

	public int getId(){return id;}
	public void setId(int id){this.id = id;}

	public int getFrame(){return frame;}
	public void setFrame(int frame){this.frame = frame;}

	public JLayerMp3Player getSource(){return source;}
	public void setSource(JLayerMp3Player source){this.source = source;}

}
