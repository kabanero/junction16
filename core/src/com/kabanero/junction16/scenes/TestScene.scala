package com.kabanero.junction16.scenes

import com.kabanero.junction16.scene.Scene
import com.kabanero.junction16.scene.Node
import com.kabanero.junction16.AllInputs
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.Material

class TestScene extends Scene {
  val PLAYER_SPEED = 10.0f

  val playerNode = {
		val node = Node("player")

		node.updateMethods += ((delta: Float, node: Node, inputs: AllInputs) => {
			if (inputs.ownInputs.up) {
				node.localPosition.add(0, 0, delta * PLAYER_SPEED)
			}
			if (inputs.ownInputs.down) {
				node.localPosition.add(0, 0, -delta * PLAYER_SPEED)
			}
			if (inputs.ownInputs.left) {
				node.localPosition.add(delta * PLAYER_SPEED, 0, 0)
			}
			if (inputs.ownInputs.right) {
				node.localPosition.add(-delta * PLAYER_SPEED, 0, 0)
			}
		})

		node
	}

  playerNode.addChild(cameraNode)

	val cubeNode = {
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

  rootNode.addChild(playerNode)
  rootNode.addChild(cubeNode)
}
