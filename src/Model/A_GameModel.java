package Model;


import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

import Enum.CellContent;

public class A_GameModel {
    private GameBoard gameBoard;

    public A_GameModel(int rows, int columns) {
        this.gameBoard = new GameBoard(rows, columns, this);
        System.out.println("Created gameboard");
    }

    // Add methods to manage game objects (player, enemies, power-ups, etc.)

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void moveCharacter(Character characterWhoCalled) {
        new Thread(() -> {
            while (characterWhoCalled.getIsRunning()) {
                try {
                    Thread.sleep(250);
                    if (characterWhoCalled.direction != null) {
                        int newRow = gameBoard.getCharacterCell(characterWhoCalled).getRow();
                        int newCol = gameBoard.getCharacterCell(characterWhoCalled).getColumn();

                        switch (characterWhoCalled.direction) {
                            case UP -> newRow -= 1;
                            case DOWN -> newRow += 1;
                            case LEFT -> newCol -= 1;
                            case RIGHT -> newCol += 1;
                        }

                        Object saveCellContent = gameBoard.getCharacterCell(characterWhoCalled).getContentUnderneath(); // Save content of cell to recover when character moves further

                        // Check if the next cell is not a wall
                        if (gameBoard.getCell(newRow, newCol).getContent() != CellContent.WALL) {
                            if (characterWhoCalled.getType() == CellContent.PLAYER) {
                                gameBoard.getCharacterCell(characterWhoCalled).setEaten(); // Pacman has eaten food
                            }
                            else {
                                gameBoard.getCharacterCell(characterWhoCalled).setContent(saveCellContent); // If not Pacman leave food or power-up where it was
                            }
                            gameBoard.setCharacterCell(characterWhoCalled, newRow, newCol, characterWhoCalled.getType()); // Move character to next cell
                        }else {
                            if (characterWhoCalled.getType() == CellContent.ENEMY){ // if current instance of Character is enemy and next cell it plans to go to is wall..
                                characterWhoCalled.changeDirection(); // then change direction
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    characterWhoCalled.setIsRunning(false);
                }
            }
        }).start();
    }
}
