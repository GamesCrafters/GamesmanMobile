package com.gamescrafters.Y;

/*Author: Pin Xu */

/** Describes the occupants of a square on a Reversi board. */
enum PieceColor {

  /** EMPTY: no piece.
   *  BLACK, WHITE: pieces. */
  EMPTY,
    P1 { 
    PieceColor opposite () { 
      return P2;
    }
  }, 
    P2 {
      PieceColor opposite () { 
	return P1;
      }
    };

    /** The piece color of my opponent, if defined. */
    PieceColor opposite () {
      throw new UnsupportedOperationException ();
    }
}
