package com.gamescrafters.othello;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.gamescrafters.gamesmanmobile.R;

import com.gamescrafters.othello.Othello;
import com.gamescrafters.othello.Othello.Game;


public class GUIGameBoard {
	int width;
	int height;
	TableLayout table;
	Othello a;
	Game g;
	
	int tile = R.drawable.oth_tile;
	int blue_tile = R.drawable.oth_tile_blue;
	int red_tile = R.drawable.oth_tile_red;

	public GUIGameBoard (Othello a) {
		this.table = (TableLayout) a.findViewById(R.id.oth_gametable);
		this.a = a;
		this.g = a.g;
		width = g.width;
		height = g.height;
	}
	
	/**
	 * @param row The row of the spot to check.
	 * @param col the column of the spot to check.
	 * @return The int id of the spot on the board (TableLayout).
	 */
	public int getID(int row, int col) {
		return 10*row + col;
	}
	
	/**
	 * Resets the board with a new game
	 * @param g The new Game to use for the GUIGameBoard.
	 */
	public void reset(Game g) {
		table.removeAllViews();
		this.g = g;
		width = g.width;
		height = g.height;
	}
	/**
	 * Initializes the board for Othello.
	 */
	public void initBoard() {
		int size = a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 275 : 200;
		int new_size = (int)Math.floor((double)size / (double)height);
		for (int row=1; row<=height; row++) {
			TableRow tr = new TableRow(a);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int col=1; col<=width; col++) {
				int c = Color.TRANSPARENT;
				if(g.board[row-1][col-1] == Othello.Game.BLACK)
					c = Color.BLACK;
				else if(g.board[row-1][col-1] == Othello.Game.WHITE)
					c = Color.WHITE;
				TileView tv = new TileView(a, row, col, c);
				tv.setSmallColor(g.previewColor(row, col));
				tv.setId(getID(row, col));
				tv.setOnClickListener(new PieceClickListener(col, row, tv));
				tr.addView(tv, new_size, new_size);
				g.tiles[row-1][col-1] = tv;
			}
			table.addView(tr);
		}
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
			if (!(a.isPlayer1Computer && a.isPlayer2Computer)) {
				if(g.moveValid(row,column) && !g.gameOver){
					if(g.isBlackTurn()){
						g.doMove(row, column, false);
						if(a.isPlayer2Computer){
							a.doComputerMove();
						}
					}else{
						g.doMove(row, column, false);
						if(a.isPlayer1Computer){
							a.doComputerMove();
						}
					}
				}
			}
		}

	}
	public class TileView extends View{

		int x,y;
		int tColor;
		boolean small;
		int pColor;
		private Animation horizontalFlip, verticalFlip, horizontalOpen, verticalOpen, rotate45;
		private AnimationSet diagonalFlip1, openDiag1, diagonalFlip2, openDiag2;
		public TileView(Context context, int x, int y, int c) {
			super(context);
			this.x = x;
			this.y = y;
			this.tColor = c;
			
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
			
			// Set Interpolators
			horizontalFlip.setInterpolator(new AccelerateInterpolator(1.1f));
			verticalFlip.setInterpolator(new AccelerateInterpolator(1.1f));
			horizontalFlip.setInterpolator(new DecelerateInterpolator(1.1f));
			verticalFlip.setInterpolator(new DecelerateInterpolator(1.1f));
			
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
				return;
			}
			this.tColor = Color.BLACK;
		}
		
		public void setColor(int c){
			this.tColor = c;
		}
		
		public void setSmallColor(int c){
			this.small = true;
			this.pColor = c;
		}
		@Override
		protected void onDraw(Canvas canvas){
			super .onDraw(canvas);
			Paint p = new Paint();
			p.setStyle(Paint.Style.FILL);
			p.setColor(Color.TRANSPARENT);
			canvas.drawPaint(p);
			if(this.tColor != Color.TRANSPARENT){
				p.setColor(this.tColor);
				p.setStyle(Style.FILL);
				canvas.drawCircle(getWidth()/2, getHeight()/2, (((getWidth()/2)*90)/100), p);
			}else if(this.pColor != Color.TRANSPARENT && this.small){
				p.setColor(this.pColor);
				p.setStyle(Style.FILL);
				canvas.drawCircle(getWidth()/2, getHeight()/2, (((getWidth()/2)*30)/100), p);
			}
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
				this.parent.startAnimation(this.nextAni);
			}

			public void onAnimationRepeat(Animation animation) {}

			public void onAnimationStart(Animation animation) {}
		}
		
	}
}
