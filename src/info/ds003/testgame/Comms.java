package info.ds003.testgame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Comms extends SwingWorker<List<Integer>, Integer> {

    private Game game;
    private JLabel lscore, lcoins;
    private JProgressBar plives;

    public Comms(Game game, JLabel lscore, JLabel lcoins, JProgressBar plives) {
        this.game = game;
        this.lscore = lscore;
        this.lcoins = lcoins;
        this.plives = plives;
    }

    @Override
    protected List<Integer> doInBackground() throws Exception {
        int score = game.getScore();
        int coins = game.getCoins();
        int lives = game.getLives();
        List<Integer> r = new ArrayList<Integer>();
        r.add(score);
        r.add(coins);
        r.add(lives);
        return r;
    }

    @Override
    protected void done() {
        try {
            List<Integer> r = get();
            int score = r.get(0);
            int coins = r.get(1);
            int lives = r.get(2);
            if (lives < 0)
                lives = 0;
            lscore.setText("Score: " + score);
            lcoins.setText("Coins: " + coins);
            plives.setValue(lives);
            plives.setString("Lives: " + lives);
        } catch (Exception e) {
        }
    }
}
