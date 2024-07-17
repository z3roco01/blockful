package z3roco01.blockful.game.input

import org.joml.Math
import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*
import z3roco01.blockful.render.Camera
import z3roco01.blockful.render.Window

class MouseInputHandler {
    private var mouseSensitivity = 0.2f
    private var prevMousePos = Vector2d(-1.0, -1.0)
    private var curMousePos = Vector2d(0.0, 0.0)
    private var rotateVec = Vector2f(0f, 0f)
    private var inWindow = false
    private var leftButtonPressed = false
    private var rightButtonPressed = false

    fun init(window: Window) {
        glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        glfwSetInputMode(window.getId(), GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
        glfwSetCursorPos(window.getId(), window.getWindowWidthPx().toDouble() / 2, window.getWindowHeightPx().toDouble() / 2)

        glfwSetCursorPosCallback(window.getId()) { windowId, posX, posY ->
            this.curMousePos.x = posX
            this.curMousePos.y = posY
        }

        glfwSetCursorEnterCallback(window.getId()) { windowId, entered ->
            this.inWindow = entered
        }

        glfwSetMouseButtonCallback(window.getId()) { windowId, button, action, mode ->
            this.leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            this.rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
        }
    }

    fun getDisplayVector(): Vector2f {
        return this.rotateVec
    }

    fun isLeftButtonPressed(): Boolean {
        return this.leftButtonPressed
    }

    fun isRightButtonPressed(): Boolean {
        return this.rightButtonPressed
    }

    fun handleInput(camera: Camera, window: Window) {
        rotateVec.zero()

        if(prevMousePos.x > 0 && prevMousePos.y > 0 && inWindow) {
            val deltaX = curMousePos.x - prevMousePos.x
            val deltaY = curMousePos.y - prevMousePos.y

            if(deltaX != 0.0)
                rotateVec.y = deltaX.toFloat()
            if(deltaY != 0.0)
                rotateVec.x = deltaY.toFloat()
        }
        prevMousePos.x = curMousePos.x
        prevMousePos.y = curMousePos.y

        camera.rotate(rotateVec.x * mouseSensitivity, Math.clamp(rotateVec.y * mouseSensitivity, -90f, 90f), 0f)

    }
}