package Model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import Enum.CellContent;

public class GameBoard extends AbstractTableModel {
    private int rows;
    private int columns;
    private Cell[][] board;
    private JTable table;

    // Inner class representing a cell in the game board
    public class Cell {
        private int row;
        private int column;
        private Object content;

        public Cell(int row, int column, Object content) {
            this.row = row;
            this.column = column;
            this.content = content;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        // Getters and setters for row, column, and content
    }

    public GameBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new Cell[rows][columns];
        this.table = new JTable(this);

        initBoard();
    }

    private void initBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                board[row][col] = new Cell(row, col, CellContent.FOOD);
            }
        }

        // Initialize the outer walls
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

        // Place the player
        board[1][1] = new Cell(1, 1, CellContent.PLAYER);

        // Place enemies (use a loop to place multiple enemies)
        board[rows - 2][columns - 2] = new Cell(rows - 2, columns - 2, CellContent.ENEMY);

        // Place power-ups (use a loop to place multiple power-ups)
        board[1][columns - 2] = new Cell(1, columns - 2, CellContent.POWER_UP);
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
        board[rowIndex][columnIndex].setContent(value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public JTable getTable() {
        return table;
    }
}