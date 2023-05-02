package View;

import javax.swing.*;

public class A_GameView extends JFrame {

    GameWindow gameWindow;

    public A_GameView() {
        // Initialize and configure the JTable with the gameModel's GameBoard

    }

    public void showGameWindow(JTable gameBoard, VPacman vPacman){
        gameWindow = new GameWindow(gameBoard, vPacman);
    }

    public void redrawGameBoard(){
        gameWindow.redrawBoard();
    }


    // Add methods to update the view based on the GameModel
}
