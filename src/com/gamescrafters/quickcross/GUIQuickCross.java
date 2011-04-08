package com.gamescrafters.quickcross;

import com.gamescrafters.gamesmanmobile.R;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;



public class GUIQuickCross {
	QuickCross qc;
	int height;
	int width;
	TableLayout table;
	Resources r;
	
	private int qc_horiz = R.drawable.qc_horiz;
	private int qc_loseh = R.drawable.qc_loseh;
	private int qc_losev = R.drawable.qc_losev;
	private int qc_neutralh = R.drawable.qc_neutralh;
	private int qc_neutralv = R.drawable.qc_neutralv;
	private int qc_tieh = R.drawable.qc_tieh;
	private int qc_tiev = R.drawable.qc_tiev;
	private int qc_vert = R.drawable.qc_vert;
	private int qc_winh = R.drawable.qc_winh;
	private int qc_winv = R.drawable.qc_winv;
		
	public GUIQuickCross(QuickCross quickcross, int width, int height)
	{
		table = (TableLayout) quickcross.findViewById(R.id.quickcross_gametable);
		qc = quickcross;
		this.width = width;
		this.height = height;
		initBoard();
		r = qc.getResources();
	}
	public void initBoard()
	{
		int max_height = qc.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 350 : 300;
		int new_height = max_height / height;
		for (int row=0; row<height; row++) {
			TableRow tr = new TableRow(qc);
			tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int col=0; col<width; col++) {
				Resources r = qc.getResources();
				ImageView iv = new ImageView(qc);
				Drawable[] layers = new Drawable[3];
				layers[0] = r.getDrawable(qc_neutralv);
				layers[1] = r.getDrawable(qc_neutralh);
				layers[2] = r.getDrawable(qc_neutralh);
				LayerDrawable layer = new LayerDrawable(layers);
				layer.setId(0, 0);
				layer.setId(1, 1);
				layer.setId(2, 2);
				iv.setImageDrawable(layer);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setAdjustViewBounds(true);
				iv.setMaxHeight(new_height);
				iv.setId(getID(row, col));
				iv.setOnClickListener(new QuickCrossClickListener(row,col));
				tr.addView(iv);
			}
			table.addView(tr);
		}
	}
	
	public int getID(int row, int col)
	{
		return width*row+col;
	}
	public void addTile(boolean blue, int row, int col)
	{
		String[] temp = new String[4];
		temp[0] = ""+row;
		temp[1] = ""+col;
		temp[2] = "H";
		temp[3] = "P";
		qc.doMove(temp);
	}
	
	public void updateGraphics(int[][] board)
	{
		for(int row = 0; row<height; row++)
		{
			for(int col = 0; col < width; col++)
			{
				if(board[row][col]==QuickCross.HORIZ)
				{
					System.out.println("HERE");
					ImageView iv = ((ImageView)table.findViewById(getID(row,col)));
					LayerDrawable temp = (LayerDrawable) iv.getDrawable();
					temp.setDrawableByLayerId(2, r.getDrawable(qc_horiz));
					iv.setImageDrawable(temp);
					
				}
				else if(board[row][col]==QuickCross.VERT)
				{
					
					ImageView iv = ((ImageView)table.findViewById(getID(row,col)));
					LayerDrawable temp = (LayerDrawable) iv.getDrawable();
					temp.setDrawableByLayerId(2, r.getDrawable(qc_vert));
					iv.setImageDrawable(temp);
				}
				else
				{
					System.out.println("HERE3");
					ImageView iv = ((ImageView)table.findViewById(getID(row,col)));
					LayerDrawable temp = (LayerDrawable) iv.getDrawable();
					temp.setDrawableByLayerId(2, r.getDrawable(qc_neutralh));
					iv.setImageDrawable(temp);
				}	
			}
		}
	}
	class QuickCrossClickListener implements View.OnClickListener
	{
		private int myRow;
		private int myCol;
		public QuickCrossClickListener(int row, int col)
		{
			myRow = row;
			myCol = col;
		}
		public void onClick(View v) {
			addTile(true, myRow, myCol);
		}
	}
}
