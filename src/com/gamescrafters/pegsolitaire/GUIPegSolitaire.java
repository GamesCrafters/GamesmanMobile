package com.gamescrafters.pegsolitaire;

import com.gamescrafters.gamesmanmobile.R;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GUIPegSolitaire{
	private int height;
	private TableLayout layout;
	private PegSolitaire o;
	
	private int tile = R.drawable.c4_tile;
	private int blueTile = R.drawable.c4_blue_tile;
	private int redTile = R.drawable.c4_red_tile;
	private int greenValueTile = R.drawable.onetwotentile_green;
	private int redValueTile = R.drawable.onetwotentile_red;
	private boolean drawMoveValues;
	
	public GUIPegSolitaire(PegSolitaire o, int height)
	{
		layout = (TableLayout) o.findViewById(R.id.pegsolitaire_gametable);
		this.o = o;
		this.height = height;
		drawMoveValues = false;
		initBoard();
	}
	public void initBoard()
	{
		int sheight = o.getWindowManager().getDefaultDisplay().getHeight();
		int max_height = o.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? (int)(sheight*.65) : 280;
		// ^ not needed: screenOrientation is set to portrait in the manifest
		int new_height = max_height / height;
		for(int i = 0; i<height; i++)// go from top to bottom because the first image will appear at the top of the screen
		{
			TableRow tr = new TableRow(o);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for(int j = i; j>=0; j--)
			{
				ImageView iv = new ImageView(o);
				iv.setImageResource(tile);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setAdjustViewBounds(true);
				iv.setMaxHeight(new_height);
				iv.setId(i);
				iv.setOnClickListener(new PegSolitaireClickListener(i));
				tr.addView(iv);
			}
			layout.addView(tr);
		}
	}
	public void addTile(boolean blue, int row)
	{
		o.doMove(row);
	}
	public void setDrawMoveValues(boolean t)
	{
		drawMoveValues = t;
	}
	public void updateGraphics(int[] board)
	{
		for(int i = 0; i<board.length; i++)
		{
			if(board[i]==PegSolitaire.RED)
				((ImageView)layout.findViewById(i)).setImageResource(redTile);
			else if(board[i]==PegSolitaire.BLUE)
				((ImageView)layout.findViewById(i)).setImageResource(blueTile);
			else if(board[i]==PegSolitaire.WIN && drawMoveValues)
				((ImageView)layout.findViewById(i)).setImageResource(redValueTile);//win child corresponds to losing move
			else if(board[i]==PegSolitaire.LOSE && drawMoveValues)
				((ImageView)layout.findViewById(i)).setImageResource(greenValueTile);//lose child corresponds to wining child
			else//board[i]==PegSolitaire.EMPTY
				((ImageView)layout.findViewById(i)).setImageResource(tile);
		}
	}
	class PegSolitaireClickListener implements View.OnClickListener
	{
		private int myNumber;
		public PegSolitaireClickListener(int num)
		{
			myNumber = num;
		}
		public void onClick(View v) {
			addTile(true, myNumber);
		}
	}
}
