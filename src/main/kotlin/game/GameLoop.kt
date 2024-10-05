package z3roco01.blockful.game

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.game.input.InputHandler
import z3roco01.blockful.render.Camera
import z3roco01.blockful.render.Renderer

/**
 * handles the main game loop
 */
class GameLoop {
    private val renderer = Renderer()
    private val camera = Camera()
    private val inputHandler = InputHandler()

    /**
     * called to run the game
     */
    fun run() {
        println("LWJGL v" + Version.getVersion())

        this.init()
        this.loop()
        this.fini()
    }

    /**
     * handles the initialization of everything needed
     */
    fun init() {
        GLFWErrorCallback.createPrint(System.err).set()

        this.renderer.init()
        this.inputHandler.init(this.renderer.window)
    }

    /**
     * the actual game loop, calls [InputHandler.handleInput] and [Renderer.render]
     * also clears the screen and polls events
     */
    fun loop() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // run until the window is set to close
        while(!this.renderer.windowShouldClose()) {
            this.inputHandler.handleInput(this.camera, this.renderer.window)
            this.renderer.render(this.camera)

            glfwPollEvents()
        }
    }

    /**
     * called after the game closes
     */
    fun fini() {
        this.renderer.fini()
    }
}