package com.kabanero.junction16.scenes

import com.kabanero.junction16.scene.Scene
import com.kabanero.junction16.scene.Node
import com.kabanero.junction16.AllInputs
import com.kabanero.junction16.collision.BoxCollider
import com.kabanero.junction16.collision.SphereCollider
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

    // val newPos = new Vector3(node.localPosition)

    // body.setLinearVelocity()

    val velo = forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED).add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))

    body.setLinearVelocity(velo.x, velo.z)

    // newPos.add(forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED))
    // newPos.add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))

    val bodyPos = node.physicsBody.get.getPosition()

    node.localPosition = new Vector3(bodyPos.x, 0, bodyPos.y)
  }

  def otherMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationY = new Quaternion(UP, -inputs.otherInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.otherInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationY)

    val moveDirection = new Vector3(0, 0, 0)

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

    node.localPosition.add(forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED))
    node.localPosition.add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))
  }

  val jsonReader = new JsonReader()
  val modelLoader = new G3dModelLoader(jsonReader);

	val testmodel = modelLoader.loadModel(Gdx.files.internal("test.g3dj"))

  def otherHeadMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationX = new Quaternion(RIGHT, -inputs.otherInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationX)
  }

  val playerNode = {
		val node = Node("player")
    node.localPosition.add(-5.0f, 0.6f, 0)

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
      val modelBuilder = new ModelBuilder()
      val model = modelBuilder.createBox(
        0.5f, 1.2f, 0.5f,
        new Material(ColorAttribute.createDiffuse(Color.GREEN)),
        Usage.Position | Usage.Normal);
      val instance = new ModelInstance(model);
      node.modelInstance = Some(instance);
    }

    node
  }
  val enemyNode = {
    val node = Node("enemy")
    node.localPosition.add(5.0f, 0.6f, 0)

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
      val modelBuilder = new ModelBuilder()
      val model = modelBuilder.createBox(
        0.5f, 1.2f, 0.5f,
        new Material(ColorAttribute.createDiffuse(Color.RED)),
        Usage.Position | Usage.Normal);
      val instance = new ModelInstance(model);
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
    val node = Node("head")
    node.localPosition.add(0, 1.15f, 0)
    if (!iAmGood) {
      val modelBuilder = new ModelBuilder()
      val model = modelBuilder.createBox(
        0.5f, 0.5f, 0.5f,
        new Material(ColorAttribute.createDiffuse(Color.GREEN)),
        Usage.Position | Usage.Normal);
      val instance = new ModelInstance(model);
      node.modelInstance = Some(instance);

      node.updateMethods += otherHeadMovement
    }

    node
  }

  val enemyHead = {
    val node = Node("head")
    node.localPosition.add(0, 1.15f, 0)

    if (iAmGood) {
      val modelBuilder = new ModelBuilder()
      val model = modelBuilder.createBox(
        0.5f, 0.5f, 0.5f,
        new Material(ColorAttribute.createDiffuse(Color.RED)),
        Usage.Position | Usage.Normal);
      val instance = new ModelInstance(model);
      node.modelInstance = Some(instance);

      node.updateMethods += otherHeadMovement
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
		val node = Node("cube")

		val instance = new ModelInstance(testmodel);
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

  override def update(delta: Float, inputs: AllInputs) {
    super.update(delta, inputs)

    world.step(delta, 6, 2)
  }
}
