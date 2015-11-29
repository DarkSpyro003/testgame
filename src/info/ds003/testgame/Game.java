package info.ds003.testgame;

import javax.swing.*;

public class Game implements Runnable {
    private GamePanel panel;
    private int numNpc;
    private int numEnem;
    private char keys[] = {'z', 's', 'q', 'd'};
    private int lives;
    private Agent agents[];
    private GameField field;
    private int objectiveAgentId;
    private int playerAgentId;
    private int r, c;
    private int coins;
    private int rewardMulti;
    private int score;
    private static final boolean debug = true;
    private int maxLives;
    private JLabel lscore, lcoins;
    private JProgressBar plives;
    private boolean gamePaused = false;

    public void buyLives(int lives, int cost) {
        this.lives += lives;
        if (lives > 100)
            lives = 100;
        coins -= cost;
        dbgMsg("Bought " + lives + " lives for " + coins + " coins.");
    }

    public void unpauseGame() {
        gamePaused = false;
    }

    public void pauseGame() {
        gamePaused = true;
    }

    public int getLives() {
        return lives;
    }

    public int getCoins() {
        return coins;
    }

    public int getScore() {
        return score;
    }

    private static void printString(String text, int startX) {
        System.out.print(text + "\r\n");
    }

    private void dbgMsg(String text) {
        if (debug)
            printString("DEBUG: " + text, 0);
    }

    public Game(GamePanel panel, JLabel lscore, JLabel lcoins, JProgressBar plives) {
        this.plives = plives;
        this.lscore = lscore;
        this.lcoins = lcoins;
        this.panel = panel;

        maxLives = 100;

        r = 40;
        c = 40;
        coins = 0;
        score = 0;
        rewardMulti = 1;

        numNpc = 1;
        numEnem = 1;

        // Player lives
        lives = 10;

        // Player + amount of npcs + amount of enemies + objective
        agents = new Agent[1 + numNpc + numEnem + 1];

        field = new GameField(r, c);

        for (int i = 0; i < agents.length; ++i)
            if (i == 0) // Player
            {
                playerAgentId = i;
                agents[i] = new Agent(3, field);
            } else if (i == 1) // Objective
            {
                agents[i] = new Agent(2, field);
                objectiveAgentId = i;
            } else if (i > 1 && i < numNpc + 2)
                agents[i] = new Agent(1, field); // Neutral NPC
            else {
                agents[i] = new Agent(0, field); // Enemy
                agents[i].setAggrToAgent(agents[playerAgentId]);
            }

        dbgMsg("Initialization complete.");

        drawBoard(); // Draw the playing board

        dbgMsg("Starting game.");
    }

    public void updateBoard() {
        if (gamePaused)
            drawBoard();
    }

    public void run() {
        while (isPlayerAlive()) {
            while (gamePaused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }

            if (lives > maxLives)
                lives = maxLives;

            if (lives < 0)
                lives = 0;

            // Loop through all by computer movable agents
            for (int i = 0; i < agents.length; ++i)
                if (agents[i].isAiMobile())
                    agents[i].makeMove();

            checkBoardConditions();
            drawBoard();

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                dbgMsg("InterruptedException on thread");
            }
        }
        if (lives < 0)
            lives = 0;

        panel.setGameOver();
        dbgMsg("Player has lost.");
    }

    public void input(char move) {
        if (isPlayerAlive() && !gamePaused) {
            int direction = -1;
            for (int i = 0; i < keys.length && direction < 0; ++i)
                if (move == keys[i])
                    direction = i;

            if (direction != -1) {
                agents[playerAgentId].makeManualMove(direction);

                checkBoardConditions();
                drawBoard();
            }
        }
    }

    private void checkBoardConditions() {
        // Check if a neutral NPC runs into an enemy
        for (int i = 0; i < agents.length; ++i)
            for (int j = 0; j < agents.length; ++j)
                if (i != j)
                    if (agents[i].isHostileToPlayer() && agents[i].isAtPositionOf(agents[j]) &&
                            agents[j].getType() == 'N') {
                        agents[i] = null;
                        agents[i] = new Agent(0, field);
                        agents[i].setAggrToAgent(agents[playerAgentId]);
                        respawnNpc(j);
                    }

        // Check if the player is to be harmed by one or more enemies
        for (int i = 0; i < agents.length; ++i)
            if (agents[i].isHostileToPlayer() && agents[playerAgentId].isAtPositionOf(agents[i])) {
                lives -= 1;
                dbgMsg("Player lost a live");
                agents[i] = null;
                agents[i] = new Agent(0, field);
                agents[i].setAggrToAgent(agents[playerAgentId]);
            }

        // Check if player reached the objective and survived
        if (isPlayerAtObjective() && isPlayerAlive())
            completeObjective();
    }

    private void respawnNpc(int agentId) {
        agents[agentId] = null;
        agents[agentId] = new Agent(1, field);

        // Respawning an npc also adds a new enemy
        addAgent(0);
    }

    private void addAgent(int type) {
        Agent newAgents[] = new Agent[agents.length + 1];
        for (int i = 0; i < agents.length; ++i)
            newAgents[i] = agents[i];

        newAgents[newAgents.length - 1] = new Agent(type, field);
        if (type == 0) // Adding an enemy
            newAgents[newAgents.length - 1].setAggrToAgent(newAgents[playerAgentId]);

        agents = new Agent[newAgents.length];
        for (int i = 0; i < newAgents.length; ++i)
            agents[i] = newAgents[i];
    }

    private void completeObjective() {
        dbgMsg("Objective completed");
        agents[objectiveAgentId] = null;
        agents[objectiveAgentId] = new Agent(2, field);
        ++score;
        coins += rewardMulti;
        if (rewardMulti < 75)
            rewardMulti *= 2;
        else if (rewardMulti < 750)
            rewardMulti *= 1.1;
        else if (rewardMulti < 7500)
            rewardMulti *= 1.05;
        else
            rewardMulti *= 1.01;

        addAgent(0);
        dbgMsg("Score now " + score);
    }

    private boolean isPlayerAtObjective() {
        return agents[playerAgentId].isAtPositionOf(agents[objectiveAgentId]);
    }

    private void drawBoard() {
        panel.paint(agents, objectiveAgentId, playerAgentId);

        Comms comm = new Comms(this, lscore, lcoins, plives);
        comm.execute();
    }

    private boolean isPlayerAlive() {
        return lives > 0;
    }
}