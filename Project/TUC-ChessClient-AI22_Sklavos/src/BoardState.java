import java.util.ArrayList;

public class BoardState {
	String[][] board;
//	int[][] kingsPositions = new int[2][2];
	int whiteScore;
	int blackScore;
	final static int WHITE = 0;
	final static int BLACK = 1;
	final static int KINGS = 0;
	final static int PAWNS = 1;
	final static int ROOKS = 2;

//	public void locateKings() {
//		String firstChar;
//		String secondChar;
//		for (int row = 0; row < board.length; row++) {
//			for (int col = 0; col < board[row].length; col++) {
//				firstChar = Character.toString(board[row][col].charAt(0));
//				if (firstChar.equals("B")) {
//					secondChar = Character.toString(board[row][col].charAt(1));
//					if (secondChar.equals("K")) {
//						kingsPositions[BLACK][0] = row;
//						kingsPositions[BLACK][1] = col;
//					}
//				} else if (firstChar.equals("W")) {
//					secondChar = Character.toString(board[row][col].charAt(1));
//					if (secondChar.equals("K")) {
//						kingsPositions[WHITE][0] = row;
//						kingsPositions[WHITE][1] = col;
//					}
//				}
//			}
//		}
//	}

	public BoardState(String[][] board, int whiteScore, int blackScore) {
		this.board = board;
		this.whiteScore = whiteScore;
		this.blackScore = blackScore;
//		locateKings();
	}
		
	
	
//	public int[][] getKingsPositions() {
//		return kingsPositions;
//	}
//
//	public void setKingsPositions(int[][] kingsPositions) {
//		this.kingsPositions = kingsPositions;
//	}

	public String[][] getBoard() {
		return board;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}

	public int getWhiteScore() {
		return whiteScore;
	}

	public void setWhiteScore(int whiteScore) {
		this.whiteScore = whiteScore;
	}

	public int getBlackScore() {
		return blackScore;
	}

	public void setBlackScore(int blackScore) {
		this.blackScore = blackScore;
	}

	public int[][] boardPawnsCount() {

		String firstLetter;
		String secondLetter;
		// initialize count
		int whitePawns = 0;
		int whiteKing = 0;
		int whiteRooks = 0;
		int blackPawns = 0;
		int blackKing = 0;
		int blackRooks = 0;

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				firstLetter = Character.toString(board[row][col].charAt(0));

				if (firstLetter.equals(" ") || firstLetter.equals("P")) {
					continue;
				} else if (firstLetter.equals("W")) { // for white pieces
					secondLetter = Character.toString(board[row][col].charAt(1));
					switch (secondLetter) {
					case "K":
						whiteKing++;
						break;
					case "P":
						whitePawns++;
						break;
					case "R":
						whiteRooks++;
						break;
					}
				} else { // for black pieces
					secondLetter = Character.toString(board[row][col].charAt(1));
					switch (secondLetter) {
					case "K":
						blackKing++;
						break;
					case "P":
						blackPawns++;
						break;
					case "R":
						blackRooks++;
						break;
					}
				}
			}
		}

		int[][] pawnsCount = { { whiteKing, whitePawns, whiteRooks }, { blackKing, blackPawns, blackRooks } };

		return pawnsCount;

	}

	public boolean hasNoMoves(ArrayList<String> availableMoves) {
		if (availableMoves.size() == 0)
			return true;
		return false;

	}

	public boolean isTerminal() {
		int[][] pawnsCount = boardPawnsCount();
		int noOfKings = pawnsCount[WHITE][KINGS] + pawnsCount[BLACK][KINGS];
		int noOfNonKingPieces = pawnsCount[WHITE][PAWNS] + pawnsCount[BLACK][PAWNS] + pawnsCount[WHITE][ROOKS]
				+ pawnsCount[BLACK][ROOKS];

		if (noOfKings == 1 || (noOfKings == 2 & noOfNonKingPieces == 0))
			return true;
		return false;
	}

	public ArrayList<String> getAvailableActions(World world, boolean isWhite) {
		ArrayList<String> availableMoves = new ArrayList<String>();

		if (isWhite)
			world.whiteMoves(board, availableMoves);
		else
			world.blackMoves(board, availableMoves);

		return availableMoves;
	}

}
