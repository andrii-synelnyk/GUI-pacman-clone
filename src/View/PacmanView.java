package View;

import java.awt.*;
import Enum.Direction;

public class PacmanView extends CharacterView {

    private int mouthDegree = 0;
    private boolean mouthFullyOpened = true;

    Direction pacmanDirection = Direction.UP;
    Thread pacmanViewThread;


    public PacmanView(int size) {
        super(size);

        pacmanViewThread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mouthDegree += mouthFullyOpened ? 5 : -5;
                if (mouthDegree == 0 || mouthDegree == 120) {
                    mouthFullyOpened = !mouthFullyOpened;
                }
            }
            System.out.println("quit pacman view");
        });
        pacmanViewThread.start();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, width, height, mouthDegree / 2 + pacmanDirection.getDirectionMultiplier(), 360 - mouthDegree);
    }

    public void setPacmanDirection(Direction pacmanDirection){
        this.pacmanDirection = pacmanDirection;
    }

    public Thread getViewThread(){
        return pacmanViewThread;
    }
}