package com.gamescrafters.abstractGame;

import com.gamescrafters.gamesmanmobile.GameActivity;

public abstract class Game {

	private String myName;
	private Board myBoard;
	private Player player1;
	private Player player2;
	private boolean player1Turn;

	public Game(Board b, Player p1, Player p2) {
		myBoard = b;
		player1 = p1;
		player2 = p2;
		player1Turn = true;
	}

	public boolean isPlayer1Turn() {
		return player1Turn;
	}
	
	public Player getCurrentPlayer() {
		if (player1Turn) {
			return player1;
		}
		else {
			return player2;
		}
	}

	public void changeTurn() {
		player1Turn = !player1Turn;
	}

	public boolean isValidMove(Object location, Piece piece) {
		return myBoard.isValid(location, piece);
	}

	// Maybe return 1 for win, 0 for lose, 2 for draw, or something
	public int gameOver() {
		if (myBoard.isGameOver()) {
			if (myBoard.hasTie()) {
				return 2;
			}
			if (!player1Turn) {
				return 1;
			}
			if (player1Turn) {
				return 3;
			}
		}
		return 0;
	}
	
	public boolean isGameOver() {
		return gameOver() > 0;
	}
	
	public void restartGame() {
		myBoard.initBoard();
	}
	
	public void restart() {
		player1 = null;
		player2 = null;
		myBoard.initBoard();
	}

	// Automatically-generated getters/setters:

	public Board getBoard() {
		return myBoard;
	}

	public void setBoard(Board board) {
		myBoard = board;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player) {
		player1 = player;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player) {
		player2 = player;
	}

	public void setPlayer1Turn(boolean ifPlayer1Turn) {
		player1Turn = ifPlayer1Turn;
	}

	public String getName() {
		return myName;
	}

	public void setName(String name) {
		myName = name;
	}

}
