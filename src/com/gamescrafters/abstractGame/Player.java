package com.gamescrafters.abstractGame;

public class Player {
	private String myName;
	private Board myBoard;

	public void makeMove(Object location, Piece piece) {
		myBoard.placePiece(location, piece);
	}
	
	public void setMyName(String name) {
		myName = name;
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
