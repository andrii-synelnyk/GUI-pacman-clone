package Model;

import Enum.CellContent;
import Enum.Direction;

public abstract class Character {
    protected boolean isRunning = true;
    protected Direction direction;

    protected GameBoard gameBoard;

    protected int timeInterval;

    public Character(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        direction = Direction.RIGHT; // RANDOMISE IF NOT PACMAN

        this.timeInterval = 300;
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
    } // CAN REMOVE IF ENEMY CLASS EXITS INNER THREAD BY CHECKING GAME OVER FROM GAME MODEL
}
