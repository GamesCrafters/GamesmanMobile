package com.gamescrafters.quickcross;



import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.gamescrafters.gamesmanmobile.GameActivity;
import com.gamescrafters.gamesmanmobile.R;

public class QuickCross extends GameActivity{
	public static final int HEIGHT = 3;
	public static final int WIDTH = 4;
	public static final int IN_ALIGN = 3;
	public final static String GAME_NAME = "QuickCross";
	public final static int EMPTY = 0;
	public final static int VERT = 2;
	public final static int HORIZ = 1;
	
	private GUIQuickCross qc;
	private TextView bottomText;
	public boolean player1Turn;//keeps track of whose turn;
	public int[][] board;
	

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setGameView(R.layout.quickcross_game);
		
		Intent myIntent = GameIntent = getIntent();
		
		qc = new GUIQuickCross(this,WIDTH,HEIGHT);
		bottomText = (TextView)findViewById(R.id.quickcross_gameover);
		isPlayer1Computer = false;//from GameActivity
		isPlayer2Computer = false;
		newGame();
	}
	
	
	public void newGame()
	{
		player1Turn = true;
		board = new int[HEIGHT][WIDTH];
		board[0][1] = HORIZ;
		bottomText.setText("Player1's Turn");
		updateVisualDisplay();
	}
	public void updateVisualDisplay()
	{
		qc.updateGraphics(board);
	}
	@Override
	public String getBoardString() {
		String game = "";
		for(int startRow = 0; startRow < HEIGHT; startRow++)
		{
			for (int startCol = 0; startCol < WIDTH; startCol++)
			{
				if(board[startRow][startCol] == 0)
					game = game + "_";
				else if(board[startRow][startCol] == 1)
					game = game + "H";
				else
					game = game + "V";
			}
		}
		game = game + ";size=12";
		return game;
	}

	public String getGameName() {
		return GAME_NAME;
	}

	@Override
	public void undoMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoMove() {
		// TODO Auto-generated method stub
		
	}

	//represents moves as an array of Strings where the first element gives the row of the move
	//the second the column of move, the third gives the move orientation, the fourth gives type of move
	//ex: 1 2 H P
	
	//not done
	public void doMove(String[] move) {
		updateVisualDisplay();
		if(!isMoveInvalid(move))
		{
			//qc.updateGraphics(board);
			int row = Integer.parseInt(move[0]);
			int col = Integer.parseInt(move[1]);
			String move_orientation = move[2];
			String move_type = move[3];
			if(move_orientation.equals("H"))
				board[row][col] = HORIZ;
			else
				board[row][col] = VERT;
			
			if(isGameOver())
			{ 
				bottomText.setText("Game over.\n" + (player1Turn ? "Player1" : "Player2") + " wins!");
				player1Turn = !player1Turn;
				return;
			}
			else
			{
				player1Turn = !player1Turn;
				bottomText.setText((player1Turn? "Player1" : "Player2") + "'s Turn");
			}
		}
	}


	public boolean isMoveInvalid(String[] move) {
		if(Integer.parseInt(move[0]) >= HEIGHT || Integer.parseInt(move[1]) >= WIDTH)
			return true;
		if(!move[2].equals("H") && !move[2].equals("V"))
			return true;
		if(!move[3].equals("P") && !move[3].equals("S"))
			return true;
		return false;
	}

	public boolean isGameOver()
	{
		for(int startRow = 0; startRow < HEIGHT; startRow++)
		{
			for(int startCol = 0; startCol < WIDTH; startCol++)
			{
				boolean case1 = true;
				//horizontal case for horizontal
				for(int inACol = startCol; inACol < startCol + IN_ALIGN; inACol++)
				{
					if(inACol >= WIDTH || board[startRow][inACol] != 1)
						case1 = false;
				}
				if(case1)
					return true;
				
				//horizontal case for vertical
				boolean case2 = true;
				for(int inACol = startCol; inACol < startCol + IN_ALIGN; inACol++)
				{
					if(inACol >= WIDTH || board[startRow][inACol] != 2)
						case2 = false;
				}
				if(case2)
					return true;
				
				//vertical case for horizontal
				boolean case3 = true;
				for(int inARow = startRow; inARow < startRow + IN_ALIGN; inARow++)
				{
					if(inARow >= HEIGHT || board[inARow][startCol] != 1)
						case3 = false;
				}
				if(case3)
					return true;
				
				//vertical case for vertical
				boolean case4 = true;
				for(int inARow = startRow; inARow < startRow + IN_ALIGN; inARow++)
				{
					if(inARow >= HEIGHT || board[inARow][startCol] != 2)
						case4 = false;
				}
				if(case4)
					return true;
				
				//diagonal case (positive slope) for horizontal
				boolean case5 = true;
				for(int inARow = startRow; inARow < startRow + IN_ALIGN;)
				{
					for(int inACol = startCol; inACol > startCol - IN_ALIGN; inACol--)
					{
						if(inARow >= HEIGHT || inACol < 0 || board[inARow][inACol] != 1)
							case5 = false;
						inARow++;
					}
				}
				if(case5)
					return true;
				
				//diagonal case (positive slope) for vertical
				boolean case6 = true;
				for(int inARow = startRow; inARow < startRow + IN_ALIGN;)
				{
					for(int inACol = startCol; inACol > startCol - IN_ALIGN; inACol--)
					{
						if(inARow >= HEIGHT || inACol < 0 || board[inARow][inACol] != 2)
							case6 = false;
						inARow++;
					}
				}
				if(case6)
					return true;
				
				//diagonal case (negative slope) for horizontal
				boolean case7 = true;
				for(int inARow = startRow; inARow < startRow + IN_ALIGN;)
				{
					for(int inACol = startCol; inACol < startCol + IN_ALIGN; inACol++)
					{
						if(inARow >= HEIGHT || inACol >= WIDTH || board[inARow][inACol] != 1)
							case7 = false;
						inARow++;
					}
				}
				if(case7)
					return true;
				
				//diagonal case (negative slope) for vertical
				boolean case8 = true;
				for(int inARow = startRow; inARow < startRow + IN_ALIGN;)
				{
					for(int inACol = startCol; inACol < startCol + IN_ALIGN; inACol++)
					{
						if(inARow >= HEIGHT || inACol >= WIDTH || board[inARow][inACol] != 2)
							case8 = false;
						inARow++;
					}
				}
				if(case8)
					return true;
			}
		}
		return false;	
		
	}
	@Override
	public void updateValuesDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePredictionDisplay() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void updateUISmart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUIRandom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMove(String move) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMoveInvalid(int move) {
		// TODO Auto-generated method stub
		return true;
	}
	/*
	public static void main(String[] args)
	{
		System.out.println("Enter Your Move: ");
		QuickCross game = new QuickCross();
		System.out.println("Enter Your Move: ");
		game.newGame();
		while(true)
		{
			System.out.println("Enter Your Move: ");
			Scanner input = new Scanner(System.in);
			String move = input.next();
			game.doMove(move);
			System.out.println(game);
			if(game.isGameOver())
				break;
		}
	}
	*/

}
