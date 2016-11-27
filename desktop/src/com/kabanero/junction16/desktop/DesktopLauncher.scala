package com.kabanero.junction16.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kabanero.junction16.Game;

object DesktopLauncher {
	def main (args: Array[String]) {
		val gameConfig = ArgParser.parse(args);
		val config = new LwjglApplicationConfiguration();
		if (gameConfig.host) {
			config.x = 0
			config.y = 0
			config.width = 1700
			config.height = 1000
		} else {
			config.x = 800
			config.width = 1440
			config.height = 900
		}
		new LwjglApplication(new Game(gameConfig), config);
	}
}
