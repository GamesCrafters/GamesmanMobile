package com.gamescrafters.abstractGame;
/**
 * This is the ai class. It uses the mutableboard as a temporary thinking 
 * module.
 * @author ali
 *
 */
public abstract class AI extends Player {
	private final String myName = "@ai";
	private Board myBoard;
	private MutableBoard myMutableBoard;
	
	/**X sets difficulty of with what precision to calculate the
	 * moves. Once it's calculated, set location and piece to the calculated
	 * ones.
	 */
	public void moveCalculator(int x) {
		Object location;
		Piece piece;
		myBoard.placePiece(location, piece);
	}
	
	/**This would suppress giving the ai a name.
	 */
	public void setName(String name) {
	}
}
