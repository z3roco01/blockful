package z3roco01.blockful.render.shader

import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.util.ResourceUtil

/**
 * holds one shader of any type, handles the reading and compilation of it
 * @param name the name of the shader, concatenated onto a hardcoded path
 * @param shaderType the type of the shader, used for compilation and for the path
 */
class Shader(val name: String, val shaderType: Int) {
    private var id: Int = 0

    /**
     * creates the shader, creates the path, reads the content of it, then compiles it
     */
    fun createShader() {
        // gets the based path
        val path = getPath()

        // create a shader with the type, stores the id
        this.id = glCreateShader(this.shaderType)
        // if the shader could not be created, throw an exception
        if(this.id == 0) throw IllegalStateException("couldnt create shader for $path :(")

        // tries the read the shader code
        val shaderCode = ResourceUtil.getResourceContents(path)
        // if it cannot throw an exception
        if(shaderCode == null) throw IllegalStateException("couldnt read shader $path :(")

        // set the source of the shader to the read code
        glShaderSource(this.id, shaderCode)
        // compile the shader, supplying the type
        glCompileShader(this.id)

        // > 0 clause added to prevent random errors that arent actual errors
        if(glGetShaderi(this.id, GL_COMPILE_STATUS) == GL_FALSE && glGetShaderInfoLog(this.id, 1024).isNotEmpty())
            throw IllegalStateException("could not compile shader for " + path + " :( with err: " + glGetShaderInfoLog(this.id, 1024))
    }

    fun fini() {
        glDeleteShader(this.id)
    }

    /**
     * returns the shader id, will only be set after calling [createShader]
     * @return the id of this shader
     */
    fun getId() = this.id

    /**
     * creates the path of the shader using the type and name.
     * vertex shaders will be at shaders/vertex/<name> and fragment shaders will be at shaders/fragment/<name>
     * @return the path to the shader
     */
    private fun getPath(): String {
        if(shaderType == GL_VERTEX_SHADER)
            return "/shaders/vertex/$name.vert"
        else
            return "/shaders/fragment/$name.frag"
    }
}