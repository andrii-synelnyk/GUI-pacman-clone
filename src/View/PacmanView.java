package View;

import Model.Pacman;

import javax.swing.*;
import java.awt.*;
import Enum.Direction;

public class PacmanView extends CharacterView {


    private int mouthOpened = 0;
    private boolean mouthFullyOpened = true;

    Direction pacmanDirection = Direction.UP;
    Thread pacmanViewThread;


    public PacmanView(int size) {
        super(size);

        pacmanViewThread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mouthOpened += mouthFullyOpened ? 10 : -10;
                if (mouthOpened == 120 || mouthOpened == 0) {
                    mouthFullyOpened = !mouthFullyOpened;
                }
                //System.out.println("updated pacmanView");
            }
        });
        pacmanViewThread.start();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, width, height, mouthOpened / 2 + pacmanDirection.getDirectionMultiplier(), 360 - mouthOpened);
    }

    public void setPacmanDirection(Direction pacmanDirection){
        this.pacmanDirection = pacmanDirection;
    }

    public Thread getViewThread(){
        return pacmanViewThread;
    }
}