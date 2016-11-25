package com.kabanero.junction16.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kabanero.junction16.Game;

object DesktopLauncher {
	def main (args: Array[String]) {
		val gameConfig = ArgParser.parse(args);
		val config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(gameConfig), config);
	}
}
