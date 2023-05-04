package Model;

import Enum.CellContent;
import Enum.Direction;

public abstract class Character {
    private boolean isRunning = true;
    protected Direction direction;

    protected GameBoard gameBoard;

    protected A_GameModel gameModel;
    protected int timeInterval;

    public Character(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        direction = Direction.LEFT; // RANDOMISE IF NOT PACMAN

        gameModel = gameBoard.getGameModel();
        this.timeInterval = 300;
    }
    public void moveCharacter(){
        gameModel.moveCharacter(this); // Start checking for change in direction
    }
    public abstract CellContent getType();

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public void changeDirection(){}

    public boolean getIsRunning(){
        return isRunning;
    }

    public void setIsRunning(Boolean isRunning){
        this.isRunning = isRunning;
    }
}
