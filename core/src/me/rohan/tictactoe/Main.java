package me.rohan.tictactoe;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.minlog.Log;
import me.rohan.tictactoe.customlibraries.GameCamera;
import me.rohan.tictactoe.gameboard.Board;
import me.rohan.tictactoe.gameboard.BoardManager;
import me.rohan.tictactoe.multiplayer.ClientManager;
import me.rohan.tictactoe.multiplayer.ServerManager;


public class Main extends ApplicationAdapter {


	public static BoardManager manager;
	public static ServerManager server;
	public static ClientManager client;
	public static Overlay overlay;

	private GameCamera camera = new GameCamera();
	private OrthographicCamera hud = new OrthographicCamera();

	public static boolean third = true, second = true, first = true;

	public enum ServerType{SERVER, CLIENT}

	public static ServerType multType;
	public static Color playerColor;
	private static boolean boardClickable = false;
	public static String currentTurn = "client";
	public static boolean mainScreenDone = false;


	@Override
	public void create () {

		Log.set(Log.LEVEL_DEBUG);

		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();

		hud.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hud.update();

		manager = new BoardManager(camera);
		manager.initBoards();

		server = new ServerManager();
		client = new ClientManager();

		overlay = new Overlay(hud);
	}

	@Override
	public void render () {
		if(first && second && third) {
			Gdx.gl.glClearColor(30/255f, 30/255f, 33/255f, 1);
		} else {
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		hud.update();


		if(mainScreenDone) {
			if(currentTurn.equals("client")) {
				if(multType == ServerType.SERVER) {
					toggleBoardClickable(false);
				} else {
					toggleBoardClickable(true);
				}
			} else if(currentTurn.equals("server")) {
				if(multType == ServerType.CLIENT) {
					toggleBoardClickable(false);
				} else {
					toggleBoardClickable(true);
				}
			}
		}

		manager.render();
		if(boardClickable) {
			manager.detectClicks();
		}

		overlay.render();
	}
	
	@Override
	public void dispose () {
	}

	public static void toggleBoardClickable(boolean value) {
		boardClickable = value;
	}
}
