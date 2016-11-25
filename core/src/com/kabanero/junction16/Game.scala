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

	lazy val playerNode = {
		val node = Node("player")

		node.updateMethods += ((delta: Float, node: Node, inputs: AllInputs) => {
			if (inputs.ownInputs.up) {
				node.localPosition.add(0, 0, delta)
			}
			if (inputs.ownInputs.down) {
				node.localPosition.add(0, 0, -delta)
			}
			if (inputs.ownInputs.left) {
				node.localPosition.add(delta, 0, 0)
			}
			if (inputs.ownInputs.right) {
				node.localPosition.add(-delta, 0, 0)
			}
		})

		node
	}
	lazy val cameraNode = {
		val node = Node("camera")

		val cam  = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// cam.position.set(10f, 10f, 10f)
		// cam.lookAt(0, 0, 0)
		cam.near = 1f
		cam.far = 300f
		cam.update()
		node.cam = Some(cam)

		playerNode.addChild(node)

		node
	}
	lazy val cubeNode = {
		val node = Node("cube")

		val modelBuilder = new ModelBuilder()
		val model = modelBuilder.createBox(
			5, 5, 5,
			new Material(ColorAttribute.createDiffuse(Color.GREEN)),
			Usage.Position | Usage.Normal);
		val instance = new ModelInstance(model);
		node.modelInstance = Some(instance);

		node.localPosition = new Vector3(0, 0, 8)

		node
	}
	lazy val rootNode = {
		val root = Node("root")
		root.addChild(playerNode)
		root.addChild(cubeNode)
		root
	}

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
						case request: Inputs => {
							println("Reveived inputs")
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

	override def render(): Unit = {
		val inputs = poll

		if (isHost && hasReceivedInputs) {
			hasReceivedInputs = false
			inputsToSend = inputs
			val otherInputs = receivedInputs
			canSend = true

			rootNode.update(0.1f, AllInputs(inputs, otherInputs))
		} else if (isClient && !waitingForInputs) {
			waitingForInputs = true
	    client.sendTCP(inputs);
		} else if (isClient && hasReceivedInputs) {
			val otherInputs = receivedInputs
			hasReceivedInputs = false
			waitingForInputs = false

			rootNode.update(0.1f, AllInputs(inputs, otherInputs))
		}

		Gdx.gl.glClearColor(0, 0, 0, 0)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)

		batch.begin(cameraNode.cam.get)
		render(rootNode, batch)
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
