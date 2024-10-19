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
 */
open class Mesh(var verts: FloatArray, var indices: IntArray, val colours: FloatArray): GameObject(), Renderable {
    private var vaoId: Int = 0
    private var vertsVboId: Int = 0
    private var idxVboId: Int = 0
    private var colourVboId: Int = 0

    /**
     * called before it can be rendered,
     */
    override fun init() {
        // Create the vertex array object(vao) id and bind it
        this.vaoId = glGenVertexArrays()
        glBindVertexArray(this.vaoId)

        // create vbos for all the attributes
        this.vertsVboId = glGenBuffers()
        this.idxVboId = glGenBuffers()
        this.colourVboId = glGenBuffers()

        rebuildMesh()
    }

    fun rebuildMesh() {
        // Create a buffer for the vertices and flip to reset to position
        val vertBuf = MemoryUtil.memAllocFloat(verts.size)
        vertBuf.put(verts).flip()

        // create a vertex buffer object(vbo) id to store the vertices
        glBindBuffer(GL_ARRAY_BUFFER, this.vertsVboId)
        glBufferData(GL_ARRAY_BUFFER, vertBuf, GL_STATIC_DRAW)

        // free the vertex buffer
        memFree(vertBuf)

        // create the attribute for position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

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

        // draw the vertices with as triangles
        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)

        // disable all attributes
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
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
        glDeleteBuffers(this.vertsVboId)
        glDeleteBuffers(this.idxVboId)
        glDeleteBuffers(this.colourVboId)

        // do the same for the vao
        glBindVertexArray(0)
        glDeleteVertexArrays(this.vaoId)
    }
}