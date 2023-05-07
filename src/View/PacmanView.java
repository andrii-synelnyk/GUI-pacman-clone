package View;

import Model.Pacman;

import javax.swing.*;
import java.awt.*;
import Enum.Direction;

public class PacmanView extends CharacterView {


    private int mouthOpened = 0;
    private boolean mouthFullyOpened = true;

    Direction pacmanDirection = Direction.UP;


    public PacmanView(int width, int height) {
        super(width, height);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        mouthOpened += mouthFullyOpened ? 10 : -10;
        if (mouthOpened == 120 || mouthOpened == 0) {
            mouthFullyOpened = !mouthFullyOpened;
        }

        g.setColor(Color.YELLOW);
        g.fillArc(x, y, width, height, mouthOpened / 2 + pacmanDirection.getDirectionMultiplier(), 360 - mouthOpened);
    }

    public void setPacmanDirection(Direction pacmanDirection){
        this.pacmanDirection = pacmanDirection;
    }

}