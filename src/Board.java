/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int[][] board;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        if (tiles.length != tiles[0].length) throw new IllegalArgumentException();
        this.n = tiles.length;
        this.board = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.board[i][j] = tiles[i][j];
            }
        }

    }

    // string representation of this board
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.n);
        for (int i = 0; i < this.n; i++) {
            b.append("\n");
            for (int j = 0; j < this.n; j++) {
                b.append(this.board[i][j]);
                b.append(" ");
            }
        }
        String s = b.toString();
        return s;
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != 0 && this.board[i][j] != i * this.n + j + 1)
                    count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < n; j++) {
                if (!(this.board[i][j] == 0 || this.board[i][j] == i * n + j + 1)) {
                    int correctRow = (this.board[i][j] - 1) / n;
                    int correctCol = (this.board[i][j] - 1) % n;
                    count += Math.abs(correctRow - i) + Math.abs(correctCol - j);
                }

            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (!(i == this.n - 1 && j == this.n - 1) && this.board[i][j] != i * this.n + j + 1)
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        int[][] that = ((Board) y).getBoard();
        if (this.n != ((Board) y).dimension()) return false;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != that[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        int r = 0, c = 0;
        boolean stop = false;
        for (int i = 0; i < this.n && !stop; i++) {
            for (int j = 0; j < this.n && !stop; j++) {
                if (this.board[i][j] == 0) {
                    r = i;
                    c = j;
                    stop = true;
                }
            }
        }
        for (int k = 0; k < 4; k++) {
            int x, y;
            if (k == 0) {
                x = r - 1;
                y = c;
            }
            else if (k == 1) {
                x = r;
                y = c + 1;
            }
            else if (k == 2) {
                x = r + 1;
                y = c;
            }
            else {
                x = r;
                y = c - 1;
            }
            if (checkBounds(x, y)) {
                int[][] b = copy(this);
                exch(b, r, c, x, y);
                neighbors.enqueue(new Board(b));
            }
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] t = copy(this);
        boolean stop = false;
        for (int i = 0; i < n && !stop; i++) {
            for (int j = 0; j < n - 1 && !stop; j++) {
                if (t[i][j] != 0 && t[i][j + 1] != 0) {
                    exch(t, i, j, i, j + 1);
                    stop = true;
                }
            }
        }
        return new Board(t);
    }

    // get board
    private int[][] getBoard() {
        return this.board;
    }

    private boolean checkBounds(int i, int j) {
        return (i < this.n && i >= 0 && j < this.n && j >= 0);
    }

    private void exch(int[][] b, int i, int j, int r, int c) {
        int temp = b[i][j];
        b[i][j] = b[r][c];
        b[r][c] = temp;
    }

    private int[][] copy(Board b) {
        int[][] grid = b.getBoard();
        int[][] result = new int[b.dimension()][b.dimension()];
        for (int i = 0; i < b.dimension(); i++) {
            for (int j = 0; j < b.dimension(); j++) {
                result[i][j] = grid[i][j];
            }
        }
        return result;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] bm = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b = new Board(bm);
        StdOut.println("toString: \n" + b.toString());
        int[][] gm = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board goal = new Board(gm);
        Board twin = b.twin();
        StdOut.println("twin toString: \n" + twin.toString());
        Board clone = new Board(bm);

        StdOut.println("board toString: \n" + b.toString());

        StdOut.println("Does board equal itself? " + b.equals(b));
        StdOut.println("Does board eqaul null? " + b.equals(null));
        StdOut.println("Does board equal twin? " + b.equals(twin));
        StdOut.println("Does board equal clone? " + b.equals(clone));
        StdOut.println("dimension: " + b.dimension());
        StdOut.println("hamming: " + b.hamming());
        StdOut.println("manhattan: " + b.manhattan());
        StdOut.println("isGoal - false: " + b.isGoal());
        StdOut.println("isGoal - true: " + goal.isGoal());

        StdOut.println("neighbors: ");
        Iterable<Board> i = b.neighbors();
        for (Board value : i) {
            StdOut.println(value.toString());
        }

        Iterable<Board> k = goal.neighbors();
        for (Board value : k) {
            StdOut.println(value.toString());
        }
    }
}
