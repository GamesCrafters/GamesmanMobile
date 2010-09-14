package com.gamescrafters.othello;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

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
		int max_height = a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 270 : 200;
		int new_height = max_height / height;
		for (int row=height-1; row>=0; row--) {
			TableRow tr = new TableRow(a);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int col=0; col<width; col++) {
				ImageView iv = new ImageView(a);
				iv.setImageResource(tile);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setAdjustViewBounds(true);
				iv.setMaxHeight(new_height);
				iv.setId(getID(row, col));
				iv.setOnClickListener(new PieceClickListener(col, row));
				tr.addView(iv);
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
}
