package View;

import javax.swing.*;

public class A_GameView extends JFrame {

    GameWindow gameWindow;

    public A_GameView() {
        // Initialize and configure the JTable with the gameModel's GameBoard
        this.setFocusable(true);
    }

    public void showGameWindow(JTable gameBoard, PacmanView pacmanView){
        gameWindow = new GameWindow(gameBoard, pacmanView);
    }

    public void redrawGameBoard(){
        gameWindow.redrawBoard();
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
    // Add methods to update the view based on the GameModel
}
