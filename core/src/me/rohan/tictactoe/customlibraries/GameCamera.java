package me.rohan.tictactoe.customlibraries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.rohan.tictactoe.Main;

public class GameCamera extends OrthographicCamera {

    private Vector3 currentCamPos = null;

    @Override
    public void update() {
        super.update();

        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !Main.manager.isLerping()) {
            if(currentCamPos == null) {
                currentCamPos = new Vector3(position);
            }
            translate(-Gdx.input.getDeltaX() * 13, Gdx.input.getDeltaY() * 13);
            //update();
        } else {
            if(currentCamPos != null) {
                Main.manager.setLerpPos(new Vector2(currentCamPos.x, currentCamPos.y));
                currentCamPos = null;
            }
        }
    }


}