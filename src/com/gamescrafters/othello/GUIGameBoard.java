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
		for (int row=height-1; row>=0; row--) {
			TableRow tr = new TableRow(a);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int col=0; col<width; col++) {
				TileView tv = new TileView(a, row, col, Color.WHITE);
				//ImageView iv = new ImageView(a);
				//iv.setImageResource(tile);
				//tv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				//tv.setAdjustViewBounds(true);
				//tv.setMaxHeight(new_height);
				tv.setId(getID(row, col));
				tv.setOnClickListener(new PieceClickListener(col, row, tv));
				tr.addView(tv, new_size, new_size);
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
			piece.flipHorizontal(1000);
			if (!(a.isPlayer1Computer && a.isPlayer2Computer)) {
				if (!g.gameOver && g.isBlueTurn()){
					g.doMove(g.coordToMove(row, column), false);
					//if game is still not over, and it's a computer's turn, computer moves
					if (!g.gameOver && a.isPlayer2Computer) {
						a.doComputerMove();
					}
				} else {
					g.doMove(g.coordToMove(row, column), false);
					if (!g.gameOver && a.isPlayer1Computer) {
						a.doComputerMove();
					}
				}
			}
		}

	}
	public class TileView extends View{

		int x,y;
		int tColor;
		Animation horizontalFlip, verticalFlip, horizontalOpen, verticalOpen, rotate45;
		public TileView(Context context, int x, int y, int c) {
			super(context);
			this.x = x;
			this.y = y;
			this.tColor = c;
			
			horizontalFlip = new ScaleAnimation(1f, 0f, 1f, 1f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			verticalFlip = new ScaleAnimation(1f, 1f, 1f, 0f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			horizontalOpen = new ScaleAnimation(0f, 1f, 1f, 1f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			verticalOpen = new ScaleAnimation(1f, 1f, 0f, 1f,
					Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
			horizontalFlip.setAnimationListener(new TileAnimation(this, horizontalOpen));
			
			flipHorizontal(5000);
		}
		
		void flipHorizontal(long time){
			horizontalFlip.setDuration(time/2);
			horizontalOpen.setDuration(time/2);
			this.startAnimation(horizontalFlip);
		}
		
		public void swapColor(){
			if(this.tColor == Color.BLACK){
				this.tColor = Color.WHITE;
				return;
			}
			this.tColor = Color.BLACK;
		}
		
		@Override
		protected void onDraw(Canvas canvas){
			super .onDraw(canvas);
			Paint p = new Paint();
			p.setStyle(Paint.Style.FILL);
			p.setColor(Color.TRANSPARENT);
			canvas.drawPaint(p);
			p.setColor(this.tColor);
			p.setStyle(Style.FILL);
			canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, p);
			//canvas.drawLine(0, 0, 0, getHeight()-1, p);
			//canvas.drawLine(0, 0, getWidth()-1, 0, p);
			//canvas.drawLine(getWidth()-1, 0, getWidth()-1, getHeight()-1, p);
			//canvas.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1, p);
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
