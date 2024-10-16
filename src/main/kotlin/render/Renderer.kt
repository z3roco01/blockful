package z3roco01.blockful.render

import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.game.Chunk
import z3roco01.blockful.render.mesh.Mesh
import z3roco01.blockful.render.shader.ShaderProgram

/**
 * handles the rendering of everything, also the clipping plane and projection matrix and fov
 */
class Renderer {
    // the window for the game
    val window = Window()
    // the shader program
    private val shader = ShaderProgram("main")

    private val testMesh = Mesh(

        floatArrayOf(
             0.5f, -0.5f,   0.5f,
            -0.5f,  0.5f,   0.5f,
            -0.5f, -0.5f,   0.5f,
             0.5f,  0.5f,   0.5f,
             0.5f,  0.5f,  -0.5f,
            -0.5f,  0.5f,  -0.5f,
        ),
        intArrayOf(
            0, 1, 2,
            0, 3, 1,
            1, 3, 4,
            4, 5, 1
        ),
        floatArrayOf(
            0.46484375f, 0.86328125f, 0.46484375f,
            0.46484375f, 0.86328125f, 0.46484375f,
            0.46484375f, 0.86328125f, 0.46484375f,
            0.46484375f, 0.86328125f, 0.46484375f,
            0.46484375f, 0.86328125f, 0.46484375f,
            0.46484375f, 0.86328125f, 0.46484375f,
        )
    )
    private val chunk = Chunk(0, 0)

    fun init() {
        // if it cannot init glfw, throw
        if(!glfwInit())
            throw IllegalStateException("couldnt init glfw :(")

        // init everything
        this.window.init()
        this.shader.init()
        this.testMesh.init()
        this.chunk.init()
        this.testMesh.transformation.position.z -= 5

        glClearColor(0.785f, 0.7890625f, 0.91796875f, 1.0f)

        // create a uniform for the projection and matrices
        this.shader.createUniformLocation("projMatrix")
        this.shader.createUniformLocation("worldMatrix")
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

        // bind the shader
        this.shader.bind()
        // set the projection matrix to the cameras projection matrix
        this.shader.setUniform("projMatrix", camera.getProjectionMatrix(window.getAspectRatio()))

        this.shader.setUniform("worldMatrix", this.chunk.mesh.transformation.getWorldMatrix())
        this.chunk.render()

        // unbind the shader
        this.shader.unbind()
        // swap the buffers to show the rendered stuff
        this.window.swapBuffers()
    }

    /**
     * handles the closing of everything
     */
    fun fini() {
        this.shader.fini()
        this.testMesh.fini()
        this.chunk.fini()
        this.window.fini()
    }
}