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

    var verts = floatArrayOf(
    )
    var indices = intArrayOf(
    )
    val colours = floatArrayOf(
    )
    val mesh = Mesh(verts, indices, colours)

    override fun init() {
        for(x in 0..15){
            for(y in 0..15) {
                addVoxel(x, y)
            }
        }
        this.mesh.init()
    }

    private fun addVoxel(x: Int, y: Int) {
        var indicesOffset = mesh.verts.size/3
        mesh.indices += intArrayOf(
            2+indicesOffset, 1+indicesOffset, 0+indicesOffset,
            3+indicesOffset, 2+indicesOffset, 0+indicesOffset,
            1+indicesOffset, 2+indicesOffset, 5+indicesOffset,
            2+indicesOffset, 6+indicesOffset, 5+indicesOffset,
            5+indicesOffset, 6+indicesOffset, 4+indicesOffset,
            6+indicesOffset, 7+indicesOffset, 4+indicesOffset,
            4+indicesOffset, 7+indicesOffset, 0+indicesOffset,
            7+indicesOffset, 3+indicesOffset, 0+indicesOffset,
            2+indicesOffset, 3+indicesOffset, 7+indicesOffset,
            7+indicesOffset, 6+indicesOffset, 2+indicesOffset,
            1+indicesOffset, 5+indicesOffset, 0+indicesOffset,
            5+indicesOffset, 4+indicesOffset, 0+indicesOffset
        )
        mesh.verts += floatArrayOf(
             0.5f+x, -0.5f,  0.5f+y,
            -0.5f+x, -0.5f,  0.5f+y,
            -0.5f+x,  0.5f,  0.5f+y,
             0.5f+x,  0.5f,  0.5f+y,
             0.5f+x, -0.5f, -0.5f+y,
            -0.5f+x, -0.5f, -0.5f+y,
            -0.5f+x,  0.5f, -0.5f+y,
             0.5f+x,  0.5f, -0.5f+y,
        )
    }

    override fun render() = this.mesh.render()

    override fun fini() = this.mesh.fini()
}