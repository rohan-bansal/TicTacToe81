package me.rohan.tictactoe.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.rohan.tictactoe.Main;
import me.rohan.tictactoe.gameboard.Board;
import me.rohan.tictactoe.gameboard.Mark;

import java.io.IOException;
import java.util.ArrayList;

public class ClientManager {

    private Client client;
    private Kryo kryo;

    public ClientManager() {

        client = new Client();
        kryo = client.getKryo();

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

    public void connect(String IPaddress) {

        try {

            client.start();
            client.connect(5000, IPaddress, 54555, 54777);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.addListeners();
    }

    public void sendRequest(GameRequest request) {
        client.sendTCP(request);
    }

    public void closeConnection() {
        client.close();
        client.stop();
    }

    private void addListeners() {

        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if(object instanceof GameRequest) {
                    Main.manager.processReceivedRequest((GameRequest) object);
                    Main.currentTurn = "client";
                }
            }
        });
    }

    public Client getClient() {
        return client;
    }
}
