package View;

import Controller.A_GameController;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private JTable gameTable;

    public GameWindow(JTable gameBoard) {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true); // Needed to accept user key input
        gameTable = gameBoard;

        // Add the gameTable to the content pane without wrapping it in a JScrollPane
        getContentPane().add(gameTable);


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void redrawBoard() {
        gameTable.repaint();
    }

}