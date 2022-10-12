import java.util.SplittableRandom;

public class Search {

	static private World world;
	static final int ROWS = 7;
	static final int COLUMNS = 5;
	final static int WHITE = 0;
	final static int BLACK = 1;
	final static int KINGS = 0;
	final static int PAWNS = 1;
	final static int ROOKS = 2;

	public Search(World world) {
		Search.world = world;
	}

	public static World getWorld() {
		return world;
	}

	public static boolean isWhite(boolean maximizingPlayer, int maximizingColor) {
		if (maximizingPlayer && maximizingColor == 0 || !maximizingPlayer && maximizingColor == 1)
			return true;
		return false;
	}

	public static int increasedPoints(int points, int increase) {
		return points += increase;
	}

	public static int evaluateMCTS(BoardState boardState, int maximizingColor) {
		if (boardState.getWhiteScore() - boardState.getBlackScore() == 0)
			return 0;

		if (maximizingColor == 0) {
			if (boardState.getWhiteScore() - boardState.getBlackScore() > 0)
				return 1;
			else
				return -4;
		} else {
			if (boardState.getBlackScore() - boardState.getWhiteScore() > 0)
				return 1;
			else
				return -4;
		}

	}

//	public static boolean onKing(BoardState state,int x2, int y2) {
//		return (x2 == state.getKingsPositions()[0][0] && y2 == state.getKingsPositions()[0][1] || 
//				x2 == state.getKingsPositions()[1][0] && y2 == state.getKingsPositions()[1][1]);
//	}

//	public static boolean kingInDanger(BoardState state) {
//		String[][] board = state.getBoard();
//		ArrayList<String> moves;
//
//		moves = state.getAvailableActions(world, false);
//		moves.addAll(state.getAvailableActions(world, true));
//		int x2;
//		int y2;
//
//		for (String move : moves) {
//			x2 = Integer.parseInt(Character.toString(move.charAt(2)));
//			y2 = Integer.parseInt(Character.toString(move.charAt(3)));
//
//			if(onKing(state, x2, y2))
//				return true;
//		}
//		return false;
//
//	}
	
	
//	public static boolean maxWinsAfter(BoardState board,int maximizingColor) {
//		return ((maximizingColor == 0 && board.getWhiteScore()+8-board.getBlackScore()>0) ||
//				(maximizingColor == 1 && board.getBlackScore()+8-board.getWhiteScore()>0));
//	}
	
	public static int evaluate(BoardState boardState, int maximizingColor, int whitePoints, int blackPoints) {

		// TODO add the prizes TODO change points
		int pieces;
		int[][] boardPawnsCount = boardState.boardPawnsCount();

//		if(kingInDanger(boardState)) {
//			if(maxWinsAfter(boardState, maximizingColor)) 
//				return 1000;
//			else
//				return -1000;
//		}
		pieces = 8 * boardPawnsCount[WHITE][KINGS] + 3 * boardPawnsCount[WHITE][ROOKS] + boardPawnsCount[WHITE][PAWNS];
		int whitePiecesScore = whitePoints + pieces;

		pieces = 8 * boardPawnsCount[BLACK][KINGS] + 3 * boardPawnsCount[BLACK][ROOKS] + boardPawnsCount[BLACK][PAWNS];
		int blackPiecesScore = blackPoints + pieces;

		if (maximizingColor == 0) // white
			return whitePiecesScore - blackPiecesScore;
		else
			return blackPiecesScore - whitePiecesScore; // Black
	}

	public static int evalMCTS(int whitePoints, int blackPoints, int maximizingColor) {
		if (maximizingColor == 0) // white
			return whitePoints - blackPoints;
		else
			return blackPoints - whitePoints; // Black
	}

	public static BoardState makeMove(BoardState board, String move, boolean isWhite) {
		int x1 = Integer.parseInt(Character.toString(move.charAt(0)));
		int y1 = Integer.parseInt(Character.toString(move.charAt(1)));
		int x2 = Integer.parseInt(Character.toString(move.charAt(2)));
		int y2 = Integer.parseInt(Character.toString(move.charAt(3)));

		return makeMoveCurBoard(board, x1, y1, x2, y2, isWhite);

	}

	public static int increaseFromCapture(String enemyPart) {
		if (enemyPart.equals("K")) {
			return 8;
		} else if (enemyPart.equals("R")) {
			return 3;
		}
		return 1;
	}

	// public static int captureSequence(BoardState boardState, int x1, int y1,int
	// x2, int y2) {
	// int myIncrease = 0;
	// int oppIncrease = 0;
	//
	// String oppColor =
	// Character.toString(boardState.getBoard()[x2][y2].charAt(0));
	// String oppPiece =
	// Character.toString(boardState.getBoard()[x2][y2].charAt(1));
	//
	// myIncrease = increaseFromCapture(oppPiece);
	//
	//
	// return 1;
	//
	// }
	//
	// public static boolean isBeneficialCapture(BoardState boardState,int x2, int
	// y2) {
	//
	//
	// if(gameWiningCapture(boardState, x2, y2) || )
	//
	// return true;
	// }

	public static boolean isCapture(String[][] board, int x2, int y2) {
		String content = Character.toString(board[x2][y2].charAt(0));
		if (content.equals(" "))
			return false;
		return true;
	}

	public static boolean gameWiningCapture(BoardState boardState, int x2, int y2) {

		String color = Character.toString(boardState.getBoard()[x2][y2].charAt(0));
		if (color.equals("P"))
			return false;

		String enemyPart = Character.toString(boardState.getBoard()[x2][y2].charAt(1));

		if (enemyPart.equals("K")) {
			if (color.equals("B") && boardState.getWhiteScore() + 8 - boardState.getBlackScore() > 0)
				return true;
			else if (color.equals("W") && boardState.getBlackScore() + 8 - boardState.getWhiteScore() > 0)
				return true;
		}
		return false;
	}

	private static BoardState makeMoveCurBoard(BoardState board, int x1, int y1, int x2, int y2, boolean isWhite) {
		String[][] currentBoard = new String[ROWS][COLUMNS];
		int whitePoints;
		int blackPoints;
		int increase = 0;
		String firstChar;

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 5; j++) {
				currentBoard[i][j] = board.getBoard()[i][j];
			}
		}

		whitePoints = board.getWhiteScore();
		blackPoints = board.getBlackScore();

		String chesspart = Character.toString(currentBoard[x1][y1].charAt(1));
		boolean pawnLastRow = false;

		// update king position if moved
//		if (chesspart.equals("K")) {
//			firstChar = Character.toString(currentBoard[x1][y1].charAt(0));
//			if (firstChar.equals("W")) {
//				board.getKingsPositions()[0][0] = x2;
//				board.getKingsPositions()[0][1] = y2;
//			} else {
//				board.getKingsPositions()[1][0] = x2;
//				board.getKingsPositions()[1][1] = y2;
//			}
//		}

		// check if it is a move that has made a move to the last line
		if (chesspart.equals("P"))
			if ((x1 == ROWS - 2 && x2 == ROWS - 1) || (x1 == 1 && x2 == 0)) {
				currentBoard[x2][y2] = " "; // in case an opponent's chess part has just been captured
				currentBoard[x1][y1] = " ";
				pawnLastRow = true;
				if (isCapture(currentBoard, x2, y2))// a capture is made
				{
					increase = increaseFromCapture(Character.toString(currentBoard[x2][y2].charAt(1)));
					increase++;
				} else
					increase = 1;
			}

		// otherwise
		if (!pawnLastRow) {
			// check if next position is empty
			firstChar = Character.toString(currentBoard[x2][y2].charAt(0));
			if (isCapture(currentBoard, x2, y2)) // Case that the score has to change
			{
				if (firstChar.equals("P")) { // Prize
					SplittableRandom random = new SplittableRandom();
					if (random.nextInt(1, 101) <= 95) // with probability 95%
						increase = 1;
				} else { // if it captures enemy piece
					increase = increaseFromCapture(Character.toString(currentBoard[x2][y2].charAt(1)));
				}
			}

			currentBoard[x2][y2] = currentBoard[x1][y1];
			currentBoard[x1][y1] = " ";
		}
		if (increase != 0) {
			if (isWhite)
				whitePoints = increasedPoints(whitePoints, increase);
			else
				blackPoints = increasedPoints(blackPoints, increase);
		}

		// check if a prize has been added in the game
		// SplittableRandom random = new SplittableRandom();
		// boolean addPrize = random.nextInt(1, 101) <= 20; // with 20% probability
		// if (addPrize) {
		// addPrize(currentBoard);
		// }

		BoardState retBoard = new BoardState(currentBoard, whitePoints, blackPoints);
		return retBoard;
	}

	public static void addPrize(String[][] currentBoard) {
		SplittableRandom random = new SplittableRandom();
		String part;
		int prizeX;
		int prizeY;
		do {
			prizeX = random.nextInt(0, ROWS);
			prizeY = random.nextInt(0, COLUMNS);
			part = Character.toString(currentBoard[prizeX][prizeY].charAt(0));
		} while (!part.equals(" "));
		currentBoard[prizeX][prizeY] = "P";
	}

}
