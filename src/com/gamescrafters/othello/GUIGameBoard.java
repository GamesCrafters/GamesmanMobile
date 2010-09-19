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
				TileView tv = new TileView(a, row, col);
				//ImageView iv = new ImageView(a);
				//iv.setImageResource(tile);
				//tv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				//tv.setAdjustViewBounds(true);
				//tv.setMaxHeight(new_height);
				tv.setId(getID(row, col));
				tv.setOnClickListener(new PieceClickListener(col, row));
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

		public PieceClickListener(int col_num, int row_num) {
			column = col_num;
			row = row_num;
		}

		public void onClick(View v) {
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
		public TileView(Context context, int x, int y) {
			super(context);
			this.x = x;
			this.y = y;
		}
		
		@Override
		protected void onDraw(Canvas canvas){
			super .onDraw(canvas);
			
			Paint p = new Paint();
			p.setStyle(Paint.Style.FILL);
			p.setColor(Color.TRANSPARENT);
			canvas.drawPaint(p);
			p.setColor(Color.WHITE);
			p.setStyle(Style.STROKE);
			canvas.drawLine(0, 0, 0, getHeight()-1, p);
			canvas.drawLine(0, 0, getWidth()-1, 0, p);
			canvas.drawLine(getWidth()-1, 0, getWidth()-1, getHeight()-1, p);
			canvas.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1, p);
		}
		
	}

}
