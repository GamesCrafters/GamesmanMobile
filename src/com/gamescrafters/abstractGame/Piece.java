package com.gamescrafters.abstractGame;

/**
 * The Piece class is simply a different naming of the Object class
 * to be used as more fluid syntax.
 * 
 * @author Naoto
 *
 */
public abstract class Piece {
	
	private String myName;
	
	public Piece(String s) {
		myName = s;
	}

	public String getName() {
		return myName;
	}

	public void setName(String name) {
		this.myName = name;
	}
	
	

}
