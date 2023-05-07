import javax.swing.*;

import Model.A_GameModel;
import View.A_GameView;
import Controller.A_GameController;

public class Main {
    public static void main(String[] args) {
        // Prompt the user for the desired board size
        int rows = promptBoardSize("Enter the number of rows/columns (10 to 100):");
        int columns = rows;

        // Create the game model, view, and controller
        A_GameModel gameModel = new A_GameModel(rows, columns);
        A_GameView gameView = new A_GameView();
        A_GameController gameController = new A_GameController(gameModel, gameView);

    }

    // promptBoardSize() method remains the same
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