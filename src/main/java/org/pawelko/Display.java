package org.pawelko;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Display extends JPanel implements ITracker {

    Path path = null;
    Maze maze;

    Position current = null;

    int pxSize = 20;

    Display(Maze maze) {
       setMaze(maze);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e);
                block(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        var dim = new Dimension(this.maze.cols() * pxSize, this.maze.rows() * pxSize);
        setMinimumSize(dim);
        setPreferredSize(dim);
        repaint();
    }

    public void showPath(Path path) {
        this.path = path;
        repaint();
    }

    private void block(int x, int y) {
        var col = x / pxSize;
        var row = y / pxSize;
        if (0 != (maze.mData[row][col] & Maze.BLOCKED)) {
            maze.mData[row][col] &= ~Maze.BLOCKED;

        } else {
            maze.mData[row][col] |= Maze.BLOCKED;

        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        for (var row = 0; row < maze.rows(); ++row) {
            for (var col = 0; col < maze.cols(); ++col) {
                var x = col * pxSize;
                var y = row * pxSize;

                var color = Color.lightGray;
                if ((maze.get(row, col) & Maze.BLOCKED) != 0)
                    color = Color.black;
//                if (maze.visited(row, col))
//                    color = Color.gray;
                g.setColor(color);
                g.fillRect(x, y, pxSize, pxSize);
                g.setColor(Color.white);
                g.drawRect(x, y, pxSize, pxSize);
            }
        }

        if (current != null)
        {
            var x = current.col * pxSize;
            var y = current.row * pxSize;
            g.setColor(Color.cyan);
            g.fillRect(x, y, pxSize, pxSize);
        }


        if (path != null) {
            for (var element : path.elements()) {
                var x = element.col * pxSize;
                var y = element.row * pxSize;
                Color c = new Color(0x00, 0x50, 0xc0, 0x90);
                g.setColor(c);
                g.fillRect(x, y, pxSize, pxSize);
            }
        }
    }

    @Override
    public void step() {
    }

    @Override
    public void setCurrent(Position position) {
    }
}
