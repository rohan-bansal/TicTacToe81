package me.rohan.tictactoe.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.rohan.tictactoe.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tic-Tac-Toe for Intellectuals";
		config.height = 1000;
		config.width = 1000;
		config.resizable = false;
		config.addIcon("ticpic.png", Files.FileType.Internal);

		new LwjglApplication(new Main(), config);
	}
}
