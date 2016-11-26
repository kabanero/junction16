package com.kabanero.junction16

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import com.badlogic.gdx.math.Vector3

import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Input.Keys


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.esotericsoftware.kryonet.Server
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Connection

import com.kabanero.junction16.scene._
import com.kabanero.junction16.scenes.TestScene

object SomeRequest {
	def apply(text: String) = {
		val r = new SomeRequest()
		r.text = text
		r
	}
}
case class SomeRequest() {
	var text: String = ""
}

object SomeResponse {
	def apply(text: String) = {
		val r = new SomeResponse()
		r.text = text
		r
	}
}
case class SomeResponse() {
	var text: String = ""
}

object Inputs {
	def apply(up: Boolean, right: Boolean, down: Boolean, left: Boolean) = {
		var i = new Inputs()
		i.up = up
		i.right = right
		i.down = down
		i.left = left
		i
	}
}
case class Inputs() {
	var up: Boolean = false
	var right: Boolean = false
	var down: Boolean = false
	var left: Boolean = false
}

case class AllInputs(ownInputs: Inputs, otherInputs: Inputs);

class Game(config: GameConfig) extends ApplicationAdapter {
	val isHost = config.host
	val isClient = !isHost

	lazy val batch = new ModelBatch()
	lazy val img = new Texture("badlogic.jpg")
	lazy val server = new Server()
	lazy val client = new Client()
	lazy val kryo = if (config.host) server.getKryo() else client.getKryo()

	var inputsToSend = Inputs()
	var canSend = false

	var receivedInputs = Inputs()
	var hasReceivedInputs = false
	var waitingForInputs = false

	lazy val scene = new TestScene()

	def poll: Inputs = {
    val upState = Gdx.input.isKeyPressed(Keys.W);
    val downState = Gdx.input.isKeyPressed(Keys.S);
    val leftState = Gdx.input.isKeyPressed(Keys.A);
    val rightState = Gdx.input.isKeyPressed(Keys.D);

    Inputs(upState, rightState, downState, leftState);
  }

	override def create(): Unit = {
		kryo.register(classOf[SomeRequest])
    kryo.register(classOf[SomeResponse])
    kryo.register(classOf[Inputs])
		if (config.host) {
			server.start()
		 	server.bind(54555, 54777)

			server.addListener(new Listener() {
				override def received(connection: Connection, obj: Object) {
					obj match {
						case inputs: Inputs => {
							println("Reveived inputs")
							hasReceivedInputs = true
							receivedInputs = inputs
							while (!canSend) {
								Thread.sleep(1)
							}
							val response = inputsToSend
							canSend = false
	            connection.sendTCP(response);
							println("Sent inputs")
						}
						case _ => {

						}
					}
				}
			})
		} else {
			client.addListener(new Listener() {
	       override def received(connection: Connection, obj: Object) {
					 obj match {
						 case response: Inputs => {
							 println("Reveived inputs")
							 receivedInputs = response
							 hasReceivedInputs = true
						 }
						 case _ => { }
					 }
	       }
	    })

	    client.start()
	    client.connect(5000, config.address, 54555, 54777)

	    val request = SomeRequest("Here is the request");
	    client.sendTCP(request);
		}
	}

	val DELTA = 1/60.0f

	override def render(): Unit = {
		val inputs = poll

		// scene.update(DELTA, AllInputs(inputs, inputs))

		if (isHost && hasReceivedInputs) {
			hasReceivedInputs = false
			inputsToSend = inputs
			val otherInputs = receivedInputs
			canSend = true

			scene.update(DELTA, AllInputs(otherInputs, inputs))
		} else if (isClient && !waitingForInputs) {
			waitingForInputs = true
	    client.sendTCP(inputs);
		} else if (isClient && hasReceivedInputs) {
			val otherInputs = receivedInputs
			hasReceivedInputs = false
			waitingForInputs = false

			scene.update(DELTA, AllInputs(otherInputs, inputs))
		}

		Gdx.gl.glClearColor(0, 0, 0, 0)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)

		batch.begin(scene.cameraNode.cam.get)
		render(scene.rootNode, batch)
		batch.end()
	}

	def render(node: Node, batch: ModelBatch): Unit = {
		node.modelInstance.foreach { instance =>
			batch.render(instance)
		}
		node.children.foreach { child =>
			render(child, batch)
		}
	}

	override def dispose(): Unit = {
		batch.dispose()
		img.dispose()
	}
}
