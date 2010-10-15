package com.gamescrafters.othello;

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
	int height,width;
	

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); 
		this.setGameView(R.layout.othello_game);
		
		height = 4;
		width = height;
		
		g = new Game(height, width, this);
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
		int[][] boardRep = g.board;
		StringBuffer board = new StringBuffer();
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				int elem = boardRep[i][j];
				switch(elem){
				case Game.BLACK:
					board.append("O");
					break;
				case Game.WHITE:
					board.append("X");
					break;
				default:
					board.append("%20");
					break;
				}
			}
		}
		board.append(";width=");
		board.append(width);
		board.append(";height=");
		board.append(height);
		
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
		Othello parent;
		
		public Game(int height, int width, Othello parent) {
			this.parent = parent;
			this.height = height;
			this.width = width;
			this.board = new int[height][width];
			this.tiles = new GUIGameBoard.TileView[height][width];
			this.previousMoves = new Stack<int [][]>();
			this.movesSoFar = 0;
			this.currentMove = 0;
			
			this.nextMoves = new Stack<int [][]>();
			this.board[height/2][width/2] = BLACK;
			this.board[height/2-1][width/2] = WHITE;
			this.board[height/2][width/2-1] = WHITE;
			this.board[height/2-1][width/2-1] = BLACK;
			
		}
		
		public void redoMove() {
			if(this.nextMoves.isEmpty()) return;
			this.previousMoves.push(this.copyBoard());
			board = this.nextMoves.pop();
			this.currentMove++;
			hSlider.updateProgress(currentMove, movesSoFar);
			swapMove();
			drawCurrentBoard();
			
		}

		int coordToMove(int row, int col){
			return ((row - 1) * this.width + col - 1);
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
				//turnImage.setBackgroundDrawable(g.getTurn() == BLACK ? bluePiece : redPiece);
				gameOverTextView.setText("");
			}
			this.currentMove--;
			hSlider.updateProgress(currentMove, movesSoFar);
			swapMove();
			drawCurrentBoard();
		}
		public void drawCurrentBoard(){
			for(int i = 0; i < this.width; i++){
				for(int j = 0; j < this.height; j++){
					this.tiles[i][j].setColor(
							(board[i][j] == EMPTY) ? Color.TRANSPARENT : ((board[i][j] == BLACK) ? Color.BLACK : Color.WHITE));
					this.tiles[i][j].invalidate();
				}
			}
			updatePreviews();
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
		public int[][] copyBoard(){
			int[][] retval = new int[this.height][this.width];
			for(int i = 0; i < this.height; i++){
				for(int j = 0; j < this.width; j++){
					retval[i][j] = this.board[i][j];
				}
			}
			return retval;
		}
		private void swapMove(){
			if(turn == BLACK)
				turn = WHITE;
			else
				turn = BLACK;
		}
		public void doMove(int row, int column, boolean isRedo){
			this.previousMoves.push(copyBoard());
			this.nextMoves.empty();
			this.tiles[row-1][column-1].setColor((turn == BLACK) ? Color.BLACK : Color.WHITE);
			this.tiles[row-1][column-1].invalidate();
			traverseFlip(row, column, UP | LEFT);
			traverseFlip(row, column, UP);
			traverseFlip(row, column, UP | RIGHT);
			traverseFlip(row, column, LEFT);
			traverseFlip(row, column, RIGHT);
			traverseFlip(row, column, DOWN | LEFT);
			traverseFlip(row, column, DOWN);
			traverseFlip(row, column, DOWN | RIGHT);
			this.board[row-1][column-1] = turn;
			swapMove();
			this.currentMove++;
			this.movesSoFar = this.currentMove;
			hSlider.updateProgress(currentMove, movesSoFar);
			updatePreviews();
		}
		public boolean isBlackTurn(){
			return (turn == BLACK);
		}

		public void updatePreviews(){
			for(int i = 0; i < this.width; i++){
				for(int j = 0; j < this.height; j++){
					this.tiles[i][j].setSmallColor(previewColor(i+1, j+1));
					this.tiles[i][j].invalidate();
				}
			}
		}
		public int previewColor(int row, int column){
			if(this.moveValid(row, column)){
				if(this.parent.isShowValues()){
					
				}else{
					return Color.MAGENTA;
				}
			}
			return Color.TRANSPARENT;
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
		
		private void traverseFlip(int row, int column, int direction){
			flipTiles(traverse(true, row, column, direction), direction);
		}
		private void flipTiles(Queue<Integer> toFlip, int direction){
			Integer currentX, currentY;
			int aniSpeed = 500;
			while((currentX = toFlip.poll()) != null && (currentY = toFlip.poll()) != null){
				if(turn == BLACK){
					this.board[currentX][currentY] = BLACK;
				}else{
					this.board[currentX][currentY] = WHITE;
				}
				if((direction & UP) != 0){
					if((direction & LEFT) != 0)
						this.tiles[currentX][currentY].flipDiagonal1(aniSpeed);						
					else if((direction & RIGHT) != 0)
						this.tiles[currentX][currentY].flipDiagonal2(aniSpeed);
					else
						this.tiles[currentX][currentY].flipVertical(aniSpeed);
				}else if((direction & DOWN) != 0){
					if((direction & LEFT) != 0)
						this.tiles[currentX][currentY].flipDiagonal2(aniSpeed);						
					else if((direction & RIGHT) != 0)
						this.tiles[currentX][currentY].flipDiagonal1(aniSpeed);
					else
						this.tiles[currentX][currentY].flipVertical(aniSpeed);
				}else if((direction & LEFT) != 0 || ((direction & RIGHT) != 0)){
					this.tiles[currentX][currentY].flipHorizontal(aniSpeed);
				}
			}
		}
		
		private boolean checkTraverse(int row, int column, int direction){
			if(traverse(false, row, column, direction).isEmpty()){
				return false;
			}
			return true;
		}
		private Queue<Integer> traverse(boolean flip, int row, int column, int direction){
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
