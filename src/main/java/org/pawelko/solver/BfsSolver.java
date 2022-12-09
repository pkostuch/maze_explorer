package org.pawelko.solver;

import org.pawelko.ITracker;
import org.pawelko.Maze;
import org.pawelko.Path;
import org.pawelko.Position;

import java.util.*;

public class BfsSolver implements ISolver {

    private final Cost infinity = new Cost(Integer.MAX_VALUE, null);
    Map<Position, Cost> costs = new HashMap<>();
    int visited = 0;
    private ITracker tracker;
    private Maze maze = null;

    public BfsSolver(ITracker tracker) {
        this.tracker = tracker;
    }

    int cost(Position position) {
        return costs.getOrDefault(position, infinity).cost;
    }

    void clear() {
        visited = 0;
        costs.clear();
        maze.clear();
    }

    @Override
    public Path solve(Maze maze, Position start, Position destination) {
        this.maze = maze;
        clear();

        costs.put(start, Cost.of(0, null));
        explore_bfs(start);

        var cost = costs.get(destination);
        var path = new ArrayList<Position>();
        if (costs.containsKey(destination)) {
            while (destination != null) {
                cost = costs.get(destination);
                path.add(destination);
                destination = cost.through;
            }
        }
        Collections.reverse(path);

        return new Path(path);
    }

    void explore_bfs(Position start) {

        Vector<Position> moves = new Vector<>();
        moves.add(start);

        while (!moves.isEmpty()) {
            var current = moves.remove(0);
            var row = current.row;
            var col = current.col;
            if (maze.visited(row, col))
                continue;
//            tracker.setCurrent(current);
//            tracker.step();


            maze.setVisited(row, col);
            var cost = cost(current);

            var next = List.of(Position.of(row + 1, col),
                    Position.of(row - 1, col),
                    Position.of(row, col - 1),
                    Position.of(row, col + 1));

            for (var pos : next) {

                if (cost + 1 < cost(pos)) {
                    costs.put(pos, Cost.of(cost + 1, current));
                }

                if (maze.valid(pos.row, pos.col) && !maze.blocked(pos.row, pos.col) && !maze.visited(pos.row, pos.col)) {
                    moves.add(pos);
                }
            }
        }
    }

    void explore(Position current) {
        if (maze.visited(current.row, current.col))
            return;
        if (maze.blocked(current.row, current.col))
            return;

        visited++;
        maze.setVisited(current.row, current.col);

        var row = current.row;
        var col = current.col;
        var cost = cost(current);

        var next = List.of(Position.of(row + 1, col),
                Position.of(row - 1, col),
                Position.of(row, col - 1),
                Position.of(row, col + 1));

        for (var pos : next) {
            if (maze.valid(pos.row, pos.col) && !maze.blocked(pos.row, pos.col)) {
                if (cost + 1 < cost(pos)) {
                    costs.put(pos, Cost.of(cost + 1, current));
                }
                explore(pos);
            }
        }
    }

    private static class Cost {
        int cost;
        Position through;

        Cost(int cost, Position through) {
            this.cost = cost;
            this.through = through;
        }

        public static Cost of(int cost, Position through) {
            return new Cost(cost, through);
        }

        @Override
        public String toString() {
            return "Cost{" +
                    "cost=" + cost +
                    ", through=" + through +
                    '}';
        }
    }
}
