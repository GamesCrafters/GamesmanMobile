package com.gamescrafters.gamesmanmobile;


public class VVHNode {
	public String move;
	public float remoteness;
	public boolean isBlueTurn;
	
	public VVHNode (String move, int remoteness, boolean whoseTurn) {
		this.move = move;
		this.remoteness = (float) remoteness;
		this.isBlueTurn = whoseTurn; // true = blue, false = red
	}
}