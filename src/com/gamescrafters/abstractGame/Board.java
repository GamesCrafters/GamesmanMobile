package com.gamescrafters.abstractGame;

public abstract class Board {
	
	private Object myBoard;
	
	public abstract void initBoard();
	
	public abstract void placePiece(Object location, Piece piece);
	
	public abstract Piece removePiece(Object location, Piece piece);
		
	public abstract boolean isValid(Object location, Piece piece);
	
	public boolean isGameOver() {
		return hasPlayerWon() || hasTie();
	}
	
	public abstract boolean hasPlayerWon();
	
	public abstract boolean hasTie();
	
	public Object getMyBoard() {
		return myBoard;
	}

	public void setMyBoard(Object board) {
		myBoard = board;
	}

	public abstract String toString();
	
}
