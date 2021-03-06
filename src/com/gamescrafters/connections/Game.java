package com.gamescrafters.connections;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class Game {

	static final int NOONE= 0;
	static final int P1=1;
	static final int P2=2;
	static final int BLANK = 0;
	static final int P1NODE = 1;
	static final int P1LINEV = 2;
	static final int P1LINEH = 3;
	static final int P2NODE = 4;
	static final int P2LINEV = 5;
	static final int P2LINEH = 6;


	//instance variables
	Piece[][] board;
	int rows;
	int cols;
	int whoseMove;
	boolean isOver;
	LinkedList<Piece> p1PossiblePaths;
	LinkedList<Piece> p2PossiblePaths;
	//Undo/Redo
	Stack<int[]> previousMoves, nextMoves; // Stacks of previousMoves and nextMoves, which is used to undo and redo moves.
	boolean isRedo,isUndo,isDo;
	int movesSoFar;
	int currentMove;
	
	//game constructor
	Game(int r, int c ){
		board = new Piece[r][c];
		p1PossiblePaths=new LinkedList<Piece>();
		p2PossiblePaths=new LinkedList<Piece>();
		this.rows=r;
		this.cols=c;
		this.whoseMove=P1;
		this.isOver=false;
		previousMoves = new Stack<int[]>();
		nextMoves = new Stack<int[]>();
		isRedo = false; isUndo = false; isDo=false;
		movesSoFar=0; currentMove=0;
		this.initBoard();
		//initializing p1PossiblePaths
		for(int i=1; i < cols; i=i+2){
			p1PossiblePaths.add(board[0][i]);
		}

		//initializing p2Paths
		for(int i=1; i < rows; i=i+2){
			p2PossiblePaths.add(board[i][0]);
		}
	}

	//undoMove
	
	boolean undoMove(){
		if(isUndo && !previousMoves.isEmpty()){
			int[] prevMv = previousMoves.pop();
			nextMoves.push(prevMv);
			Piece p = board[prevMv[0]][prevMv[1]];
			p.setBelongsTo(NOONE);
			p.setType(BLANK);
			isOver = isOver();
			currentMove--;
			return true;
		} else {
			return false;
		}
	}
	
	// doMove
	boolean doMove(int r,int c, boolean isDo, boolean isRedo) {
		if(isRedo){
			if (!nextMoves.isEmpty()){
				int[] nxtMv = nextMoves.pop();
				previousMoves.push(nxtMv);
				r = nxtMv[0];
				c = nxtMv[1];
			} else {
				return false;
			}
		}
		if((isDo || isRedo)&& isValid(r, c)){
			if(!isRedo){
			int[] m = createMove(r,c);
			previousMoves.push(m);
			}
			board[r][c].setBelongsTo(whoseMove);
			if (this.whoseMove==P1){
				if ( (c % 2) == 1 ){
					board[r][c].setType(P1LINEV);
				} else {
					board[r][c].setType(P1LINEH);
				}
			} else if (this.whoseMove==P2){
				if ( (c % 2) == 1 ){
					board[r][c].setType(P2LINEH);
				} else {
					board[r][c].setType(P2LINEV);
				}
			}
			currentMove++;
			if (!isRedo) {
				nextMoves.clear();
				movesSoFar = currentMove;
			}
			isOver = isOver();
			return true;
		}
		return false;
	}
	
	//makes [r,c] out of r,c for undo/redo
	int[] createMove(int r, int c){
		int[] rtn = new int[2];
		rtn[0]=r; rtn[1]=c;
		return rtn;
	}
	
	void switchPlayer(){
		whoseMove = whoseMove ^ 0x3;
	}

	//isValid methods and helpers
	boolean isValid(int r, int c){
		if (!isInGrid(r,c)){
			return false;
		}

		if (! (board[r][c].getType()==BLANK)){
			return false;
		}

		//down, up 
		if (isInGrid(r-1, c) && isInGrid(r+1, c) && board[r+1][c].getBelongsTo()==whoseMove) {
			return true;

			//left, right
		} else if (isInGrid(r, c-1) && isInGrid(r, c+1) && board[r][c+1].getBelongsTo()==whoseMove){
			return true;
		} else {
			return false;
		}

	}
	
	
	
	boolean isInGrid(int r, int c){
		return (r>=0 && r <rows) && (c>=0 && c <cols);
	}

	//isOver
	boolean isOver(){
			boolean isOv = false;
			if (whoseMove==P1){
				for (Piece t1:p1PossiblePaths){
					if (isPathGood(t1)) {
						isOv = true;
						break;
					}
				}
			} else {
				for (Piece t2:p2PossiblePaths){
					if (isPathGood(t2)) {
						isOv = true;
						break;
					}
				}
			}
			return isOv;
	}


	boolean isPathGood(Piece origin){
		HashSet<Piece> hashset=new HashSet<Piece>();
		Stack<Piece> fringe= new Stack<Piece>();

		fringe.push(origin);

		while(!(fringe.isEmpty())){
			Piece p = fringe.pop();
			if (!hashset.contains(p)){
				hashset.add(p);
				if (isPieceEnd(p)){
					return true;
				}
				LinkedList<Piece> possibleEdges = generateEdges(p);
				for(Piece temp: possibleEdges){
					if (!hashset.contains(temp)){
						fringe.push(temp);
					}
				}
			}
		}

		return false;
	}

	LinkedList<Piece> generateEdges(Piece origin){
		int c = origin.getC();
		int r = origin.getR();
		LinkedList<Piece> returnList = new LinkedList<Piece>();
		//Left
		if (isInGrid(r,c-1)&& origin.getBelongsTo()==this.whoseMove){
			returnList.add(this.board[r][c-1]);
		}
		//Right
		if (isInGrid(r,c+1)&& origin.getBelongsTo()==this.whoseMove){
			returnList.add(this.board[r][c+1]);
		}
		//Down
		if (isInGrid(r-1,c)&& origin.getBelongsTo()==this.whoseMove){
			returnList.add(this.board[r-1][c]);
		}
		//Up
		if (isInGrid(r+1,c)&& origin.getBelongsTo()==this.whoseMove){
			returnList.add(this.board[r+1][c]);
		}

		return returnList;
	}

	boolean isPieceEnd(Piece origin){
		if (this.whoseMove==P1){
			return (origin.getR()==(this.rows-1));
		} else{
			return (origin.getC()==(this.cols-1));
		} 
	}


	/**
	 * Piece Constructor
	 * Board initialization methods 
	 * Player1: goes from NORTH to SOUTH
	 * Player2: goes from EAST to WEST 
	 */
	private void initBoard(){
		int r,c;
		for (r=0; r < this.rows;r++){
			for (c=0; c < this.cols;c++){
				if (isBLANKSLOT(r,c)){
					this.board[r][c] = new Piece(BLANK,NOONE,r,c);
				} else if (isP1NODESLOT(r,c)){
					this.board[r][c] = new Piece(P1NODE,P1,r,c);
				} else if (isP2NODESLOT(r,c)){
					this.board[r][c] = new Piece(P2NODE,P2,r,c);
				} else {
					System.out.println("THIS IS SHOULDN'T HAPPEN IN INITBOARD");
				}
			}
		}
	}


	boolean isBLANKSLOT(int r, int c){
		return  ((r % 2 == 0)&&(c % 2 == 0))|| ((r % 2 == 1)&&(c % 2 == 1));
	}

	boolean isP1NODESLOT(int r, int c){
		return (r % 2 == 0) && (c % 2 ==1);
	}


	boolean isP2NODESLOT(int r, int c){
		return (r % 2 == 1) && (c % 2 == 0);
	}

	void printBoard(){
		for(int r=0; r < rows; r++){
			for(int c=0; c < cols; c++){
				Piece temp=board[r][c];
				temp.printPiece();
				System.out.print(" ");

			}
			System.out.println("");
		}
	}
}