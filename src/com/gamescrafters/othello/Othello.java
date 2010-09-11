package com.gamescrafters.othello;

import java.util.Stack;

// import com.gamescrafters.connect4.GUIGameBoard;
import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class Othello extends GameActivity {
	static final String GAME_NAME = "othello";

	private TextView turnTextView, remoteTextView, gameOverTextView;
	private ImageButton turnImage;
	private Drawable bluePiece, redPiece;
	// private CompPlays compPlaying = new CompPlays();

	// Game g = null;
	// GUIGameBoard gb;
	MoveValue[] values = null;
	String previousValue = "win";
	int delay;
	Stack<Integer> previousMoves, nextMoves; // Stacks of previousMoves and nextMoves, which is used to undo and redo moves.


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); 
		this.setGameView(R.layout.othello_game);
		this.initResources();
		this.setBoard(4,4);
	}
	
	/**
	 * Initializes the GUI elements used for the tiles, etc.
	 */
	private void initResources(){
		Resources res = getResources();
		this.bluePiece = res.getDrawable(R.drawable.oth_bluepiece);
		this.redPiece = res.getDrawable(R.drawable.oth_redpiece);

		this.turnTextView = (TextView) findViewById(R.id.oth_turn); 
		this.turnImage = (ImageButton) findViewById(R.id.oth_turnImage);
		this.remoteTextView = (TextView) findViewById(R.id.oth_remoteness);
		this.gameOverTextView = (TextView) findViewById(R.id.oth_gameOver);	
	}
	
	/**
	 * Initializes the internal state and GUI of the Connect 4 board. 
	 * @param width The width of the board to construct.
	 * @param height The height of the board to construct.
	 */
	private void setBoard(int width, int height) { 
		
	}
	
	@Override
	public void doMove(String move) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBoardString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGameName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void redoMove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void undoMove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePredictionDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateValuesDisplay() {
		// TODO Auto-generated method stub

	}

}
