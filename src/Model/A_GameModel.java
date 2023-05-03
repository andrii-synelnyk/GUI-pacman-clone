package Model;


import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

import Enum.CellContent;

public class A_GameModel {
    private GameBoard gameBoard;

    private int score;

    public A_GameModel(int rows, int columns) {
        this.gameBoard = new GameBoard(rows, columns, this);
        System.out.println("Created gameboard");
        this.score = 0;
    }

    // Add methods to manage game objects (player, enemies, power-ups, etc.)

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void moveCharacter(Character characterWhoCalled) {
        new Thread(() -> {
            while (characterWhoCalled.getIsRunning()) {
                try {
                    System.out.println(score);
                    Thread.sleep(250);
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

        if (characterWhoCalled.getType() == CellContent.PLAYER) {
            gameBoard.getCharacterCell(characterWhoCalled).setEaten(); // remove sprite of object Pacman moved through
            if (gameBoard.getCell(newRow, newCol).getContent() == CellContent.FOOD) { // If the cell pacman moved to is food, then increase score
                increaseScoreBy(1);
            }
        } else {
            gameBoard.getCharacterCell(characterWhoCalled).setContent(saveCellContent);
        }

        gameBoard.setCharacterCell(characterWhoCalled, newRow, newCol, characterWhoCalled.getType()); // Move character sprite to next cell
    }

    public void increaseScoreBy(int increaseFactor){
        score += increaseFactor;
    }
}
