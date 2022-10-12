import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.ldap.SortControl;

public class Minimax extends Search {
	// Constants Declaration
	final static int ROWS = 7;
	final static int COLUMNS = 5;
	final static int INDEX = 0;
	final static int EVAL = 1;
	static final long timeLimit = 3990000000L;
	HashMap<String[][], String> hashMoves;
	

	public Minimax(World world) {
		super(world);
		this.hashMoves = new HashMap<>();
	}

	

	
	public static int[] minimaxExec(BoardState board, int depth, boolean maximizingPlayer, int maximizingColor,
			long start) {
		
		boolean isWhite = isWhite(maximizingPlayer, maximizingColor);
		// if time or game over or the proper depth is searched return evaluation5
		boolean isTerminal = board.isTerminal();
		if (depth == 0 || (System.nanoTime() - start) >= timeLimit || isTerminal) {
			
			int eval = evaluate(board, maximizingColor, board.getWhiteScore(), board.getBlackScore(),isTerminal);
			int[] res = { 0, eval };
			return res;
		}

		ArrayList<String> availableMoves = board.getAvailableActions(Search.getWorld(), isWhite);
		int bestMoveIndex = -1;

		if (maximizingPlayer) { // Max
			int maxEval = Integer.MIN_VALUE;
			for (int index = 0; index < availableMoves.size(); index++) { // for each avail move
				String move = availableMoves.get(index);
				BoardState tmpBoard = makeMove(board, move, isWhite);

				int ret[] = minimaxExec(tmpBoard, depth - 1, false, maximizingColor, start);

				if (ret[EVAL] > maxEval) {
					bestMoveIndex = index;
					maxEval = ret[EVAL];
				}
			}
			int[] res = { bestMoveIndex, maxEval };
			return res;
		} else { // MIN
			int minEval = Integer.MAX_VALUE;
			for (int index = 0; index < availableMoves.size(); index++) { // for each avail move
				String move = availableMoves.get(index);
				BoardState tmpBoard = makeMove(board, move, isWhite);
				int[] ret = minimaxExec(tmpBoard, depth - 1, true, maximizingColor, start);

				if (ret[EVAL] < minEval) {
					bestMoveIndex = index;
					minEval = ret[EVAL];
				}
			}
			int[] res = { bestMoveIndex, minEval };
			return res;
		}
	}
	
	public static ArrayList<String> sortAvailMoves(BoardState boardState,ArrayList<String> availableMoves) {
		ArrayList<String> sorted = new ArrayList<>(availableMoves.size());
		
		// 1. HashMoves
		
		// 2. captures
		int captureIndex = 0;
		
		for (int i=0; i<availableMoves.size(); i++) {
			String move = availableMoves.get(i);
			int x2 = Integer.parseInt(Character.toString(move.charAt(2)));
			int y2 = Integer.parseInt(Character.toString(move.charAt(3)));
			
			if (isCapture(boardState.getBoard(), x2, y2)){
				sorted.add(captureIndex, move);
				captureIndex++;
			}
			else sorted.add(move);
		}
		
		
		return sorted;
	}

	public static int[] alphaBetaExec(BoardState board, int depth, boolean maximizingPlayer, int maximizingColor,
			long start, int alpha, int beta) {
		// if time or game over or the proper depth is searched return evaluation5
		boolean isWhite = isWhite(maximizingPlayer, maximizingColor);
		//
		// System.out.println("Depth is: " + depth);
		boolean isTerminal =board.isTerminal();
		
		if (depth == 0 || (System.nanoTime() - start) >= timeLimit || isTerminal ) {
			int eval = evaluate(board, maximizingColor, board.getWhiteScore(), board.getBlackScore(),isTerminal);
			int[] res = { 0, eval };
			return res;
		}

//		ArrayList<String> availableMoves = sortAvailMoves(board, board.getAvailableActions(getWorld(), isWhite));
		ArrayList<String> availableMoves = board.getAvailableActions(getWorld(),isWhite);
		int bestMoveIndex = -1;

		if (maximizingPlayer) { // Max
			int maxEval = Integer.MIN_VALUE;
			for (int index = 0; index < availableMoves.size(); index++) { // for each avail move
				String move = availableMoves.get(index);
				BoardState tmpBoard = makeMove(board, move, isWhite);
				int current[] = alphaBetaExec(tmpBoard, depth - 1, false, maximizingColor, start,alpha,beta);

				if (current[EVAL] > maxEval) {
					bestMoveIndex = index;
					maxEval = current[EVAL];
				}
				alpha = Math.max(alpha, current[EVAL]);
				if (beta <= alpha)
					break;
			}
			int[] res = { bestMoveIndex, maxEval };
			return res;
		} else { // MIN
			int minEval = Integer.MAX_VALUE;
			for (int index = 0; index < availableMoves.size(); index++) { // for each avail move
				String move = availableMoves.get(index);
				BoardState tmpBoard = makeMove(board, move, isWhite);

				int[] current = alphaBetaExec(tmpBoard, depth - 1, true, maximizingColor, start,alpha,beta);

				if (current[EVAL] < minEval) {
					bestMoveIndex = index;
					minEval = current[EVAL];
				}
				
				beta = Math.min(beta, current[EVAL]);
				if (beta <= alpha)
					break;
			}
			int[] res = { bestMoveIndex, minEval };
			return res;
		}
	}

}
