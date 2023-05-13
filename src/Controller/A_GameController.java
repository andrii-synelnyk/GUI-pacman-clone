package Controller;

import Model.A_GameModel;
import View.A_GameView;

import javax.swing.*;

import Enum.*;
import View.GameWindow;
import View.MenuWindow;

import java.awt.*;
import java.util.HashSet;

public class A_GameController {
    private final A_GameModel gameModel;
    private final A_GameView gameView;

    private HashSet<Thread> controllerThreads = new HashSet<>();

    KeyController keyController;

    public A_GameController(A_GameModel gameModel, A_GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        keyController = new KeyController(this);

        listenToMenuActions();
    }

    public void listenToMenuActions(){
        Thread menuListener = new Thread(() -> {
            while (gameView.getMenuWindow().isVisible()) {
                try {
                    Thread.sleep(15);
                    //System.out.println("menuListener");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MenuWindow menu = gameView.getMenuWindow();

                boolean newGame = menu.getNewGame();
                boolean highScore = menu.getHighScore();
                boolean exit = menu.getExit();

                if (newGame) {
                    int rowsInput = menu.getRowsInput();
                    int columnsInput = menu.getColumnsInput();
                    startNewGame(rowsInput, columnsInput);
                    menu.setNewGame(false);
                }else if (highScore){

                }else if (exit) System.exit(0);
            }
        });
        menuListener.start();
        //controllerThreads.add(menuListener);
    }

    public void startNewGame(int rowsInput, int columnsInput){
        gameModel.initiateGameLogic(rowsInput, columnsInput);
        configView();
    }

    public void configView(){
        // Get game board from Model to display it in View
        JTable gameBoard = gameModel.getGameTable();
        gameView.setNumberOfRows(getNumberOfRows());
        gameView.setNumberOfColumns(getNumberOfColumns());
        gameView.showGameWindow(gameBoard);

        // Start redrawing game board
        redrawGameWindow();
        checkForGameOver();
        changePacmanDirection();

        // Start listening to user input

        gameView.getGameWindow().addKeyListener(keyController);
    }

    public void redrawGameWindow(){
        Thread redrawThread = new Thread(() -> {
            while (!gameModel.getGameOver()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int lives = gameModel.getLivesRemaining();
                int score = gameModel.getScore();
                int time = gameModel.getTime();

                SwingUtilities.invokeLater(() -> {
                    gameView.getGameWindow().updateLives(lives);
                    gameView.getGameWindow().updateTime(time);
                    gameView.getGameWindow().updateScore(score);
                    gameView.getGameWindow().redrawBoard();
                });
            }
        });
        redrawThread.start();
        controllerThreads.add(redrawThread);
    }

    public void changePacmanDirection(){
        Thread changePacmanDirectionThread = new Thread(() -> {
            while (!gameModel.getGameOver()) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Direction pacmanDirection = gameModel.getPacmanDirection();
                gameView.setPacmanViewDirection(pacmanDirection);
            }
        });
        changePacmanDirectionThread.start();
        controllerThreads.add(changePacmanDirectionThread);
    }

    public void checkForGameOver(){
        Thread checkForGameOverThread = new Thread(() -> {
            while (!gameModel.getGameOver()) { // CHANGE !!!
                try {
                    Thread.sleep(15);
                    //System.out.println(Thread.currentThread().getName());
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

    public int getNumberOfColumns(){
        return gameModel.getGameBoard().getColumnCount();
    }

    public void stop(){
        // remove checkForGameOverThread from controllerThreads, because it is the thread that is calling this method,
        // therefore, it cannot join itself. But it because gameOver is false now, it will exit the loop and terminate anyway
        Thread currentThread = Thread.currentThread();
        controllerThreads.remove(currentThread);

        for (Thread t : controllerThreads){
            try{
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void gameOver(){
        System.out.println("started game over method");
        gameView.stopCharacterViewThreads();
        gameModel.stop();
        this.stop();
        System.out.println("finished game over method");
    }


    public void interruptWithShortcut(){
        System.out.println("started interrupt method");
        if (!gameModel.getGameOver()) gameModel.setGameOver(true);
        for (Window window : Window.getWindows()) {
            window.dispose();
        }
        gameView.getMenuWindow().setVisible(true);
        listenToMenuActions();
        System.out.println("finished interrupt method");
    }
}
