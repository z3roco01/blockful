package z3roco01.blockful.game.input

import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import z3roco01.blockful.render.Camera
import z3roco01.blockful.render.Window

class InputHandler {
    private var speed = 0.3f
    private var mouseInputHandler = MouseInputHandler()

    fun init(window: Window) {
        glfwSetKeyCallback(window.getId(), this::keyCallback)

        this.mouseInputHandler.init(window)
    }

    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true)
    }

    fun isLeftButtonPressed(): Boolean {
        return this.mouseInputHandler.isLeftButtonPressed()
    }

    fun isRightButtonPressed(): Boolean {
        return this.mouseInputHandler.isRightButtonPressed()
    }

    fun handleInput(camera: Camera, window: Window) {
        var moveX = 0f
        var moveY = 0f
        var moveZ = 0f
        val moveVec = Vector3f()
        if(window.isKeyPressed(GLFW_KEY_W))
            moveVec.z -= speed
        if(window.isKeyPressed(GLFW_KEY_S))
            moveVec.z += speed
        if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
            moveVec.y -= speed
        if(window.isKeyPressed(GLFW_KEY_SPACE))
            moveVec.y += speed
        if(window.isKeyPressed(GLFW_KEY_A))
            moveVec.x -= speed
        if(window.isKeyPressed(GLFW_KEY_D))
            moveVec.x += speed

        camera.move(moveVec)

        this.mouseInputHandler.handleInput(camera, window)
    }
}