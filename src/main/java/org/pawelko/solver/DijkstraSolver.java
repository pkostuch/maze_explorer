package org.pawelko.solver;

import org.pawelko.Maze;
import org.pawelko.Path;
import org.pawelko.Position;

import java.util.*;

public class DijkstraSolver implements ISolver {

    public DijkstraSolver() {
    }

    @Override
    public Path solve(Maze maze, Position source, Position destination) {

        var distance = new HashMap<Position, Integer>();
        var prev = new HashMap<Position, Position>();
        var queue = new Vector<Position>();


        for (var row = 0; row < maze.rows(); ++row) {
            for (var col = 0; col < maze.cols(); ++col) {
                if (maze.blocked(row, col))
                    continue;
                if (source.row == row && source.col == col) {
                    distance.put(Position.of(row, col), 0);
                } else {
                    distance.put(Position.of(row, col), Integer.MAX_VALUE);
                }
                queue.add(Position.of(row, col));
                prev.put(Position.of(row, col), null);
            }
        }

        while (!queue.isEmpty()) {
            var cost = Integer.MAX_VALUE;
            Position current = null;
            for (var n : queue) {
                if (distance.get(n) < cost) {
                    cost = distance.get(n);
                    current = n;
                }
            }
            queue.remove(current);

            var row = current.row;
            var col = current.col;

            var next = List.of(Position.of(row + 1, col),
                    Position.of(row - 1, col),
                    Position.of(row, col - 1),
                    Position.of(row, col + 1));

            for (var pos : next) {

                if (queue.contains(pos) && maze.valid(pos.row, pos.col) && !maze.blocked(pos.row, pos.col)) {
                    if (distance.get(current) + 1 < distance.get(pos)) {
                        distance.put(pos, distance.get(current) + 1);
                        prev.put(pos, current);
                    }
                }
            }
        }

        var l = new ArrayList<Position>();

        var dest = destination;
        while (dest != null) {
            l.add(dest);
            dest = prev.get(dest);
        }

        Collections.reverse(l);

        return new Path(l);
    }
}
