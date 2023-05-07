package View;

import javax.swing.*;
import Enum.Direction;

import java.awt.*;

public class A_GameView {

    GameWindow gameWindow;
    int imageSize;
    private PacmanView pacmanView;
    private EnemyView enemyView;
    private int numberOfRows;

    public A_GameView() {

    }

    public void showGameWindow(JTable gameBoard){
        imageSize = calculateImageSize(numberOfRows);
        pacmanView = new PacmanView(imageSize);
        enemyView = new EnemyView(imageSize);

        JTable gameBoardForView = configGameBoard(gameBoard);

        gameWindow = new GameWindow(gameBoardForView);
    }

    private int calculateImageSize(int rows) {
        final int gameViewHeight = 1000;
        return gameViewHeight / rows;
    }

    public void redrawGameBoard(Direction pacmanDirection){
        pacmanView.setPacmanDirection(pacmanDirection);
        gameWindow.redrawBoard();
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
    }

    public JTable configGameBoard(JTable gameTable){

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
        // Set up the custom cell renderer
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer(pacmanView, enemyView, imageSize);
        gameTable.setDefaultRenderer(Object.class, cellRenderer);

        return gameTable;
    }
}
