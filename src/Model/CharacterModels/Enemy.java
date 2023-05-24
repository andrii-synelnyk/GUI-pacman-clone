package Model.CharacterModels;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import Enum.Direction;
import Enum.CellContent;

import Model.Character;
import Model.GameBoardLogic.GameBoard;
import Model.GameBoardLogic.GameBoard.Cell;

public class Enemy extends Character {
    Thread changeDirectionThread;

    public Enemy(GameBoard gameBoard) {
        super(gameBoard);

        changeDirectionThread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                    changeDirection();
                } catch (InterruptedException e) {
                    isRunning = false;
                }
            }
        });
        changeDirectionThread.start();
    }

    @Override
    public void changeDirection() {
        ArrayList<Direction> availableDirections = new ArrayList<>();
        Cell currentCharacterCell = gameBoard.getCharacterCell(this);

        // Get available directions (cells around which are not walls)
        if (gameBoard.getCell(currentCharacterCell.getRow() - 1, currentCharacterCell.getColumn()).getContent() != CellContent.WALL)
            availableDirections.add(Direction.UP);

        if (gameBoard.getCell(currentCharacterCell.getRow() + 1, currentCharacterCell.getColumn()).getContent() != CellContent.WALL)
            availableDirections.add(Direction.DOWN);

        if (gameBoard.getCell(currentCharacterCell.getRow(), currentCharacterCell.getColumn() + 1).getContent() != CellContent.WALL)
            availableDirections.add(Direction.RIGHT);

        if (gameBoard.getCell(currentCharacterCell.getRow(), currentCharacterCell.getColumn() - 1).getContent() != CellContent.WALL)
            availableDirections.add(Direction.LEFT);

        // Choose randomly one direction from available directions
        if (!availableDirections.isEmpty()) {
            direction = availableDirections.get(ThreadLocalRandom.current().nextInt(0, availableDirections.size()));
        }
    }

    public CellContent getType(){
        return CellContent.ENEMY;
    }
}
