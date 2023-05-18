package View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import Enum.CellContent;
import View.CharacterViews.EnemyView;
import View.CharacterViews.PacmanView;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private ImageIcon wallImage, powerupImageSpeed, foodImage, powerupImageLife, powerupImageScore, powerupImageInvincibility, powerupImageFreeze;

    private PacmanView pacmanView;
    private EnemyView enemyView;

    public CustomTableCellRenderer(PacmanView pacmanView, EnemyView enemyView, int imageSize) {
        // Load and resize images
        wallImage = resizeImageIcon(new ImageIcon("src/Images/wall.png"), imageSize, imageSize);
        foodImage = resizeImageIcon(new ImageIcon("src/Images/food.png"), imageSize, imageSize);
        powerupImageSpeed = resizeImageIcon(new ImageIcon("src/Images/powerup-speed.png"), imageSize, imageSize);
        powerupImageLife = resizeImageIcon(new ImageIcon("src/Images/powerup-life.png"), imageSize, imageSize);
        powerupImageScore = resizeImageIcon(new ImageIcon("src/Images/powerup-score.png"), imageSize, imageSize);
        powerupImageInvincibility = resizeImageIcon(new ImageIcon("src/Images/powerup-invincibility.png"), imageSize, imageSize);
        powerupImageFreeze = resizeImageIcon(new ImageIcon("src/Images/powerup-freeze.png"), imageSize, imageSize);

        this.pacmanView = pacmanView;
        this.enemyView = enemyView;
    }

    private ImageIcon resizeImageIcon(ImageIcon originalImageIcon, int newWidth, int newHeight) {
        Image originalImage = originalImageIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        return new ImageIcon(resizedImage);
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
            setIcon(pacmanView);
        }
        else if (value == CellContent.ENEMY){
            setIcon(enemyView);
        }
        else if (value == CellContent.FOOD){
            setIcon(foodImage);
        }
        else if (value == CellContent.EMPTY){
            setIcon(null);
            //setText(value.toString());
        }
        else if (value == CellContent.POWER_UP_SPEED_INCREASE){
            setIcon(powerupImageSpeed);
        }
        else if (value == CellContent.POWER_UP_FREEZE_MONSTERS){
            setIcon(powerupImageFreeze);
        }
        else if (value == CellContent.POWER_UP_EXTRA_LIFE){
            setIcon(powerupImageLife);
        }
        else if (value == CellContent.POWER_UP_DOUBLE_SCORE){
            setIcon(powerupImageScore);
        }
        else if (value == CellContent.POWER_UP_INVINCIBLE){
            setIcon(powerupImageInvincibility);
        }
        else {
            setIcon(null);
            //setText(value.toString()); // Use the default text display
        }
        //setText(""); // Clear the text

        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        return this;
    }
}
