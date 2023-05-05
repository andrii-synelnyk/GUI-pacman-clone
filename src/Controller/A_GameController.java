package Controller;

import Model.A_GameModel;
import Model.Character;
import Model.Enemy;
import Model.Pacman;
import View.A_GameView;
import View.PacmanView;

import javax.swing.*;

import Enum.Direction;

import java.awt.*;

public class A_GameController {
    private final A_GameModel gameModel;
    private final A_GameView gameView;

    Pacman pacman;

    public A_GameController(A_GameModel gameModel, A_GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        this.pacman = gameModel.getGameBoard().getPacman();
        // Add listeners to handle user input and update the model and view accordingly
        startGame();
    }

    public void startGame(){
        // Create game board
        JTable gameBoard = gameModel.getGameBoard().getTable();
        gameView.showGameWindow(gameBoard, this);

        notifyViewToRedrawBoard();

        KeyController keyController = new KeyController(this);
        gameView.getGameWindow().addKeyListener(keyController);

        gameModel.getGameBoard().getCharacters().forEach(Character::moveCharacter);
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

//                KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
//
//                // Get the component that currently has focus
//                Component focusedComponent = keyboardFocusManager.getFocusOwner();
//
//                // Print the focused component
//                System.out.println("Focused component: " + focusedComponent);
            }
        }).start();

    }

    public void movePacman(Direction direction) {
        pacman.setDirection(direction);
        // Notify the View to update the display after moving the Pacman
        gameView.redrawGameBoard();
    }

    public Pacman getPacman(){
        return pacman;
    }

    public Enemy getEnemy(){
        if (gameModel.getGameBoard().getEnemies().size() != 0)
            return gameModel.getGameBoard().getEnemies().get(0);
        else return null;
    }
    // Add methods to manage game state and handle events like collisions or power-up activations
}
