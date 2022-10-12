import java.util.ArrayList;
import java.util.Random;

public class SearchNode {
	
	private BoardState boardState;
	private String move;
	private boolean isMaximizing;
	private boolean isWhite;
	private int visitCount;
    private double totalReward;
	private SearchNode parent; 
	private ArrayList<SearchNode> children;
	static double C =1/Math.sqrt(2);
	
	
	public SearchNode(BoardState board, String move,SearchNode parent) {
		
		this.boardState = board;
		this.visitCount = 0;
		this.totalReward = 0.0;
		this.isMaximizing = !parent.isMaximizing();
		this.isWhite = !parent.isWhite();
		this.children = new ArrayList<SearchNode>();
		this.parent = parent;
		this.move = move;
	}
	
	public SearchNode(BoardState board) {
		this.boardState = board;
		this.visitCount = 0;
		this.totalReward = 0.0;
		this.children = new ArrayList<SearchNode>();
		this.parent = null;
		this.move = "";
	}
	
	
	public SearchNode(SearchNode node) {
		// state
		String [][] currentBoard = new String[7][5];
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 5; j++) {
				currentBoard[i][j] = node.getBoardState().getBoard()[i][j];
			}
		}
		this.boardState = new BoardState(currentBoard, node.getBoardState().getWhiteScore(), node.getBoardState().getBlackScore());
		this.visitCount = node.getVisitCount();
		this.totalReward = node.getTotalReward();
		this.isMaximizing = node.isMaximizing();
		this.isWhite = node.isWhite();
		this.parent = node.getParent();
		this.move = node.getMove();
		this.children = node.getChildren();
	}
	
	public SearchNode getParent() {
		return parent;
	}
	
	

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public void setParent(SearchNode parent) {
		this.parent = parent;
	}
	
	public SearchNode randomChild() {
		Random ran = new Random();
		
		int x = ran.nextInt(children.size());

		return children.get(x);
	}
	
	
	public SearchNode bestChild() {
		double bestAvgReward = Double.NEGATIVE_INFINITY;
		SearchNode bestChild = null;
		
		for(SearchNode child : children) {
			if(child.getAvgReward() > bestAvgReward) {
				bestAvgReward = child.getAvgReward();
				bestChild = child;
			}
		}
		return bestChild;
	}
	
	
	public boolean isWhite() {
		return isWhite;
	}

	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}

	public double getTotalReward() {
		return totalReward;
	}

	public void setTotalReward(double totalReward) {
		this.totalReward = totalReward;
	}

	public ArrayList<SearchNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<SearchNode> children) {
		this.children = children;
	}

	public BoardState getBoardState() {
		return boardState;
	}


	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}


	public boolean isMaximizing() {
		return isMaximizing;
	}


	public void setMaximizing(boolean isMaximizing) {
		this.isMaximizing = isMaximizing;
	}


	public int getVisitCount() {
		return visitCount;
	}


	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}


	public double getAvgReward() {
		return (double)totalReward/(double)visitCount;
	}


	public void setAvgReward(double meanScore) {
		this.totalReward = meanScore;
	}
	
	
	public boolean isLeaf() {
		return children.size() == 0;
	}
	
	public boolean hasParent() {
		return (this.parent != null);
	}
	
	public void update(double value) {
		this.visitCount++;
		this.totalReward += value;
	}
	
	public double getUCTValue(int totalVisits) {
		if(visitCount == 0)
			return Double.POSITIVE_INFINITY;
		
		double meanReward = getAvgReward();
		return meanReward +2*C*Math.sqrt((double)Math.log(totalVisits) / (double) visitCount); 
	}


}
