package com.gamescrafters.abstractGame;

public interface Board {
	
	public abstract void placePiece(Object location, Piece piece);
	
	public abstract boolean isGameOver();
	
}
