package Controller;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import Enum.*;

import Controller.KeyInput.CompoundShortcutController;
import Controller.KeyInput.MovementController;

import Model.GameModel;
import Model.HighScoreLogic.HighScore;
import Model.HighScoreLogic.HighScoreList;

import View.GameView;
import View.ProgramWindows.MenuWindow;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;
    private HashSet<Thread> controllerThreads = new HashSet<>();
    MovementController movementController;
    CompoundShortcutController compoundShortcutController;
    private boolean needToAskForHighscoreInput = true;

    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        movementController = new MovementController(this);
        compoundShortcutController = new CompoundShortcutController(this);

        listenToMenuActions();
    }

    public void listenToMenuActions(){
        Thread menuListener = new Thread(() -> {
            while (gameView.getMenuWindow().isVisible()) {
                try {
                    Thread.sleep(15);
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
                    showHighScores();
                    menu.setHighScore(false);
                }else if (exit) System.exit(0);
            }
        });
        menuListener.start();
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
        gameView.getGameWindow().addKeyListener(movementController);
        gameView.getGameWindow().addKeyListener(compoundShortcutController);
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

    public void changePacmanDirection(){ // when the key is pressed
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
            while (!gameModel.getGameOver()) {
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

    public int getNumberOfColumns(){
        return gameModel.getGameBoard().getColumnCount();
    }

    public void stop(){
        // remove checkForGameOverThread from controllerThreads, because it is the thread that is calling this method,
        // therefore, it cannot join itself. But because gameOver is false now, it will exit the loop and terminate anyway
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
        askForHighScoreInput();

        gameView.stopCharacterViewThreads();
        gameModel.stop();
        this.stop();
    }


    public void interruptWithShortcut(){
        if (!gameModel.getGameOver()) gameModel.setGameOver(true);

        needToAskForHighscoreInput = false; // because it is not regular game over

        // Close Game Window and show Menu
        gameView.closeGameWindow();
        gameView.getMenuWindow().setVisible(true);
        listenToMenuActions(); // launch thread listening to menu actions
    }

    public void showHighScores(){
        HighScoreList highScoreList = gameModel.loadOrCreateHighScoreList();;
        Map<String, Integer> sortedHighScores = sortHighScores(highScoreList);

        gameView.showHighScoresWindow(sortedHighScores);
    }

    private Map<String, Integer> sortHighScores(HighScoreList highScoreList) {
        Map<String, Integer> sortedHighScores = new LinkedHashMap<>();
        highScoreList.getHighScores().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()))
                .forEach(highScore -> sortedHighScores.put(highScore.getName(), highScore.getScore()));
        return sortedHighScores;
    }

    public void askForHighScoreInput(){
        if (needToAskForHighscoreInput) {
            String name = gameView.showHighScoresInputWindow();
            gameModel.loadOrCreateHighScoreList(); // in case there was still no high-score file created
            gameModel.getHighScoreList().addHighScore(new HighScore(name, gameModel.getScore()));
            gameModel.getHighScoreList().saveHighScoresToFile("high_scores.ser");
            gameView.closeGameWindow();
            gameView.getMenuWindow().setVisible(true);
            listenToMenuActions(); // launch thread listening to menu actions
        }
        needToAskForHighscoreInput = true;
    }
}
