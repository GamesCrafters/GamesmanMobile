package com.gamescrafters.Y;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.MoveValue;
import com.gamescrafters.gamesmanmobile.R;

public class YInterface extends GameActivity {

	Board b = new Board();
	//int firstID;
	int numButtons;
	boolean isOver;
	boolean showmoveValues;
	boolean hasshownmoveValues;
	Drawable line;
	AbsoluteLayout myLayout;
	Activity myself = this;
	ArrayList<View> connections = new ArrayList<View>();
	ArrayList<View> Nodes = new ArrayList<View>();
	int movesSoFar, currentMove;
	//HashMap<Integer, View[]> connections = new HashMap<Integer, View[]>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setGameView(R.layout.y_board);
		//firstID = R.id.y_B0;      //ID of first button
		numButtons = 18;        //total number of Buttons
		isOver = false;
		showmoveValues = false;
		hasshownmoveValues = false;
		movesSoFar = 0;
		currentMove = 0;
		setButtons();
		//Button undo = (Button) findViewById(R.id.y_Undo);
		//Button redo = (Button) findViewById(R.id.y_Redo);
		//redo.setOnClickListener(new RedoClickListener(b));
		//undo.setOnClickListener(new UndoClickListener(b));
		//Button Replay = (Button) findViewById(R.id.y_Replay);
		//Replay.setOnClickListener(new ReplayClickListener(b));
		myLayout = (AbsoluteLayout) findViewById(R.id.y_RootLayout);
		
	}
	
	//IN GAME MENU
/*	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		menu.add("New Game");
		menu.add("Undo");
		menu.add("Redo");
		menu.add("Toggle Move Values");
		return true; 
	} 

	public boolean onOptionsItemSelected(MenuItem item) {
		CharSequence title = item.getTitle();
		if (title.equals("New Game")) {
			clearButtons();
			b.Clear(b.bd);
			isOver = false;
			TextView t = (TextView)findViewById(R.id.y_TextView01);  //sets textbox below to display button id.
			t.setText("");
		} else if(title.equals("Redo")){
			int i = b.redo();
			Drawable d;
			if (i > -1) {
				View x = findViewByTag(i);
				if( b.currentPlayer == PieceColor.P1){
					d = getResources().getDrawable(R.drawable.y_circlered);
				}
				else{
					d = getResources().getDrawable(R.drawable.y_circleblue);	
				}
				x.setBackgroundDrawable(d);
				x.setClickable(false);
				DoMove(Integer.parseInt((String)x.getTag()));
			}
		 } else if (title.equals("Undo")){
				int i = b.undo();
				if (i > -1) {
					View x = findViewByTag(i);
					x.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_buttoncolor));
					x.setClickable(true);
				}
				if (isOver) {
					isOver = false;
				}
				TextView t = (TextView)findViewById(R.id.y_TextView01);  //sets textbox below to display button id.
				t.setText("");
			
		 }else if (title.equals("Toggle Move Values")){
			 
			 if(!showmoveValues){
				 hasshownmoveValues = true;
			 }
			 
			 showmoveValues = !showmoveValues;
			 
			 if(showmoveValues){
			 TextView t = (TextView)findViewById(R.id.y_Green);  //sets textbox below to display button id.
			 t.setText("Win");
			 
			 TextView u = (TextView)findViewById(R.id.y_Red);  //sets textbox below to display button id.
			 u.setText("Lose");
			 
			 TextView v = (TextView)findViewById(R.id.y_Yellow);  //sets textbox below to display button id.
			 v.setText("Tie");
			 }
			 
			 else{
				 TextView t = (TextView)findViewById(R.id.y_Green);  //sets textbox below to display button id.
				 t.setText("");
				 
				 TextView u = (TextView)findViewById(R.id.y_Red);  //sets textbox below to display button id.
				 u.setText("");
				 
				 TextView v = (TextView)findViewById(R.id.y_Yellow);  //sets textbox below to display button id.
				 v.setText("");
				 }
				 
			 //updateMoveValues();
			 }
			 
			 
		 
		return true;
	}


	
	
	*/
	//END OF IN GAME MENU

	class RedoClickListener implements View.OnClickListener{
		Board b;
		public RedoClickListener(Board b) {
			this.b = b;
		}
		public void onClick(View v) {
			redoMove();
//			int i = b.redo();
//			Drawable d;
//			if (i > -1) {
//				View x = findViewByTag(i);
//				if( b.currentPlayer == PieceColor.P1){
//					d = getResources().getDrawable(R.drawable.y_circlered);
//				}
//				else{
//					d = getResources().getDrawable(R.drawable.y_circleblue);	
//				}
//				x.setBackgroundDrawable(d);
//				x.setClickable(false);
//				DoMove(Integer.parseInt((String)x.getTag()));
//				//updateMoveValues();
//			}
		}
	}

	class UndoClickListener implements View.OnClickListener{
		Board b;
		public UndoClickListener(Board b) {
			this.b = b;
		}

		public void onClick(View v) {
			undoMove();
//			int i = b.undo();
//			if (i > -1) {
//				View x = findViewByTag(i);
//				x.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circle));
//				x.setClickable(true);
//			}
//			if (isOver) {
//				isOver = false;
//			}
//			TextView t = (TextView)findViewById(R.id.y_TextView01);  //sets textbox below to display button id.
//			t.setText("");
//			
//			//updateMoveValues();
//			View orig = findViewByTag(i);
//			int x = (orig.getLeft() + orig.getRight() )/2;
//			int y = (orig.getTop() + orig.getBottom()) /2;
//			for (int j = 0; j< connections.size(); j++) {
//				SampleView edge = (SampleView) connections.get(j);
//				if (((edge.startx == x) && (edge.starty == y)) || ((edge.endx == x) && (edge.endy == y))) {
//					myLayout.removeView(edge);
//				}
//				
//			}
			
		}
	}

	class ReplayClickListener implements View.OnClickListener{

		Board b;
		public ReplayClickListener(Board b) {
			this.b = b;
		}

		public void onClick(View v) {
			newGame();
//			movesSoFar = currentMove = 0;
//			hSlider.updateProgress(currentMove, movesSoFar);
//			clearButtons();
//			b.Clear(b.bd);
//			isOver = false;
//			TextView t = (TextView)findViewById(R.id.y_TextView01);  //sets textbox below to display button id.
//			t.setText("");
//			updateValuesDisplay();
		}
	}

	class BClickListener implements View.OnClickListener{
		Board b;

		public BClickListener(Board b) {
			this.b = b;
		}

		public void onClick(View v) {
			movesSoFar = currentMove; 
			Drawable d;
			if (isOver) {
				return;
			}
			b.redoMoves.clear();
			if( b.currentPlayer == PieceColor.P1){
				d = getResources().getDrawable(R.drawable.y_circlered);
			}
			else{
				d = getResources().getDrawable(R.drawable.y_circleblue);	
			}
			v.setBackgroundDrawable(d);
			v.setClickable(false);
			DoMove(Integer.parseInt((String) v.getTag()));
			int startx = (v.getLeft() + v.getRight())/2;
			int starty = (v.getTop() + v.getBottom())/2;
			ArrayList<Integer> adj = b.bd[Integer.parseInt((String) v.getTag())].adj;
			for (int i = 0; i < adj.size(); i++) {
				View v2 = findViewByTag(adj.get(i));
				if (b.bd[Integer.parseInt((String) v.getTag())].color == b.bd[adj.get(i)].color) {
					int endx = (v2.getLeft() + v2.getRight())/2;
					int endy = (v2.getTop() + v2.getBottom())/2;
					if (b.bd[Integer.parseInt((String) v.getTag())].color == PieceColor.P1){
						View s = new SampleView(myself, startx, starty, endx, endy, Color.RED);
						myLayout.addView(s);
						connections.add(s);
					} else {
						View s = new SampleView(myself, startx, starty, endx, endy, Color.BLUE);
						myLayout.addView(s);
						connections.add(s);
					}
				}
			}
			updateValuesDisplay();
		}
		
	}


	public void setButtons(){
		int IDcounter = 0;
		ImageButton button;  

		for(int i = 0; i<numButtons; i++){    //Set IDlistener to all buttons      
			button = (ImageButton) this.findViewByTag(IDcounter);
			if(button.isClickable() && button.isPressed()){
			}	else{
				button.setOnClickListener(new BClickListener(b));
			}
			IDcounter++;
		}

	}

	public void DoMove(int x){
		int moveNum;     //translating button id to num of ChooseMove
		int winner;
		moveNum = x;
		winner = b.makeMove(moveNum);
		if(movesSoFar == currentMove){
			movesSoFar++;
			currentMove++;
		}
		hSlider.updateProgress(currentMove, movesSoFar);
		if(winner!=0){
			TextView t = (TextView)findViewById(R.id.y_TextView01);  //sets textbox below to display button id.
			t.setText("Player " + Integer.toString(winner) + " Wins!!!11!!");
			isOver = true;
		}
	}
	
	private View findViewByTag(int i) {
		View parentView = findViewById(R.id.y_AbsoluteLayout01);
		String t = Integer.toString(i);
		return parentView.findViewWithTag(t);
	}
	private void clearButtons() {
		int IDcounter = 0;
		ImageButton button;
		for (int i = 0; i< numButtons; i++) {
			button = (ImageButton) this.findViewByTag(IDcounter);
			button.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circle));
			button.setClickable(true);
			IDcounter++;
		}
		for (int j = 0; j<connections.size(); j++) {
			myLayout.removeView(connections.get(j));
		}
	}
	private static class SampleView extends View
	{         
		int startx;
		int starty;
		int endx;
		int endy;
		int c;
		public SampleView(Context context, int startx, int starty, int endx, int endy, int c)
		{
			super(context);
			this.startx = startx;
			this.starty = starty;
			this.endx = endx;
			this.endy = endy;
			this.c = c;

		}

		protected void onDraw(Canvas canvas)
		{
			Paint p = new Paint();
			p.setColor(c);
			p.setStrokeWidth(6);
			p.setStyle(Paint.Style.STROKE);

			//canvas.drawColor(Color.BLUE);
			canvas.drawLine(startx, starty, endx, endy, p);

		}
	}
	public String getBoardString() {
		   String ret = "";
		   for (int i = 0; i < b.bd.length; i++) {
			if (b.bd[i].color == PieceColor.EMPTY) {
				ret = ret + "%20";
			} else if (b.bd[i].color == PieceColor.P1) {
				ret = ret + "X";
			} else {
				ret = ret + "O";
			}
		}
		   ret = ret + ";centerRows=2;outerRows=2";
		   return ret;
	}
	
	
	public void updateValuesDisplay(){
		MoveValue[] mvs = getNextMoveValues();
		int IDcounter = 0;
		ImageButton button;
		int mvalue;
		if(isShowValues()){
			/*
			for (int i = 0; i< numButtons; i++) {
				button = (ImageButton) this.findViewByTag(IDcounter);
				  if(button.isClickable()){
				      mvalue = generateMoveValue();
				      if(mvalue == 0){
					  button.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circlegreen));
				      }
				      else{
				    	  button.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circledarred));
				      }
				  }
				IDcounter++;
			}
			*/
			for (int i = 0; i < mvs.length; i++) {
				button = (ImageButton) this.findViewByTag(mvs[i].getIntMove());
				if (mvs[i].getValue().equals("win")) {
					button.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circlegreen));
				} else {
					button.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circledarred));
				}
			}
		}
		
		else{
			
			for (int i = 0; i< numButtons; i++) {
				button = (ImageButton) this.findViewByTag(IDcounter);
				  if(button.isClickable()){
					  button.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circle));
				  }
				  IDcounter++;
			}
			
		}
		
	}
	
	int generateMoveValue(){
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(2);
	    return randomInt;	
	}
	public String getGameName() {
		return "y";
	}
	public void redoMove() {
		if(currentMove < movesSoFar){
			int i = b.redo();
			Drawable d;
			if (i > -1) {
				View x = findViewByTag(i);
				if( b.currentPlayer == PieceColor.P1){
					d = getResources().getDrawable(R.drawable.y_circlered);
				}
				else{
					d = getResources().getDrawable(R.drawable.y_circleblue);	
				}
				x.setBackgroundDrawable(d);
				x.setClickable(false);
				DoMove(Integer.parseInt((String)x.getTag()));
				updateValuesDisplay();
			}
			currentMove++;
			hSlider.updateProgress(currentMove, movesSoFar);
		}
	}
	
	public void undoMove() {
		if(currentMove > 0){
			int i = b.undo();
			if (i > -1) {
				View x = findViewByTag(i);
				x.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_circle));
				x.setClickable(true);
			}
			if (isOver) {
				isOver = false;
			}
			TextView t = (TextView)findViewById(R.id.y_TextView01);  //sets textbox below to display button id.
			t.setText("");
			
			updateValuesDisplay();
			View orig = findViewByTag(i);
			int x = (orig.getLeft() + orig.getRight() )/2;
			int y = (orig.getTop() + orig.getBottom()) /2;
			for (int j = 0; j< connections.size(); j++) {
				SampleView edge = (SampleView) connections.get(j);
				if (((edge.startx == x) && (edge.starty == y)) || ((edge.endx == x) && (edge.endy == y))) {
					myLayout.removeView(edge);
				}
				
			}
			currentMove--;
			hSlider.updateProgress(currentMove, movesSoFar);
		}
	}
	
	public void updatePredictionDisplay() {}
	public void newGame() {
		currentMove = movesSoFar = 0;
		hSlider.updateProgress(currentMove, movesSoFar);
		clearButtons();
		b.Clear(b.bd);
		isOver = false;
		TextView t = (TextView)findViewById(R.id.y_TextView01);  //sets textbox below to display button id.
		t.setText("");
		updateValuesDisplay();
	}
	
	public void doMove(String s) {}



}