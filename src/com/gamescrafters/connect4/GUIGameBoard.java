package com.gamescrafters.connect4;

import java.util.LinkedList;

import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.gamescrafters.connect4.Connect4.Game;
import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.R;
import com.gamescrafters.gamesmanmobile.RemoteGameValueService;

/**
 * A class representing the m x n table of tiles on which Connect 4 is played.
 * 
 * @author AlexD
 * @author Gayane Vardoyan
 */
public class GUIGameBoard {
	int width;
	int height;
	TableLayout table;
	Connect4 a;
	Game g;
	
	int tile = R.drawable.c4_tile;
	int toptile = R.drawable.c4_toptile;
	int blue_tile = R.drawable.c4_blue_tile;
	int blue_tilehl = R.drawable.c4_blue_tilehl;
	int red_tile = R.drawable.c4_red_tile;
	int red_tilehl = R.drawable.c4_red_tilehl;

	int top_tile_ids[] = {R.drawable.c4_toptile, R.drawable.c4_toptile_green, 
			R.drawable.c4_toptile_yellow, R.drawable.c4_toptile_red,
			R.drawable.c4_blue_top, R.drawable.c4_blue_top_green,
			R.drawable.c4_blue_top_yellow,R.drawable.c4_blue_top_red,
			R.drawable.c4_red_top, R.drawable.c4_red_top_green,
			R.drawable.c4_red_top_yellow, R.drawable.c4_red_top_red};; // {T,G,Y,R, BT,BG,BY,BR, RT,RG,RY,RR}

	public GUIGameBoard (Connect4 a) {
		this.table = (TableLayout) a.findViewById(R.id.c4_gametable);
		this.a = a;
		this.g = a.g;
		width = g.width;
		height = g.height;
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
	 * @param row The row of the spot to check.
	 * @param col the column of the spot to check.
	 * @return The int id of the spot on the board (TableLayout).
	 */
	public int getID(int row, int col) {
		return 10*row + col;
	}
	
	/**
	 * Initializes the GUI board tiles.
	 */
	public void initBoard() {
		int swidth = a.getWindowManager().getDefaultDisplay().getWidth();
		int sheight = a.getWindowManager().getDefaultDisplay().getHeight();
		System.out.println("Height = " + sheight);
		int max_height = 0;
		if(width>height)
			max_height = a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? (int)(swidth) : (int)((sheight-5)*height/width);
		else
			max_height = a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? (int)(swidth) : (int)((sheight)*((double)(height-1)/(double)height));	
		int new_height = max_height / height;
		for (int row=height-1; row>=0; row--) {
			TableRow tr = new TableRow(a);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int col=0; col<width; col++) {
				ImageView iv = new ImageView(a);
				iv.setImageResource(row==height-1 ? toptile : tile);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setAdjustViewBounds(true);
				iv.setMaxHeight(new_height);
				iv.setId(getID(row, col));
				//iv.setOnClickListener(new ColumnClickListener(col));
				iv.setOnTouchListener(new ColumnTouchListener(col,a,g));
				tr.addView(iv);
			}
			table.addView(tr);
		}
		
	}
	
	/**
	 * Resizes the Gameboard size to a new height.
	 * @param height The maximum height for the gameboard.
	 */
	public void resize(int height) {
		int new_height = height / height;
		for (int row = height-1; row>=0; row--) {
			TableRow tr = (TableRow) table.getChildAt(row);
			for (int col = 0; col < width; col++) {
				((ImageView) tr.getChildAt(col)).setMaxHeight(new_height);
			}
		}
	}

	/**
	 * Sets the visual representation of the value of a column/move.
	 * int top_image_ids[]; // {T,G,Y,R, BT,BG,BY,BR, RT,RG,RY,RR}
	 * @param column The column to set the value of.
	 * @param value "win" to set it green, "tie" to set it yellow, "lose" to set it red, and anything else for blank.
	 */
	public void setColumnValue(int column, String value) {
		int i=0;
		if (value.equals("win")) i=1;
		else if (value.equals("tie")) i=2;
		else if (value.equals("lose")) i=3;

		int piece = g.pieceAt(height-1, column);
		if (piece == Connect4.Game.RED) i+=8;
		else if (piece == Connect4.Game.BLUE) i+=4;

		((ImageView) table.findViewById(getID(height-1,column))).setImageResource(top_tile_ids[i]);
	}

	/**
	 * Updates a tile as a piece is placed. 
	 * @param isBluesTurn true if it's blue's turn, otherwise false.
	 * @param row The row of the piece placed.
	 * @param column The column of the piece placed.
	 */
	public void updateTile(int piece, int row, int column) {
		if (piece == Connect4.Game.EMPTY) {
			((ImageView) table.findViewById(getID(row, column))).setImageResource(tile);
		}
		else if (piece == Connect4.Game.BLUE) {
			((ImageView) table.findViewById(getID(row, column))).setImageResource(blue_tile);
		}
		else if (piece == Connect4.Game.RED){
			((ImageView) table.findViewById(getID(row, column))).setImageResource(red_tile);
		}
		else if (piece == Connect4.Game.BLUEHL) {
			((ImageView) table.findViewById(getID(row, column))).setImageResource(blue_tilehl);
		}
		else
			((ImageView) table.findViewById(getID(row, column))).setImageResource(red_tilehl);

	}

	class ColumnTouchListener implements View.OnTouchListener {
		int col;
		int currcol;
		int twidth;
		Connect4 connect;
		Game G;
		float x, y;
		public ColumnTouchListener(int c, Connect4 con, Game game) {
			col = c;
			currcol = c;
			twidth = width;
			G = game;
			connect = con;
		}
		public boolean onTouch(View v, MotionEvent event) {
			float currx = event.getX();
			float curry = event.getY();
			int w = v.getWidth();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				//g.moveAnim(col);
				x = currx;
				y = curry;
				if (!G.gameOver)
					currcol  = G.moveAnim(col,currcol,x,currx,w,twidth);
				return true;
			}
			
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (!G.gameOver)
					currcol  = G.moveAnim(col,currcol,x,currx,w,twidth);
				return true;
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				//g.upAnim(currcol);
				boolean isDatabaseAvailable = RemoteGameValueService.isInternetAvailable();
				if (!(connect.isPlayer1Computer && connect.isPlayer2Computer)) {
					if (!G.gameOver && G.isBlueTurn()){
						G.doMove(currcol, false);
						//if game is still not over, and it's a computer's turn, computer moves
						if (!G.gameOver && connect.isPlayer2Computer) {
							if (isDatabaseAvailable) {
								connect.doComputerMove();
							} else {
								connect.playRandom();
							}
						}
					} else {
						G.doMove(currcol, false);
						if (!G.gameOver && connect.isPlayer1Computer) {
							if (isDatabaseAvailable) {
								connect.doComputerMove();
							} else {
								connect.playRandom();
							}
						}
					}
				}
				return true;
			}
			return true;
		}
	}
	/**
	 * An OnClickListener that is attached to the tile image elements.
	 * Calls doMove, doComputerMove, or playRandom on a click.
	 */
	

	
}
