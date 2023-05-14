package View;

import Controller.A_GameController;
import Model.A_GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HighScoresWindow extends JFrame{


    public HighScoresWindow() {


        showHighScores();
    }

    public void showHighScores() {
        setTitle("Pacman - High Scores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        JPanel highScoresPanel = new JPanel();
        highScoresPanel.setLayout(new GridLayout(3, 1));



        //highScoresPanel.add(newGameButton);

        add(highScoresPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

