package View;

import Controller.A_GameController;
import Model.A_GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuWindow {

    private boolean newGame;
    private boolean highScore;
    private boolean exit;

    private int rows;
    private int columns;

    public MenuWindow() {
        this.newGame = false;
        this.highScore = false;
        this.exit = false;

        showMenu();
    }

    public void showMenu(){
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

                rows = promptBoardSize("Enter the number of rows (10 to 100):");
                columns = promptBoardSize("Enter the number of columns (10 to 100):");

                newGame = true;
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
                exit = true;
            }
        });

        mainMenuPanel.add(newGameButton);
        mainMenuPanel.add(highScoresButton);
        mainMenuPanel.add(exitButton);

        mainMenuFrame.add(mainMenuPanel);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    public boolean getNewGame(){
        return newGame;
    }

    public boolean getHighScore(){
        return highScore;
    }

    public boolean getExit(){
        return exit;
    }

    public void setNewGame(boolean newGame){
        this.newGame = newGame;
    }

    public void setHighScore(boolean highScore){
        this.highScore = highScore;
    }

    public int getRowsInput(){
        return rows;
    }

    public int getColumnsInput(){
        return columns;
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
