package z3roco01.blockful.game.input

import org.joml.Math
import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*
import z3roco01.blockful.render.Camera
import z3roco01.blockful.render.Window

/**
 * Handles the input from the mouse
 */
class MouseInputHandler {
    private var mouseSensitivity = 0.2f
    private var rotateVec = Vector2f(0f, 0f)
    private var prevMousePos = Vector2d(-1.0, -1.0)
    // set in the cursor pos callback
    private var curMousePos = Vector2d(0.0, 0.0)
    // set in the cursor enter callback
    private var inWindow = true
    // set in the mouse button callback
    private var leftButtonPressed = false
    private var rightButtonPressed = false

    /**
     * does all the initilaization for the mouse
     */
    fun init(window: Window) {
        // do not show the cursor
        glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        glfwSetInputMode(window.getId(), GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
        // sets the cursor to the middle of the window
        glfwSetCursorPos(window.getId(), window.getWidthPx().toDouble() / 2, window.getHeightPx().toDouble() / 2)

        // creates a callback for when the mouse position changes
        // it sets the mouse position vector to the updated values
        glfwSetCursorPosCallback(window.getId()) { windowId, posX, posY ->
            this.curMousePos.x = posX
            this.curMousePos.y = posY
        }

        // creates a callback when the mouse leaves or enters the window
        // sets the value tracking that to the updated value
        glfwSetCursorEnterCallback(window.getId()) { windowId, entered ->
            this.inWindow = entered
        }

        // creates a callback when a mouse button is pressed
        // sets the left and right pressed variables to the new values
        glfwSetMouseButtonCallback(window.getId()) { windowId, button, action, mode ->
            this.leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS)
            this.rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS)
        }
    }

    /**
     * gets the display vector, which is the same as the rotate vector
     * @return a [Vector2f] with the current display vector
     */
    fun getDisplayVector() = this.rotateVec

    /**
     * returns if the left mouse button is pressed
     * @return a boolean, true if it is pressed
     */
    fun isLeftButtonPressed() = this.leftButtonPressed

    /**
     * returns if the right mouse button is pressed
     * @return true if it is pressed
     */
    fun isRightButtonPressed() = this.rightButtonPressed

    /**
     * called in [InputHandler.handleInput], called when the input need sto be handled
     * recalculates [rotateVec] and rotates the camera
     * @param camera a [Camera], the camera that is being controlled
     * @param window a [Window], the current window of this program
     */
    fun handleInput(camera: Camera, window: Window) {
        rotateVec.zero()

        // recalculate the rotate vector, stays zero if the mouse has not moved
        if(prevMousePos.x > 0 && prevMousePos.y > 0 && inWindow) {
            val deltaX = curMousePos.x - prevMousePos.x
            val deltaY = curMousePos.y - prevMousePos.y

            if(deltaX != 0.0)
                rotateVec.y = deltaX.toFloat()
            if(deltaY != 0.0)
                rotateVec.x = deltaY.toFloat()
        }
        // set the previous mouse position to the current one
        prevMousePos.x = curMousePos.x
        prevMousePos.y = curMousePos.y

        // rotate the camera, apply the sensitivity to the rotation and clamp the y rotation to 180 degrees total
        camera.rotate(rotateVec.x * mouseSensitivity, Math.clamp(rotateVec.y * mouseSensitivity, -90f, 90f), 0f)

    }
}