package com.gamescrafters.tictactoe2;
import com.gamescrafters.abstractGame.*;

public class TicTacToeBoard extends Board {
	
	Piece[][] gameBoard;
	int width;
	int height;
	
	@Override
	public void initBoard() {
		gameBoard = new Piece[width][height];
		
	}
	
	public void placePiece(Object location, Piece piece) {
		gameBoard[((TicTacToeLocation)location).x][((TicTacToeLocation)location).y] = piece;
	}
	
	public Piece removePiece(Object prev, Piece piece) {
		//Question! If there is no piece there, this will return null.
		//is this okay, or should it error on a null return value?
		TicTacToeLocation location = (TicTacToeLocation) prev;
		Piece returnedPiece = gameBoard[location.x][location.y];
		gameBoard[location.x][location.y] = null;
		return returnedPiece;
	}
	
	public boolean isValid(Object point, Piece piece) {
		//Checks to make sure the location isn't out of bounds
		//then checks to make sure the space is empty.
		TicTacToeLocation location = (TicTacToeLocation) point;
		if(location.x < 0 || location.y < 0 || location.x >= width || location.y >= height) 
			return false;
		else if(gameBoard[location.x][location.y] != null) 
			return false;
		else
			return true;
	}
	
	public boolean hasPlayerWon() {

		for(int w = 0; w < width-2; w++) {
			for(int h = 0; h < height-2; h++) {
				//check for horizontal win starting at (w,h)
				if(gameBoard[w][h].getName().equals("X") && gameBoard[w+1][h].getName().equals("X") && gameBoard[w+2][h].getName().equals("X"))
					return true; // Player X's victory; this logic can be moved?
				else if(gameBoard[w][h].getName().equals("O") && gameBoard[w+1][h].getName().equals("O") && gameBoard[w+2][h].getName().equals("O"))
					return true; // Player O's victory
				
				//check for vertical win starting at (w,h)
				else if(gameBoard[w][h].getName().equals("X") && gameBoard[w][h+1].getName().equals("X") && gameBoard[w][h+2].getName().equals("X"))
					return true; // Player X's victory; this logic can be moved?
				else if(gameBoard[w][h].getName().equals("O") && gameBoard[w][h+1].getName().equals("O") && gameBoard[w][h+2].getName().equals("O"))
					return true; // Player O's victory
				
				//check for \ diagonal win starting at (w,h)
				else if(gameBoard[w][h].getName().equals("X") && gameBoard[w+1][h+1].getName().equals("X") && gameBoard[w+1][h+1].getName().equals("X"))
					return true; // Player X's victory
				else if(gameBoard[w][h].getName().equals("O") && gameBoard[w+1][h+1].getName().equals("O") && gameBoard[w+1][h+1].getName().equals("O"))
					return true; // Player O's victory
				
				if(w >= 2) {
					//check for / diagonal win starting at (w,h)
					if(gameBoard[w][h].getName().equals("X") && gameBoard[w-1][h+1].getName().equals("X") && gameBoard[w-2][h+2].getName().equals("X"))
						return true; // Player X's victory; this logic can be moved?
					else if(gameBoard[w][h].getName().equals("O") && gameBoard[w-1][h+1].getName().equals("O") && gameBoard[w-2][h+2].getName().equals("O"))
						return true; // Player O's victory
				}
			}
		}
		return false;
	}
	


	@Override
	public boolean hasTie() {
		// TODO Auto-generated method stub
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				if (gameBoard[w][h] == null) {
					return false;
				}
			}
		}
		return true;
	}
	
	public String toString() {
		String s = "";
		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				s += gameBoard[w][h].getName();
				s += " ";
			}
			s += "\n";
		}
		
		return s;
	}


}
