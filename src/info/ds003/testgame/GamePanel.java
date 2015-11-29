package info.ds003.testgame;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Agent agents[];
    private int pId, oId;
    private final int size = 17;
    private boolean gameOver = false;
    GameFrame gameframe;
    private int oid = 2, pid = 1;

    public void setGameOver() {
        gameOver = true;
        this.repaint();
        gameframe.setGameOver();
    }

    public GamePanel(GameFrame f) {
        this.setBackground(Color.WHITE);
        gameframe = f;
    }

    public void paint(Agent a[], int oid, int pid) {
        agents = a;
        this.pid = pid;
        this.oid = oid;
        this.repaint();
    }

    public void drawSquare(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        g.fillRect(x * size, y * size, size, size);
    }

    public void paintComponent(Graphics g) {
        if (!gameOver) {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, 800, 800);

            for (int i = 0; agents != null && i < agents.length; ++i)
                drawSquare(agents[i].getX(), agents[i].getY(), agents[i].getColor(), g);

            // make sure player & objective are always drawn on top
            drawSquare(agents[oid].getX(), agents[oid].getY(), agents[oid].getColor(), g);
            drawSquare(agents[pid].getX(), agents[pid].getY(), agents[pid].getColor(), g);

            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= 40; ++i) {
                g.drawLine(0, i * size, size * 40, i * size);
                g.drawLine(i * size, 0, i * size, size * 40);
            }
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 800);

            g.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 50));
            g.setColor(Color.GRAY);
            g.drawString("GAME OVER", 183, 328);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 185, 330);

            g.setFont(new Font("Serif", Font.PLAIN, 17));
            g.setColor(Color.WHITE);
            g.drawString("Press any key to continue", 240, 370);
        }
    }
}
