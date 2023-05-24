package Model.GameBoardLogic;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import Enum.CellContent;

import Model.CharacterModels.Character;
import Model.CharacterModels.Enemy;

public class GameBoard {
    private int rows;
    private int columns;
    private Cell[][] board;
    private HashMap<Character, Cell> characterCells;
    public int eatableCellsNumber;

    // Inner class representing a cell in the game board
    public class Cell {
        private int row;
        private int column;
        private CellContent content;
        private CellContent contentUnderneath;

        public Cell(int row, int column, CellContent content) {
            this.row = row;
            this.column = column;
            this.content = content;
            this.contentUnderneath = content;
        }

        public synchronized CellContent getContent() {
            return content;
        }

        public synchronized void setContent(CellContent content) {
            this.content = content;
        }

        public synchronized void setEaten(){
            this.content = CellContent.EMPTY;
            this.contentUnderneath = CellContent.EMPTY;
        }

        public int getRow(){
            return row;
        }

        public int getColumn(){
            return column;
        }

        public CellContent getContentUnderneath(){
            return contentUnderneath;
        }

        public void setContentUnderneath(CellContent contentUnderneath){
            this.contentUnderneath = contentUnderneath;
        }
    }

    public GameBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new Cell[rows][columns];

        characterCells = new HashMap<>();

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
        List<Cell> boundSoFar = new ArrayList<>();
        int startRow = random.nextInt(rows / 2) * 2 + 1; // 2k + 1 for odd row
        int startCol = random.nextInt(columns / 2) * 2 + 1; // 2k + 1 for odd column
        Cell startCell = new Cell(startRow, startCol, CellContent.EMPTY);
        board[startRow][startCol] = startCell; // putting cell on the board
        boundSoFar.add(startCell);

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        while (!boundSoFar.isEmpty()) {
            // Choose a random cell from the boundSoFar list
            Cell current = boundSoFar.remove(random.nextInt(boundSoFar.size())); // .remove() also returns the cell

            // Look into 4 directions for each boundSoFar cell
            for (int[] direction : directions) {
                int newRow = current.getRow() + direction[0] * 2;
                int newCol = current.getColumn() + direction[1] * 2;

                // If new cell is inbounds of board and is currently a wall, make it empty and make empty cell between current and new
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns && board[newRow][newCol].getContent() == CellContent.WALL) {
                    // Make newCell empty and add it to boundSoFar list
                    Cell newCell = new Cell(newRow, newCol, CellContent.EMPTY);
                    board[newRow][newCol] = newCell;
                    boundSoFar.add(newCell);

                    // Remove the wall between the current cell and the new cell
                    board[current.getRow() + direction[0]][current.getColumn() + direction[1]].setContent(CellContent.EMPTY);
                }
            }
        }

        // Make cells empty in the second row, row before the last, second column, and column before the last
        for (int row = 1; row < rows - 1; row++) {
            if (row == 1 || row == rows - 2) {
                for (int col = 1; col < columns - 1; col++) {
                    board[row][col] = new Cell(row, col, CellContent.EMPTY);
                }
            } else {
                board[row][1] = new Cell(row, 1, CellContent.EMPTY);
                board[row][columns - 2] = new Cell(row, columns - 2, CellContent.EMPTY);
            }
        }

        removeDeadEnds();

        // Place food on cells that are empty
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (board[row][col].getContent() == CellContent.EMPTY) {
                    board[row][col] = new Cell(row, col, CellContent.FOOD);
                }
            }
        }

    }

    public void removeDeadEnds(){
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < columns - 1; col++) {
                if (board[row][col].getContent() == CellContent.EMPTY) {
                    int wallCount = 0;
                    ArrayList<Cell> wallCellsAround = new ArrayList<>();

                    for (int[] direction : directions) {
                        int newRow = row + direction[0];
                        int newCol = col + direction[1];

                        if (board[newRow][newCol].getContent() == CellContent.WALL) {
                            wallCount++;
                            wallCellsAround.add(board[newRow][newCol]);
                        }
                    }

                    if (wallCount == 3) {
                        int indexOfCellToChange = ThreadLocalRandom.current().nextInt(0, wallCellsAround.size());
                        Cell cellToChange = wallCellsAround.get(indexOfCellToChange);
                        board[cellToChange.getRow()][cellToChange.getColumn()] = new Cell(cellToChange.getRow(), cellToChange.getColumn(), CellContent.EMPTY);
                    }
                }
            }
        }
    }

    public synchronized void setCharacterCell(Character character, int row, int column, CellContent content) {
        board[row][column].setContent(content);
        characterCells.put(character, board[row][column]);
    }

    public synchronized Cell getCharacterCell(Character character) {
        return characterCells.get(character);
    }

    public synchronized Cell getCell(int row, int column){
        return board[row][column];
    }

    public Cell getEmptyCellInTheMiddle(){
        // Calculate the center of the board
        int centerRow = rows / 2;
        int centerCol = columns / 2;

        // Set the content of the four cells that form a square in the middle of the board
        if (board[centerRow][centerCol].getContent() != CellContent.WALL){
            return board[centerRow][centerCol];
        } else if (board[centerRow - 1][centerCol].getContent() != CellContent.WALL){
            return board[centerRow - 1][centerCol];
        } else if (board[centerRow][centerCol - 1].getContent() != CellContent.WALL){
            return board[centerRow][centerCol - 1];
        } return board[centerRow - 1][centerCol - 1];
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

    public synchronized void placePowerUp(Enemy enemyWhoCalled, CellContent powerUpType){
        getCharacterCell(enemyWhoCalled).setContentUnderneath(powerUpType);
    }

    public int getEatableCellsCount(){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (board[row][col].getContent() == CellContent.FOOD) {
                    eatableCellsNumber++;
                }
            }
        } return eatableCellsNumber;
    }
}