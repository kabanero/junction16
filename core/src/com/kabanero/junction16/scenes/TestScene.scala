package com.kabanero.junction16.scenes

import com.kabanero.junction16.scene.Scene
import com.kabanero.junction16.scene.Node
import com.kabanero.junction16.AllInputs
import com.kabanero.junction16.collision.BoxCollider
import com.kabanero.junction16.collision.SphereCollider
import scala.collection.mutable.Map
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.utils.UBJsonReader
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.physics.box2d
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.kabanero.junction16.level.TestLevel

class TestScene(iAmGood: Boolean) extends Scene {
  val PLAYER_SPEED = 110.0f
  val CAMERA_SPEED = 1 / 5.0f
  val UP = new Vector3(0, 1, 0)
  val RIGHT = new Vector3(-1, 0, 0)

  val world = new World(new Vector2(0, 0), true)

  val models = Map[String, Model]()

  val jsonReader = new JsonReader()
  val modelLoader = new G3dModelLoader(jsonReader);

  val collisionSizes = Map[String, Tuple2[Float, Float]]()

  models("map_shape") = modelLoader.loadModel(Gdx.files.internal("map_shape.g3dj"))
  models("map_roof") = modelLoader.loadModel(Gdx.files.internal("map_roof.g3dj"))

  models("doc_head") = modelLoader.loadModel(Gdx.files.internal("doc_head.g3dj"))
  models("doc_body") = modelLoader.loadModel(Gdx.files.internal("doc_body.g3dj"))

  models("doc_head_attack") = modelLoader.loadModel(Gdx.files.internal("doc_head_attack.g3dj"))
  models("doc_body_attack") = modelLoader.loadModel(Gdx.files.internal("doc_body_attack.g3dj"))

  models("ent_bed") = modelLoader.loadModel(Gdx.files.internal("ent_bed.g3dj"))
  collisionSizes("ent_bed") = (1f,0.5f)

  models("ent_cabinet") = modelLoader.loadModel(Gdx.files.internal("ent_cabinet.g3dj"))
  collisionSizes("ent_cabinet") = (0.5f,0.5f)

  models("ent_chair") = modelLoader.loadModel(Gdx.files.internal("ent_chair.g3dj"))
  collisionSizes("ent_chair") = (0.5f,0.5f)

  models("ent_coffee_table") = modelLoader.loadModel(Gdx.files.internal("ent_coffee_table.g3dj"))
  collisionSizes("ent_coffee_table") = (1f,0.25f)

  models("ent_desk") = modelLoader.loadModel(Gdx.files.internal("ent_desk.g3dj"))
  collisionSizes("ent_desk") = (0.5f,1f)

  models("ent_device_ctrl_panel") = modelLoader.loadModel(Gdx.files.internal("ent_device_ctrl_panel.g3dj"))
  collisionSizes("ent_device_ctrl_panel") = (0.4f,0.4f)

  models("ent_device_iv") = modelLoader.loadModel(Gdx.files.internal("ent_device_iv.g3dj"))
  collisionSizes("ent_device_iv") = (0.2f,0.2f)

  models("ent_device_screens") = modelLoader.loadModel(Gdx.files.internal("ent_device_screens.g3dj"))
  collisionSizes("ent_device_screens") = (0.5f,0.5f)

  models("ent_sofa") = modelLoader.loadModel(Gdx.files.internal("ent_sofa.g3dj"))
  collisionSizes("ent_sofa") = (1.0f,0.5f)

  models("ent_stand") = modelLoader.loadModel(Gdx.files.internal("ent_stand.g3dj"))
  collisionSizes("ent_stand") = (0.25f,0.25f)

  models("ent_stool") = modelLoader.loadModel(Gdx.files.internal("ent_stool.g3dj"))
  collisionSizes("ent_stool") = (0.25f,0.25f)

  models("ent_table_coffee_machine") = modelLoader.loadModel(Gdx.files.internal("ent_table_coffee_machine.g3dj"))
  collisionSizes("ent_table_coffee_machine") = (0.5f,0.5f)

  models("ent_table") = modelLoader.loadModel(Gdx.files.internal("ent_table.g3dj"))
  collisionSizes("ent_table") = (0.5f,0.5f)

  models("evil_body") = modelLoader.loadModel(Gdx.files.internal("evil_body.g3dj"))
  models("evil_head") = modelLoader.loadModel(Gdx.files.internal("evil_head.g3dj"))

  models("evil_body_attack") = modelLoader.loadModel(Gdx.files.internal("evil_body_attack.g3dj"))
  models("evil_head_attack") = modelLoader.loadModel(Gdx.files.internal("evil_head_attack.g3dj"))

  models("npc_doctor") = modelLoader.loadModel(Gdx.files.internal("npc_doctor.g3dj"))
  collisionSizes("npc_doctor") = (0.25f,0.25f)

  models("npc_doctor_dead") = modelLoader.loadModel(Gdx.files.internal("npc_doctor_dead.g3dj"))
  collisionSizes("npc_doctor_dead") = (0.1f,0.1f)

  var unPressed = false

  var possessedNode: Option[Node] = None

  def unpossessScript(delta: Float, node: Node, inputs: AllInputs) {
    if (possessedNode.isDefined) {
      if (inputs.ownInputs.action) {
        if (unPressed) {
          println("UNPOSSESS")
          possessedNode.get.children -= cameraNode
          val pos = new Vector3(possessedNode.get.localPosition)
          pos.add(possessedNode.get.forward)
          // node.children.clear()
          cameraNode.localPosition.set(new Vector3(0, 0, 0))
          enemyHead.addChild(cameraNode)
          possessedNode.get.isPossessed = false
          possessedNode = None
          enemyNode.physicsBody.get.setTransform(pos.x, pos.z, 0)
          possessTimer += 2.0f
          unPressed = false
        }
      } else {
        unPressed = true
      }
    }
  }

  def ownMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationY = new Quaternion(UP, -inputs.ownInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.ownInputs.mouseY * CAMERA_SPEED)

    val body = node.physicsBody.get

    node.localRotation.set(rotationY)

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

    val velo = forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED).add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))

    body.setLinearVelocity(velo.x, velo.z)


    // val bodyPos = node.physicsBody.get.getPosition()
    //
    // node.localPosition = new Vector3(bodyPos.x, 0, bodyPos.y)
  }

  val playerModel = new ModelInstance(models("doc_body"));
  val playerHeadModel = new ModelInstance(models("doc_head"));
  val playerAttackModel = new ModelInstance(models("doc_body_attack"));
  val playerHeadAttackModel = new ModelInstance(models("doc_head_attack"));

  val enemyModel = new ModelInstance(models("evil_body"));
  val enemyHeadModel = new ModelInstance(models("evil_head"));
  val enemyAttackModel = new ModelInstance(models("evil_body_attack"));
  val enemyHeadAttackModel = new ModelInstance(models("evil_head_attack"));

  def playerAction(delta: Float, node: Node, inputs: AllInputs) {
    if (iAmGood) {
      val actionState = inputs.ownInputs.action
      node.isAttacking = actionState
    } else {
      val actionState = inputs.otherInputs.action
      node.isAttacking = actionState
    }
  }

  def playerHeadAction(delta: Float, node: Node, inputs: AllInputs) {
    if (iAmGood) {
      val actionState = inputs.ownInputs.action
      node.isAttacking = actionState
    } else {
      val actionState = inputs.otherInputs.action
      node.isAttacking = actionState
    }
  }

  // var unPressed = false
  //
  // def unpossessScript(delta: Float, node: Node, inputs: AllInputs) {
  //   if (inputs.ownInputs.action) {
  //     if (unPressed) {
  //       println("UNPOSSESS")
  //       node.children -= cameraNode
  //       enemyHead.addChild(cameraNode)
  //       node.updateMethods(0) = (delta: Float, node: Node, inputs: AllInputs) => {}
  //       unPressed = false
  //     }
  //   } else {
  //     unPressed = true
  //   }
  // }

  var possessTimer = 0.0f

  def enemyAction(delta: Float, node: Node, inputs: AllInputs) {
    if (possessTimer > 0.0f) {
      possessTimer -= delta
    }
    if (!iAmGood) {
      val actionState = inputs.ownInputs.action
      node.isAttacking = actionState
      if (actionState && possessedNode.isEmpty && possessTimer <= 0.0f) {
        val nodes = rootNode.flatten

        val probePos = new Vector3(node.localPosition)
        probePos.add(node.forward)

        var distance = 0.7f
        var found: Option[Node] = None

        nodes.foreach { otherNode =>
          if (otherNode.name != node.name && otherNode.isDynamic) {
            val diff = new Vector3(otherNode.localPosition)
            diff.sub(probePos)
            val l = diff.len
            if (l < distance) {
              distance = l
              found = Some(otherNode)
            }
          }
        }

        if (found.isDefined) {
          node.children(0).children -= cameraNode
          cameraNode.localPosition.set(new Vector3(0, 1, 0))
          val foundNode = found.get
          foundNode.addChild(cameraNode)
          foundNode.isPossessed = true
          possessedNode = Some(foundNode)
          node.physicsBody.get.setTransform(10000, 0, 0)
          // enemyNode.localPosition.set(new Vector3(10000, 0, 0))
          // foundNode.updateMethods += ownMovement
          // foundNode.updateMethods += unpossessScript
        }
      }
    } else {
      val actionState = inputs.otherInputs.action
      node.isAttacking = actionState
      if (actionState) {
        node.modelInstance = Some(enemyAttackModel);
      } else {
        node.modelInstance = Some(enemyModel);
      }
    }
  }

  def enemyHeadAction(delta: Float, node: Node, inputs: AllInputs) {
    if (!iAmGood) {
      val actionState = inputs.ownInputs.action
      node.isAttacking = actionState
    } else {
      val actionState = inputs.otherInputs.action
      node.isAttacking = actionState
      if (actionState) {
        node.modelInstance = Some(enemyHeadAttackModel);
      } else {
        node.modelInstance = Some(enemyHeadModel);
      }
    }
  }

  def otherMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationY = new Quaternion(UP, -inputs.otherInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.otherInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationY)

    val moveDirection = new Vector3(0, 0, 0)

    val body = node.physicsBody.get

    val forwardMove = node.forward
    forwardMove.y = 0
    forwardMove.nor()
    val rightMove = node.right
    rightMove.y = 0
    rightMove.nor()
    if (inputs.otherInputs.up) {
      moveDirection.z += 1
    }
    if (inputs.otherInputs.down) {
      moveDirection.z -= 1
    }
    if (inputs.otherInputs.left) {
      moveDirection.x += 1
    }
    if (inputs.otherInputs.right) {
      moveDirection.x -= 1
    }

    val velo = forwardMove.scl(moveDirection.z * delta * PLAYER_SPEED).add(rightMove.scl(moveDirection.x * delta * PLAYER_SPEED))

    body.setLinearVelocity(velo.x, velo.z)

    val bodyPos = node.physicsBody.get.getPosition()

    node.localPosition = new Vector3(bodyPos.x, 0, bodyPos.y)
  }


  def otherHeadMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationX = new Quaternion(RIGHT, -inputs.otherInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationX)
  }

  def ownHeadMovement(delta: Float, node: Node, inputs: AllInputs) {
    val rotationX = new Quaternion(RIGHT, -inputs.ownInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationX)
  }

  val playerNode = {
		val node = Node("player")
    node.isDynamic = true
    node.overrideRotation = true
    node.localPosition.add(14.0f, 0, 5.0f)

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

    if (iAmGood) {
      node.updateMethods += ownMovement
    } else {
      node.updateMethods += otherMovement
      val instance = new ModelInstance(models("doc_body"))
      node.modelInstance = Some(instance)

      node.updateVisualMethods += ((delta: Float, node: Node, inputs: AllInputs) => {
        if (node.isAttacking) {
          node.modelInstance = Some(playerAttackModel)
        } else {
          node.modelInstance = Some(playerModel)
        }
      })
    }

    node.updateMethods += playerAction

    node
  }
  val enemyNode = {
    val node = Node("enemy")
    node.isDynamic = true
    node.overrideRotation = true
    node.localPosition.add(6.0f, 0.0f, 6.0f)

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

    if (iAmGood) {
      node.updateMethods += otherMovement

      node.updateVisualMethods += ((delta: Float, node: Node, inputs: AllInputs) => {
        if (node.isAttacking) {
          node.modelInstance = Some(enemyAttackModel)
        } else {
          node.modelInstance = Some(enemyModel)
        }
      })
    } else {
      node.updateMethods += ownMovement
    }

    node.updateMethods += enemyAction
    node.updateMethods += unpossessScript

		node
  }

  cameraNode.updateVisualMethods += ( (delta: Float, node: Node, inputs: AllInputs) => {
    val rotationY = new Quaternion(UP, -inputs.ownInputs.mouseX * CAMERA_SPEED)
    val rotationX = new Quaternion(RIGHT, -inputs.ownInputs.mouseY * CAMERA_SPEED)
    node.localRotation.set(rotationY.mul(rotationX))

    node.localRotation.set(rotationY.mul(rotationX))
  })

  val playerHead = {
    val node = Node("player head")
    node.isDynamic = true

    node.localPosition.add(0, 1.25f, 0)
    if (!iAmGood) {
      node.updateMethods += otherHeadMovement

      node.updateVisualMethods += ((delta: Float, node: Node, inputs: AllInputs) => {
        if (node.isAttacking) {
          node.modelInstance = Some(playerHeadAttackModel)
        } else {
          node.modelInstance = Some(playerHeadModel)
        }
      })
    } else {
      node.updateMethods += ownHeadMovement
    }

    node.updateMethods += playerHeadAction

    node
  }

  val enemyHead = {
    val node = Node("enemy head")
    node.localPosition.add(0, 1.25f, 0)

    node.isDynamic = true

    if (iAmGood) {
      node.updateMethods += otherHeadMovement

      node.updateVisualMethods += ((delta: Float, node: Node, inputs: AllInputs) => {
        if (node.isAttacking) {
          node.modelInstance = Some(enemyHeadAttackModel)
        } else {
          node.modelInstance = Some(enemyHeadModel)
        }
      })
    } else {
      node.updateMethods += ownHeadMovement
    }

    node.updateMethods += enemyHeadAction

    node
  }

  if (iAmGood) {
    playerHead.addChild(cameraNode)
  } else {
    enemyHead.addChild(cameraNode)
  }
  playerNode.addChild(playerHead)
  enemyNode.addChild(enemyHead)

	// val cubeNode = {
	// 	val node = Node("screen thingy")
  //
	// 	val instance = new ModelInstance(models("ent_device_screens"));
	// 	node.modelInstance = Some(instance);
  //
	// 	node.localPosition = new Vector3(0, 0, 8)
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
	// 	node
	// }

  rootNode.addChild(playerNode)
  rootNode.addChild(enemyNode)
  // rootNode.addChild(cubeNode)

  val level = new TestLevel(models, world, collisionSizes)
  rootNode.addChild(level.levelRoot)

  override def update(delta: Float, inputs: AllInputs) = {
    super.update(delta, inputs)

    world.step(delta, 6, 2)
  }
}
