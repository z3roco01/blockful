package z3roco01.blockful.render.mesh

import game.gameobject.GameObject
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memFree
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
open class Mesh(var verts: FloatArray, var indices: IntArray, val colours: FloatArray, var directions: IntArray): GameObject(), Renderable {
    private var vaoId: Int = 0
    private var vertsVboId: Int = 0
    private var vertsVbo = VBO(emptyArray<Float>(), GL_FLOAT, GL_ARRAY_BUFFER, 0, 3, false, 0, 0)
    private var idxVboId: Int = 0
    private var colourVboId: Int = 0
    private var directionsVboId: Int = 0

    /**
     * called before it can be rendered,
     */
    override fun init() {
        // Create the vertex array object(vao) id and bind it
        this.vaoId = glGenVertexArrays()
        glBindVertexArray(this.vaoId)

        // create vbos for all the attributes
        vertsVbo.init()
        this.idxVboId = glGenBuffers()
        this.colourVboId = glGenBuffers()
        this.directionsVboId = glGenBuffers()

        build()
    }

    fun build() {
        // Create a buffer for the vertices and flip to reset to position
        /*val vertBuf = MemoryUtil.memAllocFloat(verts.size)
        vertBuf.put(verts).flip()

        // create a vertex buffer object(vbo) id to store the vertices
        glBindBuffer(GL_ARRAY_BUFFER, this.vertsVboId)
        glBufferData(GL_ARRAY_BUFFER, vertBuf, GL_STATIC_DRAW)

        // free the vertex buffer
        memFree(vertBuf)

        // create the attribute for position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)*/
        vertsVbo.data = verts.toTypedArray()
        vertsVbo.create()

        val idxBuf = MemoryUtil.memAllocInt(indices.size)
        idxBuf.put(indices).flip()

        // create a index buffer to lower the elements in the vert array
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.idxVboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idxBuf, GL_STATIC_DRAW)

        // free the index buffer and unbind GL_ARRAY_BUFFER
        memFree(idxBuf)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        // create the buffer for the colours
        val colourBuf = MemoryUtil.memAllocFloat(colours.size)
        colourBuf.put(colours).flip()

        // create a vbo for the colours
        glBindBuffer(GL_ARRAY_BUFFER, this.colourVboId)
        glBufferData(GL_ARRAY_BUFFER, colourBuf, GL_STATIC_DRAW)

        memFree(colourBuf)
        // create the attribute for position
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)


        // create the buffer for the colours
        val directionBuf = MemoryUtil.memAllocInt(directions.size)
        directionBuf.put(directions).flip()

        // create a vbo for the colours
        glBindBuffer(GL_ARRAY_BUFFER, this.directionsVboId)
        glBufferData(GL_ARRAY_BUFFER, directionBuf, GL_STATIC_DRAW)


        memFree(directionBuf)
        glVertexAttribPointer(2, 1, GL_INT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        glBindVertexArray(0)
    }

    /**
     * handles the rendering of the mesh
     */
    override fun render(renderer: Renderer) {
        renderer.shader.setUniform("worldMatrix", this.transformation.getWorldMatrix())
        // bind this meshes vertex array
        glBindVertexArray(this.vaoId)
        // enable the position and colour attributes
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)

        // draw the vertices with as triangles
        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)

        // disable all attributes
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(2)
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
        glDeleteBuffers(this.idxVboId)
        glDeleteBuffers(this.colourVboId)
        glDeleteBuffers(this.directionsVboId)

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