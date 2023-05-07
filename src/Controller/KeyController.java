package Controller;

import Model.Pacman;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Enum.Direction;

public class KeyController implements KeyListener {
    private final A_GameController gameController;

    public KeyController(A_GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w' -> gameController.movePacman(Direction.UP);
            case 's' -> gameController.movePacman(Direction.DOWN);
            case 'd' -> gameController.movePacman(Direction.RIGHT);
            case 'a' -> gameController.movePacman(Direction.LEFT);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
