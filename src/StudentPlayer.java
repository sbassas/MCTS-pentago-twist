package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {
	MCTSTree aTree = null;
	/**
	 * You must modify this constructor to return your student number. This is
	 * important, because this is what the code that runs the competition uses to
	 * associate you with your agent. The constructor should do nothing else.
	 */
	public StudentPlayer() {
		super("260845896");
	}

	/**
	 * This is the primary method that you need to implement. The ``boardState``
	 * object contains the current state of the game, which your agent must use to
	 * make decisions.
	 */
	public Move chooseMove(PentagoBoardState boardState) 
	{
		//setting a default time limit, if it is not update this will throw an error, which is good because
		//it means there is a mistake in the algorithm
		int timeLimit = 0;
		if (boardState.getTurnNumber() == 0 ) 
		{
			//giving a bit under 30 seconds for the first turn
			timeLimit = 29950;
			TreeNode root = new TreeNode(null,boardState, null);
			//creating a new Tree to be expanded on the first iteration
			this.aTree = new MCTSTree(root);
			/* For fuller expansion experiments
			TreeNode newRoot = this.aTree.expandTreeAsMuchAsPossible();
			this.aTree = new MCTSTree(newRoot);
			return newRoot.getMove();
			 */
		} 
		else if ( boardState.getTurnNumber() < 2 )
		{
			//giving a bit under 2 seconds for the following turns
			timeLimit = 1950;
			TreeNode newRoot = null;
			String firstBoard = boardState.toString();
			//in the second round, we can find the board state given as input among the children of the root that was stored
			for (TreeNode tn : this.aTree.getRoot().getChildren()) 
			{
				String secondBoard = tn.getBoardState().toString();
				if (firstBoard.equals(secondBoard)) 
					newRoot= tn;

			} //if we find it, it becomes the new root and we gain computation time
			if ( newRoot != null)
			{
				this.aTree = new MCTSTree(newRoot);
			}
			else //if not we create a new root on which to perform MCTS using the input board state
			{
				this.aTree = new MCTSTree(new TreeNode (null, boardState, null));
				System.out.println("HI SERAPHIN! we couldn't find the new root at turn #" + this.aTree.getRoot().getBoardState().getTurnNumber());
			}

		} 
		else //beyond turn 2 we know the above strategy is no longer viable, so we avoid losing time searching
			//and just create a new root
		{
			timeLimit = 1950;  
			this.aTree = new MCTSTree(new TreeNode (null, boardState, null));
		}

		TreeNode nextMove  = this.aTree.findBestMove(timeLimit);
		//updating the root of the tree for the next move
		this.aTree = new MCTSTree(nextMove);
		return nextMove.getMove();
	}
}