package View;

import javax.swing.*;
import java.awt.*;

public class CharacterView implements Icon {

    protected int width;
    protected int height;

    public CharacterView(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {}

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

}
