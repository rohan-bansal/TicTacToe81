package me.rohan.tictactoe.gameboard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.rohan.tictactoe.Main;
import me.rohan.tictactoe.multiplayer.GameRequest;

import java.util.ArrayList;

public class BoardManager {

    OrthographicCamera camera;

    private Board bigBoard;
    private ShapeRenderer renderer;
    private ArrayList<Board> secondBoard = new ArrayList<>();
    private ArrayList<Board> thirdBoard = new ArrayList<>();


    private Vector2 lerpPos = null;
    private boolean chooseAny = false;


    public BoardManager(OrthographicCamera cam) {
        this.camera = cam;
        renderer = new ShapeRenderer();
    }

    public void initBoards() {
        bigBoard = new Board(new Vector2(50, 50), new Vector2(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50), 15, Color.GOLDENROD);

        for(Rectangle rect : bigBoard.getAllBoxes()) {
            Board nextBoard = new Board(rect, 15, Color.DARK_GRAY);
            secondBoard.add(nextBoard);
        }

        for(Board b : secondBoard) {
            for(Rectangle rect : b.getAllBoxes()) {
                Board nextBoard = new Board(rect, 0, Color.LIGHT_GRAY);
                thirdBoard.add(nextBoard);
            }
        }

    }

    public void render() {

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        if(bigBoard.isBoardFinished()) {
            Gdx.app.log("winner winner chicken dinner", bigBoard.getWinner());
        }

        for(Board t : thirdBoard) {
            if(!Main.third) t.render(renderer);
        }

        for(Board b : secondBoard) {
            if(!Main.second) b.render(renderer);
        }

        if(!Main.first) bigBoard.render(renderer);

        if(!isLerping()) {
            Rectangle r = bigBoard.getAllBoxes()[getSuperBoardIndex()];
            renderer.setColor(Color.SALMON);
            if(!Main.first && !Main.second && !Main.third) {
                if(chooseAny) {
                    r = bigBoard.rectangle;
                    renderer.rectLine(new Vector2(r.getX(), r.getY()), new Vector2(r.getX() + r.getWidth(), r.getY()), 5);
                    renderer.rectLine(new Vector2(r.getX(), r.getY()), new Vector2(r.getX(), r.getY() + r.getHeight()), 5);
                    renderer.rectLine(new Vector2(r.getX() + r.getWidth(), r.getY() + r.getHeight()), new Vector2(r.getX() + r.getWidth(), r.getY()), 5);
                    renderer.rectLine(new Vector2(r.getX(), r.getY() + r.getHeight()), new Vector2(r.getX() + r.getWidth(), r.getY() + r.getHeight()), 5);
                } else {
                    if(!Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                        renderer.rectLine(new Vector2(r.getX(), r.getY()), new Vector2(r.getX() + r.getWidth(), r.getY()), 5);
                        renderer.rectLine(new Vector2(r.getX(), r.getY()), new Vector2(r.getX(), r.getY() + r.getHeight()), 5);
                        renderer.rectLine(new Vector2(r.getX() + r.getWidth(), r.getY() + r.getHeight()), new Vector2(r.getX() + r.getWidth(), r.getY()), 5);
                        renderer.rectLine(new Vector2(r.getX(), r.getY() + r.getHeight()), new Vector2(r.getX() + r.getWidth(), r.getY() + r.getHeight()), 5);
                    }

                }
            }

        }


        renderer.end();

        if(lerpPos != null) {
            Vector3 camPos = camera.position;
            if(camPos.dst(new Vector3(lerpPos.x, lerpPos.y, 0)) < 1) {
                camera.position.set(lerpPos.x, lerpPos.y, 0);
                lerpPos = null;
            } else {
                camPos.lerp(new Vector3(lerpPos.x, lerpPos.y, 0), 0.1f);
                camera.position.set(camPos);
            }
            camera.update();
        }
    }

    private int getSuperBoardIndex() {
        int currentSuperBoardIndex = 0;
        for(int y = 0; y < bigBoard.getAllBoxes().length; y++) {
            if(bigBoard.getAllBoxes()[y].getX() + bigBoard.getAllBoxes()[y].getWidth() / 2 == camera.position.x && bigBoard.getAllBoxes()[y].getY() + bigBoard.getAllBoxes()[y].getHeight() / 2 == camera.position.y) {
                currentSuperBoardIndex = y;
            }
        }

        return currentSuperBoardIndex;
    }

    private void processConnection(Board b, Mark plot) {
        plot.setColor(Main.playerColor);
        if(Main.multType == Main.ServerType.CLIENT) {
            Main.currentTurn = "server";
            Main.client.sendRequest(new GameRequest(plot, b));
        } else if(Main.multType == Main.ServerType.SERVER) {
            Main.currentTurn = "client";
            Main.server.sendRequest(new GameRequest(plot, b));
        }
    }

    public void detectClicks() {
        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(vec);

         if(secondBoard.get(this.getSuperBoardIndex()).isBoardFinished()) {
             chooseAny = true;
             for(Board b : thirdBoard) {
                 for(int x = 0; x < b.getAllBoxes().length; x++) {
                     if(b.getAllBoxes()[x].contains(vec.x, vec.y) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                         Mark m = new Mark(b.getAllBoxes()[x].getX() + b.getAllBoxes()[x].getWidth() / 2, b.getAllBoxes()[x].getY() + b.getAllBoxes()[x].getHeight() / 2, Main.playerColor, 5);

                         if(!b.checkDuplicateMark(m) && !b.isBoardFinished()) {
                             lerpPos = new Vector2(bigBoard.getAllBoxes()[x].getX() + bigBoard.getAllBoxes()[x].getWidth() / 2, bigBoard.getAllBoxes()[x].getY() + bigBoard.getAllBoxes()[x].getHeight() / 2);
                             b.addMark(m);
                             if(Main.multType == Main.ServerType.CLIENT) {
                                 this.processMove(b, x, Board.State.G, Color.CHARTREUSE);
                             } else {
                                 this.processMove(b, x, Board.State.R, Color.FIREBRICK);
                             }
                             this.processConnection(b, m);
                             chooseAny = false;
                         }
                     }
                 }
             }
         } else {
             for(Board b : thirdBoard) {
                 for(int x = 0; x < b.getAllBoxes().length; x++) {
                     if(b.getAllBoxes()[x].contains(vec.x, vec.y) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                         if(bigBoard.getAllBoxes()[this.getSuperBoardIndex()].contains(vec.x, vec.y)) {
                             Mark m = new Mark(b.getAllBoxes()[x].getX() + b.getAllBoxes()[x].getWidth() / 2, b.getAllBoxes()[x].getY() + b.getAllBoxes()[x].getHeight() / 2, Main.playerColor, 5);

                             if(!b.checkDuplicateMark(m) && !b.isBoardFinished()) {
                                 lerpPos = new Vector2(bigBoard.getAllBoxes()[x].getX() + bigBoard.getAllBoxes()[x].getWidth() / 2, bigBoard.getAllBoxes()[x].getY() + bigBoard.getAllBoxes()[x].getHeight() / 2);
                                 b.addMark(m);
                                 if(Main.multType == Main.ServerType.CLIENT) {
                                     this.processMove(b, x, Board.State.G, Color.CHARTREUSE);
                                 } else {
                                     this.processMove(b, x, Board.State.R, Color.FIREBRICK);
                                 }
                                 this.processConnection(b, m);
                             }
                         }
                     }
                 }
             }
         }
    }

    public boolean isLerping() {
        return lerpPos != null;
    }

    private void processMove(Board b, int boxIndex, Board.State state, Color color) {
        if(boxIndex <= 2) {
            b.move(0, boxIndex, state, color);
        } else if(boxIndex <= 5) {
            b.move(1, boxIndex - 3, state, color);
        } else {
            b.move(2, boxIndex - 6, state, color);
        }
    }

    public void processBoardWin(Board b, Board.State s, Color color) {
        for(Board board : thirdBoard) {
            if(board == b) {
                for(int rect = 0; rect < secondBoard.get(getSuperBoardIndex()).getAllBoxes().length; rect++) {
                    if(secondBoard.get(getSuperBoardIndex()).getAllBoxes()[rect].getX() + secondBoard.get(getSuperBoardIndex()).getAllBoxes()[rect].getWidth() / 2 == b.getAllBoxes()[4].getX() + b.getAllBoxes()[4].getWidth() / 2 && secondBoard.get(getSuperBoardIndex()).getAllBoxes()[rect].getY() + secondBoard.get(getSuperBoardIndex()).getAllBoxes()[rect].getHeight() / 2 == b.getAllBoxes()[4].getY() + b.getAllBoxes()[4].getHeight() / 2) {
                        secondBoard.get(rect).addMark(new Mark(b.rectangle.getX() + b.rectangle.getWidth() / 2, b.rectangle.getY() + b.rectangle.getHeight() / 2, color, 30));
                        b.setBoardFinished(true);
                        Gdx.app.log("SecondBoard Mark Added", "");
                        processMove(secondBoard.get(getSuperBoardIndex()), rect, s, color);
                        return;
                    }
                }
            }
        }

        for(int rect = 0; rect < bigBoard.getAllBoxes().length; rect++) {
            if(bigBoard.getAllBoxes()[rect].getX() + bigBoard.getAllBoxes()[rect].getWidth() / 2 == b.getAllBoxes()[4].getX() + b.getAllBoxes()[4].getWidth() / 2 && bigBoard.getAllBoxes()[rect].getY() + bigBoard.getAllBoxes()[rect].getHeight() / 2 == b.getAllBoxes()[4].getY() + b.getAllBoxes()[4].getHeight() / 2) {
                bigBoard.addMark(new Mark(b.rectangle.getX() + b.rectangle.getWidth() / 2, b.rectangle.getY() + b.rectangle.getHeight() / 2, color, 130));
                b.setBoardFinished(true);
                for(int x = 0; x < thirdBoard.size(); x++) {
                    for(Rectangle r : thirdBoard.get(x).getAllBoxes()) {
                        if(bigBoard.getAllBoxes()[rect].contains(r)) {
                            thirdBoard.get(x).setBoardFinished(true);
                        }
                    }
                }
                Gdx.app.log("FirstBoard Mark Added", "");
                processMove(bigBoard, rect, s, color);
                return;
            }
        }
    }

    public void setLerpPos(Vector2 lerpPos) {
        this.lerpPos = lerpPos;
    }

    public void processReceivedRequest(GameRequest object) {

        for(Board b : thirdBoard) {
            if(b.rectangle.getX() == object.getBoard().rectangle.getX() && b.rectangle.getY() == object.getBoard().rectangle.getY()) {

                if(!b.checkDuplicateMark(object.getPlot()) && !b.isBoardFinished()) {
                    b.addMark(object.getPlot());
                    lerpPos = new Vector2(bigBoard.getAllBoxes()[b.getLastMarkIndex()].getX() + bigBoard.getAllBoxes()[b.getLastMarkIndex()].getWidth() / 2,
                            bigBoard.getAllBoxes()[b.getLastMarkIndex()].getY() + bigBoard.getAllBoxes()[b.getLastMarkIndex()].getHeight() / 2);
                    processMove(b, b.getLastMarkIndex(), object.getPlot().getConvertedColor(), object.getPlot().getColor());
                }
            }
        }
    }
}