package View;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private JTable gameTable;
    private int imageSize = 40;

    public GameWindow(JTable gameBoard, PacmanView pacmanView) {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameTable = gameBoard;
        gameTable.setRowHeight(imageSize); // Set the desired row height
        for (int i = 0; i < gameTable.getColumnCount(); i++){
            gameTable.getColumnModel().getColumn(i).setPreferredWidth(imageSize);
        }
        gameTable.setShowGrid(false); // Remove grid lines
        gameTable.setTableHeader(null); // Remove column header
        gameTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto resizing
        gameTable.setIntercellSpacing(new Dimension(0, 0));
        gameTable.setBackground(Color.BLACK);
        gameTable.setCellSelectionEnabled(false); // Disable ability to select cells

        // Set up a JScrollPane to handle larger boards
        JScrollPane scrollPane = new JScrollPane(gameTable);
        Dimension originalDimension = gameTable.getPreferredSize();
        Dimension newDimension = new Dimension(originalDimension.width + 4, originalDimension.height + 4); // not to get scrollbars with default size
        scrollPane.setPreferredSize(newDimension);
        getContentPane().add(scrollPane);
        scrollPane.setFocusable(false); // MAYBE REMOVE

        // Set up the custom cell renderer
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer(pacmanView);
        gameTable.setDefaultRenderer(Object.class, cellRenderer);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void redrawBoard() {
        gameTable.repaint();
    }

}