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

import com.esotericsoftware.kryonet.Server
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Connection

case class SomeRequest(text: String) { }

class SomeResponse(text: String) { }

class Game(config: GameConfig) extends ApplicationAdapter {
	lazy val batch = new SpriteBatch()
	lazy val img = new Texture("badlogic.jpg")
	lazy val server = new Server()
	lazy val client = new Client()


	override def create(): Unit = {
		if (config.host) {
			server.start()
		 	server.bind(54555, 54777)

			server.addListener(new Listener() {
				override def received(connection: Connection, obj: Object) {
					obj match {
						case request: SomeRequest => {
							println(request.text)
						}
						case _ => {

						}
					}
				}
			});
		} else {
	    client.start()
	    client.connect(5000, "192.168.0.4", 54555, 54777)

	    val request = SomeRequest("Here is the request");
	    client.sendTCP(request);
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
