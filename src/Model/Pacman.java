package Model;

import Enum.CellContent;

public class Pacman extends Character{

    public Pacman(GameBoard gameBoard) {

        super(gameBoard);

    }

    public CellContent getType(){
        return CellContent.PLAYER;
    }
}