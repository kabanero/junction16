package com.kabanero.junction16.scenes

import com.kabanero.junction16.scene.Scene
import com.kabanero.junction16.scene.Node
import com.kabanero.junction16.AllInputs
import com.kabanero.junction16.collision.BoxCollider
import com.kabanero.junction16.collision.SphereCollider
import scala.collection.mutable.Map
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.utils.UBJsonReader
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.physics.box2d
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType

class TestScene(iAmGood: Boolean) extends Scene {
  val PLAYER_SPEED = 500.0f
  val CAMERA_SPEED = 1 / 5.0f
  val UP = new Vector3(0, 1, 0)
  val RIGHT = new Vector3(-1, 0, 0)

  val world = new World(new Vector2(0, 0), true)

  val models = Map[String, Model]()

  def ownMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationY = new Quaternion(UP, -inputs.ownInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.ownInputs.mouseY * CAMERA_SPEED)

    val body = node.physicsBody.get

    node.localRotation.set(rotationY)

    val moveDirection = new Vector3(0, 0, 0)

    val forwardMove = node.forward
    forwardMove.y = 0
    forwardMove.nor()
    val rightMove = node.right
    rightMove.y = 0
    rightMove.nor()
    if (inputs.ownInputs.up) {
      moveDirection.z += 1
    }
    if (inputs.ownInputs.down) {
      moveDirection.z -= 1
    }
    if (inputs.ownInputs.left) {
      moveDirection.x += 1
    }
    if (inputs.ownInputs.right) {
      moveDirection.x -= 1
    }

    val velo = forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED).add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))

    body.setLinearVelocity(velo.x, velo.z)


    val bodyPos = node.physicsBody.get.getPosition()

    node.localPosition = new Vector3(bodyPos.x, 0, bodyPos.y)
  }

  def otherMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationY = new Quaternion(UP, -inputs.otherInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.otherInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationY)

    val moveDirection = new Vector3(0, 0, 0)

    val body = node.physicsBody.get

    val forwardMove = node.forward
    forwardMove.y = 0
    forwardMove.nor()
    val rightMove = node.right
    rightMove.y = 0
    rightMove.nor()
    if (inputs.otherInputs.up) {
      moveDirection.z += 1
    }
    if (inputs.otherInputs.down) {
      moveDirection.z -= 1
    }
    if (inputs.otherInputs.left) {
      moveDirection.x += 1
    }
    if (inputs.otherInputs.right) {
      moveDirection.x -= 1
    }

    val velo = forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED).add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))

    body.setLinearVelocity(velo.x, velo.z)

    val bodyPos = node.physicsBody.get.getPosition()

    node.localPosition = new Vector3(bodyPos.x, 0, bodyPos.y)
  }

  val jsonReader = new JsonReader()
  val modelLoader = new G3dModelLoader(jsonReader);

  models("doc_head") = modelLoader.loadModel(Gdx.files.internal("doc_head.g3dj"))
  models("doc_body") = modelLoader.loadModel(Gdx.files.internal("doc_body.g3dj"))

  models("ent_cabinet") = modelLoader.loadModel(Gdx.files.internal("ent_cabinet.g3dj"))
  models("ent_chair") = modelLoader.loadModel(Gdx.files.internal("ent_chair.g3dj"))
  models("ent_desk") = modelLoader.loadModel(Gdx.files.internal("ent_desk.g3dj"))
  models("ent_device_ctrl_panel") = modelLoader.loadModel(Gdx.files.internal("ent_device_ctrl_panel.g3dj"))
  models("ent_device_iv") = modelLoader.loadModel(Gdx.files.internal("ent_device_iv.g3dj"))
  models("ent_device_screens") = modelLoader.loadModel(Gdx.files.internal("ent_device_screens.g3dj"))
  models("ent_stand") = modelLoader.loadModel(Gdx.files.internal("ent_stand.g3dj"))
  models("ent_stool") = modelLoader.loadModel(Gdx.files.internal("ent_stool.g3dj"))
  models("ent_table") = modelLoader.loadModel(Gdx.files.internal("ent_table.g3dj"))
  models("evil_body") = modelLoader.loadModel(Gdx.files.internal("evil_body.g3dj"))
  models("evil_head") = modelLoader.loadModel(Gdx.files.internal("evil_head.g3dj"))
  models("npc_doctor") = modelLoader.loadModel(Gdx.files.internal("npc_doctor.g3dj"))



  def otherHeadMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationX = new Quaternion(RIGHT, -inputs.otherInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationX)
  }

  def ownHeadMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationX = new Quaternion(RIGHT, -inputs.ownInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationX)
  }

  val playerNode = {
		val node = Node("player")
    node.isDynamic = true
    node.localPosition.add(-5.0f, 0.0f, 0)

    val bodyDef = new BodyDef()
    bodyDef.`type` = BodyType.DynamicBody
    bodyDef.position.set(
      node.localPosition.x,
      node.localPosition.z)

    val body = world.createBody(bodyDef)

    val circle = new CircleShape()
    circle.setRadius(0.25f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = circle
    fixtureDef.density = 0.5f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f

    val fixture = body.createFixture(fixtureDef)

    circle.dispose()

    node.physicsBody = Some(body)

    if (iAmGood) {
      node.updateMethods += ownMovement
    } else {
      node.updateMethods += otherMovement
      val instance = new ModelInstance(models("doc_body"));
      node.modelInstance = Some(instance);
    }

    node
  }
  val enemyNode = {
    val node = Node("enemy")
    node.isDynamic = true
    node.localPosition.add(5.0f, 0.0f, 0)

    val bodyDef = new BodyDef()
    bodyDef.`type` = BodyType.DynamicBody
    bodyDef.position.set(
      node.localPosition.x,
      node.localPosition.z)

    val body = world.createBody(bodyDef)

    val circle = new CircleShape()
    circle.setRadius(0.25f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = circle
    fixtureDef.density = 0.5f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f

    val fixture = body.createFixture(fixtureDef)

    circle.dispose()

    node.physicsBody = Some(body)

    if (iAmGood) {
      node.updateMethods += otherMovement
      val instance = new ModelInstance(models("evil_body"));
      node.modelInstance = Some(instance);
    } else {
      node.updateMethods += ownMovement
    }

		node
  }

  cameraNode.updateVisualMethods +=((delta: Float, node: Node, inputs: AllInputs) => {
    val rotationY = new Quaternion(UP, -inputs.ownInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.ownInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationY.mul(rotationX))

    node.localRotation.set(rotationY.mul(rotationX))
  })

  val playerHead = {
    val node = Node("player head")
    node.isDynamic = true

    node.localPosition.add(0, 1.25f, 0)
    if (!iAmGood) {
      val instance = new ModelInstance(models("doc_head"));
      node.modelInstance = Some(instance);

      node.updateMethods += otherHeadMovement
    } else {
      node.updateMethods += ownHeadMovement
    }

    node
  }

  val enemyHead = {
    val node = Node("enemy head")
    node.localPosition.add(0, 1.25f, 0)

    node.isDynamic = true

    if (iAmGood) {
      val instance = new ModelInstance(models("evil_head"));
      node.modelInstance = Some(instance);

      node.updateMethods += otherHeadMovement
    } else {
      node.updateMethods += ownHeadMovement
    }

    node
  }

  if (iAmGood) {
    playerHead.addChild(cameraNode)
  } else {
    enemyHead.addChild(cameraNode)
  }
  playerNode.addChild(playerHead)
  enemyNode.addChild(enemyHead)

	val cubeNode = {
		val node = Node("screen thingy")

		val instance = new ModelInstance(models("ent_device_screens"));
		node.modelInstance = Some(instance);

		node.localPosition = new Vector3(0, 0, 8)

    val groundBodyDef = new BodyDef();
    groundBodyDef.position.set(new Vector2(node.localPosition.x, node.localPosition.z));
    val groundBody = world.createBody(groundBodyDef)
    val groundBox = new PolygonShape()
    groundBox.setAsBox(1f, 1f)
    groundBody.createFixture(groundBox, 0.0f)
    groundBox.dispose()

    node.physicsBody = Some(groundBody)

		node
	}

  rootNode.addChild(playerNode)
  rootNode.addChild(enemyNode)
  rootNode.addChild(cubeNode)

  override def update(delta: Float, inputs: AllInputs) = {
    super.update(delta, inputs)

    world.step(delta, 6, 2)
  }
}
