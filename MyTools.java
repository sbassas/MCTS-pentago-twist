package student_player;

import java.util.ArrayList;
import java.util.Collections;

import boardgame.Board;
import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

public class MyTools {
	public static double getSomething() {
		return Math.random();
	}

	/*
	 * method to find the best node to visit using the UCT tree policy
	 */
	public static TreeNode findBestUCTNode(TreeNode pNode) {
		TreeNode bestChild = null;
		double bestScore = -1;
		//itearting through all the children to find and store for return the highest UCT scoring node
		for (TreeNode tn : pNode.getChildren()) {
			double score = computeUCTScore(pNode, tn);
			if (score > bestScore) {
				bestScore = score;
				bestChild = tn;
			}
		}
		return bestChild;
	}
	
	/*
	 * method to compute the Upper Confidence Tree used in the method above
	 */
	public static double computeUCTScore(TreeNode pNode, TreeNode pChild) {
		if (pChild.getNumVisits() == 0) 
			return Integer.MAX_VALUE; //nodes with no visits are set to having the highest value to increase exploration
		double scalingConstant = Math.sqrt(2.0);
		double exploitation = (pChild.getNumWins() / pChild.getNumVisits());
		double exploration = Math.sqrt(Math.log( pNode.getNumVisits()) / pChild.getNumVisits());
		return ( exploitation + (scalingConstant * exploration));
	}

	/*
	 * method to find the root's best child using UCT computation and board status
	 */
	public static TreeNode findMostCompetitiveMove(TreeNode pRoot) {
		TreeNode bestChild = null;
		double bestScore = -1;
		//iterating through the children of the root to find best move
		for (TreeNode child : pRoot.getChildren()) {
			//if the child is directly a winning state, UCT don't matter 
			if ( child.getBoardState().getWinner() == pRoot.getBoardState().getTurnPlayer() )
				return child;
			//if the child is a direct loss, it shouldn't be considered
			else if ( child.getBoardState().getWinner() == 1 - pRoot.getBoardState().getTurnPlayer() )
				continue;
			else //otherwise, returning node with highest win ratio
			{
				double score = ( (child.getNumWins()) / (child.getNumVisits() ) );
				if (score > bestScore) {
					bestScore = score;
					bestChild = child;
				}
			}
		}
		if (bestChild != null) 
			return bestChild;
		else  //in case all children represent wins or losses and one needs to be chosen to not return null
		{
			PentagoBoardState tempState = (PentagoBoardState) pRoot.getBoardState().clone();
			PentagoMove tempMove = (PentagoMove) tempState.getRandomMove();
			tempState.processMove(tempMove);
			return new TreeNode(pRoot, tempState, tempMove);
		}
	}


	/*
	 * method to instantiate all the children of a node in the MCTS tree during for later expansion
	 */
	public static void createChildren(TreeNode pNode) {
		//getting all legal available moves
		ArrayList<PentagoMove> nextMoves = pNode.getBoardState().getAllLegalMoves();
		
		for (PentagoMove move : nextMoves) {
			//for each move, I create its resulting board state
			PentagoBoardState newBoardState = (PentagoBoardState) pNode.getBoardState().clone();
			newBoardState.processMove(move);
			//here I check if the new board state already exists in a child node of the input node
			boolean contains = false;
			for (TreeNode tn : pNode.getChildren()) {
				String firstBoard = tn.getBoardState().toString();
				String secondBoard = newBoardState.toString();
				if ( firstBoard.equals(secondBoard))
					contains = true;
			}
			//if it does exist in the arraylist of children, I create a new Tree Node that I add to the other children
			if (!contains) {
				TreeNode aChild = new TreeNode(pNode, newBoardState, move);
				pNode.addChild(aChild);
			}
			Collections.shuffle(pNode.getChildren());
		}
		
	}

	/*
	 * method to simulate the rollout of a node using a random default policy
	 */
	public static int simulateRollout(PentagoBoardState pbs) {
		//keep processing new moves to iterate through game without using storage
		PentagoBoardState board = (PentagoBoardState) pbs.clone();
		while ( ! board.gameOver() ) {
			Move move = board.getRandomMove();
			board.processMove((PentagoMove) move);
			
		}

		return board.getWinner();
	}

	/*
	 * method to back propagate the results of a rollout to update the visitation and winning values of nodes in the path
	 * from the root to the leaf from which the rollout was conducted
	 */
	public static void backpropagation(TreeNode pNode, TreeNode pRoot, int winner) {
		//updating value of score to add
		TreeNode temp = pNode;
		int playerTurn = pRoot.getBoardState().getTurnPlayer();
		double win = 0;
		if (winner == playerTurn)
			win =1;
		else if ( winner == Board.DRAW)
			win = 0.5;
		//adding score to all nodes on path to root
		while (temp != pRoot) {			
			temp.updateNumWins(win);
			temp.incrementNumVisits();
			temp = temp.getParent();


		}
		temp.updateNumWins(win);
		temp.incrementNumVisits();
	}
}





