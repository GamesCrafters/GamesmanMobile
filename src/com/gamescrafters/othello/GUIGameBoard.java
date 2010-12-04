package com.gamescrafters.othello;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.os.Handler;
import android.content.res.Configuration;


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
	Handler h;

	int animsRunning;

	public GUIGameBoard (Othello a) {
		this.table = (TableLayout) a.findViewById(R.id.oth_gametable);
		this.a = a;
		this.g = a.g;
		h = new Handler();

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
		Window w = a.getWindow();
		Rect r = new Rect();
		w.getDecorView().getWindowVisibleDisplayFrame(r);
		int size = a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
				a.getWindowManager().getDefaultDisplay().getWidth() :
					a.getWindowManager().getDefaultDisplay().getHeight() - r.top - a.findViewById(R.id.gm_undoButton).getHeight() - 15;
		int new_hei = (int)Math.floor((double)table.getHeight() / (double)width);
		int new_wid = (int)Math.floor((double)size / (double)height);

//		ImageView bg = (ImageView)a.findViewById(R.id.oth_boardImage);
//		bg.setMaxHeight(size);
//		bg.setMaxWidth(size);
//		bg.setImageResource(R.drawable.oth_felt4);
//		bg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		//RelativeLayout obl = (RelativeLayout)a.findViewById(R.id.oth_boardLayout);
//		obl.removeView(table);
//		bl.addView(table,new_wid,new_hei);
		table.getLayoutParams().height = size;
		//table.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, new_hei));
		//table.setMinimumHeight(new_hei);
		//this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, theSizeIWant));
		//table.setLayoutParams(TableLayout.LayoutParams(LayoutParams.FILL_PARENT, new_hei)));
//		table.setBackgroundResource(R.drawable.oth_felt4);

		for (int row=1; row<=height; row++) {
			TableRow tr = new TableRow(a);
			tr.setId(row+1024);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int col=1; col<=width; col++) {
				int c = Color.TRANSPARENT;
				if(g.board[row-1][col-1] == Othello.Game.BLACK)
					c = Color.BLACK;
				else if(g.board[row-1][col-1] == Othello.Game.WHITE)
					c = Color.WHITE;
				TileView tv = new TileView(a, row, col, c);
				tv.setSmallColor(g.previewColor(row, col,false));
				//tv.setId(getID(row, col));
				tv.setOnClickListener(new PieceClickListener(col, row, tv));
				table.findViewById(getID(row,col));
				PositionView pv = new PositionView(a);
				ImageView iv = new ImageView(a);
				iv.setImageResource(R.drawable.oth_felt);
				RelativeLayout rl = new RelativeLayout(a);
				rl.setId(getID(row,col));
				rl.addView(iv);
				rl.addView(pv);
				rl.addView(tv);
				tr.addView(rl, new_wid, new_wid);
				g.tiles[row-1][col-1] = tv;
			}
			table.addView(tr);
		}
		LevelsView lv = new LevelsView(a);
		g.levels = lv;
		table.addView(lv,size,15);
		if(a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			updateOrient(Configuration.ORIENTATION_LANDSCAPE);
		}
		Thread previews = new Thread(){
			public void run(){
				g.updatePreviews(true);
			}
		};
		previews.start();
	}
	
	public void updateOrient(int orientation){
		Window w = a.getWindow();
		Rect r = new Rect();
		w.getDecorView().getWindowVisibleDisplayFrame(r);
		int size = orientation == Configuration.ORIENTATION_PORTRAIT ?
				a.getWindowManager().getDefaultDisplay().getWidth() :
					a.getWindowManager().getDefaultDisplay().getHeight() - r.top -a.findViewById(R.id.gm_undoButton).getHeight() - 15;		
//		int size = orientation == Configuration.ORIENTATION_PORTRAIT ?
//				a.getWindowManager().getDefaultDisplay().getWidth() :
//				table.getHeight() - a.findViewById(R.id.gm_undoButton).getHeight()
//				- a.findViewById(R.id.oth_logo).getHeight();
//		int size = a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 
//				a.getWindowManager().getDefaultDisplay().getWidth() :
//				a.getWindowManager().getDefaultDisplay().getHeight() - a.findViewById(R.id.gm_undoButton).getHeight()
//				- a.findViewById(R.id.oth_logo).getHeight();
		int new_hei = (int)Math.floor((double)table.getHeight() / (double)width);
		int new_wid = (int)Math.floor((double)size / (double)height);

		table.removeView(g.levels);
		table.addView(g.levels,size,15);
		LinearLayout orient = (LinearLayout)a.findViewById(R.id.oth_horizontalItems);
		LinearLayout global = (LinearLayout)a.findViewById(R.id.oth_global);
		
		if(orientation == Configuration.ORIENTATION_LANDSCAPE){
			table.setGravity(Gravity.LEFT);
			View move = global.findViewById(R.id.oth_GameOverAndTurn);
			View title = global.findViewById(R.id.oth_logo);
			View goText = global.findViewById(R.id.oth_gameOver);
			if(move != null){
				global.removeView(move);
				global.removeView(title);
				global.removeView(goText);
				orient.addView(title);
				orient.addView(move);
				orient.addView(goText);
			}
			//orient.addView(a.findViewById(R.id.oth_GameOverAndTurn));
			//global.setOrientation(LinearLayout.HORIZONTAL);
		}else{
			table.setGravity(Gravity.CENTER_HORIZONTAL);
			View move = orient.findViewById(R.id.oth_GameOverAndTurn);
			View table = orient.findViewById(R.id.oth_boardLayout);
			View title = orient.findViewById(R.id.oth_logo);
			View goText = orient.findViewById(R.id.oth_gameOver);
			if(move != null){
				orient.removeView(move);
				orient.removeView(title);
				orient.removeView(goText);
				
				//global.removeView(table);
				global.addView(title,0);
//				global.addView(table);
				global.addView(move);
				global.addView(goText);
				
			}
		}
		
		
		for(int row=1; row <= height; row++){
			TableRow tr = (TableRow)table.findViewById(row+1024);
			for(int col = 1; col <= width; col++){	
				RelativeLayout rl = (RelativeLayout)tr.findViewById(getID(row,col));
				tr.removeViewAt(0);
				tr.addView(rl, new_wid, new_wid);
			}
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
						g.doMove(row, column, false, false);
						if(a.isPlayer2Computer){
							g.clearPreviews();
							h.postDelayed(a.c, a.moveDelay);
						}
					}else{
						g.doMove(row, column, false, false);
						if(a.isPlayer1Computer){
							h.postDelayed(a.c, a.moveDelay);
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
		public Runnable dF1, dF2, h, v;
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
			
			float interpFactor = 1.0f; 
			
			// Set Interpolators
			horizontalFlip.setInterpolator(new AccelerateInterpolator(interpFactor));
			verticalFlip.setInterpolator(new AccelerateInterpolator(interpFactor));
//			horizontalOpen.setInterpolator(new DecelerateInterpolator(interpFactor));
//			verticalOpen.setInterpolator(new DecelerateInterpolator(interpFactor));
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
//			openDiag1.setInterpolator(new DecelerateInterpolator(interpFactor));
//			openDiag2.setInterpolator(new DecelerateInterpolator(interpFactor));
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
				//this.setImageResource(R.drawable.oth_simplewhite);
				return;
			}
			this.tColor = Color.BLACK;
			//this.setImageResource(R.drawable.oth_simpleblack);
		}
		
		public void setColor(int c){
			this.tColor = c;
			if(tColor == Color.MAGENTA){
				//this.setImageResource(R.drawable.oth_rec);
			}
		}
		
		public void setSmallColor(int c){
			this.small = true;
			this.pColor = c;
		}
		@Override
		protected void onDraw(Canvas canvas){
			super .onDraw(canvas);
//			Paint p = new Paint();
//			p.setStyle(Paint.Style.FILL);
//			p.setColor(Color.TRANSPARENT);
//			canvas.drawPaint(p);
//			if(this.tColor != Color.TRANSPARENT){
//				if(this.tColor == Color.BLACK){
//					this.setImageResource(R.drawable.oth_tileblack);
//				}else{
//					this.setImageResource(R.drawable.oth_tilewhite);
//				}
//			}else if(this.pColor != Color.TRANSPARENT && this.small){
//				//this.setImageResource(R.drawable.oth_felt);
//				p.setColor(this.pColor);
//				p.setStyle(Style.FILL);
//				canvas.drawCircle(getWidth()/2, getHeight()/2, (((getWidth()/2)*30)/100), p);
//			}
			//this.setImageResource(R.drawable.oth_tilewhite);
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
