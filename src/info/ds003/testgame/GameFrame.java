package info.ds003.testgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameFrame extends JFrame implements KeyListener {

    private GamePanel panel = new GamePanel(this);
    private Game game;
    private JPanel top, bottom;
    private JLabel score, coins;
    private JProgressBar lives;
    private JButton btnShop;
    private ShopFrame sframe;
    private boolean gameOver = false;
    private final StartGameFrame mainFrame;

    public GameFrame(StartGameFrame mainframe, String name) {
        super(name);
        this.mainFrame = mainframe;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                mainFrame.makeVisible(true);
            }
        });

        setResizable(false);

        top = new JPanel();
        bottom = new JPanel();
        setLayout(new BorderLayout());
        top.setLayout(new GridLayout(1, 3));
        bottom.setLayout(new GridLayout(1, 1));

        coins = new JLabel("Coins: 0", SwingConstants.CENTER);
        score = new JLabel("Score: 0", SwingConstants.CENTER);
        btnShop = new JButton("Open shop!");
        btnShop.setFocusable(false);

        lives = new JProgressBar(0, 100);
        lives.setStringPainted(true);
        lives.setFont(new Font("Serif", Font.BOLD, 26));

        add(panel, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);

        coins.setFont(new Font("Serif", Font.BOLD, 26));
        score.setFont(new Font("Serif", Font.BOLD, 26));
        btnShop.setFont(new Font("Serif", Font.BOLD, 26));

        top.add(coins);
        top.add(score);
        top.add(btnShop);

        bottom.add(lives);

        btnShop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.pauseGame();
                sframe.update();
                sframe.setVisible(true);
            }

        });

        StartGame();
    }

    public void setGameOver() {
        btnShop.setEnabled(false);
        gameOver = true;
    }

    public void createSframe() {
        sframe = new ShopFrame(game);
        int x, y;
        x = (Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (200 / 2);
        y = (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (140 / 2);
        sframe.setLocation(x, y);
        sframe.setSize(200, 140);
        sframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        sframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                game.unpauseGame();
            }
        });
    }

    public void StartGame() {
        game = new Game(panel, score, coins, lives);
        createSframe();
        addKeyListener(this);
        ExecutorService threadExec = Executors.newCachedThreadPool();
        threadExec.execute(game);
        threadExec.shutdown();
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        char c = arg0.getKeyChar();
        if (arg0.getKeyCode() == KeyEvent.VK_UP)
            c = 'z';
        else if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
            c = 's';
        else if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
            c = 'q';
        else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
            c = 'd';

        if (!gameOver)
            game.input(c);
        else {
            mainFrame.makeVisible(true);
            this.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }
}
