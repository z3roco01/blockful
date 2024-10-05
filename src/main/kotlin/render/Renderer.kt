package z3roco01.blockful.render

import org.joml.Math
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.render.mesh.BlockMesh
import z3roco01.blockful.render.shader.ShaderProgram

/**
 * handles the rendering of everything, also the clipping plane and projection matrix and fov
 */
class Renderer {
    // the window for the game
    val window = Window()
    // the shader program
    private val shader = ShaderProgram("main")
    private val colours: FloatArray = floatArrayOf(
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        0.5f, 0.25f, 0.5f,
    )
    // the cameras fov in radians
    private val FOV: Float = Math.toRadians(90.0f)
    // the near clipping plane
    private val Z_NEAR = 0.01f
    // and the far clipping plane
    private val Z_FAR = 1000f
    // the projection matrix for the camera
    private lateinit var projMatrix: Matrix4f
    private val mesh = BlockMesh(colours)

    fun init() {
        // if it cannot init glfw, throw
        if(!glfwInit())
            throw IllegalStateException("couldnt init glfw :(")

        // init everything
        this.window.init()
        this.shader.init()
        this.mesh.init()

        // create the projection matrix
        this.projMatrix = Matrix4f().perspective(FOV, this.window.getAspectRatio(), Z_NEAR, Z_FAR)

        // create a uniform for the projection matrix
        this.shader.createUniformLocation("projMatrix")
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
     * recalculates the projection matrix then returns it
     * @return the calculated projection matrix
     */
    fun getProjectionMatrix() = Matrix4f().perspective(FOV, this.window.getAspectRatio(), Z_NEAR, Z_FAR)

    /**
     * actually renders everything
     * @param camera the [Camera] for it
     */
    fun render(camera: Camera) {
        // clear the colour buffer and depth buffer bits
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // bind the shader
        this.shader.bind()
        // set the projection matrix, multiplied by the cameras view matrix
        this.shader.setUniform("projMatrix", this.getProjectionMatrix().mul(camera.getViewMatrix()))

        this.mesh.render()

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
        this.mesh.fini()
        this.window.fini()
    }
}