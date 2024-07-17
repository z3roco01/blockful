package z3roco01.blockful.render.shader

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.util.ResourceUtil

class Shader(val name: String, val shaderType: Int) {
    private var id: Int = 0

    fun createShader() {
        val path = getPath()

        this.id = glCreateShader(shaderType)
        if(this.id == 0) throw IllegalStateException("couldnt create shader for " + path + " :(")

        val shaderCode = ResourceUtil.getResourceContents(path)
        if(shaderCode == null) throw IllegalStateException("couldnt read shader " + path + " :(")

        glShaderSource(this.id, shaderCode)
        glCompileShader(shaderType)

        // > 0 clause added to prevent random errors that arent actual errors
        if(glGetShaderi(this.id, GL_COMPILE_STATUS) == GL_FALSE && GL20.glGetShaderInfoLog(this.id, 1024).length > 0)
            throw IllegalStateException("could not compile shader for " + path + " :( with err: " + glGetShaderInfoLog(this.id, 1024))
    }

    fun getId(): Int {
        return this.id
    }

    private fun getPath(): String {
        if(shaderType == GL_VERTEX_SHADER)
            return "/shaders/vertex/$name.vs"
        else
            return "/shaders/fragment/$name.fs"
    }
}