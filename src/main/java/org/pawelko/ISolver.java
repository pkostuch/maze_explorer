package org.pawelko;

public interface ISolver {

    Path solve(Maze maze, Position source, Position destination);
}
