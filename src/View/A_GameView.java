package View;

import javax.swing.*;

public class A_GameView extends JFrame {

    public A_GameView() {
        // Initialize and configure the JTable with the gameModel's GameBoard

    }

    public void showGameWindow(JTable gameBoard){
        GameWindow gameWindow = new GameWindow(gameBoard);
    }

    // Add methods to update the view based on the GameModel
}
