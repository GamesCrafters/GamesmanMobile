package com.gamescrafters.onetwoten;

/* Henry Bradlow, Andrew, Chi - First Project
 * OneTwoTen
 */

import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.R;

public class OneTwoTen extends GameActivity{
	public static final String GAME_NAME = "One Two... Ten!";
	public static final int EMPTY = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	public static final int WIN = 3;
	public static final int LOSE = 4;
	public static final int HEIGHT = 10;
	
	
	private GUIOneTwoTen myGUI;
	private int[] myBoard;
	private boolean blueTurn; // blue = player one
	private int myTotal;
	private TextView myBottomText;
	private Stack<Integer> nextMoves, previousMoves;
	private int myMovesSoFar, myCurrentMoveNumber;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setGameView(R.layout.onetwoten_game);
		myGUI = new GUIOneTwoTen(this,HEIGHT);
		
		Intent myIntent = GameIntent = getIntent();
		myBottomText = (TextView)findViewById(R.id.onetwoten_gameover);
		isPlayer1Computer = false;
		isPlayer2Computer = true;
		newGame();
	}
	@Override
	public String getBoardString() {
		StringBuffer board = new StringBuffer();
		for (int i=0; i < myBoard.length; i++) { //height
				int currentElem = myBoard[i];
				if (currentElem == OneTwoTen.RED)
					board.append("O");
				else if (currentElem == OneTwoTen.BLUE)
					board.append("X");
				else
					board.append("%20");
		}
		board.append(";height=");
		board.append(myBoard.length);
		return board.toString();
	}
	@Override
	public String getGameName() {
		return GAME_NAME;
	}
	@Override
	public void undoMove() {// not quite working!!!  doesnt handle turn switching quite right...
		if(previousMoves.isEmpty()) return;
		int move = previousMoves.pop();
		nextMoves.push(move);
		myBoard[move] = EMPTY;
		myTotal = -1;
		for(int i = 0; i<myBoard.length; i++)
			if(myBoard[i]==RED||myBoard[i]==BLUE) myTotal = i;
		blueTurn = !blueTurn;
		
		myCurrentMoveNumber--;
		hSlider.updateProgress(myCurrentMoveNumber, myMovesSoFar);
		
		removeLastVVHNode();
		
		myBottomText.setText("");
		updateValuesDisplay();
	}
	@Override
	public void redoMove() {
		if(nextMoves.isEmpty()) return;
		int move = nextMoves.pop();
		previousMoves.push(move);
		
		char[] values = getMoveValues();
		updateVVH(values[move-myTotal-1]=='l' ? "win" : "lose", 9-myTotal, false, blueTurn, false);
		
		myBoard[move] = blueTurn ? BLUE : RED;
		myTotal = move;
		if(isGameOver())
			myBottomText.setText("Game over.\n" + (blueTurn ? "Blue" : "Red") + " wins!");
		blueTurn = !blueTurn;
		
		myCurrentMoveNumber++;
		hSlider.updateProgress(myCurrentMoveNumber, myMovesSoFar);
		updateValuesDisplay();
	}
	@Override
	public int getCurrentMove()
	{
		return myCurrentMoveNumber;
	}
	@Override
	public int getNumMovesSoFar()
	{
		return myMovesSoFar;
	}
	@Override
	public void doMove(String move) {
		// TODO Auto-generated method stub
	}
	public boolean isGameOver()
	{
		return myTotal>=HEIGHT-1;
	}
	public void doMove(int h)
	{
		if(!isMoveInvalid(h))
		{
			previousMoves.push(h);
			nextMoves.clear();
			
			myCurrentMoveNumber++;
			myMovesSoFar = myCurrentMoveNumber;
			hSlider.updateProgress(myCurrentMoveNumber, myMovesSoFar);
			
			char[] values = getMoveValues();
			updateVVH(values[h-myTotal-1]=='l' ? "win" : "lose", 9-myTotal, false, blueTurn, false);
			myBoard[h] = blueTurn ? BLUE : RED;
			myTotal = h;
			if(isGameOver())
			{
				myBottomText.setText("Game over.\n" + (blueTurn ? "Blue" : "Red") + " wins!");
				updateValuesDisplay();
				myGUI.updateGraphics(myBoard);
				updateVVH("1", 9-myTotal, true, blueTurn, false);
				blueTurn = !blueTurn;
				return;
			}
			blueTurn = !blueTurn;	
			if((blueTurn ? isPlayer1Computer : isPlayer2Computer))
				playSmart();
			updateValuesDisplay();
		}
	}
	public void playRandom()
	{
		int move = 0;
		do
			move = myTotal+(int)(Math.random()*2+1);
		while(isMoveInvalid(move));
		doMove(move);
	}
	public void playSmart()
	{
		if(myTotal>=9)
			return;
		char[] values = this.getMoveValues();
		if(values[1] == 'l' && !isMoveInvalid(myTotal+2))
			doMove(myTotal+2);
		else
			doMove(myTotal+1);
	}
	public char[] getMoveValues()
	{
		char[] values = new char[2];
		for(int i = 1; i<=2; i++)
		{
			switch(myTotal+i)
			{
			case 0:
				values[i-1] = 'l';
				break;
			case 1:
				values[i-1] = 'w';
				break;
			case 2:
				values[i-1] = 'w';
				break;
			case 3:
				values[i-1] = 'l';
				break;
			case 4:
				values[i-1] = 'w';
				break;
			case 5:
				values[i-1] = 'w';
				break;
			case 6:
				values[i-1] = 'l';
				break;
			case 7:
				values[i-1] = 'w';
				break;
			case 8:
				values[i-1] = 'w';
				break;
			case 9:
				values[i-1] = 'l';
				break;
			case 10:
				values[i-1] = 'l';
				break;
			}
		}
		return values;
	}
	@Override
	public void newGame() {
		blueTurn = true;
		myBoard = new int[HEIGHT];
		nextMoves = new Stack<Integer>();
		previousMoves = new Stack<Integer>();
		myTotal = -1;
		myMovesSoFar = 0;
		myCurrentMoveNumber = 0;
		hSlider.updateProgress(myCurrentMoveNumber, myMovesSoFar);
		myBottomText.setText("");
		clearVVH();
		updateValuesDisplay();
		if(isPlayer1Computer)
			playSmart();
	}
	@Override
	public boolean isMoveInvalid(int move) {
		if(move>=myBoard.length) return true;
		return myBoard[move]==RED || myBoard[move]==BLUE || move-myTotal>2 || move<myTotal;
	}
	@Override
	public void updateValuesDisplay() {
		myGUI.setDrawMoveValues(isShowValues);
		for(int i = 0; i<myBoard.length; i++)
			if(myBoard[i]==WIN || myBoard[i]==LOSE)
				myBoard[i] = EMPTY;
		char[] values = getMoveValues();
		for(int i = 1; i<=2; i++)
			if(values[i-1]=='w' && myTotal+i<myBoard.length)
				myBoard[myTotal+i] = WIN;
			else if(myTotal+i<myBoard.length)
				myBoard[myTotal+i] = LOSE;
		myGUI.updateGraphics(myBoard);
	}
	@Override
	public void updatePredictionDisplay() {
		
	}
	@Override
	public void updateUISmart() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateUIRandom() {
		// TODO Auto-generated method stub
		
	}
}
