package Model.CharacterModels;

import Enum.CellContent;
import Model.Character;
import Model.GameBoardLogic.GameBoard;

public class Pacman extends Character {

    public Pacman(GameBoard gameBoard) {

        super(gameBoard);

    }

    public CellContent getType(){
        return CellContent.PLAYER;
    }
}