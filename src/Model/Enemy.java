package Model;

import Enum.Direction;
import Enum.CellContent;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import Model.GameBoard.Cell;

public class Enemy extends Character{

    private boolean isRunning = true;

    public Enemy(GameBoard gameBoard) {

        super(gameBoard);

        new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                    changeDirection();
                } catch (InterruptedException e) {
                    isRunning = false;
                }
            }
        }).start();

    }

    @Override
    public void changeDirection() {

        ArrayList<Direction> availableDirections = new ArrayList<>();

        Cell currentCharacterCell = gameBoard.getCharacterCell(this);

        if (gameBoard.getCell(currentCharacterCell.getRow() - 1, currentCharacterCell.getColumn()).getContent() != CellContent.WALL)
            availableDirections.add(Direction.UP);

        if (gameBoard.getCell(currentCharacterCell.getRow() + 1, currentCharacterCell.getColumn()).getContent() != CellContent.WALL)
            availableDirections.add(Direction.DOWN);

        if (gameBoard.getCell(currentCharacterCell.getRow(), currentCharacterCell.getColumn() + 1).getContent() != CellContent.WALL)
            availableDirections.add(Direction.RIGHT);

        if (gameBoard.getCell(currentCharacterCell.getRow(), currentCharacterCell.getColumn() - 1).getContent() != CellContent.WALL)
            availableDirections.add(Direction.LEFT);

        if (!availableDirections.isEmpty()) {
            direction = availableDirections.get(ThreadLocalRandom.current().nextInt(0, availableDirections.size()));
        }

    }

    public CellContent getCellContent(){
        return CellContent.ENEMY;
    }
}
