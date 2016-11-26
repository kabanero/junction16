package com.kabanero.junction16.collision

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.Sphere

case class SphereCollider(center: Vector3, radius: Float) extends Collider {
  val gdxSphere = new Sphere(center, radius)
}
