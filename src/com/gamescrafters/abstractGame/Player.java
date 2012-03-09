package com.gamescrafters.abstractGame;

public class Player {
	private String myName;
	private Board myBoard;

	public void makeMove(Object location, Piece piece) {
		myBoard.placePiece(location, piece);
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
