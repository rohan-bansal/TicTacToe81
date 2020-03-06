package me.rohan.tictactoe.gameboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Mark extends Vector2 {

    private Color color;
    private float size;

    public Mark(float x, float y, Color color, float size) {
        super(x, y);

        this.color = color;
        this.size = size;
    }

    public Mark(float x, float y) {
        this(x, y, Color.BLACK, 5);
    }

    public Mark() {
        this(0, 0);
    }

    public Mark(Mark vec) {
        super(vec);
        this.color = vec.color;
        this.size = vec.size;
    }

    public Color getColor() {
        return color;
    }

    public Board.State getConvertedColor() {
        if(color == Color.CHARTREUSE) {
            return Board.State.G;
        } else if(color == Color.FIREBRICK) {
            return Board.State.R;
        }
        return null;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
