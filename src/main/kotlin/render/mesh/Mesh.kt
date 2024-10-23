package z3roco01.blockful.render.mesh

import game.gameobject.GameObject
import org.lwjgl.opengl.GL33.*
import z3roco01.blockful.render.Renderable
import z3roco01.blockful.render.Renderer

/**
 * a class for each mesh, handles the initialization and rendering of it
 * also handles the transformation and projection of it
 * @param verts a [FloatArray] containing the positions of every vertex
 * @param indices a [IntArray] containing the indices from the verts for each vertex
 * @param colours a [FloatArray] containing the colours for each index
 * @param directions a
 */
open class Mesh(): GameObject(), Renderable {
    var verts: Array<Float>
        get() = vertsVbo.data
        set(value) {
            vertsVbo.data = value
        }
    var indices: Array<Int>
        get() = indicesVBO.data
        set(value) {
            indicesVBO.data = value
        }
    var colours: Array<Float>
        get() = colourVBO.data
        set(value) {
            colourVBO.data = value
        }
    var directions: Array<Int>
        get() = dirsVBO.data
        set(value) {
            dirsVBO.data = value
        }

    private var vaoId: Int = 0
    private var vertsVbo = VBO(emptyArray<Float>(), GL_FLOAT, GL_ARRAY_BUFFER, 0, 3, false, 0, 0)
    private var indicesVBO = VBO(emptyArray<Int>(), GL_INT, GL_ELEMENT_ARRAY_BUFFER, 0, 3, false, 0, 0)
    private var colourVBO = VBO(emptyArray<Float>(), GL_FLOAT, GL_ARRAY_BUFFER, 1, 3, false, 0, 0)
    private var dirsVBO = VBO(emptyArray<Int>(), GL_INT, GL_ARRAY_BUFFER, 2, 1, false, 0, 0)

    /**
     * called before it can be rendered,
     */
    override fun init() {
        // Create the vertex array object(vao) id and bind it
        this.vaoId = glGenVertexArrays()
        glBindVertexArray(this.vaoId)

        // create vbos for all the attributes
        vertsVbo.init()
        indicesVBO.init()
        colourVBO.init()
        dirsVBO.init()

        build()
    }

    fun build() {
        vertsVbo.create()
        indicesVBO.create()
        colourVBO.create()
        dirsVBO.create()
    }

    /**
     * handles the rendering of the mesh
     */
    override fun render(renderer: Renderer) {
        renderer.shader.setUniform("worldMatrix", this.transformation.getWorldMatrix())
        // bind this meshes vertex array
        glBindVertexArray(this.vaoId)
        // enable the position and colour attributes
        vertsVbo.enable()
        colourVBO.enable()
        dirsVBO.enable()

        // draw the vertices with as triangles
        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)

        // disable all attributes
        vertsVbo.disable()
        colourVBO.disable()
        dirsVBO.disable()
        // unbind the vertexes
        glBindVertexArray(0)
    }

    /**
     * free all memory and unbind/destroy everything, called once this mesh is no longer used
     */
    override fun fini() {
        glDisableVertexAttribArray(0)

        // unbind and delete the vbo
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        //glDeleteBuffers(this.vertsVboId)
        vertsVbo.fini()
        indicesVBO.fini()
        colourVBO.fini()
        dirsVBO.fini()

        // do the same for the vao
        glBindVertexArray(0)
        glDeleteVertexArrays(this.vaoId)
    }

    /**
     * enum for each faces direction, used for shading
     */
    enum class Direction(val number: Int) {
        TOP(0),
        BOTTOM(1),
        NORTH(2),
        EAST(3),
        WEST(4),
        SOUTH(5)
    }
}