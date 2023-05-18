package Controller.KeyInput;

import Controller.GameController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CompoundShortcutController implements KeyListener {

    private final GameController gameController;

    public CompoundShortcutController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
            System.out.println("Ctrl+Shift+Q pressed");
            gameController.interruptWithShortcut();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void keyReleased(KeyEvent e) {}
}
