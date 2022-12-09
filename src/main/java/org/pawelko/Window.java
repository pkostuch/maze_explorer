package org.pawelko;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Window extends JFrame implements ActionListener {

    private static final String TITLE = "Maze Explorer";

    private static final String ID_FILE_NEW = "NEW";
    private static final String ID_FILE_LOAD = "LOAD";
    private static final String ID_FILE_SAVE = "SAVE";
    private static final String ID_FILE_EXIT = "EXIT";
    private static final String ID_RUN = "RUN";

    private final Display display;

    private final String[] algorithms;

    private final Supplier<Path> findPathCallback;

    private final Function<Position, Maze> createMazeCallback;


    private final Function<FileLocation, Maze> loadMazeCallback;

    private final Consumer<FileLocation> saveMazeCallback;

    private final Consumer<String> selectAlgorithmCallback;

    public Window(Maze maze,
                  String[] algorithms,
                  Supplier<Path> findPathCallback,
                  Function<Position, Maze> createMazeCallback,
                  Function<FileLocation, Maze> loadMazeCallback,
                  Consumer<FileLocation> saveMazeCallback,
                  Consumer<String> selectAlgorithmCallback) {
        super(TITLE);
        this.algorithms = algorithms;
        this.findPathCallback = findPathCallback;
        this.createMazeCallback = createMazeCallback;
        this.loadMazeCallback = loadMazeCallback;
        this.saveMazeCallback = saveMazeCallback;
        this.selectAlgorithmCallback = selectAlgorithmCallback;
        var res = ClassLoader.getSystemResource("maze.png");
        if (res != null) {
            var icon = new ImageIcon(res);
            setIconImage(icon.getImage());
        }
        display = new Display(maze);
        addComponents();
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        JButton button = new JButton("Run");
        button.setActionCommand(ID_RUN);
        button.addActionListener(this);
        button.setMnemonic(KeyEvent.VK_R);
        panel.add(button);
        var comboBox = new JComboBox<>(algorithms);
        panel.add(comboBox);
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectAlgorithmCallback.accept((String) e.getItem());
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

    private void addMenuItem(JMenu menu, Integer mnemonic, String name, String command) {
        var menuItem = new JMenuItem(name);
        menuItem.setMnemonic(mnemonic);
        menuItem.addActionListener(this);
        menuItem.setActionCommand(command);
        menu.add(menuItem);
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

    private void create() {

        var rowAsString = JOptionPane.showInputDialog(this, "Rows");
        var columnAsString = JOptionPane.showInputDialog(this, "Columns");

        try {
            var rows = Integer.parseInt(rowAsString);
            var cols = Integer.parseInt(columnAsString);
            var maze = createMazeCallback.apply(Position.of(rows, cols));
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
                var maze = loadMazeCallback.apply(new FileLocation(dirName, fileName));
                EventQueue.invokeLater(() -> {
                            display.setMaze(maze);
                            pack();
                        }
                );
            } catch (Exception ignore) {
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
            saveMazeCallback.accept(new FileLocation(dirName, fileName));
        }
    }

    public static class FileLocation {
        public final String dirName;
        public final String fileName;

        FileLocation(String dirName, String fileName) {
            this.dirName = dirName;
            this.fileName = fileName;
        }

    }

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
            SwingUtilities.invokeLater(() -> display.showPath(findPathCallback.get()));
        }
    }
}
