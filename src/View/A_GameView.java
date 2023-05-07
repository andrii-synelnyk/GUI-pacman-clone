package View;

import javax.swing.*;
import Enum.Direction;

public class A_GameView extends JFrame {

    GameWindow gameWindow;
    int imageSize;
    private PacmanView pacmanView;
    private EnemyView enemyView;
    private int numberOfRows;

    public A_GameView() {
        // Initialize and configure the JTable with the gameModel's GameBoard

        this.setFocusable(true);
    }

    public void showGameWindow(JTable gameBoard){
        imageSize = calculateImageSize(numberOfRows);
        pacmanView = new PacmanView(imageSize);
        enemyView = new EnemyView(imageSize);
        gameWindow = new GameWindow(gameBoard, imageSize, pacmanView, enemyView);
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
}
