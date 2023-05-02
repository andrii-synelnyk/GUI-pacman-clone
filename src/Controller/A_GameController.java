package Controller;

import Model.A_GameModel;
import View.A_GameView;

import javax.swing.*;

public class A_GameController {
    private final A_GameModel gameModel;
    private final A_GameView gameView;

    public A_GameController(A_GameModel gameModel, A_GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        // Add listeners to handle user input and update the model and view accordingly
        JTable newGameBoard = gameModel.getGameBoard();
        gameView.showGameWindow(newGameBoard);
    }


    // Add methods to manage game state and handle events like collisions or power-up activations
}
