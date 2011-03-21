package com.gamescrafters.othello;

import java.util.Random;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import com.gamescrafters.othello.GUIGameBoard;
import com.gamescrafters.othello.GUIGameBoard.LevelsView;
import com.gamescrafters.othello.GUIGameBoard.PieceClickListener;
import com.gamescrafters.othello.GUIGameBoard.PositionView;
import com.gamescrafters.othello.GUIGameBoard.TileView;
import com.gamescrafters.othello.GUIGameBoard.TileView.TileAnimation;
import com.gamescrafters.othello.Othello.Game;
import com.gamescrafters.connect4.Connect4;
import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;
import android.content.res.Configuration;
import android.content.Intent;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;


/**
 * The Othello game activity handles the setup of the GUI and internal state of the Othello game.
 * It communicates with the GameValueService to get move values, the GUIGameBoard to handle the board GUI,
 * and will extend GameActivity to interact with the GameController / main frame (and through it, the VVH).
 * @version 0.1 (11 September 2010)
 * @version 1.0 (26 November 2010)
 * @author Zach Bush
 * @author Royce Cheng-Yue
 * @author Alex Degtiar
 * @author Gayane Vardoyan
 */
public class Othello2 extends GameActivity {
	static final String GAME_NAME = "othello";
	int height;
	int width;
	int moveDelay;
	static int animsRunning;
	
	Drawable blankPreview, losePreview, winPreview, tiePreview, emptyTile, blackTile, whiteTile;
	
	TileView tiles[][];
	int board[][];
	
	TableLayout gameTable;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); 
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setGameView(R.layout.othello2_game);
		
		Intent thisIntent = getIntent();
		String dim = thisIntent.getStringExtra("dimension");
		height = 4;
		if(dim.equals("4x4"))
			height = 4;
		else if(dim.equals("6x6"))
			height = 6;
		else if(dim.equals("8x8"))
			height = 8;
		width = height;
		//this.isPlayer2Computer = true;
		this.moveDelay = 1000;
		
		this.isPlayer1Computer = thisIntent.getBooleanExtra("isPlayer1Computer", this.isPlayer1Computer);
		this.isPlayer2Computer = thisIntent.getBooleanExtra("isPlayer2Computer", this.isPlayer2Computer);
		
	
		
		this.initResources();
		
		this.isShowValues = true;
		//if(this.g == null){
			//g = new Game(height, width, this);
		//}
		//if (this.gb == null){
			//gb = new GUIGameBoard(this);
			//gb.initBoard();
		//}
		
		this.board = new int[height][width];
		this.tiles = new TileView[height][width];

		
		gameTable = (TableLayout) this.findViewById(R.id.oth2_gameBoard);
		
		this.setBoard(width, height);
		/*
		c = new Runnable(){
			public void run() {
				if(gb.animsRunning <= 0){
					doComputerMove();
				}else{
					g.h.postDelayed(this, 100);
				}
			}	
		};
		
		swapMove = new Runnable(){
			public void run(){
				g.swapMove();
			}
		};
		
		checkSkip = new Runnable(){
			public void run(){
				g.checkSkip();
			}
		};
		
		updatePreviews = new Runnable(){
			public void run(){
				g.updatePreviews(false);
			}
		};
		*/
		/* TextView skip = (TextView)this.findViewById(R.id.oth_skipTurnText); 
		skip.setGravity(Gravity.RIGHT);
		skip.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Othello2.this.skipMove();
			}
		});
		*/
		
		//if(this.isPlayer1Computer)
			//g.h.post(c);

	}
	
	public int getID(int row, int col) {
		return 10*row + col;
	}
	
	/**
	 * Initializes the board for Othello.
	 */
	public void setBoard(int width, int height) {
		Window w = this.getWindow();
		Rect r = new Rect();
		w.getDecorView().getWindowVisibleDisplayFrame(r);
		int size = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
				this.getWindowManager().getDefaultDisplay().getWidth() :
					this.getWindowManager().getDefaultDisplay().getHeight() - r.top - /* this.findViewById(R.id.gm_undoButton).getHeight() */ - 15;
		int new_hei = (int)Math.floor((double)gameTable.getHeight() / (double)width);
		int new_wid = (int)Math.floor((double)size / (double)height);

//		ImageView bg = (ImageView)a.findViewById(R.id.oth_boardImage);
//		bg.setMaxHeight(size);
//		bg.setMaxWidth(size);
//		bg.setImageResource(R.drawable.oth_felt4);
//		bg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		//RelativeLayout obl = (RelativeLayout)a.findViewById(R.id.oth_boardLayout);
//		obl.removeView(table);
//		bl.addView(table,new_wid,new_hei);
		gameTable.getLayoutParams().height = size;
		//table.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, new_hei));
		//table.setMinimumHeight(new_hei);
		//this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, theSizeIWant));
		//table.setLayoutParams(TableLayout.LayoutParams(LayoutParams.FILL_PARENT, new_hei)));
//		table.setBackgroundResource(R.drawable.oth_felt4);

		for (int row=1; row<=height; row++) {
			TableRow tr = new TableRow(this);
			tr.setId(row+1024);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int col=1; col<=width; col++) {
				int c = Color.TRANSPARENT;
				if(this.board[row-1][col-1] == Othello.Game.BLACK)
					c = Color.BLACK;
				else if(this.board[row-1][col-1] == Othello.Game.WHITE)
					c = Color.WHITE;
				TileView tv = new TileView(this, row, col, c);
				//tv.setSmallColor(g.previewColor(row, col,false));
				//tv.setId(getID(row, col));
				tv.setOnClickListener(new PieceClickListener(col, row, tv));
				gameTable.findViewById(getID(row,col));
				PositionView pv = new PositionView(this);
				ImageView iv = new ImageView(this);
				iv.setImageResource(R.drawable.oth_felt);
				RelativeLayout rl = new RelativeLayout(this);
				rl.setId(getID(row,col));
				//rl.addView(iv);
				rl.addView(pv);
				rl.addView(tv);
				tr.addView(rl, new_wid, new_wid);
				this.tiles[row-1][col-1] = tv;
			}
			gameTable.addView(tr);
		}
		LevelsView lv = new LevelsView(this);
		//g.levels = lv;
		gameTable.addView(lv,size,15);
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			//updateOrient(Configuration.ORIENTATION_LANDSCAPE);
		}
		Thread previews = new Thread(){
			public void run(){
				//g.updatePreviews(true);
			}
		};
		previews.start();
	}
	
	/**
	 * An OnClickListener that is attached to the tile image elements.
	 * Calls doMove (or computerMove) on a click.
	 */
	class PieceClickListener implements View.OnClickListener {
		int column, row;
		TileView piece;
		
		public PieceClickListener(int col_num, int row_num, TileView piece) {
			column = col_num;
			row = row_num;
			this.piece = piece;
		}

		public void onClick(View v) {
			/* if (!(this.isPlayer1Computer && this.isPlayer2Computer)) {
				if(g.moveValid(row,column) && !g.gameOver){
					if(g.isBlackTurn()){
						g.doMove(row, column, false, false);
						if(a.isPlayer2Computer){
							g.clearPreviews();
							h.postDelayed(this.c, this.moveDelay);
						}
					}else{
						g.doMove(row, column, false, false);
						if(a.isPlayer1Computer){
							h.postDelayed(this.c, this.moveDelay);
						}
					}
				}
			} */
		}

	}
	private void initResources(){
		Resources res = getResources();
		if(this.blankPreview != null) return;
		this.blankPreview 	= 	res.getDrawable(R.drawable.oth_rec_scaled);
		this.losePreview 	= 	res.getDrawable(R.drawable.oth_lose_scaled);
		this.tiePreview 	= 	res.getDrawable(R.drawable.oth_tie_scaled);
		this.winPreview 	=	res.getDrawable(R.drawable.oth_win_scaled);
		this.emptyTile 		=	res.getDrawable(R.drawable.oth_empty);
		this.blackTile 		=  	res.getDrawable(R.drawable.oth_simpleblack);
		this.whiteTile 		=	res.getDrawable(R.drawable.oth_simplewhite);	
	}
	
	@Override
	public String getGameName() {
		return this.GAME_NAME;
	}
	
	@Override
	public boolean isMoveInvalid(int move) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void redoMove() {
		//g.redoMove();
	}

	@Override
	public void undoMove() {
		//g.undoMove();
	}

	public void skipMove(){
	}
	@Override
	public void updateUIRandom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUISmart() {
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
	
	@Override
	public void doMove(String move) {
		if(!move.equals("P")){
			int col = move.charAt(0) - 'a';
			int row = move.charAt(1) - '1';
			//g.doMove((this.height - row), col+1, false, true);
		}else{
			//g.skipMove();
		}
	}
	
	@Override
	public void newGame() {
		//g = new Game(this.height, this.width, this);
		//gb.reset(g);
		//gb.initBoard();
	}
	
	@Override
	public String getBoardString() {
		return "";
	/*	int[][] boardRep = g.board;
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
		if(height == width && height == 4){
			board.append(";option=136"); // this is 4x4, What are other board sizes?
		}else{
			board.append(";option=unknown");
		}
		return board.toString();
		*/
	}
	
	public class TileView extends ImageView{

		int x,y;
		int tColor;
		boolean small;
		int pColor;
		private Animation horizontalFlip, verticalFlip, horizontalOpen, verticalOpen, rotate45;
		private AnimationSet diagonalFlip1, openDiag1, diagonalFlip2, openDiag2;
		public Runnable dF1, dF2, h, v;
		public TileView(Context context, int x, int y, int c) {
			super(context);
			this.x = x;
			this.y = y;
			this.tColor = c;
			
			if(tColor == Color.BLACK){
				this.setImageDrawable(Othello2.this.blackTile);
			}else if(tColor == Color.WHITE){
				this.setImageDrawable(Othello2.this.whiteTile);
			}
			
			// Set Base animations
			rotate45 = new RotateAnimation(45f,45f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			horizontalFlip = new ScaleAnimation(1f, 0f, 1f, 1f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			verticalFlip = new ScaleAnimation(1f, 1f, 1f, 0f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			horizontalOpen = new ScaleAnimation(0f, 1f, 1f, 1f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			verticalOpen = new ScaleAnimation(1f, 1f, 0f, 1f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			
			float interpFactor = 1.0f; 
			
			// Set Interpolators
			horizontalFlip.setInterpolator(new AccelerateInterpolator(interpFactor));
			verticalFlip.setInterpolator(new AccelerateInterpolator(interpFactor));
			horizontalOpen.setInterpolator(new OvershootInterpolator(interpFactor));
			verticalOpen.setInterpolator(new OvershootInterpolator(interpFactor));
			
			// Prepare diagaonal Flips
			// flip 1
			diagonalFlip1 = new AnimationSet(false);
			diagonalFlip1.addAnimation(horizontalFlip);
			diagonalFlip1.addAnimation(rotate45);
			
			// Flip 1 open
			openDiag1 = new AnimationSet(false);
			openDiag1.addAnimation(horizontalOpen);
			openDiag1.addAnimation(rotate45);
			
			// Flip 2
			diagonalFlip2 = new AnimationSet(false);
			diagonalFlip2.addAnimation(verticalFlip);
			diagonalFlip2.addAnimation(rotate45);
			
			// Flip 2 open
			openDiag2 = new AnimationSet(false);
			openDiag2.addAnimation(verticalOpen);
			openDiag2.addAnimation(rotate45);
			
			// Set Interpolators
			diagonalFlip1.setInterpolator(new AccelerateInterpolator(interpFactor));
			diagonalFlip2.setInterpolator(new AccelerateInterpolator(interpFactor));
			openDiag1.setInterpolator(new OvershootInterpolator(interpFactor));
			openDiag2.setInterpolator(new OvershootInterpolator(interpFactor));
			
			final int delay = 500;
			
			this.dF1 = new Runnable(){
				public void run(){
					flipDiagonal1(delay);
				}
			};
			this.dF2 = new Runnable(){
				public void run(){
					flipDiagonal2(delay);
				}
			};
			this.h = new Runnable(){
				public void run(){
					flipHorizontal(delay);
				}
			};
			this.v = new Runnable(){
				public void run(){
					flipVertical(delay);
				}
			};
		}
		
		public void flipHorizontal(long time){
			horizontalFlip.setAnimationListener(new TileAnimation(this, horizontalOpen));
			horizontalFlip.setDuration(time/2);
			horizontalOpen.setDuration(time/2);
			this.startAnimation(horizontalFlip);
		}		
		
		public void flipVertical(long time){
			verticalFlip.setAnimationListener(new TileAnimation(this, verticalOpen));
			verticalFlip.setDuration(time/2);
			verticalOpen.setDuration(time/2);
			this.startAnimation(verticalFlip);
		}
		
		public void flipDiagonal1(long time){
			horizontalFlip.setAnimationListener(new TileAnimation(this, openDiag1));
			horizontalFlip.setDuration(time/2);
			horizontalOpen.setDuration(time/2);
			rotate45.setDuration(time/2);
			this.startAnimation(diagonalFlip1);			
		}
		
		public void flipDiagonal2(long time){
			verticalFlip.setAnimationListener(new TileAnimation(this, openDiag2));
			verticalFlip.setDuration(time/2);
			verticalOpen.setDuration(time/2);
			rotate45.setDuration(time/2);
			this.startAnimation(diagonalFlip2);			
		}
		
		public void swapColor(){
			if(this.tColor == Color.BLACK){
				this.tColor = Color.WHITE;
				this.setImageDrawable(Othello2.this.whiteTile);
				return;
			}
			this.tColor = Color.BLACK;
			this.setImageDrawable(Othello2.this.blackTile);
		}
		
		public void setColor(int c){
			this.tColor = c;
			if(tColor == Color.BLACK){
				this.setImageDrawable(Othello2.this.blackTile);
			}else{
				this.setImageDrawable(Othello2.this.whiteTile);
			}
		}
		
		public void setSmallColor(int c){
			switch(c){
			case Color.MAGENTA:
				this.setImageDrawable(Othello2.this.blankPreview);
			case Color.RED:
				this.setImageDrawable(Othello2.this.losePreview);
			case Color.YELLOW:
				this.setImageDrawable(Othello2.this.tiePreview);
			case Color.GREEN:
				this.setImageDrawable(Othello2.this.winPreview);
			default:
				this.setImageDrawable(Othello2.this.emptyTile);
			
			}
			this.small = true;
			this.pColor = c;
		}
		
		class TileAnimation implements Animation.AnimationListener{
			TileView parent;
			Animation nextAni;
			public TileAnimation(TileView t, Animation subsequent){
				this.parent = t;
				this.nextAni = subsequent;
			}
			
			public void onAnimationEnd(Animation animation) {
				this.parent.swapColor();
				this.nextAni.setAnimationListener(new Animation.AnimationListener() {					
					public void onAnimationStart(Animation animation) {	}				
					public void onAnimationRepeat(Animation animation) {}				
					public void onAnimationEnd(Animation animation) {
						animsRunning--;
					}
				});
				this.parent.startAnimation(this.nextAni);
			}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationStart(Animation animation) {
				animsRunning++;
			}
		}	
	}
	public class PositionView extends View{
		public PositionView(Context context) {
			super(context);
		}	
		@Override
		protected void onDraw(Canvas canvas){
			super .onDraw(canvas);
			Paint p = new Paint();
			p.setColor(Color.TRANSPARENT);
			p.setStyle(Paint.Style.FILL);
			canvas.drawPaint(p);
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeWidth(1);
			p.setColor(Color.BLACK);
			canvas.drawLine(0, 0, getWidth(), 0, p);
			canvas.drawLine(0, 0, 0, getHeight(), p);
			canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), p);
			canvas.drawLine(0, getHeight(), getWidth(), getHeight(), p);
		}
	}
	public class LevelsView extends View{
		private int black,white;
		public LevelsView(Context context) {
			super(context);
			black = 2;
			white = 2;
			// TODO Auto-generated constructor stub
		}	
		public void updateBlacks(int b){
			black = b;
		}
		public void updateWhites(int w){
			white = w;
		}
		@Override
		protected void onDraw(Canvas canvas){
			super .onDraw(canvas);
			Paint p = new Paint();
			p.setColor(Color.BLACK);
			p.setStyle(Paint.Style.FILL);
			canvas.drawPaint(p);
			int width = getWidth();
			float level = (float)white/(float)(white + black);
			width = (int)((float)width * level);
			p.setColor(Color.WHITE);
			canvas.drawRect(0, 0, width, getHeight(), p);
		}
	}
}