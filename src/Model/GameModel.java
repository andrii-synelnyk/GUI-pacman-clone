package Model;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.*;

import Enum.*;

import Model.CharacterModels.Character;
import Model.CharacterModels.Enemy;
import Model.CharacterModels.Pacman;
import Model.GameBoardLogic.GameBoard;
import Model.GameBoardLogic.GameBoardTableModel;
import Model.HighScoreLogic.HighScoreList;

public class GameModel {
    private GameBoard gameBoard;
    private GameBoardTableModel gameBoardTableModel;
    private JTable gameTable;
    private volatile boolean gameOver = false;
    private Pacman pacman;
    private Enemy enemy;
    private ArrayList<Enemy> enemies;
    private HashSet<Character> characters;
    private HashSet<Thread> modelThreads = new HashSet<>();

    // For panel on top
    private int score;
    private int time;
    private int livesRemaining;

    // For high-score
    private HighScoreList highScoreList;

    // For power-ups
    private double scoreMultiplier = 1;
    private boolean invincible = false;
    private int eatableCellsRemaining;
    private HashSet<Thread> threadsForInterruption = new HashSet<>(); // threads which should be interrupted when game ends, not to (exist/cause bugs) in the next game

    public GameModel() {
        this.score = 0;
        this.livesRemaining = 2;
        enemies = new ArrayList<>();
        characters = new HashSet<>();
    }

    public void initiateGameLogic(int rows, int columns){
        gameOver = false;

        // Initialize the game board and table model
        gameBoard = new GameBoard(rows, columns);
        gameBoardTableModel = new GameBoardTableModel(gameBoard);

        // Create the JTable with the table model
        gameTable = new JTable(gameBoardTableModel);
        gameTable.setFocusable(false); // not to lose focus from game window if user clicks on table

        // Create and place player on the board
        pacman = new Pacman(gameBoard);
        gameBoard.setCharacterCell(pacman, 1, 1, pacman.getType());
        characters.add(pacman);

        // Get number of remaining cells with food
        eatableCellsRemaining = gameBoard.getEatableCellsCount();

        // Create and place enemies on the board
        int numberOfEnemies = calcNumberOfEnemies(rows, columns); // Set the desired number of enemies
        for (int i = 0; i < numberOfEnemies; i++) {
            enemy = new Enemy(gameBoard);
            GameBoard.Cell emptyCell = gameBoard.getEmptyCellInTheMiddle();
            gameBoard.setCharacterCell(enemy, emptyCell.getRow(), emptyCell.getColumn(), enemy.getType());
            enemies.add(enemy);
            characters.add(enemy);
        }

        // Start moving characters
        characters.forEach(character -> {moveCharacter(character);});

        // Start spawning power-ups by enemies
        enemies.forEach(enemy -> {spawnPowerUps(enemy);});

        startTimer();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void moveCharacter(Character characterWhoCalled) {
        Thread moveCharacterThread = new Thread(() -> {
            while (!gameOver && !characterWhoCalled.isFrozen()) {

                if (characterWhoCalled.getDirection() != null) {
                    Point newPosition = getNewPosition(characterWhoCalled);
                    int newRow = newPosition.x;
                    int newCol = newPosition.y;

                    if (gameBoard.getCell(newRow, newCol).getContent() != CellContent.WALL) {
                        updateCharacterPosition(characterWhoCalled, newRow, newCol);
                    } else if (characterWhoCalled.getType() == CellContent.ENEMY) { // if character is enemy and the next cell is WALL
                        characterWhoCalled.changeDirection(); // then change direction
                    }
                }

                checkForDeathOrGameOver(); // Check for game over after updating the character position

                try {
                    Thread.sleep(characterWhoCalled.getTimeInterval()); // Wait till the next move
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("quit move thread");
        });
        moveCharacterThread.start();
        modelThreads.add(moveCharacterThread);
    }

    private Point getNewPosition(Character characterWhoCalled) {
        int newRow = gameBoard.getCharacterCell(characterWhoCalled).getRow();
        int newCol = gameBoard.getCharacterCell(characterWhoCalled).getColumn();

        switch (characterWhoCalled.getDirection()) {
            case UP -> newRow -= 1;
            case DOWN -> newRow += 1;
            case LEFT -> newCol -= 1;
            case RIGHT -> newCol += 1;
        }

        return new Point(newRow, newCol);
    }

    private void updateCharacterPosition(Character characterWhoCalled, int newRow, int newCol) {
        CellContent saveCellContent = gameBoard.getCharacterCell(characterWhoCalled).getContentUnderneath();
        CellContent newCellContent = gameBoard.getCell(newRow, newCol).getContent();

        // Check for all types of collisions
        if (characterWhoCalled.getType() == CellContent.PLAYER) {
            gameBoard.getCharacterCell(characterWhoCalled).setEaten(); // remove sprite of CellContent Pacman moved through
            switch (newCellContent) {
                case FOOD -> increaseScore();
                case POWER_UP_SPEED_INCREASE -> usePowerUp("speed");
                case POWER_UP_DOUBLE_SCORE -> usePowerUp("score");
                case POWER_UP_FREEZE_MONSTERS -> usePowerUp("freeze");
                case POWER_UP_EXTRA_LIFE -> usePowerUp("extra-life");
                case POWER_UP_INVINCIBLE -> usePowerUp("invincible");
            }
            if (!newCellContent.equals(CellContent.EMPTY) && !newCellContent.equals(CellContent.ENEMY)) clearedCell();
        } else {
            gameBoard.getCharacterCell(characterWhoCalled).setContent(saveCellContent);
        }

        gameBoard.setCharacterCell(characterWhoCalled, newRow, newCol, characterWhoCalled.getType()); // Move character to next cell
    }

    public void increaseScore(){
        score += 1 * scoreMultiplier;
    }

    public void clearedCell(){
        eatableCellsRemaining--;
        if (eatableCellsRemaining == 0) gameOver = true;
    }

    public int getScore(){
        return score;
    }

    public void startTimer(){
        Thread timerThread = new Thread(() -> {
            while (!gameOver) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time++;
            }
            System.out.println("quit time thread");
        });
        timerThread.start();
        modelThreads.add(timerThread);
    }

    public int getTime(){
        return time;
    }

    public void checkForDeathOrGameOver() {
        GameBoard.Cell pacmanCell = gameBoard.getCharacterCell(pacman);
        boolean collisionDetected = false;

        for (Enemy enemy : getEnemies()) {
            GameBoard.Cell enemyCell = gameBoard.getCharacterCell(enemy);
            if (pacmanCell == enemyCell) {
                collisionDetected = true;
                break;
            }
        }

        if (collisionDetected && !invincible) {
            livesRemaining--;
            if (livesRemaining <= 0) gameOver = true; // Controller constantly monitors this flag
            else {
                resetEnemies();
                revivePacman();
            }
        }
    }

    public Direction getPacmanDirection(){
        return pacman.getDirection();
    }

    public boolean getGameOver(){
        return gameOver;
    }

    public Pacman getPacman(){
        return pacman;
    }

    public ArrayList<Enemy> getEnemies(){ return enemies; }

    public void spawnPowerUps(Enemy enemy){
        Thread spawnPowerUpThread = new Thread(() -> {
            try {
                while (!gameOver) { // this thread is interrupted when the game stops
                    Thread.sleep(5000);

                    int ifToSpawnUpgrade = ThreadLocalRandom.current().nextInt(1, 100 + 1);
                    if (ifToSpawnUpgrade <= 25 && gameBoard.getCharacterCell(enemy).getContentUnderneath() == CellContent.FOOD) {
                        int whichUpgradeToSpawn = ThreadLocalRandom.current().nextInt(1, 5 + 1);
                        switch (whichUpgradeToSpawn) {
                            case 1 -> gameBoard.placePowerUp(enemy, CellContent.POWER_UP_SPEED_INCREASE);
                            case 2 -> gameBoard.placePowerUp(enemy, CellContent.POWER_UP_EXTRA_LIFE);
                            case 3 -> gameBoard.placePowerUp(enemy, CellContent.POWER_UP_FREEZE_MONSTERS);
                            case 4 -> gameBoard.placePowerUp(enemy, CellContent.POWER_UP_DOUBLE_SCORE);
                            case 5 -> gameBoard.placePowerUp(enemy, CellContent.POWER_UP_INVINCIBLE);
                        }
                    }
                }
            }catch (InterruptedException e) {
                System.out.println("Power-up spawning thread was interrupted and will now end.");
            }
        });
        spawnPowerUpThread.start();
        threadsForInterruption.add(spawnPowerUpThread);
        modelThreads.add(spawnPowerUpThread);
    }

    public void usePowerUp(String type){
        if(type.equals("speed") && !gameOver){
            Thread speedPowerUpThread = new Thread(() -> {
                try {
                    pacman.setTimeInterval(150);
                    Thread.sleep(7000);
                    pacman.setTimeInterval(300);
                }catch (InterruptedException e) {
                    System.out.println("Speed power-up thread was interrupted and will now end.");
                }
            });
            speedPowerUpThread.start();
            threadsForInterruption.add(speedPowerUpThread);
            modelThreads.add(speedPowerUpThread);
        }else if(type.equals("score") && !gameOver){
            Thread scorePowerUpThread = new Thread(() -> {
                try {
                    scoreMultiplier = 2;
                    Thread.sleep(6000);
                    scoreMultiplier = 1;
                }catch (InterruptedException e) {
                    System.out.println("Score power-up thread was interrupted and will now end.");
                }
            });
            scorePowerUpThread.start();
            threadsForInterruption.add(scorePowerUpThread);
            modelThreads.add(scorePowerUpThread);
        }else if(type.equals("extra-life") && !gameOver){
            livesRemaining++;
        }else if(type.equals("freeze") && !gameOver){
            Thread freezePowerUpThread = new Thread(() -> {
                try {
                    enemies.forEach(e -> e.setFreezeStatus(true));
                    Thread.sleep(8000);
                    enemies.forEach(e -> {
                        e.setFreezeStatus(false);
                        moveCharacter(e);
                        spawnPowerUps(e);
                    });
                }catch (InterruptedException e) {
                    System.out.println("Freeze power-up thread was interrupted and will now end.");
                }
            });
            freezePowerUpThread.start();
            threadsForInterruption.add(freezePowerUpThread);
            modelThreads.add(freezePowerUpThread);
        }else if(type.equals("invincible") && !gameOver){
            Thread invinciblePowerUpThread = new Thread(() -> {
                try {
                    invincible = true;
                    Thread.sleep(5000);
                    invincible = false;
                }catch (InterruptedException e) {
                    System.out.println("Invincibility power-up thread was interrupted and will now end.");
                }
            });
            invinciblePowerUpThread.start();
            threadsForInterruption.add(invinciblePowerUpThread);
            modelThreads.add(invinciblePowerUpThread);
        }
    }

    // Reset all Model logic
    public void stop(){
        // Interrupt power-up threads to prevent bags when they can still exist in the next game
        for (Thread t : threadsForInterruption) {
            t.interrupt();
        }
        threadsForInterruption.clear();

        // Nullify and reset all objects and variables to their initial state
        this.score = 0;
        this.time = 0;
        this.livesRemaining = 2;
        this.scoreMultiplier = 1;
        this.invincible = false;
        this.eatableCellsRemaining = 0;

        this.enemies.clear();
        this.characters.clear();
        this.modelThreads.clear();

        for (Enemy enemy : enemies) {
            enemy.setIsRunning(false);
        }

        this.gameBoard = null;
        this.gameBoardTableModel = null;
        this.gameTable = null;
        this.pacman = null;
        this.enemy = null;
        this.highScoreList = null;

        System.out.println("stopped model");
    }

    public JTable getGameTable(){
        return gameTable;
    }

    public void revivePacman(){
        gameBoard.setCharacterCell(pacman, 1, 1, pacman.getType());
    }

    public void resetEnemies(){
        for (Enemy enemy : enemies){
            gameBoard.getCharacterCell(enemy).setContent(gameBoard.getCharacterCell(enemy).getContentUnderneath());
            GameBoard.Cell emptyCell = gameBoard.getEmptyCellInTheMiddle();
            gameBoard.setCharacterCell(enemy, emptyCell.getRow(), emptyCell.getColumn(), enemy.getType());
        }
    }

    public int calcNumberOfEnemies(int rows, int columns){
        int cells = rows * columns;

        if (cells <= 144) return 1;
        else if (cells <= 256) return 2;
        else if (cells <= 441) return 4;
        else if (cells <= 1100) return 7;
        else if (cells <= 1600) return 9;
        return 10;
    }

    public int getLivesRemaining(){
        return livesRemaining;
    }

    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; } // for interrupting the game when Ctrl Shift Q is pressed

    public HighScoreList getHighScoreList(){
        return highScoreList;
    }

    public HighScoreList loadOrCreateHighScoreList(){
        File file = new File("high_scores.ser");
        if (file.exists()) {
            highScoreList = HighScoreList.loadHighScoresFromFile(file.getPath());
        } else highScoreList = new HighScoreList();
        return highScoreList;
    }

}
