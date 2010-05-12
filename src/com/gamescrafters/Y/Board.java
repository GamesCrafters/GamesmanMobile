package com.gamescrafters.Y;

/* Author: Pin Xu */
import java.util.ArrayList;

/** A Reversi board. */
class Board {
	Stack prevMoves = new Stack();
	Stack redoMoves = new Stack();
	Node[] bd = new Node[18];
	//int[] lengths = {1, 3, 5, 7, 8, 5, 1};
	//int[] sides = {1,1,0,1,1,0,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,1,1};
	int[]sides = {0,0,0,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,0,0,0,0,0,};
    Board() {
    	initBoard(bd);
    }
    
    private void initBoard(Node[] nodes) {
    	for (int i = 0; i<18; i++) {
    		nodes[i] = new Node();
    	}
    	addAdj(0, nodes, 1, 2, 12, 13, 17);
    	addAdj(1, nodes, 0,2,15,16,17);
    	addAdj(2, nodes, 0,1,13, 14, 15);
    	addAdj(3, nodes, 4, 11, 12);
    	addAdj(4, nodes, 3, 12, 13, 5);
    	addAdj(5, nodes, 4, 6, 13, 14);
    	addAdj(6, nodes, 5, 7, 14);
    	addAdj(7, nodes, 6, 14, 15, 8);
    	addAdj(8, nodes,7, 15, 16, 9);
    	addAdj(9, nodes, 8, 10, 16);
    	addAdj(10, nodes, 9, 16, 17, 11);
    	addAdj(11, nodes, 10, 17, 12, 3);
    	addAdj(12, nodes, 3, 4, 0, 11, 13, 17);
    	addAdj(13, nodes, 4, 12, 5, 14, 2, 0);
    	addAdj(14, nodes, 5, 6, 2, 7, 13, 15);
    	addAdj(15, nodes, 1, 2, 7, 8, 14, 16);
    	addAdj(16, nodes, 1, 8, 9, 10, 15, 17);
    	addAdj(17, nodes, 0, 1, 10, 11, 12, 16);
    	
    	
    	nodes[3].connected = 5;
    	nodes[4].connected = 4;
    	nodes[5].connected = 4;
    	nodes[6].connected = 6;
        nodes[7].connected = 2;
        nodes[8].connected = 2;
        nodes[9].connected =3;
        nodes[10].connected =1;
        nodes[11].connected =1;
    	/*
    	addAdj(0, nodes, 1, 2, 3);
    	addAdj(1, nodes, 0, 2, 4, 5);
    	addAdj(2, nodes, 0, 1, 3, 4, 5, 6);
    	addAdj(3, nodes, 0,2,6,7,8);
    	addAdj(4, nodes, 1,5,10,9);
    	addAdj(5, nodes, 1, 2, 4, 6, 10, 11);
    	addAdj(6, nodes, 2, 5, 7, 11, 12, 13);
    	addAdj(7, nodes, 2, 3, 6, 8, 13, 14);
    	addAdj(8, nodes,3, 7, 13, 14, 15);
    	addAdj(9, nodes, 4, 10, 16, 17);
    	addAdj(10, nodes, 4, 5, 9, 11, 17, 18);
    	addAdj(11, nodes, 5, 6, 10, 12, 18, 19);
    	addAdj(12, nodes, 6, 11, 13, 19, 20);
    	addAdj(13, nodes, 6, 7, 12, 14, 20, 21);
    	addAdj(14, nodes,7, 8, 13, 15, 21, 22);
    	addAdj(15, nodes, 8, 14, 22, 23);
    	addAdj(16, nodes,9, 17, 24);
    	addAdj(17, nodes, 9, 10, 18, 16, 24, 25);
    	addAdj(18, nodes, 10, 11, 17, 19, 25, 26);
    	addAdj(19, nodes,11, 12, 18, 20, 26);
    	addAdj(20, nodes, 12, 13, 19, 21, 26);
    	addAdj(21, nodes, 13, 14, 20, 22, 26, 27);
    	addAdj(22, nodes, 14, 15, 21, 23, 27, 28);
    	addAdj(23, nodes, 15, 22, 28);
    	addAdj(24, nodes,16, 17, 25, 29);
    	addAdj(25, nodes, 17, 18, 24, 26, 27, 29);
    	addAdj(26, nodes, 18, 19, 20, 21, 27, 25);
    	addAdj(27, nodes, 21, 22, 25, 26, 28, 29);
    	addAdj(28, nodes, 22, 23, 27, 29);
    	addAdj(29, nodes,24, 25, 26, 27, 28);
    	
    	nodes[0].connected = 6;
    	nodes[1].connected = 4;
    	nodes[4].connected = 4;
    	nodes[9].connected = 4;
    	nodes[16].connected = 5;
    	nodes[3].connected = 2;
    	nodes[8].connected = 2;
    	nodes[15].connected = 2;
    	nodes[23].connected =3;
    	nodes[24].connected =1;
    	nodes[29].connected =1;
    	nodes[28].connected =1;
    	*/
    }
    
    /** Used in init-board to initialize all the connections between nodes. */
    private void addAdj(int p, Node[] nodes, int... x) {
		for (int k = 0; k<x.length; k++) {
			nodes[p].adj.add(x[k]);
		}
	}
    
    /** Clears the board*/
    protected void Clear(Node[] bd) {
    	initBoard(bd);
    	currentPlayer = PieceColor.P1;
    }
    
    
  /** The current contents of square SQ.  */
  PieceColor get (String sq) throws Exception{
    if(sq.length() != 1) {
      throw new IllegalArgumentException("Error: Wrong string imput to board get method");
    } else {
      int c = sq.charAt(0) - '0';
      return get(c);
    }
  }
 

  /** The current contents of square c. */
  PieceColor get (int c) {
    return bd[c].color;
  }
        
    

  /** Return the color of the player who has the next move.  The
   *  value is arbitrary if the game is over. */
  PieceColor whoseMove () {
    return currentPlayer;
  }
    
  /** Total number of calls to makeMove and pass since the last
   *  clear or creation of the board. */
  int numMoves () {
    return nummoves;
  }

  /** Perform the move SQ.  Assumes that legalMove (SQ). */
  void makeMove (String sq) throws Exception{
    if(sq.length() != 1) {
      throw new IllegalArgumentException("Error: Wrong string imput to board makeMove method");
    } else {
      int x = sq.charAt(0) - '0';
      makeMove(x);
    }
  }
  
  
  /** undoes a move. Returns -1 if no moves are to be undone */
  int undo() {
	  if (prevMoves.isEmpty()) {
		  return -1;
	  }
	  int move = prevMoves.pop();
	  redoMoves.push(move);
	  bd[move].color = PieceColor.EMPTY;
	  switchPlayer();
	  Stack s = new Stack();
	  for (int i= 0; i<bd[move].adj.size(); i++) {
		  int n = bd[move].adj.get(i);
		  if (sides[n]==1&&bd[n].color == currentPlayer){
			  s.push(n);
			  if (n == 3) {
				  bd[n].connected = 5;
			  } else if (n == 4 || n == 5) {
				  bd[n].connected = 4;
			  } else if (n ==6) {
				  bd[n].connected = 6;
			  } else if (n == 7 || n == 8) {
				  bd[n].connected = 2;
			  } else if (n == 9) {
				  bd[n].connected = 3;
			  } else if (n == 10 || n == 11){
				  bd[n].connected = 1;
			  /*
			  if (n==1||n==4||n==9) {
				  bd[n].connected = 4;
			  }else if (n == 0) {
				  bd[n].connected = 6;
			  }else if (n==16) {
				  bd[n].connected = 5;
			  }else if (n==23) {
				  bd[n].connected = 3;
			  }else if (n == 3||n==8||n==15) {
				  bd[n].connected = 2;
			  } else {
				  bd[n].connected = 1;
				  */
			  }
		  } else if (bd[n].color == currentPlayer) {
			  bd[n].connected = 0;
		  } else {
			  continue;
		  }
	  }
	  while(!s.isEmpty()) {
		  update(bd[s.pop()]);
	  }
	  return move;
  }
  
  /** redo the previous move. */
  int redo(){
	  if (redoMoves.isEmpty()){
		  return -1;
	  }
	  else {
		  return redoMoves.pop();
	  }
  }
  
  
  /** Updates the connected values of all adjacent nodes. */
  void update(Node x) {
	  for (int i = 0; i<x.adj.size(); i++) {
		  Node temp = bd[x.adj.get(i)];
		  if (temp.connected != x.connected && temp.color == x.color) {
			  temp.connected = x.connected;
			  update(temp);
		  }
	  }
  }

  /** Perform the legal move x.  */
  int makeMove (int x){
    bd[x].color = currentPlayer;
    nummoves+=1;
    int newconnected = bd[x].connected;
    for (int j = 0; j<bd[x].adj.size(); j++) {
    	if (bd[bd[x].adj.get(j)].color == bd[x].color){ 
    		newconnected = bd[bd[x].adj.get(j)].connected | newconnected;
    	}
    }
    bd[x].connected = newconnected;
    prevMoves.push(x);
    update(bd[x]);
    switchPlayer();
    int k = 0;
    /*
    for (int i = 0; i < lengths.length; i ++) {
      System.out.println();
      for (int j = 0; j < lengths[i]; j++, k++) {
    	  if (bd[k].color == PieceColor.EMPTY) {
    		  System.out.print("- ");
    	  } else if (bd[k].color == PieceColor.P1) {
    		  System.out.print("P1 ");
    	  } else {System.out.print("P2 ");}
      }
    }
    */
    if (newconnected == 7) {
    	PieceColor player = currentPlayer;
     	if (player == PieceColor.P1) {
    		return 2;
    	} else{
    		return 1;
    	}    	
    }
    return 0;
  }
  
  /**The color of the next piece to be played.*/
  PieceColor currentPlayer = PieceColor.P1;
    
  /**The current number of moves that have been made.*/
  int nummoves = 0;
    
  /**Switches the current color to the opposite color.*/
  private void switchPlayer() {
    currentPlayer = currentPlayer.opposite();
  }
  class Node {
	  PieceColor color = PieceColor.EMPTY;
	  ArrayList<Integer> adj = new ArrayList<Integer>();
	  int connected = 0;
  }
  
  class Stack {
	  int index = -1;
	  ArrayList<Integer> vals = new ArrayList<Integer>();
	  void push(int i) {
		  index++;
		  vals.add(i);
	  }
	  
	  void clear() {
		  index = -1;
		  vals.clear();
	  }
	  int pop() {
		  if (vals.size() > 0){
			  int ret = vals.get(index);
			  vals.remove(index);
			  index--;
			  return ret;
		  }else {
			  return -1;
		  }
	  }
	  boolean isEmpty() {
		  return vals.isEmpty();
	  }
  }
    
  
}

