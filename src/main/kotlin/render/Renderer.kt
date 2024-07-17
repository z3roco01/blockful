package z3roco01.blockful.render

import org.joml.Math
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.render.mesh.BlockMesh
import z3roco01.blockful.render.shader.ShaderProgram

class Renderer {
    val window = Window()
    private val shader = ShaderProgram("main")
    private val verts: FloatArray = floatArrayOf(
        -1.0f,  1.0f, -6.0f,
        -1.0f, -1.0f, -6.0f,
         1.0f, -1.0f, -6.0f,
         1.0f,  1.0f, -6.0f,
    )
    private val inds: IntArray = intArrayOf(
        0, 1, 2,
        0, 3, 2
    )
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
    private val FOV: Float = Math.toRadians(90.0f)
    private val Z_NEAR = 0.01f
    private val Z_FAR = 1000f
    private lateinit var projMatrix: Matrix4f
    private val mesh = BlockMesh(colours)

    fun init() {
        if(!glfwInit())
            throw IllegalStateException("couldnt init glfw :(")

        this.window.init()

        this.shader.init()

        this.mesh.init()

        // create the projection matrix
        this.projMatrix = Matrix4f().perspective(FOV, this.window.getAspectRatio(), Z_NEAR, Z_FAR)

        this.shader.createUniformLocation("projMatrix")
    }

    fun windowShouldClose(): Boolean {
        return this.window.shouldClose()
    }

    fun setWindowKeyCallback(callback: GLFWKeyCallbackI) {
        this.window.setKeyCallback(callback)
    }

    fun getProjectionMatrix(): Matrix4f {
       return Matrix4f().perspective(FOV, this.window.getAspectRatio(), Z_NEAR, Z_FAR)
    }

    fun render(camera: Camera) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        this.shader.bind()

        this.shader.setUniform("projMatrix", this.getProjectionMatrix().mul(camera.getViewMatrix()))

        this.mesh.render()

        this.shader.unbind()

        this.window.swapBuffers()
    }

    fun fini() {
        this.shader.fini()

        this.mesh.fini()

        this.window.fini()
    }
}