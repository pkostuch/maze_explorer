package org.pawelko.solver;

import org.pawelko.Maze;
import org.pawelko.Path;
import org.pawelko.Position;

public interface ISolver {

    Path solve(Maze maze, Position source, Position destination);
}
