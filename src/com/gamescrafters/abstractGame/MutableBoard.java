package com.gamescrafters.abstractGame;
/**
 * The ai uses this class to test moves on. I'm not sure how to actually
 * implement this class as in terms of abstraction since it would have
 * to be an extension of the specific game's board.
 * @author ali
 *
 */
abstract class MutableBoard implements Board{
	
	    /** A new, empty MutableBoard. */
	    MutableBoard() {
	        //TODO
	    }

	    /** A new MutableBoard whose initial contents are copied from
	     *  where all the pieces are placed. */
	    MutableBoard(Object location) {
//	        char[][] mB = new char[SIZE][SIZE];
//	        for (int i = 0; i < SIZE; i++) {
//	            for (int j = 0; i < SIZE; j++) {
//	                mB[i][j] = board.getc(i, j);
	    	//This would copy multi arrays. TODO
	    	
	            }
	        
	    

		public void placePiece(Object location, Piece piece) {
			// TODO Auto-generated method stub
			
		}

		public boolean isGameOver() {
			// TODO Auto-generated method stub
			return false;
		}
}
