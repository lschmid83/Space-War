package com.citex.spacewar;

/**
	This class stores keyboard / touch screen control variables.
	
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

public class Control {
	/** The left key is pressed (left arrow) */
    public boolean mLeftPressed;
    /** The right key is pressed (right arrow) */
    public boolean mRightPressed;
    /** The up key is pressed (up arrow)*/
    public boolean mUpPressed;
    /** The down key is pressed (down arrow) */
    public boolean mDownPressed;
    /** The fire key is pressed (a) */
    public boolean mFire1Pressed;
    /** The fire special weapon key is pressed (s) */
    public boolean mFire2Pressed;
}
