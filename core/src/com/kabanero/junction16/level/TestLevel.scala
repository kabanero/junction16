package com.kabanero.junction16.level

import com.kabanero.junction16.scene.Node
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.physics.box2d.World
// import com.badlogic.gdx.physics.box2d.BodyDef.BodyType

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion

class TestLevel(models: collection.mutable.Map[String, Model], world: World, collisionSizes: collection.mutable.Map[String, Tuple2[Float, Float]]) {
  val levelRoot = Node("level root")

  val modelBuilder = new ModelBuilder();
  val wallModel = modelBuilder.createBox(1.0f, 3f, 1.001f,
       new Material(ColorAttribute.createDiffuse(new Color(203/255.0f, 216/255.0f, 183/255.0f, 1.0f))),
       Usage.Position | Usage.Normal)

  val floorModel = modelBuilder.createBox(100f, 0.2f, 100f,
       new Material(ColorAttribute.createDiffuse(new Color(121/255.0f, 123/255.0f, 85/255.0f, 1.0f))),
       Usage.Position | Usage.Normal)

  val floorNode = {
    val n = Node("floor")
    n.modelInstance = Some(new ModelInstance(floorModel))
    n.localPosition = new Vector3(0, -0.12f, 0)
    n
  }

  val mapModel = Node("mapModel")
  mapModel.modelInstance = Some(new ModelInstance(models("map_shape")))
  levelRoot.addChild(mapModel)

  val mapRoof = Node("mapRoof")
  mapRoof.modelInstance = Some(new ModelInstance(models("map_roof")))
  levelRoot.addChild(mapRoof)

  val office = new Office(models, collisionSizes, world) // 0, 0
  // val corridor = new Corridor(models, collisionSizes, world) // 9, 0

  // corridor.rootNode.localPosition = new Vector3(9, 0, 0)

  levelRoot.addChild(office.rootNode)

  // levelRoot.addChild(corridor.rootNode)

  levelRoot.addChild(floorNode)
  //
  // val level = Array(
  //   Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
  //   Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
  // )
  //
  // for (y <- 0 until level.length) {
  //   for (x <- 0 until level(0).length) {
  //     if (level(y)(x) != 0) {
  //       val node = createLevelNode(level(y)(x), x, y)
  //       levelRoot.addChild(node)
  //     }
  //   }
  // }

  // def createLevelNode(gridType: Int, x: Int, y: Int): Node = {
  //   val node = Node("level-" + x + "-" + y)
  //   gridType match {
  //     case 1 => node.modelInstance = Some(new ModelInstance(wallModel))
  //     case _ => { }
  //   }
  //   node.localPosition = new Vector3(x, 1.5f, y )
  //
  //   val groundBodyDef = new BodyDef();
  //   groundBodyDef.position.set(new Vector2(node.localPosition.x, node.localPosition.z));
  //   val groundBody = world.createBody(groundBodyDef)
  //   val groundBox = new PolygonShape()
  //   groundBox.setAsBox(1f, 1f)
  //   groundBody.createFixture(groundBox, 0.0f)
  //   groundBox.dispose()
  //
  //   node.physicsBody = Some(groundBody)
  //
  //   node
  // }
}
