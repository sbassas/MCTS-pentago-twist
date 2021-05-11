Implementation of a **Monte Carlo Tree Search (MCTS)** algorithm in `Java`.

# Creating an AI game playing agent for the Pentago Twist board game
## What is Pentago Twist?
Pentago Twist is a twist on the classic two player board game *Pentago*. The game is played on a 6x6 grid which is further divided into 3x3 quadrants that can either be rotated 90 degrees clockwise or flipped symmetrically on their longitudinal axis. Players play one after the other where a turn consists of two necessary steps executed in the following order:
1. Place a chip on the board
2. Rotate or flip a quadrant

The first player to align five of their chips vertically, diagonally, or horizontally wins. In the event that both players align 5 of their chips on the same turn, a draw is called.

The source code for Pentago Twist in Java can be found in [this repo](https://github.com/SaminYeasar/pentago_twist.git) written by Samin Yeasar.
## Motivation
For this project, I needed a game playing agent that was capable of handling the high branching complexity of Pentago Twist, which ranges anywhere from ~30-160 at any given move. Moreover, I needed an agent that was going to be efficient in winning this two player, zero-sum game without designing an evaluation function that requires deep knowledge of the game or being trained with machine learning. MCTS checked both of those boxes.
## Usage
In typical MCTS fashion, I created a tree representing the Tree Search aspect of the algorithm in a `MCTSTree` class. The nodes of the tree are objects from the `TreeNode` class which has the following attributes:
* A parent `TreeNode` (except the root of the tree for which this field is `null`)
* An `ArrayList<TreeNode>` representing each node’s children
* A `PentagoBoardState` object representing the board state at that node
* An `int` representing the number of times the node has been visited
* An `int` representing the number of times the node was visited in a simulation that resulted in a win

The `MCTSTree` class itself has a TreeNode argument that represents the root, and the serach itself is run from this class.
I synthesize the sequence of calls that take place in my algorithm in the following JetUML diagram and explain it below
![MCTS calls](/MCTScalls.png)
1. In the first note, we have a method `computeUCTScore()` from class `MyTools`, that takes a node as input and returns its UCT score using the formula: Q(s,a) + c√(log⁡(n(s))/(n(s,a))) where **Q(s,a)** is the winning ratio of taking action **a**, **c** is the scaling constant determining the importance of exploration/exploitation, **n(s)** is the number of times state **s** has been visited, and finally **n(s,a)** is the number of times we have taken action **a**. This method is called from within the method `findBestUCTNode()`, from class `MyTools`, which represents the selection process of the MCTS algorithm. Essentially this method iterates through the children of a node computing the UCT score for each child, and it returns the child that scored the highest at the end. This method is called iteratively until a leaf is reached (i.e. a node without children)
2. In the second note, we have the `createChildren()` method from the class `MyTools`, which creates as many tree nodes as there are number of legal moves obtained from calling `getAllLegalMoves()` on a node’s `PentagoBoardState` attribute and stores them as children of the node on which this method was called on from within the `MCTSTree` class. After this method is called, one of the children just created is randomly selected as the node from which a rollout it simulated from within the `MCTSTree` class. A call to this method is then followed by a call to `simulateRollout()` which takes a `TreeNode` as input and keeps processing random moves on it until a winning board state for either player is obtained. Finally, in this step, I call the method `backpropagation()` in `MyTools` from `MCTSTree`, which keeps updating all the nodes from which the rollout was called on to the root of the tree, adding to the total wins attribute of each node (0 for a loss, 0.5 for draw, and 1 for a win).
3. In this third step, I finally call `findMostCompetitiveMove()` on the root to find the immediate best move once the time is up, and this node is returned to the `findBestMove()` method called in step 5. It takes into consideration if a move is a win, a loss, or else its win/visits ratio.
4. In this step, we take a step back, to see that the first 2 steps are called iteratively from within the `findBestMove()` method located within the `MCTSTree` class. This loop is called until a certain time limit is reached, after which the third step is executed, and the move associated to the node returned at the end of step three is returned to the `StudentPlayer` class. In this step, we also choose whether a node has its children created or whether a rollout is called on it immediately based on whether its visit attribute is greater than 0.
5. In the `StudentPlayer` class, we look at the turn number associated to the board state passed as input to the `chooseMove()` method. Then, if the turn number is 0, then the time limit passed to the `findBestMove()` method is 30 seconds, else it is set to 2 seconds. If the loop runs for 30 seconds, I make the `findBestMove()` method run on a new `MCTSTree` object. At the end of this call, before returning the best child node’s move, I store the node as the new root of the MCTSTree. If the loop runs for 2 seconds, I run the algorithm on a child of the root of the MCTS tree which, as just mentioned, represents a board state for the opponent’s move.

