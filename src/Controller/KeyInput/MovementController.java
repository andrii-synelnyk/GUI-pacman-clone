package Controller.KeyInput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Controller.GameController;
import Enum.Direction;

public class MovementController implements KeyListener {
    private final GameController gameController;

    public MovementController(GameController gameController) {
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