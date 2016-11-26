package com.kabanero.junction16

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.InputProcessor
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
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.utils.UBJsonReader
import com.badlogic.gdx.Files.FileType

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

import scala.collection.mutable.Map

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.esotericsoftware.kryonet.Server
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Connection

import com.kabanero.junction16.scene._
import com.kabanero.junction16.scenes.TestScene
import com.kabanero.junction16.transform.TransformChange
import scala.collection.mutable.ArrayBuffer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion

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
	def apply(up: Boolean, right: Boolean, down: Boolean, left: Boolean, mouseX: Int, mouseY: Int) = {
		var i = new Inputs()
		i.up = up
		i.right = right
		i.down = down
		i.left = left
		i.mouseX = mouseX
		i.mouseY = mouseY
		i
	}
}
case class Inputs() {
	var up: Boolean = false
	var right: Boolean = false
	var down: Boolean = false
	var left: Boolean = false
	var mouseX: Int = 0
	var mouseY: Int = 0
}


object Position {
	def apply(x: Float, z: Float) = {
		var p = new Position()
		p.x = z
		p.z = z
	}
}
case class Position() {
	var x: Float = 0.0f
	var z: Float = 0.0f
}

case class AllInputs(ownInputs: Inputs, otherInputs: Inputs);

class Game(config: GameConfig) extends ApplicationAdapter with InputProcessor {
	val isHost = config.host
	val isClient = !isHost

  lazy val jsonReader = new UBJsonReader();
  lazy val modelLoader = new G3dModelLoader(jsonReader);

	val models = Map[String, Model]()

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

	lazy val scene = new TestScene(isHost)

	var newTransforms = Array[TransformChange]()

	var mouseX = 0
	var mouseY = 0

	def poll: Inputs = {
    val upState = Gdx.input.isKeyPressed(Keys.W)
    val downState = Gdx.input.isKeyPressed(Keys.S)
    val leftState = Gdx.input.isKeyPressed(Keys.A)
    val rightState = Gdx.input.isKeyPressed(Keys.D)

    Inputs(upState, rightState, downState, leftState, mouseX, mouseY);
  }

	def keyDown(x$1: Int): Boolean = false
	def keyTyped(x$1: Char): Boolean = false
	def keyUp(x$1: Int): Boolean = false
	def scrolled(x$1: Int): Boolean = false
	def touchDown(x$1: Int,x$2: Int,x$3: Int,x$4: Int): Boolean = false
	def touchDragged(x$1: Int,x$2: Int,x$3: Int): Boolean = false
	def touchUp(x$1: Int,x$2: Int,x$3: Int,x$4: Int): Boolean = false
	override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
		mouseX = screenX
		mouseY = screenY
    return false
  }

	override def create(): Unit = {
		Gdx.input.setInputProcessor(this)
		Gdx.input.setCursorCatched(true)
		Gdx.input.setCursorPosition(0, 0)

		kryo.register(classOf[SomeRequest])
    kryo.register(classOf[SomeResponse])
    kryo.register(classOf[Inputs])
    kryo.register(classOf[TransformChange])
    kryo.register(classOf[Array[TransformChange]])
    kryo.register(classOf[Vector3])
    kryo.register(classOf[Quaternion])
		if (config.host) {
			server.start()
		 	server.bind(54555, 54777)

			server.addListener(new Listener() {
				override def received(connection: Connection, obj: Object) {
					obj match {
						case inputs: Inputs => {
							// println("Reveived inputs")
							hasReceivedInputs = true
							receivedInputs = inputs
							while (!canSend) {
								Thread.sleep(2)
							}
							val response = inputsToSend
							canSend = false
	            connection.sendTCP(response);

							val nodes = scene.rootNode.flatten
							val buff = ArrayBuffer[TransformChange]()
							nodes.foreach { n =>
								if (n.physicsBody.isDefined) {
									buff += TransformChange(n.name, n.localPosition, n.localRotation)
								}
							}
							connection.sendTCP(buff.toArray)
							// println("Sent inputs")
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
							//  println("Reveived inputs")
							 receivedInputs = response
							 hasReceivedInputs = true
						 }
						 case response: Array[TransformChange] => {
							 newTransforms = response
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

	lazy val lights = {
		val env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
	  env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		env
	}

	override def render(): Unit = {
		Gdx.gl.glClearColor(0, 0, 0, 0)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)

		batch.begin(scene.cameraNode.cam.get)
		render(scene.rootNode, batch)
		batch.end()

		val inputs = poll

		// val currentTime = System.nanoTime
		// val delta = (currentTime - time) / 1e9f
		// time = currentTime

		// scene.update(DELTA, AllInputs(inputs, inputs))


		if (isHost && hasReceivedInputs) {
			hasReceivedInputs = false
			inputsToSend = inputs
			val otherInputs = receivedInputs
			canSend = true

			scene.update(DELTA, AllInputs(inputsToSend, otherInputs))
		} else if (isClient && !waitingForInputs) {
			waitingForInputs = true
	    client.sendTCP(inputs);
		} else if (isClient && hasReceivedInputs) {
			val otherInputs = receivedInputs
			hasReceivedInputs = false
			waitingForInputs = false

			// scene.update(DELTA, AllInputs(inputs, otherInputs))]
			scene.setPositions(newTransforms)
		}

		scene.updateVisual(DELTA, AllInputs(inputs, inputs))
	}

	def render(node: Node, batch: ModelBatch): Unit = {
		node.modelInstance.foreach { instance =>
			batch.render(instance, lights)
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
