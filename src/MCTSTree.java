package student_player;

public class MCTSTree {
	TreeNode aRoot;

	public MCTSTree(TreeNode pRoot) {
		assert pRoot != null;
		this.aRoot = pRoot;
	}

	public TreeNode getRoot() {
		return this.aRoot;
	}

	public TreeNode findBestMove(int timeLimit) 
	{

		int counter =0;
		long endGameTimer = System.currentTimeMillis() + timeLimit;
		while (System.currentTimeMillis() < endGameTimer) 

		{
			counter++;

			//iterating from the root each time
			TreeNode treePolicySelection = this.aRoot;
			//going through the nodes using tree policy until we reach a leaf
			while ( !treePolicySelection.getChildren().isEmpty()) 
			{
				treePolicySelection = MyTools.findBestUCTNode(treePolicySelection);
			}
			//changing the name for convenience
			TreeNode rolloutNode = treePolicySelection;

			//if the node hasn't been visited then a rollout and subsequent backpropagation are performed on it
			if ( rolloutNode.getNumVisits() == 0 ) 
			{
				int winningPlayer = MyTools.simulateRollout(rolloutNode.getBoardState());
				MyTools.backpropagation(rolloutNode, this.aRoot, winningPlayer);
			} 
			else //if it has been visited then we instantiate its children and expand a random one (i.e. Expansion phase)
			{
				if (! rolloutNode.getBoardState().gameOver())
				{
					MyTools.createChildren(rolloutNode);
					int randomNum = (int) (Math.random()*rolloutNode.getChildren().size());
					//System.out.println("You picked the random child which has index " + randomNum);
					rolloutNode = rolloutNode.getChildren().get(randomNum);
				}
				//we then also perform a rollout and subsequent backpropagation
				int winningPlayer = MyTools.simulateRollout(rolloutNode.getBoardState());
				MyTools.backpropagation(rolloutNode, this.aRoot, winningPlayer);
			}

		}
		//we return the most statistically advantageous child
		return MyTools.findMostCompetitiveMove(this.aRoot);

	}
	/*
	public TreeNode expandTreeAsMuchAsPossible()
	{
		long endGameTimer = System.currentTimeMillis() + 29950;
		while (System.currentTimeMillis() < endGameTimer)
		{
			TreeNode selectionNode = this.aRoot;
			while ( !selectionNode.getChildren().isEmpty()) 
			{
				selectionNode = MyTools.findBestUCTNode(selectionNode);
			}
			TreeNode rolloutNode = selectionNode;


			if ( rolloutNode.getNumVisits() == 0 ) 
			{
				
				while ( ! rolloutNode.getBoardState().gameOver() ) {
					PentagoBoardState board = (PentagoBoardState) rolloutNode.getBoardState().clone();
					Move move = board.getRandomMove();
					board.processMove((PentagoMove) move);
					TreeNode temp = new TreeNode(rolloutNode, board, (PentagoMove) move);
					rolloutNode.addChild(temp);
					rolloutNode = temp;
				}

				int winner = rolloutNode.getBoardState().getWinner();
				MyTools.backpropagation(rolloutNode, this.aRoot, winner);
			} 
			else 
			{
				if (! rolloutNode.getBoardState().gameOver())
				{
					MyTools.createChildren(rolloutNode);
					int randomNum = (int) (Math.random()*rolloutNode.getChildren().size());
					//System.out.println("You picked the random child which has index " + randomNum);
					rolloutNode = rolloutNode.getChildren().get(randomNum);
				}
				
				while ( ! rolloutNode.getBoardState().gameOver() ) {
					PentagoBoardState board = (PentagoBoardState) rolloutNode.getBoardState().clone();
					Move move = board.getRandomMove();
					board.processMove((PentagoMove) move);
					TreeNode temp = new TreeNode(rolloutNode, board, (PentagoMove) move);
					rolloutNode.addChild(temp);
					rolloutNode = temp;
				}

				int winner = rolloutNode.getBoardState().getWinner();
				MyTools.backpropagation(rolloutNode, this.aRoot, winner);
			}
		}
		return MyTools.findBestUCTNode(this.aRoot);
		

	}
	*/
}
