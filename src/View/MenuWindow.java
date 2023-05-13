package View;

import Controller.A_GameController;
import Model.A_GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuWindow extends JFrame{

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

    public void showMenu() {
        setTitle("Pacman - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridLayout(3, 1));

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            rows = promptBoardSize("Enter the number of rows (10 to 100):");
            columns = promptBoardSize("Enter the number of columns (10 to 100):");

            newGame = true;

            setVisible(false);
        });

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.addActionListener(e -> {
            // Implement high scores functionality here
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> exit = true);

        mainMenuPanel.add(newGameButton);
        mainMenuPanel.add(highScoresButton);
        mainMenuPanel.add(exitButton);

        add(mainMenuPanel);
        setLocationRelativeTo(null);
        setVisible(true);
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
