package View.ProgramWindows;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

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
        setSize(500, 500);

        // Create a JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setSize(new Dimension(500, 500));
        setContentPane(layeredPane);

        try {
            Image decorativeImage = ImageIO.read(new File("src/Images/pacman-for-menu.png"));
            Image resizedImage = decorativeImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            JLabel decorativeLabel = new JLabel(imageIcon);

            // Set the position to the bottom right
            int x = getWidth() - imageIcon.getIconWidth();
            int y = getHeight() - imageIcon.getIconHeight() - 25; // Application header height (change for Windows)
            decorativeLabel.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());

            // Add to layer 0 (background)
            layeredPane.add(decorativeLabel, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
        mainMenuPanel.setBackground(Color.BLACK);

        JButton newGameButton = new JButton("New Game");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            rows = promptBoardSize("Enter the number of rows (10 to 100):");
            columns = promptBoardSize("Enter the number of columns (10 to 100):");

            if (rows != -1 && columns != -1) { // if user haven't pressed CANCEL button on both input dialogs
                newGame = true;
                setVisible(false);
            }
        });

        JButton highScoresButton = new JButton("High Scores");
        styleButton(highScoresButton);
        highScoresButton.addActionListener(e -> highScore = true);

        JButton exitButton = new JButton("Exit");
        styleButton(exitButton);
        exitButton.addActionListener(e -> exit = true);

        mainMenuPanel.add(newGameButton);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainMenuPanel.add(highScoresButton);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainMenuPanel.add(exitButton);

        // Set the position and size of the menu panel
        mainMenuPanel.setBounds(0, 0, getWidth(), getHeight() - 20); // Application header height (change for Windows)

        // Add to layer 1 (foreground)
        layeredPane.add(mainMenuPanel, 1);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        // Use custom font
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Font/emulogic.ttf")).deriveFont(24f);
            button.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private int promptBoardSize(String message) {
        int size = 0;
        while (size < 10 || size > 100) {
            InputDialog dialog = new InputDialog(this, message);
            dialog.setVisible(true);
            try {
                size = dialog.getInputInt();
                if (size == -1) { // if cancel button is pressed
                    return -1; // indicate that there was no input
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 10 and 100.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return size;
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
}
