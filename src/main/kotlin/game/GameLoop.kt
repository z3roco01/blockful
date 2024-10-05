package z3roco01.blockful.game

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.game.input.InputHandler
import z3roco01.blockful.render.Camera
import z3roco01.blockful.render.Renderer

class GameLoop {
    private val renderer = Renderer()
    private val camera = Camera()
    private val inputHandler = InputHandler()

    fun run() {
        println("LWJGL v" + Version.getVersion());

        this.init();
        this.loop();
        this.fini();
    }

    fun init() {
        GLFWErrorCallback.createPrint(System.err).set()

        this.renderer.init()
        this.inputHandler.init(this.renderer.window)
    }

    fun loop() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        while(!this.renderer.windowShouldClose()) {
            this.inputHandler.handleInput(this.camera, this.renderer.window)
            this.renderer.render(this.camera)

            glfwPollEvents()
        }
    }

    fun fini() {
        this.renderer.fini()
    }
}