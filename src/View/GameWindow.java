package View;

import Controller.A_GameController;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private JTable gameTable;

    private JLabel timeLabel;
    private JLabel scoreLabel;

    public GameWindow(JTable gameBoard) {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true); // Needed to accept user key input
        gameTable = gameBoard;

        // Add the gameTable to the content pane without wrapping it in a JScrollPane
        getContentPane().add(gameTable);

        JPanel timeScorePanel = createTimeScorePanel();
        getContentPane().add(timeScorePanel, BorderLayout.NORTH);
        timeScorePanel.setBackground(Color.BLACK);


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void redrawBoard() {
        gameTable.repaint();
        timeLabel.repaint();
        scoreLabel.repaint();
    }

    private JPanel createTimeScorePanel() {
        scoreLabel = new JLabel("Score: 0");
        timeLabel = new JLabel("Time: 00:00");

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);

        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timeLabel.setForeground(Color.WHITE);

        JPanel timeScorePanel = new JPanel();
        timeScorePanel.setLayout(new GridLayout(1, 2));

        JPanel cellPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cellPanel1.add(scoreLabel);
        timeScorePanel.add(cellPanel1);
        cellPanel1.setBackground(Color.BLACK);

        JPanel cellPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cellPanel.add(timeLabel);
        timeScorePanel.add(cellPanel);
        cellPanel.setBackground(Color.BLACK);

        return timeScorePanel;
    }

    public void updateTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        String formattedTime = String.format("Time: %02d:%02d", minutes, seconds);
        timeLabel.setText(formattedTime);
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

}