package View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import Enum.CellContent;
import Model.Pacman;

public class PacmanTableCellRenderer extends DefaultTableCellRenderer {

    private ImageIcon wallImage, playerImage, enemyImage, powerupImage, foodImage;

    private VPacman vPacman;

    public PacmanTableCellRenderer(VPacman vPacman) {
        // Load images
        wallImage = new ImageIcon("src/Images/wall-40.png");
        playerImage = new ImageIcon("src/Images/player-40.png");
        enemyImage = new ImageIcon("src/Images/enemy-40.png");
        powerupImage = new ImageIcon("src/Images/powerup-40.png");
        foodImage = new ImageIcon("src/Images/food-40.png");

        this.vPacman = vPacman;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        // Call the superclass method to set up the cell's default appearance
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Determine if the cell should display a wall image
        if (value == CellContent.WALL) {
            setIcon(wallImage);
        }
        else if (value == CellContent.PLAYER){
            setIcon(vPacman);
        }
        else if (value == CellContent.POWER_UP){
            setIcon(powerupImage);
        }
        else if (value == CellContent.ENEMY){
            setIcon(enemyImage);
        }
        else if (value == CellContent.FOOD){
            setIcon(foodImage);
        }
        else if (value == CellContent.EMPTY){
            setIcon(null);
            setText(value.toString());
        }
        else {
            setIcon(null);
            setText(value.toString()); // Use the default text display
        }
        setText(""); // Clear the text

        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        return this;
    }
}
