package org.pawelko;

import java.util.List;

public class Path {

    private List<Position> elements;

    public Path(List<Position> elements) {
        this.elements = elements;
    }

    List<Position> elements() {
        return elements;
    }

}
