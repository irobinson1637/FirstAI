import java.util.ArrayList;
import java.util.Random;

class AI2 {
	Node root = new Node();
	Node recentMove = root;
	Random r = new Random();
	int whichP;
	Board testboard = new Board();

	AI2(boolean playerP1) {
		if (playerP1) whichP = 2;
		else whichP = 1;
	}

	int MCTS(int numTests, Board board) {
		testboard.swap(board);
		Node cur = recentMove;
		double best = Integer.MIN_VALUE;
		int move = 0;
		if (numTests>30000 && testboard.threerow() != -1)	return testboard.threerow(); // if we need to block a 3 in a row or win with one, do that immediately and forget evaluation
		for (int i = 0; i < numTests; i++) {
			int x = r.nextInt(7);
			if (testboard.legalMove(x)) testboard.makeMove(x);
			Node newNode = new Node(x, 0, 0, cur);
			if (cur.childAlready(newNode)) {
				cur = cur.findChild(x, cur);
			}
			else {
				cur.addChildren(newNode);
				cur = newNode;
			}
			int winner = testboard.winner();
			if (winner != 0) {
				while (cur != recentMove) {
					if (winner == whichP) cur.wins++;
					else {
						if (winner != -1) cur.loss++;
					}
					cur.plays++;
					cur = cur.parent;
				}
				testboard.swap(board);
			}
		}
		ArrayList<Node> a = recentMove.getChildren();
		for (int i = 0; i < a.size(); i++) {
			Node cur2 = a.get(i);
			double net = cur2.winpct(); 
			if (net > best) {
				best = net;
				move = cur2.key;
				recentMove = cur2;
			}
		}
		return move;
	}
}
