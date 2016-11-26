package com.kabanero.junction16.level

import com.kabanero.junction16.scene.Node
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.physics.box2d.World


class Corridor(
    models: collection.mutable.Map[String, Model],
    collisionSizes: collection.mutable.Map[String, Tuple2[Float, Float]],
    world: World) extends SubLevel("Corridor", models, collisionSizes, world) {

  val level = Array(
    Array(1, 1, 1, 1, 1),
    Array(1, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 1),
    Array(0, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0),
    Array(1, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 1), //
    Array(1, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0),
    Array(1, 0, 0, 0, 0),
    Array(1, 0, 0, 0, 1),
    Array(1, 1, 1, 1, 1)
  )

  for (y <- 0 until level.length) {
    for (x <- 0 until level(0).length) {
      if (level(y)(x) != 0) {
        val node = createLevelNode(level(y)(x), x, y)
        rootNode.addChild(node)
      }
    }
  }


  // addObject("ent_chair", 2.5f, 3.5f, 180.0f)
  // addObject("ent_desk", 3f, 3.5f, 180.0f)
  // addObject("ent_sofa", 7f, 1f, 0.0f)
  // addObject("ent_cabinet", 1f, 1f, 90.0f)
  // addObject("ent_table_coffee_machine", 8f, 6f, 180.0f)

}
