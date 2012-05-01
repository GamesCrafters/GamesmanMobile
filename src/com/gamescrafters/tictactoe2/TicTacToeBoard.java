package com.gamescrafters.tictactoe2;
import com.gamescrafters.abstractGame.*;

public class TicTacToeBoard implements Board {
	
	Piece[][] b;
	int width;
	int height;
	
	public void initBoard(int w, int h) {
		width = w;
		height = h;
		b = new Piece[w][h];
	}
	
	public void placePiece(TicTacToeLocation location, Piece piece) {
		b[location.x][location.y] = piece;
	}
	
	public Piece removePiece(TicTacToeLocation location, Piece piece) {
		//Question! If there is no piece there, this will return null.
		//is this okay, or should it error on a null return value?
		
		Piece returnedPiece = b[location.x][location.y];
		b[location.x][location.y] = null;
		return returnedPiece;
	}
	
	public void updateBoard(TicTacToeLocation location, Piece piece) {
		//Not quite sure what updateBoard is for, so for now it'll placePiece.
		
		placePiece(location, piece);
	}
	
	public boolean isValid(TicTacToeLocation location) {
		//Checks to make sure the location isn't out of bounds
		//then checks to make sure the space is empty.
		if(location.x < 0 || location.y < 0 || location.x >= width || location.y >= height) 
			return false;
		else if(b[location.x][location.y] != null) 
			return false;
		else
			return true;
	}
	
	public boolean isGameOver() {

		for(int w = 0; w < width-2; w++) {
			for(int h = 0; h < height-2; h++) {
				//check for horizontal win starting at (w,h)
				if(b[w][h].getName().equals("X") && b[w+1][h].getName().equals("X") && b[w+2][h].getName().equals("X"))
					return true; // Player X's victory; this logic can be moved?
				else if(b[w][h].getName().equals("O") && b[w+1][h].getName().equals("O") && b[w+2][h].getName().equals("O"))
					return true; // Player O's victory
				
				//check for vertical win starting at (w,h)
				else if(b[w][h].getName().equals("X") && b[w][h+1].getName().equals("X") && b[w][h+2].getName().equals("X"))
					return true; // Player X's victory; this logic can be moved?
				else if(b[w][h].getName().equals("O") && b[w][h+1].getName().equals("O") && b[w][h+2].getName().equals("O"))
					return true; // Player O's victory
				
				//check for \ diagonal win starting at (w,h)
				else if(b[w][h].getName().equals("X") && b[w+1][h+1].getName().equals("X") && b[w+1][h+1].getName().equals("X"))
					return true; // Player X's victory
				else if(b[w][h].getName().equals("O") && b[w+1][h+1].getName().equals("O") && b[w+1][h+1].getName().equals("O"))
					return true; // Player O's victory
				
				if(w >= 2) {
					//check for / diagonal win starting at (w,h)
					if(b[w][h].getName().equals("X") && b[w-1][h+1].getName().equals("X") && b[w-2][h+2].getName().equals("X"))
						return true; // Player X's victory; this logic can be moved?
					else if(b[w][h].getName().equals("O") && b[w-1][h+1].getName().equals("O") && b[w-2][h+2].getName().equals("O"))
						return true; // Player O's victory
				}
			}
		}
		return false;
	}
	
	public String toString() {
		String s = "";
		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				s += b[w][h].getName();
				s += " ";
			}
			s += "\n";
		}
		
		return s;
	}
}
