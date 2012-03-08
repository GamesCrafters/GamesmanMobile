package com.gamescrafters.abstractGame;

public abstract class Game {
	private Board myBoard;
	private Player player1;
	private Player player2;
	private boolean player1Turn;
	// Move history? 
	
	// Will add in the arguments later
	public abstract void displayBoard();
	
	public abstract boolean isPlayerTurn();
	
	public abstract boolean isValidMove();
	
	public abstract void makeMove();
	
	public abstract int gameOver(); //Maybe return 1 for win, 0 for lose, 2 for draw, or something

}
