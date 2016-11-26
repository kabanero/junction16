package com.kabanero.junction16.scene

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.physics.box2d.Body

import com.kabanero.junction16.collision.Collider
import scala.collection.mutable.ArrayBuffer
import com.kabanero.junction16.AllInputs

case class Node(
    var name: String,
    var localPosition: Vector3 = new Vector3(0, 0, 0),
    var localRotation: Quaternion = new Quaternion(),
    var localScale: Vector3 = new Vector3(1, 1, 1)) {

  var parent: Option[Node] = None
  private var _children = ArrayBuffer[Node]()
  var updateMethods = ArrayBuffer[(Float, Node, AllInputs) => Unit]()
  var updateVisualMethods = ArrayBuffer[(Float, Node, AllInputs) => Unit]()
  var modelInstance: Option[ModelInstance] = None
  var cam: Option[PerspectiveCamera] = None
  var physicsBody: Option[Body] = None

  def children = {
    _children
  }

  def addChild(node: Node) = {
    children += node
    node.parent = Some(this)
  }

  def localTransform(): Matrix4 = {
    new Matrix4(localPosition, localRotation, localScale)
  }

  def worldTransform(): Matrix4 = {
    if (parent.isDefined) {
      val mat = parent.get.worldTransform().getValues()
      parent.get.worldTransform.mul(localTransform)
    } else {
      localTransform
    }
  }

  def position: Vector3 = {
    val mat = worldTransform().getValues()
    new Vector3(mat(Matrix4.M03), mat(Matrix4.M13), mat(Matrix4.M23))
  }

  def up: Vector3 = {
    val mat = worldTransform().getValues()
    new Vector3(mat(Matrix4.M01), mat(Matrix4.M11), mat(Matrix4.M21))
  }

  def right: Vector3 = {
    val mat = worldTransform().getValues()
    new Vector3(mat(Matrix4.M00), mat(Matrix4.M10), mat(Matrix4.M20))
  }

  def forward: Vector3 = {
    val mat = worldTransform().getValues()
    new Vector3(mat(Matrix4.M02), mat(Matrix4.M12), mat(Matrix4.M22))
  }

  def update(delta: Float, inputs: AllInputs) {

    updateMethods.foreach { method =>
      method(delta, this, inputs)
    }
    modelInstance.foreach(instance => {
      instance.transform = worldTransform()
    })
    children.foreach(child => {
      child.update(delta, inputs)
    })
  }

  def updateVisual(delta: Float, inputs: AllInputs) {
    updateVisualMethods.foreach { method =>
      method(delta, this, inputs)
    }
    cam.foreach(cam => {
      val localMat = localTransform
      val mat = localMat.getValues()
      cam.position.set(position)

      cam.up.set(new Vector3(mat(Matrix4.M01), mat(Matrix4.M11), mat(Matrix4.M21)))
      cam.direction.set(new Vector3(mat(Matrix4.M02), mat(Matrix4.M12), mat(Matrix4.M22)))
      cam.update()
    })
    children.foreach(child => {
      child.updateVisual(delta, inputs)
    })
  }


}
