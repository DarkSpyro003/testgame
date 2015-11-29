package info.ds003.testgame;

import javax.swing.*;
import java.awt.*;

public class StartGame {

    private static final String name = "Treasure Collector";

    public static void main(String[] args) {
        StartGameFrame frame = new StartGameFrame(name);
        int x, y;
        x = (Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (300 / 2);
        y = (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (120 / 2);
        frame.setLocation(x, y);
        frame.setSize(300, 120);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
