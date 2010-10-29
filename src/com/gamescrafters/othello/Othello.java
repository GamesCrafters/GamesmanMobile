package com.gamescrafters.othello;

import java.util.Random;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

import com.gamescrafters.othello.GUIGameBoard;
import com.gamescrafters.connect4.Connect4;
import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.os.Handler;
import android.os.SystemClock;

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
	private Drawable blackPiece, whitePiece;
	// private CompPlays compPlaying = new CompPlays();

	Game g = null;
	GUIGameBoard gb;
	MoveValue[] values = null;
	String previousValue = "win";
	int delay;
	int height,width;
	int moveDelay;
	Runnable c;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); 
		this.setGameView(R.layout.othello_game);
		

//		this.isPlayer2Computer = true;
		this.moveDelay = 1000;
		
		height = 4;
		width = height;
		this.initResources();
		
		this.isShowValues = true;
		g = new Game(height, width, this);
		if (this.gb == null)
			gb = new GUIGameBoard(this);
		else
			gb.reset(g);
		gb.initBoard();
		this.setBoard(width, height);
		c = new Runnable(){
			public void run() {
				doComputerMove();
			}	
		};
	}
	
	/**
	 * Initializes the GUI elements used for the tiles, etc.
	 */
	private void initResources(){
		Resources res = getResources();
		this.whitePiece = res.getDrawable(R.drawable.oth_whitepiece);
		this.blackPiece = res.getDrawable(R.drawable.oth_blackpiece);
		//this.bluePiece = res.getDrawable(R.drawable.oth_bluepiece);
		//this.redPiece = res.getDrawable(R.drawable.oth_redpiece);

		this.turnTextView = (TextView) findViewById(R.id.oth_turn); 
		this.turnImage = (ImageButton) findViewById(R.id.oth_turnImage);
		this.gameOverTextView = (TextView) findViewById(R.id.oth_gameOver);	
	}
	
	/**
	 * Initializes the internal state and GUI of the Othello board. 
	 * @param width The width of the board to construct.
	 * @param height The height of the board to construct.
	 */
	private void setBoard(int width, int height) { 
		
	}
	
	void skip(){
		g.swapMove();
	}
	
	@Override
	public void doMove(String move) {
		if(!move.equals("P")){
			int col = move.charAt(0) - 'a';
			int row = move.charAt(1) - '1';
			g.doMove((this.height - row), col+1, false, true);
		}else{
			g.swapMove();
		}
	}
	
	@Override
	public void doComputerMove() {
		MoveValue values[] = getNextMoveValues();
		if(values.length > 0){
			
			MoveValue bestMove = values[0];
			Random gen = new Random();
			for (MoveValue val : values) {
				String bestMoveValue = bestMove.getValue();
				String valValue = val.getValue();
				int valRemoteness = val.getRemoteness();
				int bestMoveRemoteness = bestMove.getRemoteness();
				if (valValue.equals("lose")) {
					if (((bestMoveValue.equals("lose")) && 
							(valRemoteness < bestMoveRemoteness))
							|| (!bestMoveValue.equals("lose"))) {
						bestMove = val;
					} else if (bestMoveValue.equals("lose") && (valRemoteness == bestMoveRemoteness)) {
						double randomnum = gen.nextDouble();
						if (randomnum >= 0.5) {
							bestMove = val;
						}
					}
				} else if (valValue.equals("tie")) {
					if ((bestMoveValue.equals("tie")) && (valRemoteness > bestMoveRemoteness)
							|| (bestMoveValue.equals("lose"))) {
						bestMove = val;
					} else if (bestMoveValue.equals("tie") && (valRemoteness == bestMoveRemoteness)) {
						double randomnum = gen.nextDouble();
						if (randomnum >= 0.5) {
							bestMove = val;
						}
					}
				} else if ((bestMoveValue.equals("win")) && (valRemoteness > bestMoveRemoteness)) {
					bestMove = val;
				} else if (bestMoveValue.equals("win") && (valRemoteness == bestMoveRemoteness)) {
					double randomnum = gen.nextDouble();
					if (randomnum >= 0.5) {
						bestMove = val;
					}
				}
			}
			doMove(bestMove.getMove());
		}
	}

	@Override
	public String getBoardString() {
		int[][] boardRep = g.board;
		StringBuffer board = new StringBuffer();
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				int elem = boardRep[i][j];
				switch(elem){
				case Game.BLACK:
					board.append("B");
					break;
				case Game.WHITE:
					board.append("W");
					break;
				default:
					board.append("_");
					break;
				}
			}
		}
		board.append(";player=");
		board.append((this.g.getTurn() == this.g.BLACK) ? "1" : "2");
		//board.append(";width=");
		//board.append(width);
		//board.append(";height=");
		//board.append(height);
		board.append(";option=136");
		
		// Why are these here?
		//board.append(";");
		//board.append("pieces=4");
		
		return board.toString();
	}

	@Override
	public String getGameName() {
		return this.GAME_NAME;
	}

	@Override
	public void newGame() {
		g = new Game(this.height, this.width, this);
		gb.reset(g);
		gb.initBoard();
	}

	@Override
	public void redoMove() {
		g.redoMove();
	}

	@Override
	public void undoMove() {
		g.undoMove();
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
	public class Game {
		final static boolean BLUE_TURN = false;
		final static boolean RED_TURN = true;
		final static int BLACK = 1;
		final static int WHITE = 2;
		final static int EMPTY = 0;
		
		final static int	UP = 1<<0,
							DOWN = 1<<1,
							LEFT = 1<<2,
							RIGHT = 1<<3;
		

		private int turn = BLACK; // first player,black
		int board[][]; 	// The internal state of the game. Either BLACK, WHITE, or EMPTY.
		int height, width;
		boolean gameOver = false;
		private int movesSoFar;
		private int currentMove;
		private Stack<int [][]> previousMoves, nextMoves; // Stacks of previousMoves and nextMoves, which is used to undo and redo moves.
		GUIGameBoard.TileView tiles[][];
		MoveValue[] values;
		String previousValue;
		Othello parent;
		Handler h;
		TextView turnText;
		ImageButton turnImage;
		GUIGameBoard.LevelsView levels;
		
		public Game(int height, int width, Othello parent) {
			this.parent = parent;
			this.height = height;
			this.width = width;
			this.board = new int[height][width];
			this.tiles = new GUIGameBoard.TileView[height][width];
			this.previousMoves = new Stack<int [][]>();
			this.movesSoFar = 0;
			this.currentMove = 0;
			this.h = new Handler();
			
			this.nextMoves = new Stack<int [][]>();
			this.board[height/2][width/2] = BLACK;
			this.board[height/2-1][width/2] = WHITE;
			this.board[height/2][width/2-1] = WHITE;
			this.board[height/2-1][width/2-1] = BLACK;
			turnImage = (ImageButton)this.parent.findViewById(R.id.oth_turnImage);
			turnText = (TextView)this.parent.findViewById(R.id.oth_turn);
			turnText.setText("Player: ");
			turnImage.setBackgroundDrawable(this.parent.blackPiece);
			turnImage.setEnabled(false);
			

			
		}
		
		public int getTurn() {
			return turn;
		}

		public int getMovesSoFar() {
			return movesSoFar;
		}

		public int getCurrentMove() {
			return currentMove;
		}

		public boolean isBlackTurn(){
			return (turn == BLACK);
		}

		public boolean moveValid(int row, int column) {
			if(this.board[row-1][column-1] == EMPTY){
				if(		!checkTraverse(row, column, UP | LEFT) &&
						!checkTraverse(row, column, UP) &&
						!checkTraverse(row, column, UP | RIGHT) &&
						!checkTraverse(row, column, LEFT) &&
						!checkTraverse(row, column, RIGHT) &&
						!checkTraverse(row, column, DOWN | LEFT) &&
						!checkTraverse(row, column, DOWN) &&
						!checkTraverse(row, column, DOWN | RIGHT))
					return false;			
		
				return true;
			}
			return false;
		}

		private void swapMove(){
			if(turn == BLACK)
				turn = WHITE;
			else
				turn = BLACK;
			turnImage.setBackgroundDrawable((turn == BLACK) ? this.parent.blackPiece : this.parent.whitePiece);
			
		}
		
		private int[] countUp(){
			int[] retval = new int[2];
			for(int i = 0; i < this.height; i++){
				for(int j = 0; j < this.width; j++){
					if(this.board[i][j] != this.EMPTY)
						retval[this.board[i][j] - 1]++;
				}
			}
			return retval;
		}

		public void redoMove() {
			if(this.nextMoves.isEmpty()) return;
			this.previousMoves.push(this.copyBoard());
			board = this.nextMoves.pop();
			this.currentMove++;
			hSlider.updateProgress(currentMove, movesSoFar);
			swapMove();
			drawCurrentBoard();
			Thread server = new Thread(new Runnable(){
				public void run() {
					values = getNextMoveValues();
					if((values != null) && (values.length > 0)){
						previousValue = getBoardValue(values);
						int remoteness = getRemoteness(previousValue, values);
						updateVVH(previousValue, remoteness, false, isBlackTurn(), false);
					}
					updatePreviews(true);					
				}
			});
			server.start();
			
		}

		/**
		 * Undoes a move. If no previous moves, does nothing.
		 */
		public void undoMove() {
			if (this.previousMoves.isEmpty()) return;
			this.nextMoves.push(copyBoard());
			board = this.previousMoves.pop();
			//int[][] col = this.previousMoves.pop();
			// Remove the last move from the VisualValueHistory.
			removeLastVVHNode();
			if(g.gameOver){
				g.gameOver = false;
				turnTextView.setText("Turn: ");
				gameOverTextView.setText("");
			}
			this.currentMove--;
			hSlider.updateProgress(currentMove, movesSoFar);
			swapMove();
			drawCurrentBoard();
			Thread server = new Thread(new Runnable(){
				public void run() {
					values = getNextMoveValues();
					if((values != null) && (values.length > 0)){
						previousValue = getBoardValue(values);
						int remoteness = getRemoteness(previousValue, values);
						updateVVH(previousValue, remoteness, false, isBlackTurn(), false);
					}
					updatePreviews(true);					
				}
			});
			server.start();
		}

		public void doMove(int row, int column, boolean isRedo, boolean isComputer){
			this.previousMoves.push(copyBoard());
			this.nextMoves.empty();
			this.tiles[row-1][column-1].setColor((turn == BLACK) ? Color.BLACK : Color.WHITE);
			this.tiles[row-1][column-1].postInvalidate();
			traverseFlip(row, column, UP | LEFT);
			traverseFlip(row, column, UP);
			traverseFlip(row, column, UP | RIGHT);
			traverseFlip(row, column, LEFT);
			traverseFlip(row, column, RIGHT);
			traverseFlip(row, column, DOWN | LEFT);
			traverseFlip(row, column, DOWN);
			traverseFlip(row, column, DOWN | RIGHT);
			this.board[row-1][column-1] = turn;
			this.levels.updateBlacks(countUp()[BLACK-1]);
			this.levels.updateWhites(countUp()[WHITE-1]);
			this.levels.postInvalidate();
			swapMove();
			clearPreviews();
			Thread server = new Thread(new Runnable(){
				public void run() {
					values = getNextMoveValues();
					if((values != null) && (values.length > 0)){
						previousValue = getBoardValue(values);
						int remoteness = getRemoteness(previousValue, values);
						updateVVH(previousValue, remoteness, false, isBlackTurn(), false);
					}
					updatePreviews(true);					
				}
			});
			updatePreviews(false);
			server.start();
		
			currentMove++;
			movesSoFar = currentMove;
			hSlider.updateProgress(currentMove, movesSoFar);		
		}

		public int[][] copyBoard(){
			int[][] retval = new int[this.height][this.width];
			for(int i = 0; i < this.height; i++){
				for(int j = 0; j < this.width; j++){
					retval[i][j] = this.board[i][j];
				}
			}
			return retval;
		}

		public void drawCurrentBoard(){
			for(int i = 0; i < this.width; i++){
				for(int j = 0; j < this.height; j++){
					this.tiles[i][j].setColor((board[i][j] == EMPTY) ? Color.TRANSPARENT : 
						((board[i][j] == BLACK) ? Color.BLACK : Color.WHITE));
					this.tiles[i][j].invalidate();
				}
			}
			updatePreviews(false);
		}

		public void clearPreviews(){
			for(int i = 0; i < this.width; i++){
				for(int j = 0; j < this.height; j++){
					this.tiles[i][j].setSmallColor(Color.TRANSPARENT);
					this.tiles[i][j].postInvalidate();
				}
			}
		}
		public void updatePreviews(boolean serverColor){
			for(int i = 0; i < this.width; i++){
				for(int j = 0; j < this.height; j++){
					this.tiles[i][j].setSmallColor(previewColor(i+1, j+1, serverColor));
					this.tiles[i][j].postInvalidate();
				}
			}
		}

		/**
		 * Gets the color of the specified empty space, MAGENTA if show values are not enabled, and RED/YELLOW/RED otherwise. 
		 * @param row The specified row
		 * @param column The specified column
		 * @return The appropriate color for the preview dot.
		 */
		public int previewColor(int row, int column, boolean serverColor){
			char colCh = (char)('a' + (column-1));
			char rowCh = (char)('1' + (this.height - row));
			String current;
			StringBuilder c = new StringBuilder();
			c.append(colCh);
			c.append(rowCh);
			current = c.toString();
			if(this.moveValid(row, column)){
				if(this.parent.isShowValues() && serverColor){
					if(values == null)
						values = getNextMoveValues();
					for(MoveValue val : values){
						String move = val.getMove();
						if(move.equalsIgnoreCase(current)){
							String color = val.getValue();
							if(color.equals("lose")){
								return Color.GREEN;
							}else if(color.equals("win")){
								return Color.RED;
							}else if(color.equals("tie")){
								return Color.YELLOW;
							}
						}
					}
					return Color.MAGENTA;
				}else{
					return Color.MAGENTA;
				}
			}
			return Color.TRANSPARENT;
		}

//		int coordToMove(int row, int col){
//			return ((row - 1) * this.width + col - 1);
//		}
		
		/**
		 * Flip tiles in the appropriate direction after traversing a row.
		 * @param row The current row of the new tile 
		 * @param column The current column of the new tile
		 * @param direction The direction of traversal (Determines flip direction)
		 */
		private void traverseFlip(int row, int column, int direction){
			flipTiles(traverse(row, column, direction), direction);
		}
		/**
		 * Flips the tiles discovered during the traversal
		 * @param toFlip The queue of coordinates x,y,x,y,x,y,... to flip
		 * @param direction The direction of traversal (Determines flip direction)
		 */
		private void flipTiles(Queue<Integer> toFlip, int direction){
			Integer currentX, currentY;
			int aniSpeed = 1000;
			int del = 0;
			while((currentX = toFlip.poll()) != null && (currentY = toFlip.poll()) != null){
				if(turn == BLACK){
					this.board[currentX][currentY] = BLACK;
				}else{
					this.board[currentX][currentY] = WHITE;
				}
				if((direction & UP) != 0){
					if((direction & LEFT) != 0)
						this.h.postDelayed(this.tiles[currentX][currentY].dF1, del);
						//this.tiles[currentX][currentY].flipDiagonal1(aniSpeed);
					else if((direction & RIGHT) != 0)
						this.h.postDelayed(this.tiles[currentX][currentY].dF2, del);
						//this.tiles[currentX][currentY].flipDiagonal2(aniSpeed);
					else
						this.h.postDelayed(this.tiles[currentX][currentY].v, del);
						//this.tiles[currentX][currentY].flipVertical(aniSpeed);
				}else if((direction & DOWN) != 0){
					if((direction & LEFT) != 0)
						//this.h.postAtTime(this.tiles[currentX][currentY].dF2, uptimeMillis() + del);
						this.h.postDelayed(this.tiles[currentX][currentY].dF2, del);
						//this.tiles[currentX][currentY].flipDiagonal2(aniSpeed);						
					else if((direction & RIGHT) != 0)
						this.h.postDelayed(this.tiles[currentX][currentY].dF1, del);
						//this.tiles[currentX][currentY].flipDiagonal1(aniSpeed);
					else
						this.h.postDelayed(this.tiles[currentX][currentY].v, del);
						//this.tiles[currentX][currentY].flipVertical(aniSpeed);
				}else if((direction & LEFT) != 0 || ((direction & RIGHT) != 0)){
					this.h.postDelayed(this.tiles[currentX][currentY].h, del);
					//this.tiles[currentX][currentY].flipHorizontal(aniSpeed);
				}
				del += (aniSpeed/2);
			}
		}

		/**
		 * Simply checks if there are any tiles to flip in the specified direction.
		 * @param row The current row of the new tile
		 * @param column The current column of the new tile
		 * @param direction The direction of traversal
		 * @return whether or not there are any tiles to flip
		 */
		private boolean checkTraverse(int row, int column, int direction){
			if(traverse(row, column, direction).isEmpty()){
				return false;
			}
			return true;
		}
		
		/**
		 * Goes through from a new tile in a certain direction to find which tiles to flip.
		 * @param row The row of the new tile
		 * @param column The column of the new tile
		 * @param direction The direction of traversal (there are eight)
		 * @return A Queue of tile coordinates to be flipped.
		 */
		private Queue<Integer> traverse(int row, int column, int direction){
			Queue<Integer> toFlip=new LinkedList<Integer>();
			int currentRow = row-1, currentColumn = column-1;
			while(true){
				if((direction & UP) != 0)
					currentRow--;
				if((direction & DOWN) != 0)
					currentRow++;
				if((direction & LEFT) != 0)
					currentColumn--;
				if((direction & RIGHT) != 0)
					currentColumn++;
				if(	(currentColumn < 0 || currentRow < 0) ||
					(currentColumn >= this.width || currentRow >= this.height) ||
					board[currentRow][currentColumn] == 0){
					toFlip.clear();
					return toFlip;
				}
				if(this.turn == BLACK){
					if(board[currentRow][currentColumn] == BLACK){
						break;
					}else if(board[currentRow][currentColumn] == WHITE){
						toFlip.add(currentRow);
						toFlip.add(currentColumn);
						continue;
					}
				}else if(this.turn == WHITE){
					if(board[currentRow][currentColumn] == WHITE){
						break;
					}else if(board[currentRow][currentColumn] == BLACK){						
						toFlip.add(currentRow);
						toFlip.add(currentColumn);
						continue;
					}
				}
			}
			
			return toFlip;
		}
	}
}
