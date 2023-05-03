package Model;

import Enum.Direction;
import Enum.CellContent;

import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Character{

    private boolean isRunning = true;

    private GameBoard gameBoard;

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
    public void changeDirection(){
        int nextDirection = ThreadLocalRandom.current().nextInt(0, 4 + 1);
        switch (nextDirection){
            case 0 -> direction = Direction.UP;
            case 1 -> direction = Direction.DOWN;
            case 2 -> direction = Direction.LEFT;
            case 3 -> direction = Direction.RIGHT;
        }
    }

    public CellContent getCellContent(){
        return CellContent.ENEMY;
    }
}
