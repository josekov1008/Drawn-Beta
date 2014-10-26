package com.drawn.drawn;

import java.util.LinkedList;

public class Line {
	private int color = 0xFF660000;
	public LinkedList<Coordinate> coords;
	private int i;
	
	public Line(int paintColor) {
		color = paintColor;
		i = 0;
		coords = new LinkedList<Coordinate>();
	}
	
	public void add(float x, float y) {
		coords.add(new Coordinate(x, y));
		i++;
	}
	
	public int getSize() {
		return i;
	}
	
	public int getColor() {
		return color;
	}

	
	
}