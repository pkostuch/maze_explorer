package org.pawelko;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

public class MazeExplorer extends JFrame implements ActionListener {

    private static final String TITLE = "Maze Explorer";

    private static final String ID_FILE_NEW = "NEW";
    private static final String ID_FILE_LOAD = "LOAD";
    private static final String ID_FILE_SAVE = "SAVE";
    private static final String ID_FILE_EXIT = "EXIT";
    private static final String ID_RUN = "RUN";

    private static final String ALGO_BFS = "BFS";

    private static final String ALGO_DIJKSTRA = "Dijkstra";

    private static final String[] ALGO_CHOICE = {ALGO_BFS, ALGO_DIJKSTRA};

    private ISolver create(String algorithm) {
        if (algorithm.equals(ALGO_BFS))
            return new Solver(display);
        else if (algorithm.equals(ALGO_DIJKSTRA))
            return new DijkstraSolver();
        return null;
    }

    private Maze maze;
    private final Display display;

    private ISolver solver;

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        JButton button = new JButton("Run");
        button.setActionCommand(ID_RUN);
        button.addActionListener(this);
        button.setMnemonic(KeyEvent.VK_R);
        panel.add(button);
        var comboBox = new JComboBox<>(ALGO_CHOICE);
        panel.add(comboBox);
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                solver = create((String) e.getItem());
            }
        });
        return panel;
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        var layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        add(panel);
        panel.add(createControlPanel());
        panel.add(display);
        addMenu();
    }

    MazeExplorer() {
        super(TITLE);

        var res = ClassLoader.getSystemResource("maze.png");
        if (res != null) {
            var icon = new ImageIcon(res);
            setIconImage(icon.getImage());
        }
        maze = new Maze(10, 20);
        display = new Display(maze);
        solver = create(ALGO_CHOICE[0]);
        addComponents();

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void create() {
        var rowAsString = JOptionPane.showInputDialog(this, "Rows");
        var columnAsString = JOptionPane.showInputDialog(this, "Columns");

        try {
            var rows = Integer.parseInt(rowAsString);
            var cols = Integer.parseInt(columnAsString);
            maze = new Maze(rows, cols);
            SwingUtilities.invokeLater(() -> {
                display.setMaze(maze);
                pack();
                repaint();
            });
        } catch (NumberFormatException ignore) {

        }

    }

    private void load() {
        var fileDialog = new FileDialog(this, "Load file", FileDialog.LOAD);
        fileDialog.setVisible(true);
        var dirName = fileDialog.getDirectory();
        var fileName = fileDialog.getFile();
        if (fileName != null && dirName != null) {

            try {
                var maze = MazeSnapshot.read(dirName, fileName);
                EventQueue.invokeLater(() -> {
                    if (maze.isPresent()) {
                        this.maze = maze.get();
                        display.setMaze(this.maze);
                        pack();
                    }
                });
            } catch (InvalidSizeException ignore) {
                JOptionPane.showMessageDialog(this, "Invalid data size");
            }
        }
    }

    private void save() {
        var fileDialog = new FileDialog(this, "Save file", FileDialog.SAVE);
        fileDialog.setVisible(true);
        var dirName = fileDialog.getDirectory();
        var fileName = fileDialog.getFile();
        if (fileName != null && dirName != null) {
            MazeSnapshot.write(dirName, fileName, maze);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ID_FILE_EXIT)) {
            System.exit(0);
        } else if (e.getActionCommand().equals(ID_FILE_NEW)) {
            create();
        } else if (e.getActionCommand().equals(ID_FILE_LOAD)) {
            load();
        } else if (e.getActionCommand().equals(ID_FILE_SAVE)) {
            save();
        } else if (e.getActionCommand().equals(ID_RUN)) {
            SwingUtilities.invokeLater(() -> display.showPath(solver.solve(maze, Position.of(0, 0), Position.of(maze.rows() - 1, maze.cols() - 1))));
        }
    }

    private JMenuItem addMenuItem(JMenu menu, Integer mnemonic, String name, String command) {
        var menuItem = new JMenuItem(name);
        menuItem.setMnemonic(mnemonic);
        menuItem.addActionListener(this);
        menuItem.setActionCommand(command);
        menu.add(menuItem);
        return menuItem;
    }

    private void addMenu() {
        var menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        addMenuItem(menu, KeyEvent.VK_N, "New", ID_FILE_NEW);
        addMenuItem(menu, KeyEvent.VK_L, "Load", ID_FILE_LOAD);
        addMenuItem(menu, KeyEvent.VK_S, "Save", ID_FILE_SAVE);
        menu.addSeparator();
        addMenuItem(menu, KeyEvent.VK_E, "Exit", ID_FILE_EXIT);
        var menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }


    public static void main(String[] args) {
        new MazeExplorer();
    }

}
