package z3roco01.blockful.render.shader

import org.joml.Matrix4f
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryStack

/**
 * handles the creation of a shader program, with a vertex and fragment shader
 * @param vertName the name of the vertex shader
 * @param fragName the name of the fragment shader
 */
class ShaderProgram(vertName: String, fragName: String) {
    /**
     * alt constructor where both shaders share the same name
     * @param shaderName the name of both the fragment and vertex shader
     */
    constructor(shaderName: String) : this(shaderName, shaderName)

    // maps all uniforms to their id
    private val uniformMap = HashMap<String, Int>()

    // id of the shader program
    private var programId: Int = 0
    private var vertShader = Shader(vertName, GL_VERTEX_SHADER)
    private var fragShader = Shader(fragName, GL_FRAGMENT_SHADER)

    /**
     * handles the initialization of both the individual shaders and the shader program
     */
    fun init() {
        // create a program id
        this.programId = glCreateProgram()
        // if it cannot, throw an execption
        if(this.programId == 0) throw IllegalStateException("Coulnt make shader program :(")

        // try to create the vertex shader
        vertShader.createShader()
        // then attach it to this program
        glAttachShader(this.programId, vertShader.getId())

        // do the same with the fragment shader
        fragShader.createShader()
        glAttachShader(this.programId, fragShader.getId())
        this.link()
    }

    /**
     * binds this shader program, makes it usable
     */
    fun bind() {
        glUseProgram(this.programId)
    }

    /**
     * unbinds the shader program
     */
    fun unbind() {
        glUseProgram(0)
    }

    /**
     * creates a uniform and its id, then maps it to the supplied name in [uniformMap]
     * @param uniformName the name of this uniform
     */
    fun createUniformLocation(uniformName: String) {
        // attempt to create the id with the supplied name
        val uniformId = glGetUniformLocation(this.programId, uniformName)
        // if it cannot, throw an exception
        if(uniformId < 0) throw IllegalStateException("could not create uniform for :$uniformName :(")

        // map the uniform name to its id
        uniformMap.put(uniformName, uniformId)
    }

    /**
     * set the value of a uniform with its name
     * @param uniformName the name of the uniform
     * @param value the [Matrix4f] that it will be set to
     */
    fun setUniform(uniformName: String, value: Matrix4f) {
        // first check if the uniform exists, if not throw an exception
        if(uniformMap.get(uniformName) == null) throw IllegalStateException("could not find uniform $uniformName :(")

        // push the stack then get it
        val stack = MemoryStack.stackPush()

        // create a float buffer for the values
        val floatBuf = stack.mallocFloat(16)
        // copy the values into the float buffer
        value.get(floatBuf)
        // then actually set the value of the uniform
        glUniformMatrix4fv(uniformMap.get(uniformName)!!, false, floatBuf)

        // and pop the stack
        stack.pop()
    }

    /**
     * called when this shader program is no longer in use, also calls [Shader.fini] on the frag and vert shaders
     */
    fun fini() {
        // unbind this shader
        this.unbind()

        // call the vertex and frag shaders fini
        this.vertShader.fini()
        this.fragShader.fini()

        // as long as the program id is valid ( it should be ) delete the program
        if(this.programId != 0)
            glDeleteProgram(this.programId)
    }

    /**
     * links the shader code to this program
     */
    private fun link() {
        // link up all the code
        glLinkProgram(this.programId)
        // if it couldnt, throw an exception
        if(glGetProgrami(this.programId, GL_LINK_STATUS) == 0) throw IllegalStateException("couldnt link shader code :( with error: " + glGetProgramInfoLog(this.programId))

        // detach both the shaders as theyre no longer needed
        glDetachShader(this.programId, this.vertShader.getId())
        glDetachShader(this.programId, this.fragShader.getId())

        // validate this shader program
        glValidateProgram(this.programId)
        // if it is not valid then throw an exception
        if(glGetProgrami(this.programId, GL_VALIDATE_STATUS) == 0) System.err.println("validating shader code :3 : " + glGetProgramInfoLog(this.programId))
    }
}