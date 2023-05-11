package Model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import Enum.CellContent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameBoard {
    private int rows;
    private int columns;
    private Cell[][] board;

    private HashMap<Character, Cell> characterCells;

    // DEBUG
    public HashSet<Cell> foods = new HashSet<>();

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

    public GameBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new Cell[rows][columns];

        characterCells = new HashMap<>();

        //table.setFocusable(false);

        initBoard();
    }

    private void initBoard() {
        checkInput();
        // Fill the board with walls
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                board[row][col] = new Cell(row, col, CellContent.WALL);
            }
        }

        // Generate maze using randomized Prim's algorithm
        Random random = new Random();
        List<Cell> frontier = new ArrayList<>();
        int startRow = random.nextInt(rows / 2) * 2 + 1;
        int startCol = random.nextInt(columns / 2) * 2 + 1;
        Cell startCell = new Cell(startRow, startCol, CellContent.EMPTY);
        board[startRow][startCol] = startCell;
        frontier.add(startCell);

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        while (!frontier.isEmpty()) {
            // Choose a random cell from the frontier list
            Cell current = frontier.remove(random.nextInt(frontier.size()));

            // Look for a neighboring cell in the maze
            for (int[] direction : directions) {
                int newRow = current.getRow() + direction[0] * 2;
                int newCol = current.getColumn() + direction[1] * 2;

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns && board[newRow][newCol].getContent() == CellContent.WALL) {
                    // Add the neighboring cell to the frontier and connect it to the current cell
                    Cell newCell = new Cell(newRow, newCol, CellContent.EMPTY);
                    board[newRow][newCol] = newCell;
                    frontier.add(newCell);

                    // Remove the wall between the current cell and the new cell
                    board[current.getRow() + direction[0]][current.getColumn() + direction[1]].setContent(CellContent.EMPTY);
                }
            }
        }

        // Place power-ups (use a loop to place multiple power-ups)
        board[rows - 2][columns - 5] = new Cell(rows - 2, columns - 5, CellContent.POWER_UP);

        // Place food on empty cells
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (board[row][col].getContent() == CellContent.EMPTY) {
                    board[row][col] = new Cell(row, col, CellContent.FOOD);
                    foods.add(board[row][col]);
                }
            }
        }

        System.out.println(foods.size());

        // Make all cells empty in the second row, row before the last, second column, and column before the last
        for (int row = 1; row < rows - 1; row++) {
            if (row == 1 || row == rows - 2) {
                for (int col = 1; col < columns - 1; col++) {
                    board[row][col] = new Cell(row, col, CellContent.FOOD);
                }
            } else {
                board[row][1] = new Cell(row, 1, CellContent.FOOD);
                board[row][columns - 2] = new Cell(row, columns - 2, CellContent.FOOD);
            }
        }

        removeDeadEnds();
    }

    public void removeDeadEnds(){
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int row = 1; row < rows - 1; row++) {
                for (int col = 1; col < columns - 1; col++) {
                    if (board[row][col].getContent() == CellContent.FOOD) {
                        int wallCount = 0;
                        ArrayList<Cell> wallCellsAround = new ArrayList<>();

                        for (int[] direction : directions) {
                            int newRow = row + direction[0];
                            int newCol = col + direction[1];

                            if (board[newRow][newCol].getContent() == CellContent.WALL) {
                                wallCount++;
                                System.out.println(wallCount);
                                wallCellsAround.add(board[newRow][newCol]);
                            }
                        }

                        if (wallCount == 3) {
                            System.out.println("bla");
                            int indexOfCellToChange = ThreadLocalRandom.current().nextInt(0, wallCellsAround.size());
                            Cell cellToChange = wallCellsAround.get(indexOfCellToChange);
                            board[cellToChange.getRow()][cellToChange.getColumn()] = new Cell(cellToChange.getRow(), cellToChange.getColumn(), CellContent.FOOD);
                        }
                    }
                }
        }
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

    public Cell getRandomAvailableCell() {
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

    public int getRowCount(){
        return rows;
    }

    public int getColumnCount(){
        return columns;
    }

    public void checkInput(){
        if (this.rows % 2 == 0) this.rows++;
        if (this.columns % 2 == 0) this.columns++;
        this.board = new Cell[rows][columns];
    }
}