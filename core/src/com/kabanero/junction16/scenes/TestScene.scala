package com.kabanero.junction16.scenes

import com.kabanero.junction16.scene.Scene
import com.kabanero.junction16.scene.Node
import com.kabanero.junction16.AllInputs
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.Gdx
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

class TestScene(iAmGood: Boolean) extends Scene {
  val PLAYER_SPEED = 5.0f
  val CAMERA_SPEED = 1 / 5.0f
  val UP = new Vector3(0, 1, 0)
  val RIGHT = new Vector3(-1, 0, 0)

  def ownMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationY = new Quaternion(UP, -inputs.ownInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.ownInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationY.mul(rotationX))

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

    node.localPosition.add(forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED))
    node.localPosition.add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))
  }

  def otherMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationY = new Quaternion(UP, -inputs.otherInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.otherInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationY.mul(rotationX))

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

  val playerNode = {
		val node = Node("player")
    node.localPosition.add(-5.0f, 0, 0)

    if (iAmGood) {
      node.updateMethods += ownMovement
    } else {
      node.updateMethods += otherMovement
      val modelBuilder = new ModelBuilder()
      val model = modelBuilder.createBox(
        1, 1, 1,
        new Material(ColorAttribute.createDiffuse(Color.GREEN)),
        Usage.Position | Usage.Normal);
      val instance = new ModelInstance(model);
      node.modelInstance = Some(instance);
    }

    node
  }
  val enemyNode = {
    val node = Node("enemy")
    node.localPosition.add(5.0f, 0, 0)
    if (iAmGood) {
      node.updateMethods += otherMovement
      val modelBuilder = new ModelBuilder()
      val model = modelBuilder.createBox(
        1, 1, 1,
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
    node.localPosition.add(0, 1.2f, 0)

    node
  }
  playerHead.addChild(cameraNode)
  playerNode.addChild(playerHead)

	val cubeNode = {
		val node = Node("cube")

		// val modelBuilder = new ModelBuilder()
		// val model = modelBuilder.createBox(
		// 	5, 5, 5,
		// 	new Material(ColorAttribute.createDiffuse(Color.GREEN)),
		// 	Usage.Position | Usage.Normal);
		val instance = new ModelInstance(testmodel);
		node.modelInstance = Some(instance);

		node.localPosition = new Vector3(0, 0, 8)

		node
	}

  rootNode.addChild(playerNode)
  rootNode.addChild(enemyNode)
  rootNode.addChild(cubeNode)
}
