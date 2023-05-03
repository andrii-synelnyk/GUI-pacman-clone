package Model;

import Enum.Direction;
import Enum.CellContent;

public class Pacman {
    private int mouthOpened = 0;
    private boolean mouthFullyOpened = true;

    private boolean isRunning = true;

    private Direction direction = Direction.RIGHT;
    private GameBoard gameBoard;

    public Pacman(A_GameModel gameModel) {

        gameBoard = gameModel.getGameBoard();

        new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(10);
                    mouthOpened += mouthFullyOpened ? 10 : -10;
                    if (mouthOpened == 120 || mouthOpened == 0) {
                        mouthFullyOpened = !mouthFullyOpened;
                    }
                } catch (InterruptedException e) {
                    isRunning = false;
                }
            }
        }).start();

        move(); // Start checking for change in direction
    }

    public int getMouthOpened() {
        return mouthOpened;
    }

    public void move() {
        new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(250);
                    if (direction != null) {
                        int newRow = gameBoard.getPacmanCell().getRow();
                        int newCol = gameBoard.getPacmanCell().getColumn();

                        switch (direction) {
                            case UP -> newRow -= 1;
                            case DOWN -> newRow += 1;
                            case LEFT -> newCol -= 1;
                            case RIGHT -> newCol += 1;
                        }

                        // Check if the next cell is not a wall
                        if (gameBoard.getCell(newRow, newCol).getContent() != CellContent.WALL) {
                            gameBoard.getPacmanCell().setContent(CellContent.EMPTY);
                            gameBoard.setPacmanCell(newRow, newCol);
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