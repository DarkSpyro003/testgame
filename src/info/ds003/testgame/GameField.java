package info.ds003.testgame;

public class GameField {
    public int r;
    public int c;
    public int aggrRadius;

    public GameField(int r, int c) {
        aggrRadius = 5;
        this.r = r;
        this.c = c;
    }

    public boolean isInField(int x, int y) {
        return (x >= 0 && y >= 0) && (x < r && y < c);
    }
}
