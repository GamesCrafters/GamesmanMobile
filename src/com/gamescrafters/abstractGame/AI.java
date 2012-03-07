package com.gamescrafters.abstractGame;

public abstract class AI extends Player {
	private final String myName = "@ai";
	private Board myBoard;
	
	public void makeMove(Object location, Piece piece) {
		myBoard.placePiece(location, piece);
	}
	
	public String getMyName() {
		return myName;
	}
	
	public void setBoard(Board board) {
		myBoard = board;
	}
	
	public Board getBoard() {
		return myBoard;
	}
}
