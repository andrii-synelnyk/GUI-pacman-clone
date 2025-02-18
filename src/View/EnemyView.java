package View;

import Model.Enemy;

import javax.swing.*;
import java.awt.*;

public class EnemyView implements Icon {
    private int width;
    private int height;
    private Color enemyColor;

    private boolean isRunning = true;
    private int animationFrame = 1;

    private Enemy enemy;

    public EnemyView(int width, int height, Enemy enemy) {
        this.width = width;
        this.height = height;

        this.enemyColor = Color.red;
        this.enemy = enemy;

        new Thread(() -> {
            while (enemy.getIsRunning()) {
                try {
                    Thread.sleep(280);
                    if (animationFrame == 1) animationFrame = 2;
                    else animationFrame = 1;
                } catch (InterruptedException e) {
                    enemy.setIsRunning(false);
                }
            }
        }).start();

    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(enemyColor);

        // Draw the round body of the ghost
        g.fillRoundRect(x, y, width, (int) (height * 0.85), width / 4, height / 4);

        // Draw the "legs" of the ghost
        if (animationFrame == 1) {
            for (int i = 0; i < 4; i++) {
                g.fillArc(x + i * width / 4, y + height / 2, width / 4, height / 2, 0, -180);
            }
        }else{
            for (int i = 0; i < 3; i++) {
                g.fillArc(x + i * width / 3, y + height / 2, width / 3, height / 2, 0, -180);
            }
        }

        // Draw the eyes of the ghost
        g.setColor(Color.WHITE);
        g.fillOval(x + width / 4, y + height / 4, width / 4, height / 4);
        g.fillOval(x + 2 * width / 4, y + height / 4, width / 4, height / 4);

        // Draw the pupils of the ghost
        g.setColor(Color.BLACK);
        g.fillOval(x + width * 3 / 8, y + height * 5 / 16, width / 8, height / 8);
        g.fillOval(x + width * 5 / 8, y + height * 5 / 16, width / 8, height / 8);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}