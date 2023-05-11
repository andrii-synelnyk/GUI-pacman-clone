package Model;


import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import Enum.*;

public class A_GameModel {
    private GameBoard gameBoard;
    private GameBoardTableModel gameBoardTableModel;
    private JTable gameTable;
    private int score;

    private boolean gameOver = false;

    private Pacman pacman;

    private Enemy enemy;

    private ArrayList<Enemy> enemies;
    private HashSet<Character> characters;

    private HashSet<Thread> modelThreads = new HashSet<>();

    private int time;

    private int livesRemaining;

    public A_GameModel() {
        this.score = 0;
        this.livesRemaining = 2;
        enemies = new ArrayList<>();
        characters = new HashSet<>();
    }

    public void initiateGameLogic(int rows, int columns){
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

        // Create and place enemies on the board
        int numberOfEnemies = 5; // Set the desired number of enemies
        for (int i = 0; i < numberOfEnemies; i++) {
            enemy = new Enemy(gameBoard);
            GameBoard.Cell emptyCell = gameBoard.getRandomAvailableCell();
            gameBoard.setCharacterCell(enemy, emptyCell.getRow(), emptyCell.getColumn(), enemy.getType());
            enemies.add(enemy);
            characters.add(enemy);
        }

        // Start moving characters
        characters.forEach(character -> {moveCharacter(character);});

        startTimer();
    }

    // Add methods to manage game objects (player, enemies, power-ups, etc.)

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void moveCharacter(Character characterWhoCalled) {
        Thread moveCharacterThread = new Thread(() -> {
            while (!gameOver) {

                if (characterWhoCalled.direction != null) {
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
                    Thread.sleep(characterWhoCalled.timeInterval); // Wait till the next move
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        moveCharacterThread.start();
        modelThreads.add(moveCharacterThread);
    }

    private Point getNewPosition(Character characterWhoCalled) {
        int newRow = gameBoard.getCharacterCell(characterWhoCalled).getRow();
        int newCol = gameBoard.getCharacterCell(characterWhoCalled).getColumn();

        switch (characterWhoCalled.direction) {
            case UP -> newRow -= 1;
            case DOWN -> newRow += 1;
            case LEFT -> newCol -= 1;
            case RIGHT -> newCol += 1;
        }

        return new Point(newRow, newCol);
    }

    private void updateCharacterPosition(Character characterWhoCalled, int newRow, int newCol) {
        Object saveCellContent = gameBoard.getCharacterCell(characterWhoCalled).getContentUnderneath();
        Object newCellContent = gameBoard.getCell(newRow, newCol).getContent();

        // Check for all types of collisions
        if (characterWhoCalled.getType() == CellContent.PLAYER) {
            gameBoard.getCharacterCell(characterWhoCalled).setEaten(); // remove sprite of object Pacman moved through
            if (newCellContent == CellContent.FOOD) { // If the cell pacman moved to is food, then increase score
                increaseScoreBy(1);
            }else if (newCellContent == CellContent.POWER_UP){ // Give Pacman higher speed for 7 sec
                // Power up thread
                useSpeedPowerUp();
            }
        } else {
            gameBoard.getCharacterCell(characterWhoCalled).setContent(saveCellContent);
        }

        gameBoard.setCharacterCell(characterWhoCalled, newRow, newCol, characterWhoCalled.getType()); // Move character to next cell
    }

    public void increaseScoreBy(int increaseFactor){
        score += increaseFactor;
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

        if (collisionDetected) {
            livesRemaining--;
            if (livesRemaining == 0) gameOver = true; // Controller constantly monitors this flag
            else revivePacman();
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

    public void useSpeedPowerUp(){
        Thread powerUpThread = new Thread(() -> {
            pacman.timeInterval = 150;
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pacman.timeInterval = 300;
        });
        powerUpThread.start();
        modelThreads.add(powerUpThread);
    }

    public void stop(){
        // Join threads in Model class (moving characters, power-ups activations)
        for (Thread t : modelThreads){
            try{
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        // Join threads in Enemy class (choosing new direction)
        for (Enemy enemy : enemies) {
            enemy.setIsRunning(false);
            try {
                enemy.getChangeDirectionThread().join();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public JTable getGameTable(){
        return gameTable;
    }

    public void revivePacman(){
        gameBoard.setCharacterCell(pacman, 1, 1, pacman.getType());
    }

    public int getLivesRemaining(){
        return livesRemaining;
    }
}
