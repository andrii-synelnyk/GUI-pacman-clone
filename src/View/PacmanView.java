package View;

import Model.Pacman;

import javax.swing.*;
import java.awt.*;

public class PacmanView implements Icon {

    private int width;
    private int height;
    private int mouthOpened = 0;
    private boolean mouthFullyOpened = true;
    private boolean isRunning = true;

    private Pacman pacman;

    public PacmanView(int width, int height, Pacman pacman) {
        this.width = width;
        this.height = height;
        this.pacman = pacman;

        new Thread(() -> {
            while (pacman.getIsRunning()) {
                try {
                    Thread.sleep(10);
                    mouthOpened += mouthFullyOpened ? 10 : -10;
                    if (mouthOpened == 120 || mouthOpened == 0) {
                        mouthFullyOpened = !mouthFullyOpened;
                    }
                } catch (InterruptedException e) {
                    pacman.setIsRunning(false);
                }
            }
        }).start();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, width, height, mouthOpened / 2 + pacman.getDirection().getDirectionMultiplier(), 360 - mouthOpened);
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