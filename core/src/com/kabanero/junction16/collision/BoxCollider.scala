package com.kabanero.junction16.collision

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

case class BoxCollider(min: Vector3, max: Vector3) extends Collider {
  val gdxBox = new BoundingBox(min, max)
}
