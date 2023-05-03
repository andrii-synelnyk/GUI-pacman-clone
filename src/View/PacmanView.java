package View;



import Model.Pacman;

import javax.swing.*;
import java.awt.*;

public class PacmanView implements Icon {

    private int width;
    private int height;

    private Pacman pacman;

    public PacmanView(int width, int height, Pacman pacman) {
        this.width = width;
        this.height = height;

        this.pacman = pacman;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, width, height, pacman.getMouthOpened()/2 + pacman.getDirection().getDirectionMultiplier(), 360 - pacman.getMouthOpened());
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