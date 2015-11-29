package info.ds003.testgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGameFrame extends JFrame {

    private JButton start;
    private static String name;

    public void makeVisible(boolean vis) {
        setVisible(vis);
    }

    public StartGameFrame(String name) {
        super(name);
        setResizable(false);
        this.name = name;
        start = new JButton("Start game");
        start.setFont(new Font("Serif", Font.BOLD, 18));

        add(start);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                int x, y;
                x = (Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (698 / 2);
                y = (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (806 / 2);
                GameFrame frame = new GameFrame(StartGameFrame.this, StartGameFrame.name);
                frame.setLocation(x, y);
                frame.setSize(698, 806);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                StartGameFrame.this.makeVisible(false);
            }
        });
    }
}
