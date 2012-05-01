package com.gamescrafters.abstractGame;

public class Player {
	private String myName;
	private Board myBoard;

	public boolean makeMove(Object location, Piece piece) {
		if (myBoard.isValid(location, piece)) {
			myBoard.placePiece(location, piece);
			return true;
		}
		return false;
	}
	
	public void setName(String name) {
		myName = name;
	}
	
	public String getName() {
		return myName;
	}
	
	public void setBoard(Board board) {
		myBoard = board;
	}
	
	public Board getBoard() {
		return myBoard;
	}
}
