package com.gamescrafters.abstractGame;

public interface Board {
	
	public abstract void initBoard();
	
	public abstract void placePiece(Object location, Piece piece);
	
	public abstract Piece removePiece(Object location, Piece piece);
	
	public abstract void updateBoard(Object location, Piece piece);
	
	public abstract boolean isValid(Object location);
	
	public abstract boolean isGameOver();
	
	public abstract String toString();
	
}
