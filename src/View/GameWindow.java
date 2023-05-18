package View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JFrame {
    private JTable gameTable;

    private JLabel timeLabel;
    private JLabel scoreLabel;
    private JLabel livesLabel;

    private JPanel timeScorePanel;

    private Font customFont;

    public GameWindow(JTable gameBoard) {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true); // Needed to accept user key input
        gameTable = gameBoard;

        // Add the gameTable to the content pane without wrapping it in a JScrollPane
        getContentPane().add(gameTable);

        JPanel timeScorePanel = createTimeScorePanel();
        getContentPane().add(timeScorePanel, BorderLayout.NORTH);
        timeScorePanel.setBackground(Color.BLACK);

        // Import custom font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Font/emulogic.ttf")).deriveFont(12f); // Changed for lower resolution
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        pack();
        System.out.println("packed");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setGameTable(JTable gameTable){
        this.gameTable = gameTable;
    }

    public void redrawBoard() {
        gameTable.repaint();
        timeLabel.repaint();
        scoreLabel.repaint();
    }

    private JPanel createTimeScorePanel() {
        livesLabel = new JLabel("Lives: 2");
        scoreLabel = new JLabel("Score: 0");
        timeLabel = new JLabel("Time: 00:00");

        livesLabel.setFont(customFont);
        livesLabel.setForeground(Color.WHITE);

        scoreLabel.setFont(customFont);
        scoreLabel.setForeground(Color.WHITE);

        timeLabel.setFont(customFont);
        timeLabel.setForeground(Color.WHITE);

        timeScorePanel = new JPanel();
        timeScorePanel.setLayout(new GridLayout(1, 3)); // can remove rows and cols, do not do anything

        JPanel livesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        livesPanel.add(livesLabel);
        timeScorePanel.add(livesPanel);
        livesPanel.setBackground(Color.BLACK);

        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scorePanel.add(scoreLabel);
        timeScorePanel.add(scorePanel);
        scorePanel.setBackground(Color.BLACK);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timePanel.add(timeLabel);
        timeScorePanel.add(timePanel);
        timePanel.setBackground(Color.BLACK);

        return timeScorePanel;
    }

    public void updateTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        String formattedTime = String.format("Time: %02d:%02d", minutes, seconds);
        timeLabel.setText(formattedTime);
    }

    public void updateScore(int score) {
        if (score < 0) score = 0;
        scoreLabel.setText("Score: " + score);
    }

    public void updateLives(int lives){
        livesLabel.setText("Lives: " + lives);
    }

    public void setFontSize(float fontSize){
        Font resizedFont = customFont.deriveFont(fontSize);
        livesLabel.setFont(resizedFont);
        scoreLabel.setFont(resizedFont);
        timeLabel.setFont(resizedFont);
    }

    public int getHeightOfTopPanel(){
        return timeScorePanel.getHeight();
    }
}