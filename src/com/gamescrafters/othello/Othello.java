package com.gamescrafters.othello;

import java.util.Stack;

// import com.gamescrafters.connect4.GUIGameBoard;
import com.gamescrafters.othello.GUIGameBoard;
import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * The Othello game activity handles the setup of the GUI and internal state of the Othello game.
 * It communicates with the GameValueService to get move values, the GUIGameBoard to handle the board GUI,
 * and will extend GameActivity to interact with the GameController / main frame (and through it, the VVH).
 * @version 0.1 (11 September 2010)
 * @author Zach Bush
 * @author Royce Cheng-Yue
 * @author Alex Degtiar
 * @author Gayane Vardoyan
 */
public class Othello extends GameActivity {
	static final String GAME_NAME = "othello";

	private TextView turnTextView, remoteTextView, gameOverTextView;
	private ImageButton turnImage;
	private Drawable bluePiece, redPiece;
	// private CompPlays compPlaying = new CompPlays();

	Game g = null;
	GUIGameBoard gb;
	MoveValue[] values = null;
	String previousValue = "win";
	int delay;
	


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); 
		this.setGameView(R.layout.othello_game);
		
		int height = 8;
		int width = 8;
		g = new Game(height, width);
		if (this.gb == null)
			gb = new GUIGameBoard(this);
		else
			gb.reset(g);
		gb.initBoard();
		this.initResources();
		this.setBoard(width, height);
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
	 * Initializes the internal state and GUI of the Othello board. 
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
		return this.GAME_NAME;
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
		g.undoMove();
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
	
	/**
	 * A class that contains the internal state of the Connect 4 game,
	 * as well as accessor and modifier methods.
	 */
	class Game {
		final static boolean BLUE_TURN = false;
		final static boolean RED_TURN = true;
		final static int RED = 1;
		final static int BLUE = 2;
		final static int EMPTY = 0;

		private int turn = BLUE; // first player, blue
		int board[][]; 	// The internal state of the game. Either RED, BLUE, or EMPTY.
		int height, width;
		boolean gameOver = false;
		private int movesSoFar;
		private int currentMove;
		private Stack<int [][]> previousMoves, nextMoves; // Stacks of previousMoves and nextMoves, which is used to undo and redo moves.
		
		public Game(int height, int width) {
			this.height = height;
			this.width = width;
			this.board = new int[height][width];
			this.previousMoves = new Stack<int [][]>();
			this.movesSoFar = 0;
			this.currentMove = 0;
			
			this.nextMoves = new Stack<int [][]>();
			
		}
		
		int coordToMove(int row, int col){
			return ((row - 1) * this.width + col - 1);
		}
		
		/**
		 * Undoes a move. If no previous moves, does nothing.
		 */
		public void undoMove() {
			if (this.previousMoves.isEmpty()) return;
			this.nextMoves.push(board);
			board = this.previousMoves.pop();
			int[][] col = this.previousMoves.pop();
			// Remove the last move from the VisualValueHistory.
			removeLastVVHNode();
			if(g.gameOver){
				g.gameOver = false;
				turnTextView.setText("Turn: ");
				turnImage.setBackgroundDrawable(g.getTurn() == BLUE ? bluePiece : redPiece);
				gameOverTextView.setText("");
			}
			this.currentMove--;
			hSlider.updateProgress(currentMove, movesSoFar);
		}
		public int getMovesSoFar() {
			return movesSoFar;
		}

		public int getCurrentMove() {
			return currentMove;
		}
		public int getTurn() {
			return turn;
		}
		public void doMove(int move, boolean isRedo){
			
		}
		public boolean isBlueTurn(){
			return turn == BLUE;
		}
	}
}
