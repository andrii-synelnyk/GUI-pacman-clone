package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Enum.Direction;

public class MovementController implements KeyListener {
    private final A_GameController gameController;

    public MovementController(A_GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gameController.movePacman(Direction.UP);
            case KeyEvent.VK_S -> gameController.movePacman(Direction.DOWN);
            case KeyEvent.VK_D -> gameController.movePacman(Direction.RIGHT);
            case KeyEvent.VK_A -> gameController.movePacman(Direction.LEFT);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}