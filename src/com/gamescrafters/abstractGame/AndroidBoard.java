package com.gamescrafters.abstractGame;

import com.gamescrafters.gamesmanmobile.GameActivity;

import android.widget.TableLayout;

public abstract class AndroidBoard extends Board{

	private int myHeight;
	private int myWidth;
	private TableLayout myTable;
	
	public AndroidBoard(int height, int width, GameActivity a) {
		myHeight = height;
		myWidth = width;
		myTable = a.findViewById(a.findGameTable());
	}
	
	public void reset(){
		initBoard();
	}
	
	public abstract void getID(Object location);
	
	public abstract void showVVH();
	
	public void updateBoard(Object location, Piece p) {
		placePiece(location, p);
	}
	
	public int getHeight() {
		return myHeight;
	}
	
	public void setHeight(int height) {
		myHeight = height;
	}
	
	public int getWidth() {
		return myWidth;
	}
	
	public void setWidth(int width) {
		myWidth = width;
	}

	public TableLayout getTable() {
		return myTable;
	}

	public void setTable(TableLayout table) {
		myTable = table;
	}
}
