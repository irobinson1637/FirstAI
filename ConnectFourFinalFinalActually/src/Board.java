import java.util.Stack;

public class Board {
	boolean p1turn, gameover; // p1turn is true if player is 1st, false if ai is 1st
	int[][] board;
	Stack<Integer> undoStack = new Stack<Integer>();
	Stack<Integer> redoStack = new Stack<Integer>();
	
	public Board() {
		board = new int[7][6];
		gameover = false;
		p1turn = true;
	}
	
	public Board(int[][] x, boolean p1) {
		board = x;
		gameover = false;
		p1turn = p1;
	}
	
	boolean getTurn() {
		return p1turn;
	}
	
	int[][] getBoard() {
		return board;
	}
	
	void setP1(boolean a) {
		p1turn = a;
	}
	
	void setBoard(int[][] newBoard) {
		board = newBoard;
	}

	boolean legalMove(int x) {
		
		if(board[x][5] == 0) return true;
		
		return false;
	}
	
	void makeMove(int x) {
		undoStack.clear(); // so you cant redo/undo moves from earlier turns
		redoStack.clear();
		if (!legalMove(x)) {
			return; // turn order still in question here
		}
		for (int i=0; i<6; i++) {
			if (board[x][i] == 0) {
				if(p1turn) {
					board [x][i] = 1; // 1 is a human move, 2 is an AI move
				}
				else {
					board[x][i] = 2;
				}
				p1turn = !p1turn;
				undoStack.push(x);
				return;
			}
		}
	}
	
	void undo() {
		if (undoStack.size() !=0) {
			int x = undoStack.pop();
			redoStack.push(x); // take the last move and pop from undo-able to redo-able
			for (int i = 5; i>=0; i--) { // work top to bottom to find most recent move in column x and delte it
				if (board[x][i] != 0) {
					board[x][i] = 0;
				}
			}
			p1turn = !p1turn; // undos turn change from makeMove
		}
	}
	
	void redo() { 
		if (redoStack.size() != 0) {
			makeMove(redoStack.pop());
			p1turn = !p1turn; // undos turn change from undoing a move
		}
	}
	int winner() {
		for (int y=0; y<6; y++) { // check horizontal
			for (int x = 0; x<4; x++) {
				if (board[x][y] == 1 && board[x+1][y] == 1 && board[x+2][y] == 1 && board[x+3][y] == 1) return 1;
				if (board[x][y] == 2 && board[x+1][y] == 2 && board[x+2][y] == 2 && board[x+3][y] == 2) return 2;
			}
		}
		for (int y=0; y<3; y++) { // check vertical
			for (int x=0; x<7; x++) {
				if (board[x][y] == 1 && board[x][y+1] == 1 && board[x][y+2] == 1 && board[x][y+3] == 1) return 1;
				if (board[x][y] == 2 && board[x][y+1] == 2 && board[x][y+2] == 2 && board[x][y+3] == 2) return 2;
			}
		}
		for (int y=0; y<3; y++) { // check ascending diagonal
			for (int x=0; x<4; x++) {
				if (board[x][y] == 1 && board[x+1][y+1] == 1 && board[x+2][y+2] == 1 && board[x+3][y+3] == 1) return 1;
				if (board[x][y] == 2 && board[x+1][y+1] == 2 && board[x+2][y+2] == 2 && board[x+3][y+3] == 2) return 2;
			}
		}
			for (int y=5; y>2; y--) {
			for (int x=0; x<3; x++) {
				if (board[x][y] == 1 && board[x+1][y-1] == 1 && board[x+2][y-2] == 1 && board[x+3][y-3] == 1) return 1;
				if (board[x][y] == 2 && board[x+1][y-1] == 2 && board[x+2][y-2] == 2 && board[x+3][y-3] == 2) return 2;
			}
			} 
		for (int x=0; x<7; x++) {
			if (legalMove(x)) return 0; // board isn't filled but no winner yet
		}
		return -1; // tie game
	}

	int threerow() { // checking three in a rows to compensate for AI missing potential insta-loss moves
		for (int y=0; y<6; y++) { // get ready because this is gonna be a wild ride of if statements and for loops. viewer discretion is advised
			for (int x=0; x<4; x++) {  // these 2 loops check for horizontal 3 in a rows
				if(board[x][y] != 0) { // we need this so we dont just check that we have a bunch of 0s next to each other
					if (board[x][y] == board[x+1][y] && board[x][y] == board[x+2][y]) { // this if statement begins check for XXX_ or _XXX horizontals
						if (board[x+3][y] == 0) { // we check 3 matches and if the last spot is empty we continue
							if (y>0) {
								if (board[x+3][y-1] != 0) return x+3; // here if we arent on bottom row we have to see if there's a piece under the open spot
							}
							else return x+3;
						}
						if(x>0 && board[x-1][y] == 0) { // same sort of thing but for _XXX horizontal
							if (y>0) {
								if(board[x-1][y-1] != 0) return x-1;
							}
							else return x-1; // else is for when it's on bottom row, we don't need to check y-1 beccause it's nonexistant
						}
					}
					if(board[x][y] == board[x+1][y] && board[x][y] == board[x+3][y]) { // now we're checking XX_X
						if (board[x+2][y] == 0)
							if (y>0) {
								if(board[x+2][y-1] != 0) return x+2;
							}
							else return x+2; 
					}
					if(board[x][y] == board[x+2][y] && board[x][y] == board[x+3][y]) { // X_XX
						if (board[x+1][y] == 0)
							if (y>0) {
								if(board[x+1][y-1] != 0) return x+1;
							}
							else return x+1;
					}
				}
			}
		} 
		for (int x=0; x<7; x++) { // checking verticals
			for (int y=0; y<3; y++) {
				if (board[x][y] != 0 && board[x][y] == board[x][y+1] && board[x][y] == board[x][y+2] && board[x][y+3] == 0) return x; // dont need to do gravity checks like horizontals
			}
		}
		for (int x=0; x<4; x++) { // ascending diagonals
			for (int y=0; y<3; y++) {
				if(board[x][y] != 0) {
					if (board[x][y] == board[x+1][y+1] && board[x][y] == board[x+2][y+2] && board[x+3][y+3] == 0 && board[x+3][y+2] != 0) return x+3; // last spot is empty
					if (board[x][y] == board[x+1][y+1] && board[x][y] == board[x+3][y+3] && board[x+2][y+2] == 0 && board[x+2][y+1] != 0) return x+2; // 3rd spot
					if (board[x][y] == board[x+2][y+2] && board[x][y] == board[x+3][y+3] && board[x+1][y+1] == 0 && board[x+1][y] != 0) return x+1; // 2nd spot
					}
				if(board[x+1][y+1] != 0) { // cant check xy b/c xy has to be 0 here
					if (board[x+1][y+1] == board[x+2][y+2] && board[x+2][y+2] == board[x+3][y+3] && board[x][y] == 0) {
						if (y>0) {
							if(board[x][y-1] != 0) return x; //check, if first piece is on bottom row or not, if it is check if it has pieces under it
						}
						else return x;
					}
				}
			}
		}
		for (int x=0; x<4; x++) { // descending diagonals
			for(int y=5; y>2; y--) {
				if(board[x][y] != 0) {
						if(board[x][y] == board[x+1][y-1] && board[x][y] == board[x+2][y-2] && board[x+3][y-3] == 0) { // last spot empty
							if(y>3) {
								if(board[x+3][y-4] != 0) return x+3;
							}
							else return x+3;
						}
						if(board[x][y] == board[x+1][y-1] && board[x][y] == board[x+3][y-3] && board[x+2][y-2] == 0 && board[x+2][y-3] != 0) return x+2; // 3rd spot
						if(board[x][y] == board[x+2][y-2] && board[x][y] == board[x+3][y-3] && board[x+1][y-1] == 0 && board[x+1][y-2] != 0) return x+1; // 2nd spot
						}
				if(board[x+1][y-1] != 0) { // can't check xy because here xy has to be 0
					if(board[x+1][y-1] == board[x+2][y-2] && board[x+1][y-1] == board[x+3][y-3] && board[x][y] == 0 && board[x][y-1] != 0) return x; // 1st spot
				}
			}
		}
		return -1; // no 3 in a row
	}

	public void roughdisp() { // displays board top-down instead of bottom up -- just a rough method for testing purposes
		for (int y=0; y<6; y++) {
			for (int x=0; x<7; x++) {
				System.out.print(board[x][y]);
			}
			System.out.println();
		}
	}
	
	void swap(Board b2) { // swaps this board with the boardstate of another game, used to reset the search after a terminal move
		int[][] a = new int[7][6];
		int[][] b = b2.getBoard();
		for (int x=0; x<7; x++) {
			for (int y=0; y<6; y++) {
				a[x][y] = b[x][y];
			}
		}
		setBoard(a);
	}
}
