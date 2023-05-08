import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.A_GameModel;
import View.A_GameView;
import Controller.A_GameController;

public class Main {

    public static void main(String[] args) {
        // Display the main menu window
        showMainMenu();
    }

    private static void showMainMenu() {
        JFrame mainMenuFrame = new JFrame("Pacman - Main Menu");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(300, 200);

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridLayout(3, 1));

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.setVisible(false);
                mainMenuFrame.dispose();

                int rows = promptBoardSize("Enter the number of rows (10 to 100):");
                int columns = promptBoardSize("Enter the number of columns (10 to 100):");

                A_GameModel gameModel = new A_GameModel(rows, columns);
                A_GameView gameView = new A_GameView();
                A_GameController gameController = new A_GameController(gameModel, gameView);
            }
        });

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement high scores functionality here
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        mainMenuPanel.add(newGameButton);
        mainMenuPanel.add(highScoresButton);
        mainMenuPanel.add(exitButton);

        mainMenuFrame.add(mainMenuPanel);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    private static int promptBoardSize(String message) {
        int size = 0;
        while (size < 10 || size > 100) {
            try {
                String input = JOptionPane.showInputDialog(message);
                if (input == null) {
                    System.exit(0);
                }
                size = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 10 and 100.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return size;
    }
}