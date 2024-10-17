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

    val mesh = Mesh(floatArrayOf(), intArrayOf(), floatArrayOf())

    override fun init() {
        for(x in 0..15){
            for(y in 0..15) {
                addVoxel(x, y)
            }
        }

        this.mesh.init()
    }

    /**
     * rebuilds the mesh based on [Chunk.blocks] by using [Mesh.updateMesh] and [Chunk.addVoxel]
     */
    fun rebuildMesh() {
        // empty arrays
        this.mesh.indices = intArrayOf()
        this.mesh.verts = floatArrayOf()

        // loop over each voxel
        for(x in 0..15) {
            for(y in 0..15)
                addVoxel(x, y)
        }

        this.mesh.updateMesh()
    }

    /**
     * adds a voxel to the mesh at the supplied coords
     * @param x the x part of the coordinates
     * @param y the y part of the coordinates
     */
    private fun addVoxel(x: Int, y: Int) {
        var indicesOffset = mesh.verts.size/3

        mesh.indices += intArrayOf(
            // top face
            2+indicesOffset, 3+indicesOffset, 7+indicesOffset,
            7+indicesOffset, 6+indicesOffset, 2+indicesOffset,

            // bottom face
            1+indicesOffset, 5+indicesOffset, 0+indicesOffset,
            5+indicesOffset, 4+indicesOffset, 0+indicesOffset
        )

        if(!getBlock(x-1, y))
            mesh.indices += intArrayOf(
                // left face
                1+indicesOffset, 2+indicesOffset, 5+indicesOffset,
                2+indicesOffset, 6+indicesOffset, 5+indicesOffset,
            )
        if(!getBlock(x+1, y))
            mesh.indices += intArrayOf(
                // right face
                4+indicesOffset, 7+indicesOffset, 0+indicesOffset,
                7+indicesOffset, 3+indicesOffset, 0+indicesOffset,
            )
        if(!getBlock(x, y-1))
            mesh.indices += intArrayOf(
                // back face
                5+indicesOffset, 6+indicesOffset, 4+indicesOffset,
                6+indicesOffset, 7+indicesOffset, 4+indicesOffset,
            )
        if(!getBlock(x, y+1))
            mesh.indices += intArrayOf(
                // front face
                2+indicesOffset, 1+indicesOffset, 0+indicesOffset,
                3+indicesOffset, 2+indicesOffset, 0+indicesOffset,
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

    /**
     * gets the block at the supplied coordinates, return air when invalid coords
     * @param x the x part of the coordinates
     * @param y the y part of the coordinates
     */
    fun getBlock(x: Int, y: Int): Boolean {
        if(x > 15 || x < 0 || y > 15 || y < 0) return false

        return blocks[x][y]
    }

    override fun render() = this.mesh.render()

    override fun fini() = this.mesh.fini()
}