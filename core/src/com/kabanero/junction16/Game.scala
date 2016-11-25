package com.kabanero.junction16

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

class Game(config: GameConfig) extends ApplicationAdapter {
	lazy val batch = new SpriteBatch()
	lazy val img = new Texture("badlogic.jpg")


	override def create(): Unit = {
		if (config.host) {
			val server = Gdx.net.newServerSocket(Protocol.TCP, config.port, null);
			val client = server.accept(null);

			try {
				val message = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
				Gdx.app.log("SocketTest", "got client message: " + message);
				client.getOutputStream().write("I GOT YOUR MESSAGE\n".getBytes());
			} catch {
				case e: IOException => Gdx.app.log("PingPongSocketExample", "an error occured", e);
			}
		} else {
			val client = Gdx.net.newClientSocket(Protocol.TCP, config.address, config.port, null);

			try {
				client.getOutputStream().write("HELLO SERVER\n".getBytes())
				val response = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine()
				Gdx.app.log("SocketTest", "got server response: " + response);
			} catch {
				case _: Throwable => Gdx.app.log("SocketTest", "error")
			}
		}
	}

	override def render(): Unit = {
		Gdx.gl.glClearColor(1, 0, 0, 1)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		batch.begin()
		batch.draw(img, 0, 0)
		batch.end()
	}

	override def dispose(): Unit = {
		batch.dispose()
		img.dispose()
	}
}
