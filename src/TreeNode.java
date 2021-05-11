package student_player;

import java.util.ArrayList;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

public class TreeNode {
	private TreeNode aParent;
	private PentagoMove aMove;
	private PentagoBoardState aState;
	private ArrayList<TreeNode> aChildren;
	private double aNumVisits =0;
	private double aNumWins = 0;
	
	/*
	 * constructor that takes as input a parent, a board state and a move
	 */
	public TreeNode(TreeNode pParent, PentagoBoardState pState, PentagoMove pMove) {
		this.aParent = pParent;
		this.aState =  pState;
		this.aChildren = new ArrayList<TreeNode>();
		this.aMove = pMove;
	}
	

	
	public TreeNode getParent() {
		return this.aParent;
	}
	
	public PentagoBoardState getBoardState() {
		return this.aState;
	}
	
	public ArrayList<TreeNode> getChildren() {
		return this.aChildren;
	}
	
	public double getNumVisits() {
		return this.aNumVisits;
	}
	
	public double getNumWins() {
		return this.aNumWins;
	}
	
	public PentagoMove getMove() {
		return this.aMove;
	}
	
	public void incrementNumVisits() {
		this.aNumVisits++;
	}
	
	public void updateNumWins(double wins) {
		this.aNumWins+= wins;
	}
	
	public void addChild(TreeNode pChild) {
		this.aChildren.add(pChild);
	}	
	
	/*
	 * toString method for debugging
	 */
	public String toString() {
		String state = "\n" + this.aState.toString();
		String visits = "and has " + this.aNumWins + "/" + this.aNumVisits + " wins/visits.";
		return state + visits;
	}
	
}
