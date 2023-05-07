package View;

import Controller.A_GameController;
import Model.A_GameModel;

import javax.swing.*;
import Enum.Direction;
import Model.Enemy;

import java.util.HashSet;

public class A_GameView extends JFrame {

    GameWindow gameWindow;
    int imageSize;

    private PacmanView pacmanView;
    private EnemyView enemyView;
    private int numberOfRows;

    private HashSet<CharacterView> characterViews; // NOW IS NOT IN USE

    public A_GameView() {
        // Initialize and configure the JTable with the gameModel's GameBoard
        characterViews = new HashSet<>();
        this.setFocusable(true);
    }

    public void showGameWindow(JTable gameBoard){
        imageSize = calculateImageSize(numberOfRows);
        pacmanView = new PacmanView(imageSize, imageSize);
        enemyView = new EnemyView(imageSize, imageSize);
        characterViews.add(pacmanView);
        characterViews.add(enemyView);
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

    public HashSet<CharacterView> getCharacterViews() {
        return characterViews;
    }

    public PacmanView getPacmanView(){
        return pacmanView;
    }
}
