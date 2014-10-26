package com.drawn.drawn;

public class Drawing {
	private String json;
	private String name;
	
	Drawing(String s, String n) {
		json = s;
		name = n;
	}
	
	public String getJSON() {
		return json;
	}
	
	public String getName() {
		return name;
	}
}