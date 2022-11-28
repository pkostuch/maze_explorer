package org.pawelko;

import java.util.List;

public class Path {

    private List<Position> mElements = List.of();

    public Path(List<Position> elements) {
        mElements = elements;
    }

    List<Position> elements() {
        return mElements;
    }

}
