package Model;


import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class A_GameModel {
    private GameBoard gameBoard;

    public A_GameModel(int rows, int columns) {
        this.gameBoard = new GameBoard(rows, columns);
    }

    // Add methods to manage game objects (player, enemies, power-ups, etc.)

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    // GameBoard class remains the same as before, but make it a static inner class
}
