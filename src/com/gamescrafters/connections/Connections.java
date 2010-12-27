package com.gamescrafters.connections;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;


public class Connections extends GameActivity {
    /** Called when the activity is first created. */
    GameBoard gb; // GUI board and internal game state
    MoveValue[] values = null;
    String previousValue = "win";
    LinkedList<int[]> previousValues=null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGameView(R.layout.connections_game);
        
        //grab board size from Connection_Options
        Intent myIntent = getIntent();
		int size = myIntent.getIntExtra("size", 7);
		
		//initialize GameBoard which initializes both GUIBoard and internal game state
		gb = new GameBoard(size,this);
		
		//VVH preparation
		values = getNextMoveValues();
		updatePredictionDisplay();
		updateValuesDisplay();
		if (values != null && values.length != 0) {
			previousValue = getBoardValue(values);
			int remoteness = getRemoteness(previousValue, values);
			clearVVH();
			updateVVH(previousValue, remoteness, gb.g.isOver, gb.g.whoseMove==2 ? true : false , false);
		}
		
    }
	

	@Override
	public
	void doMove(String move) {
		// TODO Auto-generated method stub
		
	}
	/*
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		int max_height = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 270 : 168;
		gb.resize(max_height);
	}
	*/

	@Override
	public
	String getBoardString() {
		Game thisGame=gb.g;
		Piece[][] board=thisGame.board;
		int  row=thisGame.rows, col=thisGame.cols;
		int size=row/2;
		String vert="", horiz="", space="%20";
		int startR=row-2;
	   int startC=1;
	  //int startR=((thisGame.whoseMove==Game.P1) ? row-2 : row-3);
	  //int startC=((thisGame.whoseMove==Game.P1) ? 1 : 2);
      // int vertical=((thisGame.whoseMove==Game.P1) ? thisGame.P1LINEV: thisGame.P2LINEV);
	  //int horizontal=((thisGame.whoseMove==Game.P1) ? thisGame.P1LINEH : thisGame.P2LINEH);
		//handles the vertical edges
		for(int r=startR; r >0; r=r-2){
			for(int c=startC; c <=col-2; c=c+2){
				if((board[r][c].getType()==Game.BLANK)){
					vert=vert+space;
				}else if((board[r][c].getType()==Game.P1LINEV) || (board[r][c].getType()==Game.P1LINEH)){
					vert=vert+"X";
				}else if((board[r][c].getType()==Game.P2LINEV) || (board[r][c].getType()==Game.P2LINEH)){
					vert=vert+"O";
				}
			}
		}
		 startR=row-3;
		startC=2;
	   // startR=((thisGame.whoseMove==Game.P1) ? row-3 : row-2);
	    //startC=((thisGame.whoseMove==Game.P1) ? 2 : 1);
		//handles the horizontal edges
		for(int r=startR; r >0; r=r-2){
			for(int c=startC; c <=col-2; c=c+2){
				if((board[r][c].getType()==Game.BLANK)){
					horiz=horiz+space;
				}else if((board[r][c].getType()==Game.P1LINEV) || (board[r][c].getType()==Game.P1LINEH)){
					horiz=horiz+"X";
				}else if((board[r][c].getType()==Game.P2LINEV) || (board[r][c].getType()==Game.P2LINEH)){
					horiz=horiz+"O";
				}
			}
		}
		return vert + horiz +";side=" +size;
		
	}

	@Override
	public
	String getGameName() {
		// TODO Auto-generated method stub
		return "connections";
	}

	@Override
	public
	void redoMove() {
		// TODO Auto-generated method stub
		gb.setIsRedo(true);
		gb.reUnDoMove(0,0);
		gb.setIsRedo(false);
	}

	@Override
	public
	void undoMove() {
		// TODO Auto-generated method stub
		gb.setIsUndo(true);
		gb.reUnDoMove(0,0);
		gb.setIsUndo(false);
	}
	
	/**
	 * Gets the number of moves so far. Override this to get the slider to work.
	 * @return the number of total moves done so far (should be updated to currentMove when
	 * undo -> doMove is done).
	 */
	public int getNumMovesSoFar() {
		return gb.g.movesSoFar;
	}
	
	/**
	 * Gets the index of the current move. Override this to get the slider to work.
	 * This is different from numMovesSoFar when undo/redo is done. Make sure to update this.
	 * @return the index of the current move.
	 */
	public int getCurrentMove() {
		return gb.g.currentMove;
	}

	@Override
	public
	void updatePredictionDisplay() {
		
		TextView remoteTextView = (TextView) findViewById(R.id.con_remoteness);
		if (!isShowPrediction()) {
			remoteTextView.setText("");
			
		} else if ((values != null) && (values.length != 0)) {
			previousValue = getBoardValue(values);

			int remoteness = getRemoteness(previousValue, values);
			//updateVVH(previousValue, remoteness);
			
			if (remoteness != -1)
				remoteTextView.setText(previousValue + " in " + (remoteness+1) + " moves");
			else remoteTextView.setText("Prediction not available.");
		} else if (!isNetworkAvailable) {
			remoteTextView.setText("Prediction not available.");
		} else {
			remoteTextView.setText((previousValue.equals("win") ? "lose" : "tie") + " in 0 moves");
		}
	}

	
	/**
	 * Updates whether or not the values will display on the game GUI.
	 * It is called when a user changes the "Show Values" option. 
	 */
	@Override
	public
	void updateValuesDisplay() {
	  if(isShowValues()){
		  if(previousValues !=null){
			  gb.revertImages(previousValues);
			  previousValues=null;
		  }
		  if(gb.g.isOver()){
				return; 
			 }
		 
		  
		  //gets an arraylist from generated moves
		  MoveValue []mvs=getNextMoveValues();
		  
		  LinkedList<int[]>values=gb.generateMoves(mvs);
		  
		
		   gb.swapImages(mvs);
		   previousValues=values;
		  
	  }else{
		  //reset them all. 
		  if(previousValues !=null){
			  gb.revertImages(previousValues);
			  previousValues=null;
		  }
	  }
	}
	

	@Override
	public void newGame() {
		    int sizeB=gb.size;
			gb.resetGame();
			gb = new GameBoard(sizeB,this);
			gb.g.whoseMove=Game.P1;
			updateValuesDisplay();
			
	}

	@Override
	public boolean isMoveInvalid(int move) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void updateUIRandom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUISmart() {
		// TODO Auto-generated method stub
		
	}

}