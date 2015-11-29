package info.ds003.testgame;

import java.awt.*;

public class Agent {
    private int x;
    private int y;
    private int aggrType;
    private char type;
    private GameField field;
    private int aggrRadius;
    private Agent aggrToAgent;
    private int stepsPerMove;
    private Color color;

    public char getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void setAggrToAgent(Agent aggrTo) {
        aggrToAgent = aggrTo;
    }

    public boolean isInAggrRadius() {
        int xdiff = aggrToAgent.x - x;
        int ydiff = aggrToAgent.y - y;
        int aggrRadiusNeg = aggrRadius - aggrRadius - aggrRadius;

        if (xdiff == 0 && ydiff == 0) // Player in exact same spot as enemy, ignore
            return false;

        return (xdiff <= aggrRadius && xdiff >= aggrRadiusNeg) && (ydiff <= aggrRadius && ydiff >= aggrRadiusNeg);
    }

    public boolean isHostileToPlayer() {
        return aggrType == 0;
    }

    public boolean isAtPositionOf(Agent compareTo) {
        int mx = compareTo.x;
        int my = compareTo.y;
        return isAtPosition(mx, my);
    }

    public boolean isAtPosition(int mx, int my) {
        return x == mx && y == my;
    }

    public boolean isAiMobile() {
        return aggrType == 0 || aggrType == 1;
    }

    public boolean makeMove() {
        switch (aggrType) {
            case 0:
                return makeMoveEnemy();
            case 1:
                return makeMoveNpc();
            default:
                return false;
        }
    }

    public boolean makeManualMove(int direction) {
        if (direction < 0 || direction > 3)
            return false;

        int tempX = x;
        int tempY = y;

        switch (direction) {
            case 0: // up
                tempY -= stepsPerMove;
                break;
            case 1: // down
                tempY += stepsPerMove;
                break;
            case 2: // left
                tempX -= stepsPerMove;
                break;
            case 3: // right
                tempX += stepsPerMove;
                break;
        }

        return tryDoMove(tempX, tempY);
    }

    private boolean makeMoveEnemy() {
        int chance = (int) (Math.random() * 5);
        if (isInAggrRadius() && chance != 0) {
            int tempX = x;
            int tempY = y;
            int xdiff = aggrToAgent.x - x;
            int ydiff = aggrToAgent.y - y;

            if (Math.abs(xdiff) >= Math.abs(ydiff)) {
                if (xdiff > 0)
                    tempX += stepsPerMove;
                else
                    tempX -= stepsPerMove;
            } else {
                if (ydiff > 0)
                    tempY += stepsPerMove;
                else
                    tempY -= stepsPerMove;
            }

            return tryDoMove(tempX, tempY);
        } else
            return randomMove();
    }

    private boolean makeMoveNpc() {
        return randomMove();
    }

    private boolean randomMove() {
        int direction = (int) (Math.random() * 4);

        int tempX = x;
        int tempY = y;

        switch (direction) {
            case 0: // up
                tempY -= stepsPerMove;
                break;
            case 1: // down
                tempY += stepsPerMove;
                break;
            case 2: // left
                tempX -= stepsPerMove;
                break;
            case 3: // right
                tempX += stepsPerMove;
                break;
        }

        return tryDoMove(tempX, tempY);
    }

    public boolean teleportAgent(int mx, int my) {
        return tryDoMove(mx, my);
    }

    private boolean tryDoMove(int mx, int my) {
        if (field.isInField(mx, my)) {
            x = mx;
            y = my;
            return true;
        }

        return false;
    }

    public Agent(int type, GameField field) {
        aggrType = type;
        this.field = field;
        aggrRadius = 0;

        switch (type) {
            case 0:
                type = 'E';
                aggrRadius = 5;
                color = Color.RED;
                break;
            case 1:
                type = 'N';
                color = Color.BLUE;
                break;
            case 2:
                type = 'O';
                color = Color.YELLOW;
                break;
            case 3:
                type = 'X';
                color = Color.GREEN;
                break;
        }

        x = (int) (Math.random() * field.r);
        y = (int) (Math.random() * field.c);

        aggrToAgent = null;
        stepsPerMove = 1;
    }
}