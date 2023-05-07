package Model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import Enum.CellContent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameBoard extends AbstractTableModel {
    private int rows;
    private int columns;
    private Cell[][] board;
    private JTable table;

    private HashMap<Character, Cell> characterCells;

    private A_GameModel gameModel; // REMOVE


    // Inner class representing a cell in the game board
    public class Cell {
        private int row;
        private int column;
        private Object content;
        private Object contentUnderneath;

        public Cell(int row, int column, Object content) {
            this.row = row;
            this.column = column;
            this.content = content;
            this.contentUnderneath = content;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public void setEaten(){
            this.content = CellContent.EMPTY;
            this.contentUnderneath = CellContent.EMPTY;
        }

        // Getters and setters for row, column, and content
        public int getRow(){
            return row;
        }

        public int getColumn(){
            return column;
        }

        public Object getContentUnderneath(){
            return contentUnderneath;
        }
    }

    public GameBoard(int rows, int columns, A_GameModel gameModel) {
        this.rows = rows;
        this.columns = columns;
        this.board = new Cell[rows][columns];
        this.table = new JTable(this);

        this.gameModel = gameModel;

        characterCells = new HashMap<>();

        table.setFocusable(false);

        initBoard();
    }

    private void initBoard() {
        // Spawn food everywhere (later will be replaced by walls or other objects if necessary)
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                board[row][col] = new Cell(row, col, CellContent.FOOD);
            }
        }

        // Initialize the outer walls (rewrite cells which were originally occupied by food)
        for (int row = 0; row < rows; row++) {
            board[row][0] = new Cell(row, 0, CellContent.WALL);
            board[row][columns - 1] = new Cell(row, columns - 1, CellContent.WALL);
        }
        for (int col = 0; col < columns; col++) {
            board[0][col] = new Cell(0, col, CellContent.WALL);
            board[rows - 1][col] = new Cell(rows - 1, col, CellContent.WALL);
        }

        // Initialize the inner walls
        for (int row = 2; row < rows - 2; row += 2) {
            for (int col = 2; col < columns - 2; col += 2) {
                board[row][col] = new Cell(row, col, CellContent.WALL);
            }
        }

        // Place power-ups (use a loop to place multiple power-ups)
        board[rows - 2][columns - 5] = new Cell(rows - 2, columns - 5, CellContent.POWER_UP);
    }

    public void setCharacterCell(Character character, int row, int column, CellContent content) {
        board[row][column].setContent(content);
        characterCells.put(character, board[row][column]);
    }

    public Cell getCharacterCell(Character character) {
        return characterCells.get(character);
    }

    public Cell getCell(int row, int column){
        return board[row][column];
    }

    public A_GameModel getGameModel(){
        return gameModel;
    }

    public Cell getRandomEmptyCell() {
        List<Cell> emptyCells = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (board[row][col].getContent() == CellContent.FOOD) {
                    emptyCells.add(board[row][col]);
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, emptyCells.size());
            return emptyCells.get(randomIndex);
        } else {
            throw new IllegalStateException("No empty cells available to place an enemy.");
        }
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return board[rowIndex][columnIndex].getContent();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public JTable getTable() {
        return table;
    }
}