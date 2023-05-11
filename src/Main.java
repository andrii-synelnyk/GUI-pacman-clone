import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.A_GameModel;
import View.A_GameView;
import Controller.A_GameController;
import View.MenuWindow;

public class Main {

    public static void main(String[] args) {
        // Display the main menu window
        A_GameModel gameModel = new A_GameModel();
        A_GameView gameView = new A_GameView();
        A_GameController gameController = new A_GameController(gameModel, gameView);
    }

}