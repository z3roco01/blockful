package z3roco01.blockful.game.input

import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import z3roco01.blockful.render.Camera
import z3roco01.blockful.render.Window

/**
 * Handles all the keyboard input and uses [MouseInputHandler] to handle mouse input
 */
class InputHandler {
    // the cameras movement speed
    private var speed = 0.3f
    private var mouseInputHandler = MouseInputHandler()

    /**
     * does all the input initialization
     * @param window thw current [Window] of the game
     */
    fun init(window: Window) {
        // set the key callback to the method in this class
        glfwSetKeyCallback(window.getId(), this::keyCallback)

        // initialize the mouse input handler
        this.mouseInputHandler.init(window)
    }

    /**
     * the key callback, set to the callback method in [init]
     * @param window the id of the window
     * @param key the key id that was pressed
     * @param scancode the key scancode that was pressed
     * @param action the action that was taken on the key ( release, press, ect )
     * @param mods the modifiers such as super, alt, ctrl and shift
     */
    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        // if the escape key has just been release...
        if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true) // close the window
    }

    /**
     * returns if the left mouse button has been pressed
     * from [MouseInputHandler.isLeftButtonPressed]
     * @return true if it is pressed
     */
    fun isLeftMouseButtonPressed() = this.mouseInputHandler.isLeftButtonPressed()

    /**
     * returns if the right mouse button has been pressed
     * from [MouseInputHandler.isRightButtonPressed]
     * @return true if it is pressed
     */
    fun isRightMouseButtonPressed() = this.mouseInputHandler.isRightButtonPressed()

    /**
     * handles all the keyboard input, then passes it off the [MouseInputHandler.handleInput]
     * @param camera the [Camera] that is being controlled
     * @param window the [Window] of the game
     */
    fun handleInput(camera: Camera, window: Window) {
        // the vector that the camera will be moved by
        val moveVec = Vector3f()

        // calculate the amount based on what keys are pressed
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

        // apply the movement to the camera
        camera.move(moveVec)

        // handle all the mouse input afterwards
        this.mouseInputHandler.handleInput(camera, window)
    }
}