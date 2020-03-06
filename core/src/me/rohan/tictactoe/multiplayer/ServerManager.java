package me.rohan.tictactoe.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import me.rohan.tictactoe.Main;
import me.rohan.tictactoe.gameboard.Board;
import me.rohan.tictactoe.gameboard.Mark;

import java.io.IOException;
import java.util.ArrayList;

public class ServerManager {

    private Server server;
    private Kryo kryo;

    public ServerManager() {

        server = new Server();
        kryo = server.getKryo();

        kryo.register(GameRequest.class);
        kryo.register(Mark.class);
        kryo.register(Board.class);
        kryo.register(Vector2.class);
        kryo.register(Board.State[][].class);
        kryo.register(Board.State[].class);
        kryo.register(Board.State.class);
        kryo.register(Color.class);
        kryo.register(Rectangle.class);
        kryo.register(ArrayList.class);

    }

    public void startServer() {
        try {
            server.start();
            server.bind(54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.addListeners();
    }

    // modify this code to have more than 2 players
    public void sendRequest(GameRequest request) {
        server.sendToTCP(server.getConnections()[0].getID(), request);
    }

    public void stopServer() {
        server.stop();
    }

    private void addListeners() {

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if(object instanceof GameRequest) {
                    Main.manager.processReceivedRequest((GameRequest) object);
                    Main.currentTurn = "server";
                }
            }
        });
    }

    public Server getServer() {
        return server;
    }
}
