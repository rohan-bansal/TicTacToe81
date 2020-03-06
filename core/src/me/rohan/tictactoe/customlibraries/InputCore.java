package me.rohan.tictactoe.customlibraries;

import com.badlogic.gdx.Input;
import me.rohan.tictactoe.Main;
import me.rohan.tictactoe.Overlay;
import me.rohan.tictactoe.multiplayer.ClientManager;

public class InputCore implements Input.TextInputListener {

    private ClientManager client;

    public InputCore(ClientManager client) {
        this.client = client;
    }

    @Override
    public void input(String text) {
        if(!text.equals("")) {
            try {
                this.client.connect(text);
                //Overlay.showBoard();
                Main.toggleBoardClickable(true);
                Main.mainScreenDone = true;
                Overlay.zoomLerp = true;
            } catch (Exception e) {
            }

        }

    }

    @Override
    public void canceled() {
        Main.multType = null;
    }
}
