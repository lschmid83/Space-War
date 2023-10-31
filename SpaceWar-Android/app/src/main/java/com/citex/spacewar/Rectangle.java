package com.citex.spacewar;

/**
	This class stores the coordinates and dimensions of a rectangle and 
	tests for intersections between other rectangles.
	
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

public class Rectangle {
	private float x, y, w, h;
	
	/**
	 * Constructs the Rectangle
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param w The width
	 * @param h The height
	 */
	public Rectangle(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;	
	}
	
	/**
	 * Tests if the interior of this Rectangle2D intersects the interior of a specified set of rectangular coordinates
	 * @param r The specified rectangle
	 * @return True if the rectangle intersects the specified rectangle
	 */
	public boolean intersect(Rectangle r)
	{
		if (x < r.x + r.w && x + w > r.x &&
			y < r.y + r.h && y + h > r.y)
			return true;
		else
			return false;	
	}	
}
