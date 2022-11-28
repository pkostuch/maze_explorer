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

    int rows() {
        return mRows;
    }

    int cols() {
        return mCols;
    }

    int get(int row, int col) {
        return mData[row][col];
    }

    boolean valid(int row, int col) {
        return (row >= 0 && row < mRows && col >= 0 && col < mCols);
    }

    boolean blocked(int row, int col)
    {
        return 0 != (mData[row][col] & BLOCKED);
    }

    void reset() {
        for (int[] mDatum : mData) {
            Arrays.fill(mDatum, 0);
        }
    }

    void walk(BiConsumer<Integer, Integer> consumer) {
        for (var row = 0; row < rows(); ++row) {
            for (var col = 0; col < cols(); ++col) {
                consumer.accept(row, col);
            }
        }
    }
    void clear() {
        for (var row = 0; row < rows(); ++row) {
            for (var col = 0; col < cols(); ++col) {
                mData[row][col] &= ~Maze.VISITED;
            }
        }
    }

    boolean visited(int row, int col)
    {
        return  0 != (mData[row][col] & VISITED);
    }

}