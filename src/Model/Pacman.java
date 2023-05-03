package Model;

import Enum.Direction;
import Enum.CellContent;

public class Pacman extends Character{
    private int mouthOpened = 0;
    private boolean mouthFullyOpened = true;

    private boolean isRunning = true;

    private GameBoard gameBoard;

    public Pacman(GameBoard gameBoard) {

        super(gameBoard);

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

    }

    public int getMouthOpened() {
        return mouthOpened;
    }

    public CellContent getCellContent(){
        return CellContent.PLAYER;
    }
}