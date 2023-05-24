package View.ProgramWindows;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.*;

public class HighScoresWindow extends JFrame{
    private Map<String, Integer> sortedHighScores;
    private Font customFont;

    public HighScoresWindow(Map<String, Integer> sortedHighScores) {
        this.sortedHighScores = sortedHighScores;
        showHighScores();

        // Import custom font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Font/emulogic.ttf")).deriveFont(12f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        setFontForAllComponents(this, customFont);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showHighScores() {
        setTitle("Pacman - High Scores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(300, 500));

        // Create the DefaultListModel
        DefaultListModel<String> listModel = highScoresToDefaultListModel();

        // Create a JList and set the model
        JList<String> highScoresJList = new JList<>(listModel);
        highScoresJList.setBackground(Color.BLACK);
        highScoresJList.setForeground(Color.WHITE);

        // Create a JScrollPane and add the JList to it
        JScrollPane scrollPane = new JScrollPane(highScoresJList);
        scrollPane.setBorder(null);

        // Add the JScrollPane to the JFrame
        add(scrollPane);
    }

    private DefaultListModel<String> highScoresToDefaultListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Map.Entry<String, Integer> entry : sortedHighScores.entrySet()) {
            listModel.addElement(entry.getKey() + " - " + entry.getValue());
        }
        return listModel;
    }

    public void setFontForAllComponents(Container container, Font font) {
        for (Component c : container.getComponents()) {
            c.setFont(font);
            if (c instanceof Container) {
                setFontForAllComponents((Container) c, font);
            }
        }
    }
}

