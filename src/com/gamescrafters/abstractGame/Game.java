package com.gamescrafters.abstractGame;

public abstract class Game {
	private Board myBoard;
	private Player player1;
	private Player player2;
	private boolean player1Turn;
	// Move history? 
	
	// Will add in the arguments later
	public abstract void displayBoard();
	
	public boolean isPlayer1Turn() {
		return player1Turn;
	}
	
	public abstract boolean isValidMove(Object location, Piece piece);
	
	public abstract void makeMove(Object location, Piece piece);
	
	public abstract int gameOver(); //Maybe return 1 for win, 0 for lose, 2 for draw, or something

}
