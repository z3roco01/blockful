package z3roco01.blockful.render.shader

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryStack

class ShaderProgram(val vertName: String, val fragName: String) {
    constructor(shaderName: String) : this(shaderName, shaderName)

    private val uniformMap = HashMap<String, Int>()

    private var programId: Int = 0
    private var vertShader = Shader(vertName, GL_VERTEX_SHADER)
    private var fragShader = Shader(fragName, GL_FRAGMENT_SHADER)

    fun init() {
        this.programId = glCreateProgram()
        if(this.programId == 0)
            throw IllegalStateException("Coulnt make shader program :(")

        vertShader.createShader()
        glAttachShader(this.programId, vertShader.getId())
        fragShader.createShader()
        glAttachShader(this.programId, fragShader.getId())
        this.link()
    }

    fun bind() {
        glUseProgram(this.programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun createUniformLocation(uniformName: String) {
        val uniformId = glGetUniformLocation(this.programId, uniformName)
        if(uniformId < 0) throw IllegalStateException("could not create uniform for :$uniformName :(")

        uniformMap.put(uniformName, uniformId)
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        if(uniformMap.get(uniformName) == null) throw IllegalStateException("could not find uniform $uniformName :(")

        val stack = MemoryStack.stackPush()

        val floatBuf = stack.mallocFloat(16)
        value.get(floatBuf)
        glUniformMatrix4fv(uniformMap.get(uniformName)!!, false, floatBuf)

        stack.pop()
    }

    fun fini() {
        this.unbind()
        if(this.programId != 0)
            glDeleteProgram(this.programId)
    }

    private fun link() {
        glLinkProgram(this.programId)
        if(glGetProgrami(this.programId, GL_LINK_STATUS) == 0) throw IllegalStateException("couldnt link shader code :( with error: " + glGetProgramInfoLog(this.programId))

        glDetachShader(this.programId, this.vertShader.getId())
        glDetachShader(this.programId, this.fragShader.getId())

        glValidateProgram(this.programId)
        if(glGetProgrami(this.programId, GL_VALIDATE_STATUS) == 0) System.err.println("validating shader code :3 : " + glGetProgramInfoLog(this.programId))
    }
}