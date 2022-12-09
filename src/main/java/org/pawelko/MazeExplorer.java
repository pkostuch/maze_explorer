package org.pawelko;

import org.pawelko.solver.BfsSolver;
import org.pawelko.solver.DijkstraSolver;
import org.pawelko.solver.ISolver;

import java.util.Optional;

public class MazeExplorer {


    private static final String ALGO_BFS = "BFS";

    private static final String ALGO_DIJKSTRA = "Dijkstra";

    private static final String[] ALGO_CHOICE = {ALGO_BFS, ALGO_DIJKSTRA};
    private Maze maze;
    private ISolver solver;

    MazeExplorer() {
        maze = new Maze(10, 20);
        solver = create(ALGO_CHOICE[0]);
        new Window(maze, ALGO_CHOICE,
                this::findPath,
                this::createMaze,
                this::load,
                this::save,
                this::selectAlgorithm);
    }

    public static void main(String[] args) {
        new MazeExplorer();
    }

    private ISolver create(String algorithm) {
        if (algorithm.equals(ALGO_BFS))
            return new BfsSolver(null);
        else if (algorithm.equals(ALGO_DIJKSTRA))
            return new DijkstraSolver();
        return null;
    }

    private Path findPath() {
        var start = Position.of(0, 0);
        var exit = Position.of(maze.rows() - 1, maze.cols() - 1);
        return solver.solve(maze, start, exit);
    }

    private Maze createMaze(Position position) {
        maze = new Maze(position.row, position.col);
        return maze;
    }

    private void selectAlgorithm(String input) {
        solver = create(input);
    }


    private void save(Window.FileLocation fileLocation) {
        MazeSnapshot.write(fileLocation.dirName, fileLocation.fileName, maze);
    }

    private Maze load(Window.FileLocation fileLocation) {
        Optional<Maze> optMaze;
        try {
            optMaze = MazeSnapshot.read(fileLocation.dirName, fileLocation.fileName);
        } catch (InvalidSizeException e) {
            throw new RuntimeException(e);
        }
        optMaze.ifPresent(value -> maze = value);
        return maze;
    }

}
