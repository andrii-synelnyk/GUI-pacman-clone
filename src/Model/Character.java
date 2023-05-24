package Model;

import Enum.CellContent;
import Enum.Direction;

import Model.GameBoardLogic.GameBoard;

public abstract class Character {
    protected boolean isRunning = true;
    protected Direction direction;
    protected GameBoard gameBoard;
    protected int timeInterval;
    protected boolean freeze = false;

    public Character(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        direction = Direction.RIGHT;

        this.timeInterval = 300; // default time interval. Overwritten by children
    }

    public abstract CellContent getType();

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public void changeDirection(){}

    public void setIsRunning(Boolean isRunning){
        this.isRunning = isRunning;
    }

    public boolean isFrozen(){
        return freeze;
    }

    public void setFreezeStatus(boolean freeze){
        this.freeze = freeze;
    }
}
