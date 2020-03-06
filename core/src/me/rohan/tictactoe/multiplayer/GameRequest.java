package me.rohan.tictactoe.multiplayer;

import me.rohan.tictactoe.gameboard.Board;
import me.rohan.tictactoe.gameboard.Mark;

public class GameRequest {

    private Mark plot;
    private Board board;

    public GameRequest() {}

    public GameRequest(Mark plot, Board board) {
        this.plot = plot;
        this.board = board;
    }

    public Mark getPlot() {
        return plot;
    }

    public Board getBoard() {
        return board;
    }
}