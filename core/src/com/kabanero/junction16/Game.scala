package com.kabanero.junction16

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Game extends ApplicationAdapter {
	lazy val batch = new SpriteBatch()
	lazy val img = new Texture("badlogic.jpg")

	override def create(): Unit = {

	}

	override def render(): Unit = {
		Gdx.gl.glClearColor(1, 0, 0, 1)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		batch.begin()
		batch.draw(img, 0, 0)
		batch.end()
	}

	override def dispose(): Unit = {
		batch.dispose()
		img.dispose()
	}
}
