package View;

import javax.swing.*;
import Enum.Direction;
import View.CharacterViews.EnemyView;
import View.CharacterViews.PacmanView;
import View.ProgramWindows.GameWindow;
import View.ProgramWindows.HighScoresWindow;
import View.ProgramWindows.InputDialog;
import View.ProgramWindows.MenuWindow;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

public class GameView {

    GameWindow gameWindow;
    MenuWindow menu;
    int imageSize;
    private PacmanView pacmanView;
    private EnemyView enemyView;
    private int numberOfRows;
    private int numberOfColumns;
    private double aspectRatio;

    public GameView() {
        menu = new MenuWindow();
    }

    public void showGameWindow(JTable gameBoard){
        imageSize = calculateImageSize(numberOfRows, numberOfColumns);
        pacmanView = new PacmanView(imageSize);
        enemyView = new EnemyView(imageSize);

        JTable gameBoardForView = configGameBoard(gameBoard);

        gameWindow = new GameWindow(gameBoardForView);
        aspectRatio = (double) numberOfRows / numberOfColumns;
        gameWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                keepAspectRatio();
                resizeContents(gameBoard);
            }
        });
        setFontSize();
    }

    private int calculateImageSize(int rows, int columns) {
        // Replace these constants with the desired dimensions of your game view
        final int gameViewWidth = 600; // Changed for lower resolution
        final int gameViewHeight = 600; // Changed for lower resolution

        int widthPerCell = gameViewWidth / columns;
        int heightPerCell = gameViewHeight / rows;

        // Choose the smaller dimension to fit both width and height
        return Math.min(widthPerCell, heightPerCell);
    }

    private int calculateNewImageSize(int rows, int columns){
        // Replace these constants with the desired dimensions of your game view
        final int gameViewWidth = gameWindow.getWidth();
        final int gameViewHeight = gameWindow.getHeight() - gameWindow.getHeightOfTopPanel() - 30; // Application header height (change for Windows)

        int widthPerCell = gameViewWidth / columns;
        int heightPerCell = gameViewHeight / rows;

        // Choose the smaller dimension to fit both width and height
        return Math.min(widthPerCell, heightPerCell);
    }

    public void setPacmanViewDirection(Direction direction){
        pacmanView.setPacmanDirection(direction);
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
    }
    public void setNumberOfColumns(int numberOfColumns) {this.numberOfColumns = numberOfColumns; }

    public void keepAspectRatio(){
        // Calculate new width and height based on aspect ratio
        int newWidth = gameWindow.getWidth();
        int newHeight = (int) Math.round(newWidth * aspectRatio) + gameWindow.getHeightOfTopPanel() + 30; // Application header height (change for Windows)

        // Set the new size while maintaining aspect ratio
        gameWindow.setSize(newWidth, newHeight);
    }

    public void resizeContents(JTable gameBoard){
        imageSize = calculateNewImageSize(numberOfRows, numberOfColumns);
        pacmanView.setNewImageSize(imageSize);
        enemyView.setNewImageSize(imageSize);
        JTable resizedTable = configGameBoard(gameBoard);
        gameWindow.setGameTable(resizedTable);
        // Set font size for score, time, lives panel
        setFontSize();
    }

    public JTable configGameBoard(JTable gameTable){

        gameTable.setRowHeight(imageSize); // Set the desired row height
        for (int i = 0; i < gameTable.getColumnCount(); i++){
            gameTable.getColumnModel().getColumn(i).setPreferredWidth(imageSize);
            gameTable.getColumnModel().getColumn(i).setMinWidth(imageSize);
            gameTable.getColumnModel().getColumn(i).setMaxWidth(imageSize);
        }
        gameTable.setShowGrid(false); // Remove grid lines
        gameTable.setTableHeader(null); // Remove column header
        gameTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto resizing
        gameTable.setIntercellSpacing(new Dimension(0, 0));
        gameTable.setBackground(Color.BLACK);
        gameTable.setCellSelectionEnabled(false); // Disable ability to select cells

        // Set up the custom cell renderer
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer(pacmanView, enemyView, imageSize);
        gameTable.setDefaultRenderer(Object.class, cellRenderer);

        return gameTable;
    }

    public void stopCharacterViewThreads(){
        pacmanView.setIsRunning(false);
        enemyView.setIsRunning(false);
        try {
            pacmanView.getViewThread().join();
            enemyView.getViewThread().join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public MenuWindow getMenuWindow(){
        return menu;
    }

    public void setFontSize() {
        int defaultImageSize = calculateImageSize(numberOfRows, numberOfColumns);
        double scaleFactor = (double) imageSize / defaultImageSize;
        int fontSize = (int) (12 * scaleFactor); // Changed for lower resolution
        gameWindow.setFontSize(fontSize);
    }

    public void showHighScoresWindow(Map<String, Integer> sortedHighScores){
        HighScoresWindow highScoresWindow = new HighScoresWindow(sortedHighScores);
    }

    public String showHighScoresInputWindow(){
        String nameForHighScore = "";
        while (nameForHighScore.isEmpty()) {
            try {
                InputDialog dialog = new InputDialog(null, "Enter a name under which you want to be saved in high scores:");
                dialog.setVisible(true);
                nameForHighScore = dialog.getInputString();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a name for a high scores tab.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return nameForHighScore;
    }

    public void closeGameWindow(){
        gameWindow.dispose();
    }
}
