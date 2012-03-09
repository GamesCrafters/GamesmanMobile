package com.gamescrafters.abstractGame;

import com.gamescrafters.gamesmanmobile.GameActivity;

public abstract class AndroidGame extends GameActivity {
	
	private Game myGame;
	private AndroidBoard gameBoard;

	@Override
	public String getBoardString() {
		// TODO Auto-generated method stub
		return gameBoard.toString();
	}

	@Override
	public String getGameName() {
		// TODO Auto-generated method stub
		return myGame.getName();
	}

	@Override
	public void undoMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redoMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMove(String move) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMoveInvalid(int move) {
		// TODO Auto-generated method stub
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

	public Game getGame() {
		return myGame;
	}

	public void setGame(Game myGame) {
		this.myGame = myGame;
	}

	public AndroidBoard getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(AndroidBoard gameBoard) {
		this.gameBoard = gameBoard;
	}




}
