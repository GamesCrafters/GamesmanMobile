package com.gamescrafters.tictactoe;

import java.util.Random;
import java.util.Stack;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;
import com.gamescrafters.gamesmanmobile.RemoteGameValueService;

public class TicTacToe extends GameActivity {
	static final String GAME_NAME = "ttt";

	private TextView turnTextView, remoteTextView, gameOverTextView;
	private ImageButton turnImage;
	private Drawable bluePiece, redPiece;
	private CompPlays compPlaying = new CompPlays();

	Game g = null;
	GUIGameBoard gb;
	MoveValue[] values = null;
	String previousValue = "win";
	int delay;
	Stack<int []> previousMoves;
	Stack <Integer> nextMoves; // Stacks of previousMoves and nextMoves, which is used to undo and redo moves.


	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setGameView(R.layout.tictactoe_game);

		//Retrieve data passed from previous activity.
		Intent myIntent = GameIntent = getIntent();
		isPlayer1Computer = myIntent.getBooleanExtra("isPlayer1Computer", false);
		isPlayer2Computer = myIntent.getBooleanExtra("isPlayer2Computer", false);
		int height = myIntent.getIntExtra("numRows", 3);
		int width = myIntent.getIntExtra("numCols", 3);
		delay = myIntent.getIntExtra("numDelay", 1);

		initResources();

		setBoard(width, height);
	}
	
	class CompPlays extends Handler {
	
		public void handleMessage(Message msg) {
			TicTacToe.this.updateUI();
		}
		
		public void sleep(long delay) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delay);
		}
	};
	
	private void updateUI() {
		if (!g.gameOver) {
			compPlaying.sleep(delay*1000);
			doComputerMove();
		}
	}
	
	@Override
	public void newGame() {
		setBoard(g.width, g.height);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{// TODO fix!!! very inefficient!
		super.onRestoreInstanceState(savedInstanceState);
		TicTacToe old = (TicTacToe) getLastNonConfigurationInstance();
		isShowValues = old.isShowValues;
		isShowPrediction = old.isShowPrediction;
		moveValues = old.moveValues;
		for (int [ ] m : old.previousMoves) {
			g.doMove(m[0], m[1], false);
		}
		nextMoves = old.nextMoves;
		/*isPlayer1Computer = old.isPlayer1Computer;
		isPlayer2Computer = old.isPlayer2Computer;
		table = old.table;
		turnTextView.setText(old.turnTextView.getText());
		remoteTextView.setText(old.remoteTextView.getText());
		gameOverTextView.setText(old.gameOverTextView.getText());
		turnImage.setImageDrawable(old.turnImage.getDrawable());
		g = old.g;
		values = old.values;
		previousValue = old.previousValue;
		gb = old.gb;
		initResources();
		previousMoves = old.previousMoves;
		nextMoves = old.nextMoves;
		moveValues = old.moveValues;
		*/
	}

	/**
	 * Initializes the GUI elements used for the tiles, etc.
	 */
	private void initResources(){
		Resources res = getResources();
		//bluePiece = res.getDrawable(R.drawable.c4_bluepiece);
		bluePiece = res.getDrawable(R.drawable.ttt_bluex);
		redPiece = res.getDrawable(R.drawable.ttt_circlered);

		turnTextView = (TextView) findViewById(R.id.c4_turn); 
		turnImage = (ImageButton) findViewById(R.id.c4_turnImage);
		remoteTextView = (TextView) findViewById(R.id.c4_remoteness);
		gameOverTextView = (TextView) findViewById(R.id.c4_gameOver);	
	}

	/**
	 * Initializes the internal state and GUI of the Connect 4 board. 
	 * @param width The width of the board to construct.
	 * @param height The height of the board to construct.
	 */
	private void setBoard(int width, int height) { 
		g = new Game(height, width);
		
		if (gb == null)
			gb = new GUIGameBoard(this);
		else
			gb.reset(g);
		
		gb.initBoard();

		turnTextView.setText("Turn: ");
		turnImage.setBackgroundDrawable(bluePiece);
		turnImage.setEnabled(false);
		
		gameOverTextView.setText("");

		if (getLastNonConfigurationInstance() == null) {
			//values = getMoveValue();
			 values = getNextMoveValues();
		}
		
		g.updateRemoteness();
		g.updateValues();
		
		if (values != null && values.length != 0) {
			isNetworkAvailable = true;
			previousValue = getBoardValue(values);
			int remoteness = getRemoteness(previousValue, values);
			clearVVH();
			updateVVH(previousValue, remoteness, g.gameOver, g.isBlueTurn(), g.isTie());
		}
	//	g.updateVVH();

		
		//computer vs. computer
		if (isPlayer1Computer && isPlayer2Computer) {
			updateUI();
		} 
		else if (isPlayer1Computer) {
			doComputerMove();
		}
		
	} 

	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}

	/**
	 * @return The name of the game, as used in the online db.
	 */
	@Override
	public String getGameName() {
		return GAME_NAME;
	}
	
	@Override
	public int getNumMovesSoFar() {
		return g.getNumMovesSoFar();
	}
	
	@Override
	public int getCurrentMove() {
		return g.currentMove;
	}

	/**
	 * @return a string representing the current board.
	 */
	@Override
	public String getBoardString() {
		int[][] boardRep = g.board;
		StringBuffer board = new StringBuffer();
		
		for (int j = 0; j < 3; j++) { 
			for (int i = 0; i < 3; i++) { 
				int currentElem = boardRep [i][j];
				if (currentElem == TicTacToe.Game.RED) {
					board.append("O");
				} 
				else if (currentElem == TicTacToe.Game.BLUE) {
					board.append("X");
				} 
				else {
					board.append("%20");
				}
			}
		}
		
		board.append(";");
		board.append("width=3;height=3;pieces=3");
		return board.toString();
		
		/* URL we're trying to use:
		 * http://nyc.cs.berkeley.edu:8080/gcweb/service/gamesman/puzzles/ttt/getNextMoveValues;board=%20%20%20%20%20%20%20%20X;width=3;height=3;pieces=3
		 * Untested	
		 */
	}

	/**
	 * Given a String move in the database move representation, does the move.
	 */
	@Override
	public void doMove(String move) {
		char col = move.charAt(0);
		char temp = move.charAt(1);
		String rowString = temp + "";
		int row = Integer.parseInt(rowString);
		
		if (col == 'A') {
			col = 0;
		}
		else if (col == 'B') {
			col = 1;
		}
		else {
			col = 2;
		}
		
		row = row - 1;
		/*col = (mov - 1) % 3;
		row = (mov - 1) / 3;*/
			
		g.doMove(col, row, false);
		// g.doMove(Integer.parseInt(move), 0, false);
	}	

	/**
	 * Updates whether or not the values will display on the game GUI.
	 * It is called when a user changes the "Show Values" option. 
	 */
	@Override
	public void updateValuesDisplay() {
		g.updateValues();
	}

	/**
	 * Updates whether or not the prediction will display on the game GUI.
	 * It is called when a user changes the "Show Prediction" option.
	 */
	@Override
	public void updatePredictionDisplay() {
		g.updateRemoteness();
	}
	
	/**
	 * Redo a move. If no next moves, does nothing.
	 */
	@Override
	public void redoMove() {
		if (isPlayer1Computer && isPlayer2Computer) {
			redoHelper();
		}
		else if (isPlayer2Computer || isPlayer1Computer) {
			redoHelper();
			redoHelper();
		}
		else {
			redoHelper();
		}
	}
	
	private void redoHelper() {
		if (!nextMoves.isEmpty()) {
			int col = nextMoves.pop();
			g.doMove(col, 0, true);
		}
	}

	@Override
	public void undoMove() {
		if (isPlayer1Computer && isPlayer2Computer) {
			g.undoMove();
		}
		else if (isPlayer2Computer) {
			g.undoMove();
			g.undoMove();
		}
		else if (isPlayer1Computer) {
			if (previousMoves.size() != 1) {
				g.undoMove();
				g.undoMove();
			}
		}
		else {
			g.undoMove();
		}
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

		boolean turn = BLUE_TURN; // first player, blue
		int board[][]; 	// The internal state of the game. Either RED, BLUE, or EMPTY.
		int height, width;
		boolean gameOver = false;
		int movesSoFar;
		int currentMove;

		public Game(int height, int width) {
			this.height = height;
			this.width = width;
			board = new int[height][width];
			previousMoves = new Stack< int []>();
			movesSoFar = 0;
			currentMove = 0;
			
			nextMoves = new Stack<Integer>();
			
		}

		/**
		 * Undoes a move. If no previous moves, does nothing.
		 */
		public void undoMove() {
			if (previousMoves.isEmpty()) return;
			int []position = previousMoves.pop();
			int col = position[0];
			int row = position[1];
			
			// Remove the last move from the VisualValueHistory.
			removeLastVVHNode();
			
			if (g.board[col][row] != EMPTY) { // find first available piece in col to remove
				if (g.gameOver) { // primitive position undo
					g.gameOver = false;
					turnTextView.setText("Turn: ");
					turnImage.setBackgroundDrawable(g.getTurn() == BLUE ? bluePiece : redPiece);
					gameOverTextView.setText("");
				}
				board[col][row] = EMPTY;
				gb.updateTile(EMPTY, row, col);
				values = getNextMoveValues();
				updateRemoteness();
				updateValues();
			}

			currentMove--;
			hSlider.updateProgress(currentMove, movesSoFar);
			switchTurn();
			nextMoves.push(col);
		
			/*
			// Remove the last move from the VisualValueHistory.
			removeLastVVHNode();
			for (int i = height - 1; i >= 0; i--) {
				if (g.board[i][col] != EMPTY) { // find first available piece in col to remove
					if (g.gameOver) { // primitive position undo
						g.gameOver = false;
						turnTextView.setText("Turn: ");
						turnImage.setBackgroundDrawable(g.getTurn() == BLUE ? bluePiece : redPiece);
						gameOverTextView.setText("");
					}
					board[col][row] = EMPTY;
					gb.updateTile(EMPTY, row, col);
					values = getNextMoveValues();
					updateRemoteness();
					updateValues();
					break;
				}
			}
			currentMove--;
			hSlider.updateProgress(currentMove, movesSoFar);
			switchTurn();
			nextMoves.push(col);
			*/
		}

//		public void goToMoveN(int N) {
//			if (0 <= N && N <= movesSoFar) {
//				while (N < currentMove) {
//					undoMove();
//				}
//				while (N > currentMove) {
//					redoMove();
//				}
//			}
//		}

		/**
		 * Checks if the move (column number) is valid, and performs the move if it is.
		 * Assume nextMoves stack is not empty.
		 * @param move The column number of the piece to drop.
		 * @param row 
		 * @param isRedo Boolean if move is a redo move (true) or not (false).
		 */

		

		public void doMove(int column, int row, boolean isRedo) {
			if (!(isFull(column, row) || gameOver)) {
				updateGameState(column, row); 

				switchTurn();
				values = getNextMoveValues();
				updateRemoteness();
				updateValues();
				if ((values != null) && (values.length != 0)) {
					previousValue = getBoardValue(values);
					int remoteness = getRemoteness(previousValue, values);
					TicTacToe.this.updateVVH(previousValue, remoteness, gameOver, isBlueTurn(), isTie());
				}
				//updateVVH();
				previousMoves.push(new int [ ] {column, row});
				currentMove++;
				if (!isRedo) {
					nextMoves.clear();
					movesSoFar = currentMove;
				}
				hSlider.updateProgress(currentMove, movesSoFar);
			}
		}



		/**
		 * Updates the GUI move values (the values instance variable needs to be up-to-date!).
		 */
		private void updateValues() {
			if ((values != null) && isShowValues()) {
				boolean has_value[] = new boolean[width];
				for (MoveValue val : values){ // set values of columns that have values
					int col = val.getIntMove();
					gb.setColumnValue(col, val.getValue());
					has_value[col] = true;
				}
				for (int col=0; col<width; col++) { // no fill for columns without values
					if (!has_value[col]) gb.setColumnValue(col, "noval");
				}
			} 
			else {
				for (int col = 0; col<width; col++) {
					gb.setColumnValue(col, "noval");
				}
			}
		}

		/**
		 * Updates the remoteness text field based on the "values" variable.
		 */
		private void updateRemoteness() {

			if (!isShowPrediction()) {
				remoteTextView.setText("");
			}
			else if ((values != null) && (values.length != 0)) {
				
				previousValue = getBoardValue(values);
				int remoteness = getRemoteness(previousValue, values);
				
				if (remoteness != -1)
					remoteTextView.setText(previousValue + " in " + (remoteness+1) + " moves");
				else
					remoteTextView.setText("Prediction not available.");
			} 
			else if (!isNetworkAvailable) {
				remoteTextView.setText("Prediction not available.");
			} 
			else {
				remoteTextView.setText((previousValue.equals("win") ? "lose" : "tie") + " in 0 moves");
			}
		}

//		private void updateVVH () {
//			VVHNode node;
//			if ((values != null) && (values.length != 0)) {
//				previousValue = getBoardValue(values);
//				
//				int remoteness = getRemoteness(previousValue, values);
//				if (remoteness == -1) return;
//				if (gameOver && isTie())
//				node = new VVHNode("gameover-tie", remoteness, !isBlueTurn());
//				else if (gameOver)
//					node = new VVHNode("gameover", remoteness, !isBlueTurn());
//				else node = new VVHNode(previousValue, remoteness, isBlueTurn());
//				VVHList.add(node);
//			}
//		}
	


		/**
		 * @param colnum The column number to check (0->width-1).
		 * @return true or false, whether a column is full.
		 */
		private boolean isFull(int col, int row) {
			return board[col][row] != EMPTY; 
		}

		/**
		 * @return Whether or not it is blue's turn.
		 */
		public boolean isBlueTurn() {
			return turn == BLUE_TURN;
		}

		/**
		 * @return Whether or not it is red's turn.
		 */
		public boolean isRedTurn() {
			return !isBlueTurn();
		}

		/**
		 * @return Returns the total moves made so far.
		 */
		public int getNumMovesSoFar() {
			return movesSoFar;
		}

		/**
		 * @return RED or BLUE, depending on whose turn it is.
		 */
		private int getTurn() {
			return isRedTurn() ? RED : BLUE;
		}

		/**
		 * @param row The row of the board to check.
		 * @param column The column of the board to check.
		 * @return The piece at that spot, RED, BLUE, or EMPTY.
		 */
		public int pieceAt(int row, int column) {
			return board[row][column];
		}

		/**
		 * Toggles the turn, and updates the picture for the turn field.
		 */
		private void switchTurn() {
			turn = !turn;
			if (!gameOver) turnImage.setBackgroundDrawable(isBlueTurn() ? bluePiece : redPiece);
		}

		/**
		 * Updates the gamestate given a column in which a piece was placed.
		 * @param col The column in which the piece was placed.
		 */
		private void updateGameState(int col, int row) { 
			
			if (isRedTurn()) {
				board[col][row] = RED;
				gb.updateTile(RED, row, col);
			}
			else if (isBlueTurn()){
				board[col][row] = BLUE;
				gb.updateTile(BLUE, row, col);
			}
			else {
				System.out.println("Error");
			}
			checkBoard(row, col);
		}

		/**
		 * Checks if there are N in a row pieces.
		 * @param row The row of the piece just placed.
		 * @param col The column of the piece just placed.
		 * @param N The number of pieces to check.
		 * @return Whether there were N in a row or not.
		 */
		private boolean isPrimitive(int row, int col) {
			int value = getTurn();
			int vertInRow = 1, horizInRow = 1, DRInRow = 1, DLInRow = 1;
		
			if (board[0][0] == value && board [1][0] == value && board [2][0] ==value)
				return true;
			else if (board[0][1] == value && board [1][1] == value && board [2][1] ==value)
				return true;
			else if (board[0][2] == value && board [1][2] == value && board [2][2] ==value)
				return true;
			else if (board[0][0] == value && board [0][1] == value && board [0][2] ==value)
				return true;
			else if (board[1][0] == value && board [1][1] == value && board [1][2] ==value)
				return true;
			else if (board[2][0] == value && board [2][1] == value && board [2][2] ==value)
				return true;
			else if (board[0][0] == value && board [1][1] == value && board [2][2] ==value)
				return true;
			else if (board[2][0] == value && board [1][1] == value && board [0][2] ==value)
				return true;
			else 
				return false;	
		}

		/**
		 * Checks the board for a possible primitive position, and modifies gameOver.
		 * @param row The row of the piece placed.
		 * @param col The column of the piece placed.
		 */
		private void checkBoard(int row, int col) {
			if (isPrimitive(row, col)) {
				turnTextView.setText("");
				turnImage.setBackgroundDrawable(null);
				gameOverTextView.setText("Game over.\n" + (isBlueTurn() ? "Blue" : "Red") + " wins!");
				gameOver = true;
				if ((values != null) && (values.length != 0)) {
					previousValue = getBoardValue(values);
					int remoteness = getRemoteness(previousValue, values);
					TicTacToe.this.updateVVH(previousValue, remoteness, gameOver, isBlueTurn(), isTie());
				}
			} else if(isTie()) {
				turnTextView.setText("");
				turnImage.setBackgroundDrawable(null);
				gameOverTextView.setText("Game over.\nIt's a draw!"); 
				gameOver = true; 
				if ((values != null) && (values.length != 0)) {
					previousValue = getBoardValue(values);
					int remoteness = getRemoteness(previousValue, values);
					TicTacToe.this.updateVVH(previousValue, remoteness, gameOver, isBlueTurn(), isTie());
				}
			} 
		}

		/**
		 * @return Whether or not the game is a tie.
		 */
		private boolean isTie() { 
			for(int j=0; j<3; j++) {
				for (int i=0;i<3;i++) {
					if (board [i][j] == EMPTY)
						return false;
				}
			}
			return true;
		}
	}

	@Override
	public boolean isMoveInvalid(int move) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void updateUIRandom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUISmart() {
		// TODO Auto-generated method stub
		
	}
	
} 