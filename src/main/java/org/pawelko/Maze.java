package org.pawelko;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class Maze {

    public static final int VISITED = 1;

    public static final int BLOCKED = 2;

    public static final int FILTER = BLOCKED;
    int mRows;
    int mCols;
    int[][] mData;

    Maze(int rows, int cols) {
        mRows = rows;
        mCols = cols;
        mData = new int[mRows][mCols];
        reset();
    }

    public int rows() {
        return mRows;
    }

    public int cols() {
        return mCols;
    }

    public int get(int row, int col) {
        return mData[row][col];
    }

    public boolean valid(int row, int col) {
        return (row >= 0 && row < mRows && col >= 0 && col < mCols);
    }

    public boolean blocked(int row, int col) {
        return 0 != (mData[row][col] & BLOCKED);
    }

    public void reset() {
        for (int[] mDatum : mData) {
            Arrays.fill(mDatum, 0);
        }
    }

    public void walk(BiConsumer<Integer, Integer> consumer) {
        for (var row = 0; row < rows(); ++row) {
            for (var col = 0; col < cols(); ++col) {
                consumer.accept(row, col);
            }
        }
    }

    public void clear() {
        for (var row = 0; row < rows(); ++row) {
            for (var col = 0; col < cols(); ++col) {
                mData[row][col] &= ~Maze.VISITED;
            }
        }
    }

    public boolean visited(int row, int col) {
        return 0 != (mData[row][col] & VISITED);
    }

    public void setVisited(int row, int col) {
        mData[row][col] |= VISITED;
    }

}