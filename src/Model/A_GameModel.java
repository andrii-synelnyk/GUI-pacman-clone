package Model;


import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

import Enum.CellContent;

public class A_GameModel {
    private GameBoard gameBoard;

    private int score;
    private Pacman pacman;

    public A_GameModel(int rows, int columns) {
        this.gameBoard = new GameBoard(rows, columns, this);
        System.out.println("Created gameboard");
        this.score = 0;

        pacman = gameBoard.getPacman();
    }

    // Add methods to manage game objects (player, enemies, power-ups, etc.)

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void moveCharacter(Character characterWhoCalled) {
        new Thread(() -> {
            while (characterWhoCalled.getIsRunning()) {
                try {
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

                    checkForGameOver(); // Check for game over after updating the character position

                    Thread.sleep(characterWhoCalled.timeInterval); // Sleep after checking for game over
                } catch (InterruptedException e) {
                    characterWhoCalled.setIsRunning(false);
                }
            }
        }).start();
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

        if (characterWhoCalled.getType() == CellContent.PLAYER) {
            gameBoard.getCharacterCell(characterWhoCalled).setEaten(); // remove sprite of object Pacman moved through
            if (newCellContent == CellContent.FOOD) { // If the cell pacman moved to is food, then increase score
                increaseScoreBy(1);
            }else if (newCellContent == CellContent.POWER_UP){ // Give Pacman higher speed for 5 sec
                new Thread(() -> {
                    characterWhoCalled.timeInterval = 150;
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    characterWhoCalled.timeInterval = 300;
                }).start();
            }
        } else {
            gameBoard.getCharacterCell(characterWhoCalled).setContent(saveCellContent);
        }

        gameBoard.setCharacterCell(characterWhoCalled, newRow, newCol, characterWhoCalled.getType()); // Move character sprite to next cell
    }

    public void increaseScoreBy(int increaseFactor){
        score += increaseFactor;
    }

    public void checkForGameOver() {
        GameBoard.Cell pacmanCell = gameBoard.getCharacterCell(pacman);
        boolean collisionDetected = false;

        for (Enemy enemy : gameBoard.getEnemies()) {
            GameBoard.Cell enemyCell = gameBoard.getCharacterCell(enemy);
            if (pacmanCell == enemyCell) {
                collisionDetected = true;
                break;
            }
        }

        if (collisionDetected) { // CALL SEPARATE GAME OVER FUNCTION WHICH HANDLES ALL GAME OVER LOGIC (PROBABLY IN CONTROLLER)
            pacman.setIsRunning(false);
            for (Enemy enemy : gameBoard.getEnemies()) {
                enemy.setIsRunning(false);
            }
        }
    }
}
