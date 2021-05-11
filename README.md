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
I synthesize the sequence of calls that take place in my algorithm in the following JetUML diagram.
![MCTS calls](https://github.com/purpleboiii/MCTS-pentago-twist/MCTScalls.png)
