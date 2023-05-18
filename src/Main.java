import Model.GameModel;
import View.GameView;
import Controller.GameController;

public class Main {

    public static void main(String[] args) {
        // Display the main menu window
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();
        GameController gameController = new GameController(gameModel, gameView);
    }

}