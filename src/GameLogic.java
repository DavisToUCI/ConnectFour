public class GameLogic {
	
	int gameBoard[][];
	int currentPlayer;
	int winner;
	public int RED_PLAYER = 1;
	int YELLOW_PLAYER = 2;
	int NONE = 0;
	int winningSequence[][];

	
	// Initializes the gameboard
	public GameLogic() {
		resetBoard();		
	}
	
	// Resets the gameboard and creates a fresh board
	
	public void resetBoard() {
		gameBoard = new int[6][7];
		winningSequence = new int[4][2];
		for(int i = 0; i<gameBoard.length; i++) {
			for (int j=0; j<gameBoard[i].length; j++) {
				gameBoard[i][j] = 0;
			}
		}
		currentPlayer = RED_PLAYER;
		winner = NONE;	
		for (int k = 0; k<winningSequence.length; k++) {
			for(int l = 0; l<winningSequence[k].length; l++) {
				winningSequence[k][l]=0;
			}
		}
	}
	
	// The method that allows users to make moves
	
	public void makeMove(int col, GUI UI) {
		for (int row = gameBoard.length-1; row>-1; row--) {
			if (!invalidRowCol(row,col-1) && gameBoard[row][col-1] == NONE && !isGameOver()) {
				gameBoard[row][col-1] = currentPlayer;
				UI.placeDisc(currentPlayer, row, col-1);
				if (checkWinner(row,col-1)) {winner = currentPlayer; UI.winningResult();}
				else currentPlayer = oppositePlayer();
				UI.updateBottomDisplay();
				break;
			}
		}
	}
	
	// Returning the string for the winner and current player
	
	public String returnWinner() {
		if (winner==RED_PLAYER) return "RED";
		else if (winner==YELLOW_PLAYER) return "YELLOW";
		else return "NONE";
	}
	
	public String returnCurrentPlayer() {
		if (currentPlayer == RED_PLAYER) return "RED";
		else return "YELLOW";
	}
	
	// Determines whether or not the game is over.
	
	public boolean isGameOver() {
		int availableMoves = 42;
		for(int i=0;i<6;i++){
			for (int j = 0; j<7; j++) {
				if (gameBoard[i][j] != 0) availableMoves--;
			}
		}
	return winner != NONE || availableMoves == 0;
	}
	
	public int[][] winningCombination(){
		return winningSequence;
	}

	
	//Private Functions
	
	private int oppositePlayer() {
		if (currentPlayer == RED_PLAYER){
			return YELLOW_PLAYER;
		}
		else
			return RED_PLAYER;
	}
	
	// Checks and determines if there is a winner
	
	private boolean checkWinner(int row, int col) {
		return checkHorizontal(row,col) || checkVertical(row,col) || checkDiagonal(row,col);
	}
	
	// Check horizontal victory
	
	private boolean checkHorizontal(int row, int col){
		int horizontal = 0;
		int i = -3;
		while (i<4 && horizontal !=4) {
			if (!invalidRowCol(row,col+i) && gameBoard[row][col+i] == gameBoard[row][col]) {
				winningSequence[horizontal][0]= row;
				winningSequence[horizontal][1]= col+i;
				horizontal++;}
			else if (!invalidRowCol(row,col+i) && gameBoard[row][col+i] != gameBoard[row][col]){
				horizontal *=0;}
			i++;
		}
		return horizontal == 4;
	}
	
	// Check vertical victory
	
	private boolean checkVertical(int row, int col){
		int vertical = 0;
		int i = -3;
		while (i<4 && vertical !=4)  {
			if (!invalidRowCol(row+i,col) && gameBoard[row+i][col] == gameBoard[row][col]) {
				winningSequence[vertical][0]= row+i;
				winningSequence[vertical][1]= col;				
				vertical++;}
			else if (!invalidRowCol(row+i,col) && gameBoard[row+i][col] != gameBoard[row][col]) {
				vertical*=0;}	
			i++;
		}
		return vertical == 4;
	}
	
	// Check diagonal victory
	
	private boolean checkDiagonal(int row, int col) {
		int diagonal1 = 0;
		int diagonal2 = 0;
		int i = -3;
		int winningSequence1[][] = new int[4][2];
		int winningSequence2[][] = new int[4][2];
		while (i<4 && diagonal1 !=4 && diagonal2 !=4) {
			if (!invalidRowCol(row+i,col+i) && gameBoard[row+i][col+i] == gameBoard[row][col]) {
				winningSequence1[diagonal1][0]= row+i;
				winningSequence1[diagonal1][1]= col+i;
				diagonal1++;}
			if (!invalidRowCol(row+i,col-i) && gameBoard[row+i][col-i] == gameBoard[row][col]) {
				winningSequence2[diagonal2][0]= row+i;
				winningSequence2[diagonal2][1]= col-i;				
				diagonal2++;}
			if (!invalidRowCol(row+i,col-i) && gameBoard[row+i][col-i] != gameBoard[row][col]) {
				diagonal2*=0;}
			if(!invalidRowCol(row+i,col+i) && gameBoard[row+i][col+i] != gameBoard[row][col]) {
				diagonal1*=0;}
			i++;
			}
		if (diagonal1 == 4) winningSequence = winningSequence1;
		if (diagonal2==4) winningSequence = winningSequence2;
		return diagonal1 == 4 || diagonal2 == 4;
		}
	
	
	private boolean invalidRowCol(int row, int col) {
		return (row<0 || row>5) || (col<0 || col>6);
	}
/*	
	private void printBoard() {
		for(int i = 0; i<gameBoard.length; i++) {
			for (int j=0; j<gameBoard[i].length; j++) {		
				if(gameBoard[i][j] == RED_PLAYER){
					System.out.print("R ");
				}
				else if(gameBoard[i][j] == YELLOW_PLAYER){
					System.out.print("Y ");
				}
				else {
					System.out.print(". ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}	*/
}
