package View;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class HighScoresWindow extends JFrame{

    private Map<String, Integer> sortedHighScores;

    public HighScoresWindow(Map<String, Integer> sortedHighScores) {
        this.sortedHighScores = sortedHighScores;
        showHighScores();
    }

    public void showHighScores() {
        setTitle("Pacman - High Scores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Change to DISPOSE_ON_CLOSE
        //setSize(300, 400);

        // Create the DefaultListModel
        DefaultListModel<String> listModel = highScoresToDefaultListModel();

        // Create a JList and set the model
        JList<String> highScoresJList = new JList<>(listModel);
        highScoresJList.setBackground(Color.BLACK);
        highScoresJList.setForeground(Color.WHITE);
        highScoresJList.setFont(new Font("Arial", Font.PLAIN, 16));

        // Create a JScrollPane and add the JList to it
        JScrollPane scrollPane = new JScrollPane(highScoresJList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        // Add the JScrollPane to the JFrame
        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private DefaultListModel<String> highScoresToDefaultListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Map.Entry<String, Integer> entry : sortedHighScores.entrySet()) {
            listModel.addElement(entry.getKey() + " - " + entry.getValue());
        }
        return listModel;
    }
}

