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
        System.out.println("pressed");
        switch (e.getKeyChar()) {
            case 'w':
                gameController.movePacman(Direction.UP);
                break;
            case 's':
                gameController.movePacman(Direction.DOWN);
                break;
            case 'd':
                gameController.movePacman(Direction.RIGHT);
                break;
            case 'a':
                gameController.movePacman(Direction.LEFT);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
