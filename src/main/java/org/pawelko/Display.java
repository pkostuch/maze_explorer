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

    Color pathColor = new Color(0x00, 0x50, 0xc0, 0x90);
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
        if (maze.blocked(row, col)) {
            maze.mData[row][col] &= ~Maze.BLOCKED;

        } else {
            maze.mData[row][col] |= Maze.BLOCKED;
        }
        repaint();
    }

    private void paintBlock(Graphics g, int row, int col, Color color) {
        var x = col * pxSize;
        var y = row * pxSize;
        g.setColor(color);
        g.fillRect(x, y, pxSize, pxSize);
        g.setColor(Color.white);
        g.drawRect(x, y, pxSize, pxSize);
    }

    private void paintBlock(Graphics g, int row, int col) {
        var color = maze.blocked(row, col) ? Color.black : Color.lightGray;
        paintBlock(g, row, col, color);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        maze.walk((row, col) -> paintBlock(g, row, col));

        if (current != null) {
            var x = current.col * pxSize;
            var y = current.row * pxSize;
            g.setColor(Color.cyan);
            g.fillRect(x, y, pxSize, pxSize);
        }


        if (path != null) {
            for (var element : path.elements()) {
                paintBlock(g, element.row, element.col, pathColor);
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
