package com.citex.spacewar;

/**
	This class stores the camera coordinates.
	
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

public class Camera {
	
	/** The camera x coordinate */
	float x;
	/** The camera y coordinate */
	float y;
	
	/**
	 * Constructs the Camera
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Camera(float x, float y)
	{
		this.x = x;
		this.y = y;	
	}
}
