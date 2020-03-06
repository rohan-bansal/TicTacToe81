package me.rohan.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import me.rohan.tictactoe.customlibraries.InputCore;

public class Overlay {

    private OrthographicCamera camera;
    private InputCore inputListener;

    private Sprite serverIcon, clientIcon, ticTacToe, player2;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();

    public static boolean zoomLerp = false;
    private boolean zoomLerp2 = false;

    public Overlay(OrthographicCamera camera) {
        this.camera = camera;

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/ari2.fnt"));
        font.setColor(Color.GOLDENROD);


        serverIcon = new Sprite(new Texture("server.png"));
        serverIcon.setCenter(Gdx.graphics.getWidth() / 2 - 60, Gdx.graphics.getHeight() / 2);

        clientIcon = new Sprite(new Texture("client.png"));
        clientIcon.setCenter(Gdx.graphics.getWidth() / 2 + 60, Gdx.graphics.getHeight() / 2);

        ticTacToe = new Sprite(new Texture("tictactoe.png"));
        ticTacToe.setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 200);

        player2 = new Sprite(new Texture("player2.png"));
        player2.setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        inputListener = new InputCore(Main.client);
    }

    public static void showBoard() {
        Main.first = false;
        Main.second = false;
        Main.third = false;
    }

    public void render() {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if(Main.first && Main.second && Main.third) {
            serverIcon.draw(batch);
            clientIcon.draw(batch);
            ticTacToe.draw(batch);

            if(serverIcon.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                layout.setText(font, "Host Game");
                font.draw(batch, "Host Game", serverIcon.getX() + (serverIcon.getWidth() / 2) - layout.width / 2, serverIcon.getY() - 15);
                if(Gdx.input.justTouched()) {
                    Main.server.startServer();
                    Main.multType = Main.ServerType.SERVER;
                    Main.playerColor = Color.FIREBRICK;
                    zoomLerp = true;
                    Main.toggleBoardClickable(false);
                }
            } else if(clientIcon.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                layout.setText(font, "Join Game");
                font.draw(batch, "Join Game", clientIcon.getX() + (clientIcon.getWidth() / 2) - layout.width / 2, clientIcon.getY() - 15);
                if(Gdx.input.justTouched()) {
                    Gdx.input.getTextInput(inputListener, "Server", "", "IP Address");
                    Main.multType = Main.ServerType.CLIENT;
                    Main.playerColor = Color.CHARTREUSE;
                }
            }
        }

        if(Main.multType == Main.ServerType.SERVER) {
            if(Main.server.getServer().getConnections().length == 0) {
                player2.draw(batch);
            } else {
                Main.toggleBoardClickable(true);
                Main.mainScreenDone = true;
            }
        }

        if(zoomLerp) {
            float zoom = camera.zoom;
            if(zoomLerp2) {
                if(camera.zoom >= 0.95f) {
                    camera.zoom = 1;
                    zoomLerp2 = false;
                    zoomLerp = false;
                } else {
                    camera.zoom = MathUtils.lerp(zoom, 1f, 0.1f);
                }
            } else {
                if(camera.zoom <= 0.02f) {
                    camera.zoom = 0.01f;
                    zoomLerp2 = true;
                    showBoard();
                } else {
                    camera.zoom = MathUtils.lerp(zoom, 0.01f, 0.1f);
                }
            }

        }

        batch.end();

    }
}
