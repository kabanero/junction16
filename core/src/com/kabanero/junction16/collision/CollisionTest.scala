package com.kabanero.junction16.collision


object CollisionTest {

  def checkCollision(a: Collider, b: Collider): Boolean = {
    (a, b) match {
      case (box: BoxCollider, sphere: SphereCollider) => {
        checkCollision(box, sphere)
      }
      case (sphere: SphereCollider, box: BoxCollider) => {
        checkCollision(sphere, box)
      }
      case _ => {
        println("Collision not implemented")
        false
      }
    }
  }

  def checkCollision(a: BoxCollider, b: SphereCollider): Boolean = {
    val boundingBox = a.gdxBox
    val sphere = b.gdxSphere
    var dmin = 0.0

    val center = sphere.center
    val bmin = boundingBox.min
    val bmax = boundingBox.max

    if (center.x < bmin.x) {
        dmin += Math.pow(center.x - bmin.x, 2);
    } else if (center.x > bmax.x) {
        dmin += Math.pow(center.x - bmax.x, 2);
    }

    if (center.y < bmin.y) {
        dmin += Math.pow(center.y - bmin.y, 2);
    } else if (center.y > bmax.y) {
        dmin += Math.pow(center.y - bmax.y, 2);
    }

    if (center.z < bmin.z) {
        dmin += Math.pow(center.z - bmin.z, 2);
    } else if (center.z > bmax.z) {
        dmin += Math.pow(center.z - bmax.z, 2);
    }

    return dmin <= Math.pow(sphere.radius, 2);
  }
}
