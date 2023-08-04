import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // whether sites are opened
    private boolean[] sites;
    private WeightedQuickUnionUF WQUUF;
    private int N;

    public Percolation(int N) {
        sites = new boolean[N * N];  // default false
        this.N = N;

        // the last two items are virtual top site and virtual bottom site,
        // refer to video https://www.youtube.com/watch?v=1RQz8ITHLME&list=PLNSdoiHk6ujjZs46s6XVXEbZUuF1MIO7g&index=4
        WQUUF = new WeightedQuickUnionUF(N * N + 2);
        for (int ix = 0; ix < N; ix++) {
            WQUUF.union(ix, N * N);
        }
        for (int ix = N * (N - 1); ix < N * N; ix++) {
            WQUUF.union(ix, N * N + 1);
        }
    }

    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        int currIndex = xyTo1D(row, col);
        sites[currIndex] = true;

        if (validate(row - 1, col) && isOpen(row - 1, col)) {
            int tempIndex = xyTo1D(row - 1, col);
            WQUUF.union(tempIndex, currIndex);
        }
        if (validate(row + 1, col) && isOpen(row + 1, col)) {
            int tempIndex = xyTo1D(row + 1, col);
            WQUUF.union(tempIndex, currIndex);
        }
        if (validate(row, col - 1) && isOpen(row, col - 1)) {
            int tempIndex = xyTo1D(row, col - 1);
            WQUUF.union(tempIndex, currIndex);
        }
        if (validate(row, col + 1) && isOpen(row, col + 1)) {
            int tempIndex = xyTo1D(row, col + 1);
            WQUUF.union(tempIndex, currIndex);
        }
    }

    public boolean isOpen(int row, int col) {
        return sites[xyTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            return WQUUF.connected(xyTo1D(row, col), N * N);
        } else {
            return false;
        }
    }

    public int numberOfOpenSites() {
        int result = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (isOpen(row, col)) {
                    result++;
                }
            }
        }
        return result;
    }

    public boolean percolates() {
        return WQUUF.connected(N * N, N * N + 1);
    }

    int xyTo1D(int row, int col) {
        return row * N + col;
    }

    boolean validate(int row, int col) {
        return row >= 0 && row < N && col >= 0 && col < N;
    }
}
