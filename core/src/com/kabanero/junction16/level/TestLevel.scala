package com.kabanero.junction16.level

import com.kabanero.junction16.scene.Node
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion

class TestLevel(models: collection.mutable.Map[String, Model], world: World) {
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
    n.localPosition = new Vector3(0, -0.1f, 0)
    n
  }

  levelRoot.addChild(floorNode)

  val level = Array(
    Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
  )

  for (y <- 0 until level.length) {
    for (x <- 0 until level(0).length) {
      if (level(y)(x) != 0) {
        val node = createLevelNode(level(y)(x), x, y)
        levelRoot.addChild(node)
      }
    }
  }

  def createLevelNode(gridType: Int, x: Int, y: Int): Node = {
    val node = Node("level-" + x + "-" + y)
    gridType match {
      case 1 => node.modelInstance = Some(new ModelInstance(wallModel))
      case _ => { }
    }
    node.localPosition = new Vector3(x - level(0).length/2, 1.5f, y - level.length / 2)

    val groundBodyDef = new BodyDef();
    groundBodyDef.position.set(new Vector2(node.localPosition.x, node.localPosition.z));
    val groundBody = world.createBody(groundBodyDef)
    val groundBox = new PolygonShape()
    groundBox.setAsBox(1f, 1f)
    groundBody.createFixture(groundBox, 0.0f)
    groundBox.dispose()

    node.physicsBody = Some(groundBody)

    node
  }
}
