package z3roco01.blockful.render.texture

import de.matthiasmann.twl.utils.PNGDecoder
import z3roco01.blockful.util.ResourceUtil
import java.nio.ByteBuffer
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil

/**
 * handles the loading of a png for use as a texture
 * @param path the path to the texture file
 */
class Texture(val path: String) {
    lateinit var texture: PNGDecoder
    var textureId: Int = 0

    fun init() {
        // parse the png at the path
        texture = PNGDecoder(ResourceUtil.getResourceStream(path))

        // create the buffer for the png to be decoded into
        // each pixel is 4 bytes worth of data
        val buf = ByteBuffer.allocateDirect(4 * texture.width * texture.height)
        texture.decode(buf, texture.width *4, PNGDecoder.Format.RGBA)
        buf.flip()

        // generate the id for this texture
        this.textureId = glGenTextures()
        bind()

        // describe how to unpack the texture
        // each component ( r, g, b and a ) are each one byte
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        // copy the texture into vmem
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, texture.width, texture.height,
            0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

        MemoryUtil.memFree(buf)

        // generate mipmaps
        glGenerateMipmap(GL_TEXTURE_2D)

        // set interpolation to nearest
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)

        unbind()
    }

    /**
     * calls [glBindTexture] on this textures [textureId]
     */
    fun bind() = glBindTexture(GL_TEXTURE_2D, this.textureId)

    /**
     * calls [glBindTexture] and binds it to 0, effectively unbinding it
     */
    fun unbind() = glBindTexture(GL_TEXTURE_2D, 0)
}