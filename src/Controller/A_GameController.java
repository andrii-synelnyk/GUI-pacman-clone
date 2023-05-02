package Controller;

import Model.A_GameModel;
import Model.Pacman;
import View.A_GameView;
import View.VPacman;

import javax.swing.*;

public class A_GameController {
    private final A_GameModel gameModel;
    private final A_GameView gameView;

    public A_GameController(A_GameModel gameModel, A_GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        // Add listeners to handle user input and update the model and view accordingly
        startGame();
    }

    public void startGame(){

        Pacman pacman = new Pacman();
        VPacman vPacman = new VPacman(40, 40, pacman);

        // Create game board
        JTable newGameBoard = gameModel.getGameBoard();
        gameView.showGameWindow(newGameBoard, vPacman);

        notifyViewToRedrawBoard();
    }

    public void notifyViewToRedrawBoard(){

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gameView.redrawGameBoard();
            }
        }).start();
    }
    // Add methods to manage game state and handle events like collisions or power-up activations
}
