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
import com.badlogic.gdx.math.Matrix4

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
    // gridType match {
    //   case 1 => node.modelInstance = Some(new ModelInstance(wallModel))
    //   case _ => { }
    // }
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

  def ownMovement(delta: Float, node: Node, inputs: AllInputs) {
    if (node.isPossessed) {
      val rotationY = new Quaternion(UP, -inputs.ownInputs.mouseX / 5.0f)
      val body = node.physicsBody.get

      val moveDirection = new Vector3(0, 0, 0)

      val mat = new Array[Float](16)

      rotationY.toMatrix(mat)

      val forwardMove = new Vector3(mat(Matrix4.M02), mat(Matrix4.M12), mat(Matrix4.M22))
      forwardMove.y = 0
      forwardMove.nor()
      val rightMove = node.right
      rightMove.y = 0
      rightMove.nor()
      if (inputs.ownInputs.up) {
        moveDirection.z += 1
      }
      if (inputs.ownInputs.down) {
        moveDirection.z -= 1
      }
      if (inputs.ownInputs.left) {
        moveDirection.x += 1
      }
      if (inputs.ownInputs.right) {
        moveDirection.x -= 1
      }

      val velo = forwardMove.scl(moveDirection.z * delta * 30).add(rightMove.scl(moveDirection.x * delta * 30))

      body.setLinearVelocity(velo.x, velo.z)
    }
  }

  def fastMovement(delta: Float, node: Node, inputs: AllInputs) {
    if (node.isPossessed) {
      val rotationY = new Quaternion(UP, -inputs.ownInputs.mouseX / 5.0f)
      val body = node.physicsBody.get

      node.localRotation.set(rotationY)
      node.overrideRotation = true

      val moveDirection = new Vector3(0, 0, 0)

      val forwardMove = node.forward
      forwardMove.y = 0
      forwardMove.nor()
      val rightMove = node.right
      rightMove.y = 0
      rightMove.nor()
      if (inputs.ownInputs.up) {
        moveDirection.z += 1
      }
      if (inputs.ownInputs.down) {
        moveDirection.z -= 1
      }
      if (inputs.ownInputs.left) {
        moveDirection.x += 1
      }
      if (inputs.ownInputs.right) {
        moveDirection.x -= 1
      }

      val velo = forwardMove.scl(moveDirection.z * delta * 110).add(rightMove.scl(moveDirection.x * delta * 110))

      body.setLinearVelocity(velo.x, velo.z)
    }
  }

  def createObjectNode(id: String, x: Float, y: Float, rotation: Float): Node = {
    val node = Node(name + "|" + id + "|" + x + "|" + y)

    node.localPosition = new Vector3(x, 0, y)

    node.modelInstance = Some(new ModelInstance(models(id)))

    val collisionSize = collisionSizes(id)

    val groundBodyDef = new BodyDef();
    groundBodyDef.`type` = BodyType.DynamicBody
    groundBodyDef.position.set(new Vector2(node.localPosition.x, node.localPosition.z));
    groundBodyDef.angle = rotation/180.0f * Math.PI.toFloat;
    groundBodyDef.linearDamping = 1.0f
    groundBodyDef.angularDamping = 1.0f
    val groundBody = world.createBody(groundBodyDef)

    val groundBox = new PolygonShape()
    groundBox.setAsBox(collisionSize._1, collisionSize._2)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = groundBox
    fixtureDef.density = 10.0f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.0f

    val fixture = groundBody.createFixture(fixtureDef)

    groundBox.dispose()

    node.physicsBody = Some(groundBody)

    node.isDynamic = true

    node.updateMethods += ownMovement

    node

  }

  // def wander(x1: Float, z: Float, )

  def genNPC(x: Float, z: Float): Node = {
    val node = Node("NPC|" + x + "|" + z)
    node.localPosition.set(new Vector3(x, 0, z))
    node.modelInstance = Some(new ModelInstance(models("npc_doctor")))

    val bodyDef = new BodyDef()
    bodyDef.`type` = BodyType.DynamicBody
    bodyDef.fixedRotation = true
    bodyDef.position.set(
      node.localPosition.x,
      node.localPosition.z)

    val body = world.createBody(bodyDef)

    val circle = new CircleShape()
    circle.setRadius(0.1f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = circle
    fixtureDef.density = 0.5f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.0f

    val fixture = body.createFixture(fixtureDef)

    circle.dispose()

    node.physicsBody = Some(body)

    node.isDynamic = true

    node.updateMethods += fastMovement

    node
  }

  rootNode.addChild(genNPC(14, 4))

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
