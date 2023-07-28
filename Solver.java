/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private Node last;
    private boolean solvable;
    private int numMoves;

    private class Node {
        Node prev;
        int moves;
        Board board;
        int man;

        public Node(Node p, int m, Board b, int man) {
            this.prev = p;
            this.moves = m;
            this.board = b;
            this.man = man;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();
        // initialize pq
        ManhattanPriority nodeComparator = new ManhattanPriority();
        MinPQ<Node> pq = new MinPQ<>(nodeComparator);
        Node first = new Node(null, 0, initial, initial.manhattan());
        pq.insert(first);

        // initialize twin pq
        MinPQ<Node> twinpq = new MinPQ<>(nodeComparator);
        Node firstTwin = new Node(null, 0, initial.twin(), initial.twin().manhattan());
        twinpq.insert(firstTwin);

        while (true) {
            // check if board solved
            Node cur = pq.delMin();
            if (cur.board.isGoal()) {
                numMoves = cur.moves;
                solvable = true;
                last = cur;
                break;
            }

            // check if twin board solved
            Node curTwin = twinpq.delMin();
            if (curTwin.board.isGoal()) {
                numMoves = -1;
                solvable = false;
                last = null;
                break;
            }

            // insert neighbors into pq if not equal to previous board
            Iterable<Board> neighbors = cur.board.neighbors();
            for (Board neighbor : neighbors) {
                if (cur != first && neighbor.equals(cur.prev.board)) continue;
                pq.insert(new Node(cur, cur.moves + 1, neighbor, neighbor.manhattan()));
            }

            // insert neighbors into twin pq
            Iterable<Board> twinNeighbors = curTwin.board.neighbors();
            for (Board neighbor : twinNeighbors) {
                if (curTwin != firstTwin && neighbor.equals(curTwin.prev.board)) continue;
                twinpq.insert(new Node(curTwin, curTwin.moves + 1, neighbor, neighbor.manhattan()));
            }

        }


    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.numMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> boards = new Stack<>();
        if (!this.isSolvable()) return null;
        Node n = this.last;
        while (n != null) {
            boards.push(n.board);
            n = n.prev;
        }
        return boards;
    }

    private class ManhattanPriority implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            return Integer.compare(n1.moves + n1.man,
                                   n2.moves + n2.man);
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
