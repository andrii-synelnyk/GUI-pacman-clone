package View;

import Controller.A_GameController;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private JTable gameTable;

    PacmanView pacmanView;
    EnemyView enemyView;

    public GameWindow(JTable gameBoard, int imageSize, A_GameController gameController) {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameTable = gameBoard;
        gameTable.setRowHeight(imageSize); // Set the desired row height
        for (int i = 0; i < gameTable.getColumnCount(); i++){
            gameTable.getColumnModel().getColumn(i).setPreferredWidth(imageSize);
            gameTable.getColumnModel().getColumn(i).setMinWidth(imageSize);
            gameTable.getColumnModel().getColumn(i).setMaxWidth(imageSize);
        }
        gameTable.setShowGrid(false); // Remove grid lines
        gameTable.setTableHeader(null); // Remove column header
        gameTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto resizing
        gameTable.setIntercellSpacing(new Dimension(0, 0));
        gameTable.setBackground(Color.BLACK);
        gameTable.setCellSelectionEnabled(false); // Disable ability to select cells

        // Set the preferred size of the gameTable directly
        Dimension tableSize = new Dimension(
                gameTable.getColumnCount() * imageSize,
                gameTable.getRowCount() * imageSize);
        gameTable.setSize(tableSize);

        // Add the gameTable to the content pane without wrapping it in a JScrollPane
        getContentPane().add(gameTable);
        gameTable.setFocusable(false); // MAYBE REMOVE

        // Set up the custom cell renderer
        pacmanView = new PacmanView(imageSize, imageSize, gameController.getPacman());
        if (gameController.getEnemy() != null) enemyView = new EnemyView(imageSize, imageSize, gameController.getEnemy());
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer(pacmanView, enemyView, imageSize);
        gameTable.setDefaultRenderer(Object.class, cellRenderer);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void redrawBoard() {
        gameTable.repaint();
    }

}