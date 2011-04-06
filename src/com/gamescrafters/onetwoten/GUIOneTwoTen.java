package com.gamescrafters.onetwoten;

import com.gamescrafters.gamesmanmobile.R;

import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

public class GUIOneTwoTen{
	private int height;
	private TableLayout layout;
	private OneTwoTen o;
	
	private int tile = R.drawable.c4_tile;
	private int blueTile = R.drawable.c4_blue_tile;
	private int redTile = R.drawable.c4_red_tile;
	private int greenValueTile = R.drawable.onetwotentile_green;
	private int redValueTile = R.drawable.onetwotentile_red;
	private boolean drawMoveValues;
	
	public GUIOneTwoTen(OneTwoTen o, int height)
	{
		layout = (TableLayout) o.findViewById(R.id.onetwoten_gametable);
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
		for(int i = height-1; i>=0; i--)// go from top to bottom because the first image will appear at the top of the screen
		{
			ImageView iv = new ImageView(o);
			iv.setImageResource(tile);
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			iv.setAdjustViewBounds(true);
			iv.setMaxHeight(new_height);
			iv.setId(i);
			iv.setOnClickListener(new OneTwoTenClickListener(i));
			layout.addView(iv);
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
			if(board[i]==OneTwoTen.RED)
				((ImageView)layout.findViewById(i)).setImageResource(redTile);
			else if(board[i]==OneTwoTen.BLUE)
				((ImageView)layout.findViewById(i)).setImageResource(blueTile);
			else if(board[i]==OneTwoTen.WIN && drawMoveValues)
				((ImageView)layout.findViewById(i)).setImageResource(redValueTile);//win child corresponds to losing move
			else if(board[i]==OneTwoTen.LOSE && drawMoveValues)
				((ImageView)layout.findViewById(i)).setImageResource(greenValueTile);//lose child corresponds to wining child
			else//board[i]==OneTwoTen.EMPTY
				((ImageView)layout.findViewById(i)).setImageResource(tile);
		}
	}
	class OneTwoTenClickListener implements View.OnClickListener
	{
		private int myNumber;
		public OneTwoTenClickListener(int num)
		{
			myNumber = num;
		}
		public void onClick(View v) {
			addTile(true, myNumber);
		}
	}
}
