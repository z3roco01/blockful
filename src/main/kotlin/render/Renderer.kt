package z3roco01.blockful.render

import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.game.chunk.Chunk
import z3roco01.blockful.math.Colour

/**
 * handles the rendering of everything, also the clipping plane and projection matrix and fov
 */
class Renderer {
    // the window for the game
    val window = Window()
    // colour used in glClearColor
    val clearColour = Colour(0x9D, 0xCA, 0xEB)

    private val chunk = Chunk(0, 0)
    private val chunk2 = Chunk(2, 2)
    //private val chunkManager = ChunkManager()

    fun init() {
        // if it cannot init glfw, throw
        if(!glfwInit())
            throw IllegalStateException("couldnt init glfw :(")

        // init everything
        this.window.init()
        this.chunk.init(this)
        //this.chunkManager.init()

        glClearColor(clearColour.rFloat(), clearColour.gFloat(), clearColour.bFloat(), clearColour.aFloat())
    }

    /**
     * returns if the window should close, from [Window.shouldClose]
     * @return true if it should close
     */
    fun windowShouldClose() = this.window.shouldClose()

    /**
     * set the key callback to the supplied callback
     * @param callback the callback
     */
    fun setWindowKeyCallback(callback: GLFWKeyCallbackI) {
        this.window.setKeyCallback(callback)
    }

    /**
     * actually renders everything
     * @param camera the [Camera] for it
     */
    fun render(camera: Camera) {
        // clear the colour buffer and depth buffer bits
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)


        this.chunk.render(this, camera)
        //this.chunkManager.render(this)

        // swap the buffers to show the rendered stuff
        this.window.swapBuffers()
    }

    /**
     * handles the closing of everything
     */
    fun fini() {
        //this.chunkManager.fini()
        this.chunk.fini()
        this.window.fini()
    }
}