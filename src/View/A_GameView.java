package View;

import Controller.A_GameController;
import Model.A_GameModel;

import javax.swing.*;

public class A_GameView extends JFrame {

    GameWindow gameWindow;
    int imageSize;

    public A_GameView(A_GameModel gameModel) {
        // Initialize and configure the JTable with the gameModel's GameBoard
        imageSize = calculateImageSize(gameModel.getGameBoard().getRowCount());

        this.setFocusable(true);
    }

    public void showGameWindow(JTable gameBoard, A_GameController gameController){
        gameWindow = new GameWindow(gameBoard, imageSize, gameController);
    }

    private int calculateImageSize(int rows) {
        // Replace these constants with the desired dimensions of your game view
        final int gameViewHeight = 1000;
        // Choose the smaller dimension to fit both width and height
        return gameViewHeight / rows;
    }

    public void redrawGameBoard(){
        gameWindow.redrawBoard();
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
    // Add methods to update the view based on the GameModel
}
