package me.rohan.tictactoe.gameboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.rohan.tictactoe.Main;

import java.util.ArrayList;

public class Board {

    public Rectangle rectangle;
    private int marginSpacing;
    private Color color;

    private int lastIndex;

    private ArrayList<Mark> marks = new ArrayList<>();

    public enum State{Blank, R, G};

    int n = 3;
    State[][] board = new State[n][n];
    int moveCount;

    private boolean boardFinished = false;
    private String winner = "";

    public Board() {}

    public Board(Vector2 botCorner, Vector2 topCorner, int marginSpacing, Color color) {

        float xRange = topCorner.x - botCorner.x;
        float yRange = topCorner.y - botCorner.y;
        this.marginSpacing = marginSpacing;
        this.color = color;

        this.rectangle = new Rectangle(botCorner.x, botCorner.y, xRange, yRange);

        for(int r = 0; r < n; r++) {
            for(int g = 0; g < n; g++) {
                board[r][g] = State.Blank;
            }
        }

    }

    public int getLastMarkIndex() {
        return lastIndex;
    }

    public boolean isBoardFinished() {
        return boardFinished;
    }

    public void setBoardFinished(boolean boardFinished) {
        this.boardFinished = boardFinished;
    }

    public Board(Rectangle rect, int marginSpacing, Color color) {

        this(new Vector2(rect.x, rect.y), new Vector2(rect.x + rect.width, rect.y + rect.height), marginSpacing, color);

    }

    public void render(ShapeRenderer renderer) {

        renderer.setColor(color);

        //borders
        renderer.rectLine(this.rectangle.getX(), this.rectangle.getY(), this.rectangle.getX(), this.rectangle.getY() + this.rectangle.getHeight(), 5);
        renderer.rectLine(this.rectangle.getX() + this.rectangle.getWidth(), this.rectangle.getY(), this.rectangle.getX() + this.rectangle.getWidth(), this.rectangle.getY() + this.rectangle.getHeight(), 5);
        renderer.rectLine(this.rectangle.getX(), this.rectangle.getY(), this.rectangle.getX() + this.rectangle.getWidth(), this.rectangle.getY(), 5);
        renderer.rectLine(this.rectangle.getX(), this.rectangle.getY() + this.rectangle.getHeight(), this.rectangle.getX() + this.rectangle.getWidth(), this.rectangle.getY() + this.rectangle.getHeight(), 5);

        //horizontal
        renderer.rectLine(this.rectangle.getX() + this.rectangle.getWidth() / 3, this.rectangle.getY(), this.rectangle.getX() + this.rectangle.getWidth() / 3, this.rectangle.getY() + this.rectangle.getHeight(), 5);
        renderer.rectLine(this.rectangle.getX() + this.rectangle.getWidth() / 3 * 2, this.rectangle.getY(), this.rectangle.getX() + this.rectangle.getWidth() / 3 * 2, this.rectangle.getY() + this.rectangle.getHeight(), 5);

        //vertical
        renderer.rectLine(this.rectangle.getX(), this.rectangle.getY() + this.rectangle.getHeight() / 3, this.rectangle.getX() + this.rectangle.getHeight(), this.rectangle.getY() + this.rectangle.getHeight() / 3, 5);
        renderer.rectLine(this.rectangle.getX(), this.rectangle.getY() + this.rectangle.getHeight() / 3 * 2, this.rectangle.getX() + this.rectangle.getHeight(), this.rectangle.getY() + this.rectangle.getHeight() / 3 * 2, 5);

        for(int x = 0; x < marks.size(); x++) {
            renderer.setColor(marks.get(x).getColor());
            renderer.circle(marks.get(x).x, marks.get(x).y, marks.get(x).getSize());
        }

    }

    public Rectangle[] getAllBoxes() {
        Rectangle[] rects = new Rectangle[9];

        rects[0] = new Rectangle(marginSpacing + this.rectangle.getX(), marginSpacing + this.rectangle.getY(),
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[1] = new Rectangle(marginSpacing + this.rectangle.getX() + this.rectangle.getWidth() / 3, marginSpacing + this.rectangle.getY(),
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[2] = new Rectangle(marginSpacing + this.rectangle.getX() + this.rectangle.getWidth() / 3 * 2, marginSpacing + this.rectangle.getY(),
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[3] = new Rectangle(marginSpacing + this.rectangle.getX(), marginSpacing + this.rectangle.getY() + this.rectangle.getHeight() / 3,
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[4] = new Rectangle(marginSpacing + this.rectangle.getX() + this.rectangle.getWidth() / 3, marginSpacing + this.rectangle.getY() + this.rectangle.getHeight() / 3,
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[5] = new Rectangle(marginSpacing + this.rectangle.getX() + this.rectangle.getWidth() / 3 * 2, marginSpacing + this.rectangle.getY() + this.rectangle.getHeight() / 3,
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[6] = new Rectangle(marginSpacing + this.rectangle.getX(), marginSpacing + this.rectangle.getY() + this.rectangle.getHeight() / 3 * 2,
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[7] = new Rectangle(marginSpacing + this.rectangle.getX() + this.rectangle.getWidth() / 3, marginSpacing + this.rectangle.getY() + this.rectangle.getHeight() / 3 * 2,
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        rects[8] = new Rectangle(marginSpacing + this.rectangle.getX() + this.rectangle.getWidth() / 3 * 2, marginSpacing + this.rectangle.getY() + this.rectangle.getHeight() / 3 * 2,
                this.rectangle.getWidth() / 3 - 2 * marginSpacing, this.rectangle.getHeight() / 3 - 2 * marginSpacing);

        return rects;
    }

    public Color processStateColor(State s) {
        if(s.equals(State.R)) {
            return Color.FIREBRICK;
        } else if(s.equals(State.G)) {
            return Color.CHARTREUSE;
        }
        return null;
    }

    public void move(int x, int y, State s, Color color) {
        if(board[x][y] == State.Blank) {
            board[x][y] = s;
        }
        moveCount++;

        //check col
        for(int i = 0; i < n; i++) {
            if(board[x][i] != s)
                break;
            if(i == n-1) {
                Main.manager.processBoardWin(this, s, color);
                winner = s + "";
            }
        }
        //check row
        for(int i = 0; i < n; i++) {
            if(board[i][y] != s)
                break;
            if(i == n-1) {
                Main.manager.processBoardWin(this, s, color);
                winner = s + "";
            }
        }
        //check diag
        if(x == y) {
            for(int i = 0; i < n; i++) {
                if(board[i][i] != s)
                    break;
                if(i == n-1) {
                    Main.manager.processBoardWin(this, s, color);
                    winner = s + "";
                }
            }
        }
        //check anti diag
        if(x + y == n - 1) {
            for(int i = 0; i < n; i++) {
                if(board[i][(n-1)-i] != s)
                    break;
                if(i == n-1) {
                    //this.addMark(new Mark(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2, processStateColor(s), 30));
                    Main.manager.processBoardWin(this, s, color);
                    winner = s + "";
                }
            }
        }
        //check draw
        if(moveCount == (Math.pow(n, 2) - 1)) {
            this.addMark(new Mark(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2, Color.DARK_GRAY, 30));
            setBoardFinished(true);
        }
    }

    public void addMark(Mark vec) {
        this.marks.add(vec); // creating new mark changes color - TODO fix this problem, fix all problems?

        for(int x = 0; x < getAllBoxes().length; x++) {
            if(getAllBoxes()[x].contains(vec.x, vec.y)) {
                lastIndex = x;
            }
        }
    }

    public boolean checkDuplicateMark(Mark vec) {
        for(Mark mark : marks) {
            if(mark.x == vec.x && mark.y == vec.y) {
                return true;
            }
        }
        return false;
    }

    public String getWinner() {
        return winner;
    }
}
