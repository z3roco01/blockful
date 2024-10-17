package z3roco01.blockful.game

import z3roco01.blockful.render.Renderable
import z3roco01.blockful.render.mesh.Mesh

/**
 * holds the data and handles mesh creation for a chunk of the world
 * @param chunkX the x coordinate of the chunk, not blocks
 * @param chunkY the y coordinate of the chunk
 */
class Chunk(val chunkX: Int, val chunkY: Int): Renderable {
    val blocks = Array<Array<Boolean>>(16){ Array<Boolean>(16){true} }

    val verts = floatArrayOf(
        0.5f, -0.5f,  0.5f,
        -0.5f, -0.5f,  0.5f,
        -0.5f,  0.5f,  0.5f,
        0.5f,  0.5f,  0.5f,
        0.5f, -0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        -0.5f,  0.5f, -0.5f,
        0.5f,  0.5f, -0.5f,
    )
    val indices = intArrayOf(
        0, 1, 2
    )
    val colours = floatArrayOf(
        0.46484375f, 0.86328125f, 0.46484375f,
        0.46484375f, 0.86328125f, 0.46484375f,
        0.46484375f, 0.86328125f, 0.46484375f,
        0.46484375f, 0.86328125f, 0.46484375f,
        0.46484375f, 0.86328125f, 0.46484375f,
        0.46484375f, 0.86328125f, 0.46484375f,
    )
    val mesh = Mesh(verts, indices, colours)

    override fun init() {
        for(x in 0..15){
            for(y in 0..15) {
                blocks[x][y]
            }
        }
        this.mesh.init()
    }

    private fun makeVoxel(x: Int, y: Int) {

    }

    override fun render() = this.mesh.render()

    override fun fini() = this.mesh.fini()
}