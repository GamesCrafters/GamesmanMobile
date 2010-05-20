package com.gamescrafters.connections;



import java.util.LinkedList;
import java.util.Stack;

import android.content.res.Configuration;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;

public class GameBoard {

	Connections cActivity;
	TableLayout tLayout;
	Game g; //Internal game state
	int size;

	public GameBoard (int s,Connections c){
		//initialize instance variables;
		cActivity = c;
		tLayout = (TableLayout)cActivity.findViewById(R.id.con_tLayout);
		size = s;

		//initialize internal representation of Game
		g = new Game(size,size);

		//initialize GUIBoard
		initGUIBoard();	
		
	}
	public void resetGame() {
		tLayout.removeAllViews();
		
		TextView player = (TextView) cActivity.findViewById(R.id.con_winner);
		TextView remoteTextView = (TextView) cActivity.findViewById(R.id.con_remoteness);
		
		player.setText("Player " + g.whoseMove + "'s turn");
		remoteTextView.setText("");
	    player.setTextColor(cActivity.getResources().getColor(R.color.solid_red));
	}
	

	public void initGUIBoard(){
		int max_height = cActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 270 : 168;
		int new_height = max_height /size;
		
		for (int r=0; r<size; r++) {
			TableRow tr = new TableRow(cActivity);
			tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			tr.setGravity(Gravity.CENTER_HORIZONTAL);
			for (int c=0; c<size; c++) {
				ImageView iv = new ImageView(cActivity);
				setPieceImage(iv,g.board[r][c].getType());
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setAdjustViewBounds(true);
				iv.setMaxHeight(new_height); //just added
				if(r !=0 && c != 0 && r!=(size-1) && c!= (size-1)){
					iv.setOnClickListener(new PieceClickListener(r,c));
				}
				tr.addView(iv);
			}
			tLayout.addView(tr);
		}
	}
	public void resize(int new_height) {
		int new_tile_height = new_height / size; //height=# of rows
		for (int row = size-1; row>=0; row--) {
			TableRow tr = (TableRow) tLayout.getChildAt(row);
			for (int col = 0; col < size; col++) {
				((ImageView) tr.getChildAt(col)).setMaxHeight(new_tile_height);
			}
		}
	}
	
	public void setPieceImage(ImageView iv, int type){

		int colorID=0;
		switch(type){
		case Game.BLANK: colorID=R.drawable.con_blank; break; //blank
		case Game.P1NODE: colorID=R.drawable.con_p1; break; //n1
		case Game.P1LINEV: colorID=R.drawable.con_v1; break; //v1
		case Game.P1LINEH: colorID=R.drawable.con_h1; break; //h1
		case Game.P2NODE: colorID=R.drawable.con_p2; break; //n2
		case Game.P2LINEV: colorID=R.drawable.con_v2; break; //v2
		case Game.P2LINEH: colorID=R.drawable.con_h2; break; //h2
		}

		iv.setImageResource(colorID);
	}
	
	public void revertImages(LinkedList<int[]>values){
		
		       for(int[]pos : values){
		    	   int r=pos[0];
		    	   int c=pos[1];
		    	   TableRow tr = (TableRow) tLayout.getChildAt(r);
		    	   ImageView image= (ImageView)tr.getChildAt(c);  //old image
		    	   if(g.board[r][c].getType()==Game.BLANK){
		    	          image.setImageResource(R.drawable.con_blank);
		    	   }
		       }
	}
	private int[] convertMove(int move){
		int[] pos=new int[2];
		int mvPerRow=(g.cols/2);
			if (move >=mvPerRow*mvPerRow){
				move=move-mvPerRow*mvPerRow;
				mvPerRow--;
				pos[0]=((g.rows-2) - 2*(move/mvPerRow))-1;
				pos[1]=2*(move % mvPerRow)+2;
			}else{
			pos[0]=(g.rows-2) - 2*(move/mvPerRow);
			pos[1]=2*(move % mvPerRow)+1;
			}
	   return pos;
		}
	/**
	 * Generates all valid moves
	 * @return 
	 */
	LinkedList<int[]> generateMoves(MoveValue[]mvs) {
		
		LinkedList<int[]> toReturn= new LinkedList<int[]>();
	     for(MoveValue mv: mvs){
	    	   int move=mv.getIntMove();
	    	   int []pos=convertMove(move);
	    	   toReturn.add(pos);
	     }
		/*
		for (int r=0; r < rows; r++){
			for (int c=0; c < cols; c++){
				int[] tmp = {r,c};
				if (isValid(r,c) && r != 0 && c !=0 && r !=rows-1 && c !=cols-1){
					toReturn.add(tmp);
			}
		}
		
		*/
		return toReturn;
	}
	public void swapImages(MoveValue []mvs){
		   if(mvs== null){
			   return;
		   }
		       for(MoveValue mv: mvs){
		    	   int move=mv.getIntMove();
		    	   String val=mv.getValue();
		    	   int []pos=convertMove(move);
		    	   
		              int r=pos[0];
		              int c=pos[1];
		    	      TableRow tr = (TableRow) tLayout.getChildAt(r);
		    	      ImageView image= (ImageView)tr.getChildAt(c);  //old image
		    	
		    		if (g.whoseMove==Game.P1){
		    			
						if ( (c % 2) == 1 ){
							//image.setImageResource((rInt > 0.5) ? 
							image.setImageResource((val.equals("win")) ? R.drawable.con_wv : R.drawable.con_lv);
						} else {
							image.setImageResource((val.equals("win"))? R.drawable.con_wh : R.drawable.con_lh);
						}
					} else if (g.whoseMove==Game.P2){
						if ( (c % 2) == 1 ){
							image.setImageResource((val.equals("win"))? R.drawable.con_wh : R.drawable.con_lh);
							
						} else {
							image.setImageResource((val.equals("win"))? R.drawable.con_wv : R.drawable.con_lv);
				
						}
					}
		       }
	}
	
	//set internal gamestate isRedo variable
	void setIsRedo(boolean b){
		g.isRedo = b;
	}
	
	//set internal gamestate isRedo variable
	void setIsUndo(boolean b){
		g.isUndo = b;
	}
	
	//Sets internal and gui state of the board according to whether movie is an regular move, redo, undo move
	public void reUnDoMove(int r, int c){
		//MoveValue[] mv = cActivity.getNextMoveValues();
		TextView player = (TextView) cActivity.findViewById(R.id.con_winner);
		if (!g.isOver){
			player.setText("Player " + g.whoseMove + "'s turn");
			if (g.whoseMove == Game.P1){
				player.setTextColor(cActivity.getResources().getColor(R.color.solid_red));
			} else {
				player.setTextColor(cActivity.getResources().getColor(R.color.solid_blue));
			}
		} 
		int x=r; int y=c;
		if (g.isUndo && !g.previousMoves.isEmpty()){
			Stack <int[]> previousMoves = g.previousMoves;
			int[] toUndo = g.previousMoves.peek();
			x=toUndo[0]; y=toUndo[1];
		}
		if (g.isRedo && !g.nextMoves.isEmpty()){
			Stack <int[]> nextMoves = g.nextMoves;
			int[] toRedo = g.nextMoves.peek();
			x=toRedo[0]; y=toRedo[1];
		}
		if ((!g.isOver || g.isRedo || g.isUndo) && (g.undoMove() || g.doMove(r,c,g.isDo,g.isRedo))){
			cActivity.getHSlider().updateProgress(g.currentMove, g.movesSoFar);
			r=x;c=y;
			TableRow tr = (TableRow) tLayout.getChildAt(r);
   	      	ImageView v= (ImageView)tr.getChildAt(c);
			GameBoard.this.setPieceImage((ImageView) v, g.board[r][c].getType());
			if(g.isOver){
				player.setText("Player " + g.whoseMove + " Wins!");
				if (g.whoseMove == Game.P1){
					player.setTextColor(cActivity.getResources().getColor(R.color.solid_red));
				} else {
					player.setTextColor(cActivity.getResources().getColor(R.color.solid_blue));
				}
				if(!g.isUndo){
					if ((cActivity.values != null) && (cActivity.values.length != 0)) {
						cActivity.previousValue = cActivity.getBoardValue(cActivity.values);
						int remoteness = cActivity.getRemoteness(cActivity.previousValue, cActivity.values);
						cActivity.updateVVH(cActivity.previousValue, remoteness, g.isOver, g.whoseMove==2 ? true : false, false);
					}
				} else {
					cActivity.removeLastVVHNode();
				}
				g.switchPlayer();
				cActivity.values = cActivity.getNextMoveValues(); //try this 
				cActivity.updateValuesDisplay(); //updates the GUI
				cActivity.updatePredictionDisplay();
				return;
			}
			g.switchPlayer();
			player.setText("Player " + g.whoseMove + "'s turn");
			if (g.whoseMove == Game.P1){
				player.setTextColor(cActivity.getResources().getColor(R.color.solid_red));
			} else {
				player.setTextColor(cActivity.getResources().getColor(R.color.solid_blue));
			}
			cActivity.values = cActivity.getNextMoveValues(); //try this 
			cActivity.updateValuesDisplay(); //try this?
			cActivity.updatePredictionDisplay();
			if (!g.isUndo &&(cActivity.values != null) && (cActivity.values.length != 0)) {
				cActivity.previousValue = cActivity.getBoardValue(cActivity.values);
				int remoteness = cActivity.getRemoteness(cActivity.previousValue, cActivity.values);
				cActivity.updateVVH(cActivity.previousValue, remoteness, g.isOver, g.whoseMove==2 ? true : false, false);
			}
			if (g.isUndo){
				cActivity.removeLastVVHNode();
			}
		}
	}
	
	/**
	 * This is the OnClickListener we attach to each image. It waits from a user to click
	 * on the image, and calls onClick when it does. The View passed is the one that was clicked.
	 */
	class PieceClickListener implements View.OnClickListener {
		int r,c;

		public PieceClickListener(int row, int col) {
			r=row;
			c=col;
		}

		public void onClick(View v) {
			g.isDo=true;
			reUnDoMove(r,c);
			g.isDo=false;
			/**
			//MoveValue[] mv = cActivity.getNextMoveValues();
			
			TextView player = (TextView) cActivity.findViewById(R.id.con_winner);
			if (!g.isOver){
				player.setText("Player " + g.whoseMove + "'s turn");
				if (g.whoseMove == Game.P1){
					player.setTextColor(cActivity.getResources().getColor(R.color.solid_red));
				} else {
					player.setTextColor(cActivity.getResources().getColor(R.color.solid_blue));
				}
				
			} 
			if (!g.isOver && g.doMove(r,c)){
                //cActivity.getNextMoveValues();
				GameBoard.this.setPieceImage((ImageView) v, g.board[r][c].getType());
				
				if(g.isOver){
					player.setText("Player " + g.whoseMove + " Wins!");
					if (g.whoseMove == Game.P1){
						player.setTextColor(cActivity.getResources().getColor(R.color.solid_red));
					} else {
						player.setTextColor(cActivity.getResources().getColor(R.color.solid_blue));
					}
					
					cActivity. updateValuesDisplay(); //updates the GUI 
					return;
				}
				g.switchPlayer();
				player.setText("Player " + g.whoseMove + "'s turn");
				if (g.whoseMove == Game.P1){
					player.setTextColor(cActivity.getResources().getColor(R.color.solid_red));
				} else {
					player.setTextColor(cActivity.getResources().getColor(R.color.solid_blue));
				}
				cActivity.updateValuesDisplay(); //try this?
			}
			**/
		}
	}
}
