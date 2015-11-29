package info.ds003.testgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopFrame extends JFrame {

    private JPanel panels[];
    private JButton buttons[];
    private final int costs[] = {300, 5100};
    private final int lives[] = {1, 20};
    private final String items[] = {"1 life - " + costs[0] + " coins", "20 lives - " + costs[1] + " coins"};
    private final int n = items.length;
    private JLabel lcoins;
    private int coins;
    private Game game;

    public ShopFrame(Game game) {
        super("Shop");
        setResizable(false);
        this.game = game;
        setLayout(new GridLayout(n + 1, 2));

        coins = game.getCoins();
        lcoins = new JLabel("Coins: " + coins, SwingConstants.CENTER);
        lcoins.setFont(new Font("Serif", Font.BOLD, 18));
        add(lcoins);

        panels = new JPanel[n];
        buttons = new JButton[n];

        for (int i = 0; i < n; i++) {
            panels[i] = new JPanel();
            buttons[i] = new JButton(items[i]);

            panels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
            add(panels[i]);
            panels[i].add(buttons[i]);

            buttons[i].addActionListener(new HandlePurchase());
        }
    }

    public void update() {
        coins = game.getCoins();
        lcoins.setText("Coins: " + coins);
        game.updateBoard();
    }

    private class HandlePurchase implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ev) {
            int s = -1;
            for (int i = 0; i < n; i++)
                if (ev.getSource() == buttons[i])
                    s = i;

            if (s >= 0 && s < n && coins >= costs[s]) {
                if (game.getLives() + lives[s] > 100)
                    JOptionPane.showMessageDialog(ShopFrame.this,
                            "You cannot buy these lives, the maximum amount of lives is 100.",
                            "Cannot purchase", JOptionPane.ERROR_MESSAGE);
                else {
                    game.buyLives(lives[s], costs[s]);
                    update();
                }
            } else
                JOptionPane.showMessageDialog(ShopFrame.this,
                        "You do not have enough coins for this purchase.",
                        "Cannot purchase", JOptionPane.ERROR_MESSAGE);
        }
    }
}
