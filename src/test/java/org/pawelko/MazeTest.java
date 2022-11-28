package org.pawelko;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class MazeTest {

    Maze maze = new Maze(4,5);

    @ParameterizedTest
    @MethodSource("provideParamsForCheckValid")
    void checkValid(int row, int col, boolean result) {
        Assertions.assertEquals(result, maze.valid(row, col));
    }

    static Stream<Arguments> provideParamsForCheckValid() {
        return Stream.of(
                Arguments.of(0, 0, true),
                Arguments.of(0, 1, true),
                Arguments.of(3, 4, true),
                Arguments.of(0, -1, false),
                Arguments.of(-1, 0, false),
                Arguments.of(4, 0, false),
                Arguments.of(3, 5, false)
        );
    }
}
