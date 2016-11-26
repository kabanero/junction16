package com.kabanero.junction16.scene

import com.kabanero.junction16.AllInputs
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion
import com.kabanero.junction16.transform.TransformChange

class Scene {

  var rootNode = Node("root")

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

  def setPositions(newTransforms: Array[TransformChange]) {
    val nodes = rootNode.flatten
    newTransforms.foreach { t =>
      nodes.foreach { n =>
        if (n.name == t.name) {
          n.localPosition = t.position
          n.localRotation = t.rotation
        }
      }
    }
  }
}
