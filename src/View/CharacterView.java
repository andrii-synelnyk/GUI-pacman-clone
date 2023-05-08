package View;

import javax.swing.*;
import java.awt.*;

public class CharacterView implements Icon {

    protected int width;
    protected int height;

    public CharacterView(int size){
        this.width = size;
        this.height = size;
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

    public void setNewImageSize(int size){
        this.width = size;
        this.height = size;
    }

}
