package Model;

import Enum.CellContent;
import Enum.Direction;

public abstract class Character {
    private boolean isRunning = true;
    protected Direction direction;

    protected GameBoard gameBoard;

    public Character(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        direction = Direction.RIGHT; // RANDOMISE IF NOT PACMAN

        System.out.println(gameBoard);
        move(); // Start checking for change in direction
    }

    public abstract CellContent getCellContent();

    public void move() {
        new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(250);
                    if (direction != null) {
                        int newRow = gameBoard.getCharacterCell(this).getRow();
                        int newCol = gameBoard.getCharacterCell(this).getColumn();

                        switch (direction) {
                            case UP -> newRow -= 1;
                            case DOWN -> newRow += 1;
                            case LEFT -> newCol -= 1;
                            case RIGHT -> newCol += 1;
                        }

                        // Check if the next cell is not a wall
                        if (gameBoard.getCell(newRow, newCol).getContent() != CellContent.WALL) {
                            if (getCellContent() == CellContent.PLAYER) {
                                gameBoard.getCharacterCell(this).setContent(CellContent.EMPTY);
                            }
                            gameBoard.setCharacterCell(this, newRow, newCol, this.getCellContent());
                        }
                    }
                } catch (InterruptedException e) {
                    isRunning = false;
                }
            }
        }).start();
    }

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }
}
