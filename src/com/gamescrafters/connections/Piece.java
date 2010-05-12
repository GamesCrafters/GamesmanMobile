package com.gamescrafters.connections;

class Piece {

	//instance variables 
	private int r;
	private int c;
	private int type;
	private int belongsTo;

	/**
	 * Piece Constructor
	 * @param int type, int r, int c.
	 */
	Piece(int type, int belongsTo, int r, int c){
		this.type=type;
		this.belongsTo = belongsTo;
		this.r = r;
		this.c = c;
	}

	//getter methods
	int getType(){
		return this.type;
	}

	int getR(){
		return this.r;
	}

	int getC(){
		return this.c;
	}

	int getBelongsTo(){
		return this.belongsTo;
	}

	void setType(int t){
		this.type=t;
	}

	void setBelongsTo(int bT){
		this.belongsTo=bT;
	}

	//hashcode methods
	public int hashCode(){
		return (this.r << 16) + this.c;
	}

	public boolean equals(Object obj){
		return this.type == ((Piece)obj).type && this.r == ((Piece)obj).r && this.c == ((Piece)obj).c;
	}

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

	void printPiece(){
		switch(type){
		case BLANK: System.out.print("__"); break;
		case P1NODE: System.out.print("n1"); break;
		case P1LINEV: System.out.print("v1"); break;
		case P1LINEH: System.out.print("h1"); break;
		case P2NODE: System.out.print("n2"); break;
		case P2LINEV: System.out.print("v2"); break;
		case P2LINEH: System.out.print("h2"); break;


		}
	}

}