package com.kabanero.junction16.scene

import com.kabanero.junction16.AllInputs
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion

class Scene {

  val rootNode = Node("root")

  val cameraNode = {
		val node = Node("camera")

		val cam  = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 1f
		cam.far = 300f
		cam.update()
		node.cam = Some(cam)

		node
	}

  def update(delta: Float, inputs: AllInputs) {
    rootNode.update(delta, inputs)
  }

  def updateVisual(delta: Float, inputs: AllInputs) {
    rootNode.updateVisual(delta, inputs)
  }

  // val UP = new Vector3(0, 1, 0)

  // def updateCamera(delta: Float, mouseMovement: MouseMovement) {
  //   val rotationY = Quaternion(UP, mouseMovement.x)
  //   cameraNode.localRotation =
  // }
}
