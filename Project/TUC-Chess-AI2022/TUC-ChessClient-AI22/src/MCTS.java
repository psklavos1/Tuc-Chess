import java.util.ArrayList;
import java.util.Random;

public class MCTS extends Search {
	static final int ROWS = 7;
	static final int COLUMNS = 5;
//	static final long timeLimit = 399000000000L;
	static final long timeLimit = 3990000000L;

	public MCTS(World world) {
		super(world);
	}
	
	public static SearchNode findMaximizingUCTNode(SearchNode node) {
		int totalParentVisits = node.getVisitCount();
		SearchNode bestNode = null;
		double bestUCT = Double.NEGATIVE_INFINITY;

		for (SearchNode child : node.getChildren()) {
			double childUCT = child.getUCTValue(totalParentVisits);
			if (childUCT > bestUCT) {
				bestUCT = childUCT;
				bestNode = child;
			}
		}
		return bestNode;
	}

	private static SearchNode selectPromisingNode(SearchNode root) {
		SearchNode node = root;
		while (!node.isLeaf()) {
			node = findMaximizingUCTNode(node);
		}
		return node;
	}

	public static boolean expand(SearchNode node, boolean isMaximizing, int maximizingColor) {
		BoardState boardState = node.getBoardState();
		boolean isWhite = isWhite(isMaximizing, maximizingColor);
		boolean isExpanded = false;
		
//		if (!boardState.isTerminal()) {
		ArrayList<String> availableMoves = boardState.getAvailableActions(Search.getWorld(), isWhite);
		if(!boardState.isTerminal() && !boardState.hasNoMoves(availableMoves)) {
			for (String move : availableMoves) {
				BoardState newBoardState = makeMove(boardState, move, isWhite);
				SearchNode newNode = new SearchNode(newBoardState,move, node);
				node.getChildren().add(newNode);
			}
			isExpanded = true;
		}
		return isExpanded;
	}

	private static String selectRandomAction(ArrayList<String> availableMoves) {
		Random ran = new Random();
		
		int x = ran.nextInt(availableMoves.size());

		return availableMoves.get(x);
	}

	public static double rollout(SearchNode node) {
		SearchNode tmpNode = new SearchNode(node);
		
		ArrayList<String> availableMoves;
		
		while(!tmpNode.getBoardState().isTerminal()) {
			availableMoves = tmpNode.getBoardState().getAvailableActions(getWorld(), tmpNode.isWhite());
			
			if(tmpNode.getBoardState().hasNoMoves(availableMoves)) {
				if(tmpNode.isWhite()) {
					tmpNode.getBoardState().setBlackScore(tmpNode.getBoardState().getBlackScore()+8);
				}
				else tmpNode.getBoardState().setWhiteScore(tmpNode.getBoardState().getWhiteScore()+8);
				break;
			}
			
			String move = selectRandomAction(availableMoves);
			BoardState newBoardState = makeMove(tmpNode.getBoardState(), move, tmpNode.isWhite());
			tmpNode = new SearchNode(newBoardState, move, tmpNode);
		}
		int maximizingColor = (tmpNode.isMaximizing() && tmpNode.isWhite() || !tmpNode.isMaximizing() && !tmpNode.isWhite()) ? 0 : 1;
		BoardState state = tmpNode.getBoardState();
		
		return (double)evaluateMCTS(state,maximizingColor);
	}

	public static void backPropagation(SearchNode node, double reward) {
		SearchNode tmpNode = node;
		while (tmpNode != null) {
			tmpNode.update(reward);
			tmpNode = tmpNode.getParent();
		}
	}

	public static String MCTSExec(BoardState boardState, boolean maximizingPlayer, int maximizingColor, long start) {
		SearchNode root = new SearchNode(boardState);
		root.setMaximizing(maximizingPlayer);
		root.setWhite(isWhite(maximizingPlayer, maximizingColor));
		SearchNode current = root;
		
		expand(current, maximizingPlayer, maximizingColor);
		
		while((System.nanoTime()-start)<timeLimit) {
			
			current = selectPromisingNode(root); // select
			
			// TODO ATTENTION TO EXPAND CONDITION- maybe expand 1 by 1
			if(current.getVisitCount()>0) { // if it has been visited before
				boolean isExpanded=expand(current, maximizingPlayer, maximizingColor);
				
				if(isExpanded)
					current = current.randomChild();
			}
			
			double reward = rollout(current);
			backPropagation(current, reward);
		}
		
		if(root.getChildren().size()<=0)
			throw new NullPointerException();
		
		return root.bestChild().getMove();
	}
}
