package com.gamescrafters.abstractGame;

public abstract class Game {
	private Board myBoard;
	private Player player1;
	private Player player2;
	private boolean player1Turn;
	
	public Game(Board b, Player p1, Player p2){
		myBoard = b;
		player1 = p1;
		player2 = p2;
		player1Turn = true;
	}
	
	public abstract void displayBoard();
	
	public boolean isPlayer1Turn() {
		return player1Turn;
	}
	
	public void changeTurn() {
		player1Turn = !player1Turn;
	}
	
	public abstract boolean isValidMove(Object location, Piece piece);
	
	public abstract void makeMove(Object location, Piece piece);
	
	public abstract int gameOver(); //Maybe return 1 for win, 0 for lose, 2 for draw, or something

	// Automatically-generated getters/setters:
	
	public Board getMyBoard() {
		return myBoard;
	}

	public void setMyBoard(Board myBoard) {
		this.myBoard = myBoard;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public void setPlayer1Turn(boolean player1Turn) {
		this.player1Turn = player1Turn;
	}
	
	

}
