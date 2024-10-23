package z3roco01.blockful.render.mesh

import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil

/**
 * a class for handling a vertex buffer object ( vbo )
 * @param data an [Array] with type [T] that holds the data
 * @param type the GL_TYPE of this vbo
 * @param buffer the GL_BUFFER of the vbo ( ex. GL_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER )
 */
class VBO<T>(var data: Array<T>, val type: Int, val buffer: Int, val index: Int, val size: Int, val normalized: Boolean, val stride: Int, val offset: Long) {
    // the id of this vbo
    private var id: Int = 0

    /**
     * creates the id for this vbo, needs to be called after a vao has been bound
     */
    fun init() {
        this.id = glGenBuffers()
    }

    /**
     * sets the data to what is being stored, and sets the data format
     */
    fun create() {
        // bind this buffer
        glBindBuffer(this.buffer, this.id)

        when(this.type) {
            GL_BYTE, GL_UNSIGNED_BYTE -> {
                // create a buffer with the data
                var dataBuf = MemoryUtil.memAlloc(data.size)
                // make a primative array for the data array, then put and flip it
                dataBuf.put((data as Array<Byte>).toByteArray()).flip()

                // set the vbos data to the buffer
                glBufferData(this.buffer, dataBuf, GL_STATIC_DRAW)

                // free the buffer
                MemoryUtil.memFree(dataBuf)
            }
            GL_SHORT, GL_UNSIGNED_SHORT -> {
                var dataBuf = MemoryUtil.memAllocShort(data.size)
                dataBuf.put((data as Array<Short>).toShortArray()).flip()

                glBufferData(this.buffer, dataBuf, GL_STATIC_DRAW)

                MemoryUtil.memFree(dataBuf)
            }
            GL_INT, GL_UNSIGNED_INT -> {
                var dataBuf = MemoryUtil.memAllocInt(data.size)
                dataBuf.put((data as Array<Int>).toIntArray()).flip()

                glBufferData(this.buffer, dataBuf, GL_STATIC_DRAW)

                MemoryUtil.memFree(dataBuf)
            }
            GL_FLOAT -> {
                var dataBuf = MemoryUtil.memAllocFloat(data.size)
                dataBuf.put((data as Array<Float>).toFloatArray()).flip()

                glBufferData(this.buffer, dataBuf, GL_STATIC_DRAW)

                MemoryUtil.memFree(dataBuf)
            }
        }

        // set the description for this vbo
        when(this.type) {
            GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT, GL_UNSIGNED_SHORT, GL_INT, GL_UNSIGNED_INT ->
                // create an integer attribute pointer
                glVertexAttribIPointer(this.index, this.size, this.type, this.stride, this.offset)
            // create a float attribute pointer
            GL_FLOAT -> glVertexAttribPointer(this.index, this.size, this.type, this.normalized, this.stride, this.offset)
        }

        // unbind the buffer
        glBindBuffer(this.buffer, 0)
    }

    /**
     * calls [glEnableVertexAttribArray] on this vbo to enable it
     */
    fun enable() = glEnableVertexAttribArray(this.id)

    /**
     * calls [glDisableVertexAttribArray] on this vbo to disable it
     */
    fun disable() = glDisableVertexAttribArray(this.id)

    /**
     * disables the buffer and calls [glDeleteBuffers] on it
     */
    fun fini() {
        this.disable()
        glDeleteBuffers(this.id)
    }
}