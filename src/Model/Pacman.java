package Model;

import java.awt.Point;

public class Pacman {
    private int mouthOpened = 0;
    private boolean mouthFullyOpened = true;

    private boolean isRunning = true;

    public Pacman() {

        new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(10);
                    mouthOpened += mouthFullyOpened ? 10 : -10;
                    if (mouthOpened == 120 || mouthOpened == 0) {
                        mouthFullyOpened = !mouthFullyOpened;
                    }
                } catch (InterruptedException e) {
                    isRunning = false;
                }
            }
        }).start();

    }

    public int getMouthOpened() {
        return mouthOpened;
    }
}