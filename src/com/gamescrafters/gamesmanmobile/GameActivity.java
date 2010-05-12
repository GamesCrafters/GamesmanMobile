package com.gamescrafters.gamesmanmobile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.gamescrafters.connect4.Connect4;

import android.app.Activity;
import android.content.Intent;
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
	public  boolean isPlayer1Computer, isPlayer2Computer, isNetworkAvailable, isShowValues = false, isShowPrediction = false;
	protected Map<String, MoveValue[]> moveValues; // A map of boards to MoveValue[]. Keeps undos/redos from making web calls.
	static public LinkedList<VVHNode> VVHList; // List of Visual Value History nodes added since the last VisualValueHistory update.
	static public Intent GameIntent, VVHIntent; // Intents for the Connect4 Activity and the VisualValueHistory Activity.
	static int numMovesToRemove = 0; // A counter for how many moves have been undone since the last VisualValueHistory update.
	static boolean clear_vvh = false;
	MenuItem moves, prediction;
	protected HorizontalSlider hSlider;
	private ImageButton undoButton, redoButton;

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
		GameIntent.setFlags(131072);
		VVHIntent = new Intent(this, VisualValueHistory.class);
		VVHIntent.setFlags(131072);
		// Create a new VVHList.
		VVHList = new LinkedList<VVHNode>();
	}

	protected void setPlayers(boolean player1, boolean player2) {
		isPlayer1Computer = player1;
		isPlayer2Computer = player2;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		menu.add("New Game");
		menu.add("Toggle Move Values");
		menu.add("Toggle Prediction");
		menu.add("Display Visual Value History");
		return true;
	} 

	public boolean onOptionsItemSelected(MenuItem item) {
		CharSequence title = item.getTitle();
		if (title.equals("New Game")) {
			newGame();
		} else if (title.equals("Toggle Move Values")){
			isShowValues = !isShowValues;
			updateValuesDisplay();
		} else if (title.equals("Toggle Prediction")) {
			isShowPrediction = !isShowPrediction;
			updatePredictionDisplay();
		} else {
			updateVVHDisplay();
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
	 * Displays the Visual Value History for this Connect4 game.
	 */
	public void updateVVHDisplay() {
		this.startActivity(VVHIntent);
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
	public void updateVVH(MoveValue val) {}

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
	 * Determines and executes the best possible move
	 * during a computer's turn.
	 */
	public void doComputerMove() {
		MoveValue values[] = getNextMoveValues();
		MoveValue bestMove = values[0];
		for (MoveValue val : values) {
			String bestMoveValue = bestMove.getValue();
			String valValue = val.getValue();
			int valRemoteness = val.getRemoteness();
			int bestMoveRemoteness = bestMove.getRemoteness();
			if (valValue.equals("win")) {
				if (((bestMoveValue.equals("win")) && 
						(valRemoteness <= bestMoveRemoteness))
						|| (!bestMoveValue.equals("win"))) {
					bestMove = val;
				}
			} else if (valValue.equals("tie")) {
				if ((bestMoveValue.equals("tie")) && (valRemoteness >= bestMoveRemoteness)
						|| (bestMoveValue.equals("lose"))) {
					bestMove = val;
				}
			} else if ((bestMoveValue.equals("lose")) && (valRemoteness >= bestMoveRemoteness)) {
				bestMove = val;
			}
		}
		doMove(bestMove.getMove());
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
	}
}
