package org.pawelko;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MazeSnapshot {

    private int rows;
    private int cols;

    private int[] data;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public boolean valid() {
        var size = cols * rows;
        return size == data.length;
    }

    public static MazeSnapshot create(Maze maze) {
        var mazeSnapshot = new MazeSnapshot();
        mazeSnapshot.setCols(maze.cols());
        mazeSnapshot.setRows(maze.rows());
        int[] data = new int[maze.cols() * maze.rows()];
        maze.walk((row, col) -> {
            data[row * maze.cols() + col] = maze.get(row, col) & Maze.FILTER;
        });
        mazeSnapshot.setData(data);
        return mazeSnapshot;
    }

    public static void write(String dirName, String fileName, Maze maze) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(dirName, fileName), create(maze));
        } catch (IOException ignored) {
        }
    }

    public static Optional<Maze> read(String dirName, String fileName) throws InvalidSizeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MazeSnapshot snapshot = mapper.readValue(new File(dirName, fileName), MazeSnapshot.class);
            if (!snapshot.valid())
                throw new InvalidSizeException();
            var maze = new Maze(snapshot.getRows(), snapshot.getCols());
            for (var e = 0; e < snapshot.getData().length; ++e) {
                var row = e / snapshot.getCols();
                var col = e % snapshot.getCols();
                maze.mData[row][col] = snapshot.getData()[e];
            }
            return Optional.of(maze);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}