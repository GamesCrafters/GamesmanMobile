package com.gamescrafters.gamesmanmobile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * An abstract class to be extended by all games in GamesmanMobile.
 * Allows the GameController to interface with the new game.
 */
public abstract class GameActivity extends Activity {
	String gameName;
	public  boolean isPlayer1Computer, isPlayer2Computer, isBlueTurn, isShowValues = false, isShowPrediction = false;
	public boolean isDatabaseAvailable = RemoteGameValueService.isInternetAvailable();
	protected Map<String, MoveValue[]> moveValues; // A map of boards to MoveValue[]. Keeps undos/redos from making web calls.
	static public LinkedList<VVHNode> VVHList; // List of Visual Value History nodes added since the last VisualValueHistory update.
	static public Intent GameIntent, VVHIntent; // Intents for the Connect4 Activity and the VisualValueHistory Activity.
	static int numMovesToRemove = 0; // A counter for how many moves have been undone since the last VisualValueHistory update.
	static boolean clear_vvh = false;
	MenuItem moves, prediction;
	protected HorizontalSlider hSlider;
	private ImageButton undoButton, redoButton;
	private static final int NEW_GAME = 0, TOGGLE_MOVE_VALUES = 1, TOGGLE_PREDICTION = 2, DISPLAY_VVH = 3, SWITCH_PLAYERS = 4;
	private static final int VVHTAG = 11111;
	private Thread dbChecker;
	private Runnable dbUpdater;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_activity_layout);
		gameName = getGameName();
		moveValues = new HashMap<String, MoveValue[]>();
		initBottomBar();
		/* Setting flags for the Intents so they bring an existing Activity
		 * instance to the top of the stack, instead of creating a new
		 * instance.
		 */
		GameIntent = getIntent();
		GameIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		VVHIntent = new Intent(this, VisualValueHistory.class);
		VVHIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// Create a new VVHList.
		VVHList = new LinkedList<VVHNode>();
		
		/*
		 * Run this thread in the background to continuously check for
		 * database connection.
		 */
		//TODO: what if internet fails during game?
		dbUpdater = new Runnable() {
			public void run() {
				isDatabaseAvailable = RemoteGameValueService.isInternetAvailable();
			}
		};
		dbChecker = new Thread(dbUpdater);
		dbChecker.setDaemon(true);
		dbChecker.setPriority(Thread.MIN_PRIORITY);
		dbChecker.start();
	}
	@Override
	public void onBackPressed()
	{
		this.finishActivity(VVHTAG);
		super.onBackPressed();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}

	protected void setPlayers(boolean player1, boolean player2) {
		isPlayer1Computer = player1;
		isPlayer2Computer = player2;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		menu.add(0, NEW_GAME, 0, "New Game");
		menu.add(0, TOGGLE_MOVE_VALUES, 0, "Toggle Move Values");
		menu.add(0, TOGGLE_PREDICTION, 0, "Toggle Prediction");
		menu.add(0, DISPLAY_VVH, 0, "Display VVH");
		menu.add(0, SWITCH_PLAYERS, 0, "Switch Player");
		return true;
	} 

	public boolean onOptionsItemSelected(MenuItem item) {
//		CharSequence title = item.getTitle();
//		if (title.equals("New Game")) {
//			newGame();
//		} else if (title.equals("Toggle Move Values")){
//			isShowValues = !isShowValues;
//			updateValuesDisplay();
//		} else if (title.equals("Toggle Prediction")) {
//			isShowPrediction = !isShowPrediction;
//			updatePredictionDisplay();
//		} else {
//			updateVVHDisplay();
//		}
		switch(item.getItemId()){
		case NEW_GAME:
			newGame();
			break;
		case TOGGLE_MOVE_VALUES:
			isShowValues = !isShowValues;
			updateValuesDisplay();
			break;
		case TOGGLE_PREDICTION:
			isShowPrediction = !isShowPrediction;
			updatePredictionDisplay();
			break;
		case DISPLAY_VVH:
			updateVVHDisplay();
			break;
		case SWITCH_PLAYERS:
			updatePlayerInfo();
			break;
		}
		return true; 
	}

	/**
	 * @return The String representation of the board, to be used in the web database query.  
	 */
	public abstract String getBoardString();

	/**
	 * @return The name of the game, as used in the web database query.
	 */
	public abstract String getGameName();

	/**
	 * Undoes a previously done move, or does nothing if at start of game. 
	 */
	public abstract void undoMove();

	/**
	 * Redos a previously undone move, or does nothing if at latest position. 
	 */
	public abstract void redoMove();

	/**
	 * Given a String move in the database move representation, does the move.
	 * @param move The String move, as represented in the web API (solver team should know how).
	 */
	public abstract void doMove(String move);

	public abstract void newGame();

	/**
	 * Given a move, returns true if this move is valid and false otherwise.
	 */
	public abstract boolean isMoveInvalid(int move);
	
	/**
	 * @return The type of the game (default "puzzles"). Override if otherwise.
	 */
	public String getGameType() {
		return "puzzles";
	}

	/**
	 * Looks for a MoveValue corresponding to the board returned by getBoard by querying the database. 
	 * Should be used for calling updateVVH. For better efficiency, call only on initial board, and
	 * then pass correct value from getNextMoveValues(). 
	 * @return The MoveValue of this board.
	 */
	public MoveValue getMoveValue() {
		return GameValueService.getMoveValue(getGameType(), getGameName(), getBoardString());
	}

	/**
	 * Looks for the MoveValues corresponding to the children of the board returned by getBoard, first
	 * in the local table, then querying the database and adding to the local table if not found.
	 * @return The MoveValues of the boards reachable by a move from this board.
	 */
	public MoveValue[] getNextMoveValues() {
		
		String board = getBoardString();
		MoveValue[] mvs = moveValues.get(board);
		if (mvs == null){
			mvs = GameValueService.getNextMoveValues(getGameType(), getGameName(), board);
			moveValues.put(board, mvs);
		}
		return mvs;
	}

	/**
	 * @return Whether or not the "Show Value" option is checked.
	 */
	public boolean isShowValues() {
		return isShowValues;
	}

	/**
	 * Updates whether or not the values will display on the game GUI.
	 * It is called when a user changes the "Show Values" option. 
	 */
	public abstract void updateValuesDisplay();

	/**
	 * Displays the Visual Value History for this game.
	 */
	public void updateVVHDisplay() {
		this.startActivityForResult(VVHIntent,VVHTAG);
	}

	/**
	 * Clears the Visual Value History if a new game is needed.
	 */
	public void clearVVH() {
		clear_vvh = true;
	}

	/**
	 * Marks another move to be removed from the Visual Value History.
	 */
	public void removeLastVVHNode() {
		if (VVHList.isEmpty())
			numMovesToRemove++;
		else VVHList.removeLast();
	}


	/**
	 * @return Whether or not the "Show Predictions" option is checked.
	 */	
	public boolean isShowPrediction() {
		return isShowPrediction;
	}

	/**
	 * Updates whether or not the prediction will display on the game GUI.
	 * It is called when a user changes the "Show Prediction" option.
	 */
	public abstract void updatePredictionDisplay();

	/**
	 * Updates the Visual Value History. Should be called at an appropriate place for each move.
	 * @param val The MoveValue to add as a node to the VVH (i.e. the one returned by getMoveValue()).
	 */
	public void updateVVH (String previousValue, int remoteness,
			boolean gameOver, boolean isBlueTurn, boolean isDraw) {
		VVHNode node;
			if (remoteness == -1) return;
			if (gameOver && isDraw)
			node = new VVHNode("gameover-tie", remoteness, !isBlueTurn);
			else if (gameOver)
				node = new VVHNode("gameover", remoteness, !isBlueTurn);
			else node = new VVHNode(previousValue, remoteness, isBlueTurn);
			VVHList.add(node);
	}
	
	/**
	 * Updates the player information if the user decides to switch one or more players
	 * from human to computer or vice versa.
	 */
	public void updatePlayerInfo() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String player_1 = GameIntent.getStringExtra("player1_name");
		String player_2 = GameIntent.getStringExtra("player2_name");
		final CharSequence[] items = {player_1 + " to Human", player_2 + " to Human",
									player_1 + " Computer", player_2 + " to Computer"};
		                /*
		                 * Player changes, if any
		                 */
		builder.setTitle("Switch Player");
		       		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
		       			public void onClick(DialogInterface dialog, int item) {
		       				switch(item){
		       				case 0:
		       					isPlayer1Computer = false;
		       					GameIntent.putExtra("isPlayer1Computer", false);
		    					break;
		       				case 1:
		    					isPlayer2Computer = false;
		    					GameIntent.putExtra("isPlayer2Computer", false);
		    					break;
		    				case 2:
		    					isPlayer1Computer = true;
		    					GameIntent.putExtra("isPlayer1Computer", true);
		    					
		    					switchToComputer();
		    					break;
		    				case 3:
		    					isPlayer2Computer = true;
		    					GameIntent.putExtra("isPlayer2Computer", true);
		    					
		    					switchToComputer();
		    					break;
		    		    	}
		    		    	dialog.dismiss();
		    		    }
		    		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * Helper method for updatePlayerInfo(). Handles case where at least one player
	 * is a computer.
	 */
	public void switchToComputer() {
		if (isPlayer1Computer && isPlayer2Computer) {
			if (isDatabaseAvailable) {
				updateUISmart();
			} else {
				updateUIRandom();
			}
		}
		if (isPlayer1Computer || isPlayer2Computer) {
			if (isDatabaseAvailable) {
				doComputerMove();
			} else {
				playRandom();
			}
		}
	}

	/**
	 * Determines the value of the current board or position.
	 * @param values The MoveValues for the children of this board (returned by getNextMoveValues()).
	 * @return "win" if state is a win, "tie" if it is a tie, or "lose" if it is a lose.
	 */
	public static String getBoardValue(MoveValue[] mv) {
		for (MoveValue v : mv) {
			if (v.getValue().equals("win")) return "win";
		}
		for (MoveValue v : mv) {
			if (v.getValue().equals("tie")) return "tie";
		}
		return "lose";
	}

	/**
	 * Gets the number of moves so far. Override this to get the slider to work.
	 * @return the number of total moves done so far (should be updated to currentMove when
	 * undo -> doMove is done).
	 */
	public int getNumMovesSoFar() {
		return 0;
	}
	
	/**
	 * Gets the index of the current move. Override this to get the slider to work.
	 * This is different from numMovesSoFar when undo/redo is done. Make sure to update this.
	 * @return the index of the current move.
	 */
	public int getCurrentMove() {
		return 0;
	}
	

	/**
	 * gets hSlider so so you can update the progress after a call to redo/undo or a regular doMove
	 * @return the index of the current move. -added by Derrick
	 */
	public HorizontalSlider getHSlider() {
		return hSlider;
	}
	
	public void goToMoveN(int N) {
		if (0 <= N && N <= getNumMovesSoFar()) {
			while (N < getCurrentMove()) {
				undoMove();
			}
			while (N > getCurrentMove()) {
				redoMove();
			}
		}
	}

	/**
	 * Calculates the remoteness of this board/position.
	 * @param boardValue The value of the current board ("win", "lose", or "tie").
	 * @param values The MoveValues for the children of this board (returned by getNextMoveValues()).
	 * @return The remoteness of the current game state, or -1 if not available.
	 */
	public static int getRemoteness(String boardValue, MoveValue[] values){
		if (values == null || values.length == 0) return -1;

		boolean max = boardValue.equals("lose") || boardValue.equals("tie");
		int remoteness = max ? 0 : Integer.MAX_VALUE;
		for (MoveValue v : values){
			if (!v.hasRemoteness()) return -1;
			if (v.getValue().equals(boardValue)){
				if (max) remoteness = Math.max(v.getRemoteness(), remoteness);
				else remoteness = Math.min(v.getRemoteness(), remoteness);
			}
		}
		return remoteness;
	}
	
	/**
	 * updates the UI with a smart computer move (i.e. move and remoteness values
	 * are available and database is being used)
	 */
	public abstract void updateUISmart();
	
	/**
	 * updates the UI with a random computer move
	 * called when no connection to the database is available
	 */
	public abstract void updateUIRandom();

	/**
	 * Determines and executes the best possible move
	 * during a computer's turn.
	 */
	public void doComputerMove() {
		MoveValue values[] = getNextMoveValues();
		MoveValue bestMove = values[0];
		Random gen = new Random();
		for (MoveValue val : values) {
			String bestMoveValue = bestMove.getValue();
			String valValue = val.getValue();
			int valRemoteness = val.getRemoteness();
			int bestMoveRemoteness = bestMove.getRemoteness();
			if (valValue.equals("win")) {
				if (((bestMoveValue.equals("win")) && 
						(valRemoteness < bestMoveRemoteness))
						|| (!bestMoveValue.equals("win"))) {
					bestMove = val;
				} else if (bestMoveValue.equals("win") && (valRemoteness == bestMoveRemoteness)) {
					double randomnum = gen.nextDouble();
					if (randomnum >= 0.5) {
						bestMove = val;
					}
				}
			} else if (valValue.equals("tie")) {
				if ((bestMoveValue.equals("tie")) && (valRemoteness > bestMoveRemoteness)
						|| (bestMoveValue.equals("lose"))) {
					bestMove = val;
				} else if (bestMoveValue.equals("tie") && (valRemoteness == bestMoveRemoteness)) {
					double randomnum = gen.nextDouble();
					if (randomnum >= 0.5) {
						bestMove = val;
					}
				}
			} else if ((bestMoveValue.equals("lose")) && (valRemoteness > bestMoveRemoteness)) {
				bestMove = val;
			} else if (bestMoveValue.equals("lose") && (valRemoteness == bestMoveRemoteness)) {
				double randomnum = gen.nextDouble();
				if (randomnum >= 0.5) {
					bestMove = val;
				}
			}
		}
		doMove(bestMove.getMove());
	}
	
	/**
	 * Plays a random computer move.
	 * Used when there does not exist a connection to the database and no move or remoteness
	 * values are available.
	 */
	public void playRandom() {
		Random gen = new Random();
		int numcols = GameIntent.getIntExtra("numCols", 7);
		int randomcol = gen.nextInt(numcols);
		while (isMoveInvalid(randomcol)) {
			randomcol = gen.nextInt(numcols);
		}
		doMove(randomcol + "");
	}
	
	public boolean isDBAvailable() {
		return isDatabaseAvailable;
	}

	/**
	 * Call this method in place of setContentView. 
	 * @param resource_id The resource id of the xml element (R.id.NAME_OF_RESOURCE).
	 */
	public void setGameView(int resource_id) {
		getLayoutInflater().inflate(resource_id, (ViewGroup) findViewById(R.id.gm_GameView));
	}

	public void initBottomBar() {
		hSlider = (HorizontalSlider) findViewById(R.id.gm_slider);
		
		undoButton = (ImageButton) findViewById(R.id.gm_undoButton);
		redoButton = (ImageButton) findViewById(R.id.gm_redoButton);		
		undoButton.setEnabled(true);
		redoButton.setEnabled(true);
		
		OnClickListener backListener = new OnClickListener(){
			public void onClick(View arg0) {				
				undoMove();
			}
		};
		undoButton.setOnClickListener(backListener);
		
		OnClickListener forwardListener = new OnClickListener(){
			public void onClick(View arg0) {
				redoMove();
			}
		};
		redoButton.setOnClickListener(forwardListener);
		
		OnTouchListener tListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getAction();
				ProgressBar b = (ProgressBar) v;

				if (action == MotionEvent.ACTION_DOWN
						|| action == MotionEvent.ACTION_MOVE) {
					float x_mouse = event.getX() - hSlider.getPadding();
					float width = b.getWidth() - 2*hSlider.getPadding();
					int progress = Math.round((float) b.getMax() * (x_mouse / width));
					int moveNumber = Math.round((float) getNumMovesSoFar() * (x_mouse / width));

					if (progress < 0)
						progress = 0;

					hSlider.setProgress(progress);
					goToMoveN(moveNumber);

					if (hSlider.getListener() != null)
						hSlider.getListener().onProgressChanged(hSlider, progress);

				}

				return true;
			}
		};
		hSlider.setOnTouchListener(tListener);
		hSlider.updateProgress(0, 0);

	}
}
