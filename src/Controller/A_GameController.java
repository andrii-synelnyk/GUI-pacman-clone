package Controller;

import Model.A_GameModel;
import View.A_GameView;

import javax.swing.*;

import Enum.*;

import java.util.HashSet;

public class A_GameController {
    private final A_GameModel gameModel;
    private final A_GameView gameView;

    private HashSet<Thread> controllerThreads = new HashSet<>();

    public A_GameController(A_GameModel gameModel, A_GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        configView();
    }

    public void configView(){
        // Get game board from Model to display it in View
        JTable gameBoard = gameModel.getGameTable();
        gameView.setNumberOfRows(getNumberOfRows());
        gameView.showGameWindow(gameBoard);

        // Start redrawing game board
        notifyViewToRedrawBoard();
        checkForGameOver();

        // Start listening to user input
        KeyController keyController = new KeyController(this);
        gameView.getGameWindow().addKeyListener(keyController);
    }

    public void notifyViewToRedrawBoard(){
        Thread redrawThread = new Thread(() -> {
            while (!gameModel.getGameOver()) { // CHANGE !!!
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Direction pacmanDirection = gameModel.getPacmanDirection();
                gameView.redrawGameBoard(pacmanDirection); // Pass score and time to this method form Model too
            }
        });
        redrawThread.start();
        controllerThreads.add(redrawThread);
    }

    public void checkForGameOver(){
        Thread checkForGameOverThread = new Thread(() -> {
            while (!gameModel.getGameOver()) { // CHANGE !!!
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (gameModel.getGameOver()){
                    gameOver();
                }
            }
        });
        checkForGameOverThread.start();
        controllerThreads.add(checkForGameOverThread);
    }

    public void movePacman(Direction direction) {
        gameModel.getPacman().setDirection(direction);
    }

    public int getNumberOfRows(){
        return gameModel.getGameBoard().getRowCount();
    }

    public void stop(){
        for (Thread t : controllerThreads){
            try{
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void gameOver(){
        System.out.println("game over");
        gameModel.stop();
        this.stop();
    }
    // Add methods to manage game state and handle events like collisions or power-up activations
}
