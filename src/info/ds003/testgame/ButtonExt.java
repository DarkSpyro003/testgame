package info.ds003.testgame;

import javax.swing.*;

public class ButtonExt extends JButton {

    public ButtonExt(String s) {
        super(s);
    }

    @Override
    // To prevent stealing focus from the main frame
    // Else it causes problems with the KeyListener
    public boolean isFocusTraversable() {
        return false;
    }
}