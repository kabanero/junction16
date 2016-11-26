package com.kabanero.junction16.scene

import com.kabanero.junction16.AllInputs
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.Gdx;

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
}
