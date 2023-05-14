package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreList implements Serializable {
    private List<HighScore> highScores;

    public HighScoreList() {
        highScores = new ArrayList<>();
    }

    public void addHighScore(HighScore highScore) {
        highScores.add(highScore);
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public void saveHighScoresToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HighScoreList loadHighScoresFromFile(String filename) {
        HighScoreList highScoreList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            highScoreList = (HighScoreList) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return highScoreList;
    }
}