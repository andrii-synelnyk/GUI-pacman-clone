package Model;

import Enum.Direction;
import Enum.CellContent;

public class Pacman extends Character{

    private boolean isRunning = true;

    public Pacman(GameBoard gameBoard) {

        super(gameBoard);

    }

    public CellContent getType(){
        return CellContent.PLAYER;
    }
}