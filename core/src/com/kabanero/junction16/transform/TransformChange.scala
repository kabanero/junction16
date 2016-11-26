package com.kabanero.junction16.transform

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion

object TransformChange {
  def apply(name: String, position: Vector3, rotation: Quaternion) = {
    val t = new TransformChange()
    t.name = name
    t.position = position
    t.rotation = rotation
    t
  }
}

class TransformChange {
  var name = ""
  var position = new Vector3(0, 0, 0)
  var rotation = new Quaternion()

  override def toString(): String = {
    name + position + rotation
  }
}
