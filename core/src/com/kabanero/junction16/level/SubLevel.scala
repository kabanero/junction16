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
import com.kabanero.junction16.AllInputs

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion

abstract class SubLevel(
    name: String,
    models: collection.mutable.Map[String, Model],
    collisionSizes: collection.mutable.Map[String, Tuple2[Float, Float]],
    world: World) {

  val modelBuilder = new ModelBuilder();
  val wallModel = modelBuilder.createBox(1.0f, 3f, 1.001f,
       new Material(ColorAttribute.createDiffuse(new Color(203/255.0f, 216/255.0f, 183/255.0f, 1.0f))),
       Usage.Position | Usage.Normal)

  val rootNode = Node("root node")

  val level: Array[Array[Int]]

  def createLevelNode(gridType: Int, x: Float, y: Float): Node = {
    val node = Node("level-" + x + "-" + y)
    gridType match {
      case 1 => node.modelInstance = Some(new ModelInstance(wallModel))
      case _ => { }
    }
    node.localPosition = new Vector3(x, 1.5f, y)

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

  def createObjectNode(id: String, x: Float, y: Float, rotation: Float): Node = {
    val node = Node(name + "|" + id + "|" + x + "|" + y)

    node.localPosition = new Vector3(x, 0, y)

    node.modelInstance = Some(new ModelInstance(models(id)))

    val collisionSize = collisionSizes(id)

    val groundBodyDef = new BodyDef();
    groundBodyDef.position.set(new Vector2(node.localPosition.x, node.localPosition.z));
    groundBodyDef.angle = rotation/180.0f * Math.PI.toFloat;
    val groundBody = world.createBody(groundBodyDef)
    val groundBox = new PolygonShape()
    groundBox.setAsBox(collisionSize._1, collisionSize._2)
    groundBody.createFixture(groundBox, 0.0f)
    groundBox.dispose()

    node.physicsBody = Some(groundBody)

    node

  }

  val UP = new Vector3(0, 1, 0)

  var time = 0.0f

  def addObject(id: String, x: Float, y: Float, rotation: Float): Unit = {
    val n = createObjectNode(id, x, y, rotation)

    val rotationY = new Quaternion(UP, rotation)
    n.localRotation.set(rotationY)

    val body = n.physicsBody.get

    rootNode.addChild(n)
  }
}
