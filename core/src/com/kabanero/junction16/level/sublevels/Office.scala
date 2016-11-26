package com.kabanero.junction16.level

import com.kabanero.junction16.scene.Node
import com.badlogic.gdx.graphics.g3d.Model
// import com.badlogic.gdx.graphics.g3d.Material
// import com.badlogic.gdx.graphics.g3d.ModelInstance
// import com.badlogic.gdx.graphics.Color
// import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
// import com.badlogic.gdx.graphics.VertexAttributes.Usage
// import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.physics.box2d.World

// import com.badlogic.gdx.math.Vector2
// import com.badlogic.gdx.math.Vector3
// import com.badlogic.gdx.math.Quaternion

class Office(
    models: collection.mutable.Map[String, Model],
    collisionSizes: collection.mutable.Map[String, Tuple2[Float, Float]],
    world: World) extends SubLevel("office", models, collisionSizes, world) {

    val level = Array(
      Array(1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
      Array(1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
    )

  for (y <- 0 until level.length) {
    for (x <- 0 until level(0).length) {
      if (level(y)(x) != 0) {
        val node = createLevelNode(level(y)(x), x, y)
        rootNode.addChild(node)
      }
    }
  }

  // Office 0, 0
  addObject("ent_chair", 2.5f, 3.5f, 180.0f)
  addObject("ent_desk", 3f, 3.5f, 180.0f)
  addObject("ent_sofa", 7f, 1f, 0.0f)
  addObject("ent_cabinet", 1f, 1f, 90.0f)
  addObject("ent_table_coffee_machine", 7.9f, 5.9f, 180.0f)

  // Staff room 0, 7
  addObject("ent_sofa", 1f, 9f, 90.0f)
  addObject("ent_coffee_table", 2f, 9f, 90.0f)
  addObject("ent_sofa", 5f, 13f, 180.0f)
  addObject("ent_coffee_table", 5f, 12f, 180.0f)
  addObject("ent_coffee_table", 5f, 8f, 180.0f)
  addObject("ent_table_coffee_machine", 7.9f, 12.9f, 0.0f)
  addObject("ent_cabinet", 7f, 8f, 0.0f)
  addObject("ent_cabinet", 8f, 8f, 0.0f)

  // South staff room 0, 14
  addObject("ent_cabinet", 7f, 19f, 180.0f)
  addObject("ent_cabinet", 8f, 19f, 180.0f)
  addObject("ent_table", 4f, 19f, 180.0f)
  addObject("ent_chair", 4f, 18f, 90.0f)
  addObject("ent_chair", 3f, 19f, 180.0f)

  // Small south room 9, 14
  addObject("ent_device_ctrl_panel", 12f, 19f, 180.0f)
  addObject("ent_device_screens", 10.5f, 19f, 180.0f)

  // South hall 13, 10
  addObject("ent_sofa", 18f, 13f, -90.0f)
  addObject("ent_sofa", 17f, 13f, 90.0f)
  addObject("ent_sofa", 18f, 18f, -90.0f)
  addObject("ent_sofa", 17f, 18f, 90.0f)

  addObject("ent_table_coffee_machine", 20f, 19f, -90.0f)
  addObject("ent_table", 14f, 18f, -90.0f)
  addObject("ent_table", 14f, 19f, -90.0f)

  addObject("ent_cabinet", 14f, 13f, -90.0f)
  addObject("ent_cabinet", 14f, 12f, -90.0f)

  // South storage dead end 22, 14
  addObject("ent_device_iv", 23f, 15f, 90.0f)
  addObject("ent_device_iv", 24f, 15f, 43.0f)
  addObject("ent_stool", 25f, 19f, 0.0f)
  addObject("ent_stool", 25.75f, 19f, 0.0f)

  // North operation room 13, 0
  addObject("ent_device_iv", 14f, 1f, 0.0f)
  addObject("ent_device_screens", 16f, 1f, 0.0f)
  addObject("ent_device_screens", 17.5f, 1.5f, -30.0f)
  addObject("ent_device_iv", 14f, 8f, 0.0f)
  addObject("ent_device_ctrl_panel", 15.5f, 8f, 180.0f)
  addObject("ent_device_ctrl_panel", 19f, 1f, 0.0f)
  addObject("ent_bed", 17f, 5f, 0.0f)
  addObject("npc_doctor_dead", 15f, 3f, 90.0f)

  // East corridor 22, 0
  addObject("ent_bed", 24f, 13f, 0.0f)
  addObject("ent_device_iv", 26f, 13f, 0.0f)
  addObject("ent_bed", 24f, 1, 0.0f)
  addObject("ent_device_iv", 26f, 1, 0.0f)
  addObject("ent_table", 25f, 5f, -90.0f)

  // East ward 27, 0
  // addObject("ent_device_ctrl_panel", 28f, 1f, 0.0f)
  addObject("ent_bed", 29f, 2f, 90.0f)
  addObject("ent_bed", 32f, 2f, 90.0f)
  addObject("ent_device_iv", 28f, 2f, 0.0f)
  addObject("ent_device_iv", 31f, 2f, 10.0f)
  addObject("ent_device_screens", 33f, 8f, 180.0f)

  // East ward 35, 0
  addObject("ent_bed", 36.6f, 4f, 180.0f)
  addObject("ent_bed", 36.6f, 7f, 180.0f)
  addObject("ent_bed", 40f, 10f, 0.0f)
  addObject("ent_bed", 40f, 12.5f, 0.0f)
  addObject("ent_device_ctrl_panel", 41f, 7f, 90.0f)
}
