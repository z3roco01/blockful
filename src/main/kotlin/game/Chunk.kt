package z3roco01.blockful.game

import org.joml.Vector3i
import z3roco01.blockful.render.Renderable
import z3roco01.blockful.render.mesh.Mesh

/**
 * holds the data and handles mesh creation for a chunk of the world
 * @param chunkX the x coordinate of the chunk, not blocks
 * @param chunkY the y coordinate of the chunk
 */
class Chunk(val chunkX: Int, val chunkY: Int): Renderable {
    private val blocks = Array(128){Array(16){Array(16){true}}}

    val mesh = Mesh(floatArrayOf(), intArrayOf(), floatArrayOf())

    override fun init() {
        addAllVoxels()

        this.mesh.init()
    }

    /**
     * rebuilds the mesh based on [Chunk.blocks] by using [Mesh.rebuildMesh] and [Chunk.addVoxel]
     */
    fun rebuildMesh() {
        // empty arrays
        this.mesh.indices = intArrayOf()
        this.mesh.verts = floatArrayOf()

        Thread(Runnable({
            addAllVoxels()
            this.mesh.rebuildMesh()
        })).start()
    }

    /**
     * loops over every voxel and calls [Chunk.addVoxel] for it
     */
    private fun addAllVoxels() {
        for(y in 0..127) {
            for(x in 0..15) {
                for(z in 0..15)
                    addVoxel(x, y, z)
            }
        }
    }

    /**
     * adds a voxel to the mesh at the supplied coords
     * @param x the x part of the coordinate
     * @param y the y part of the coordinate
     * @param z the z part of the coordinate
     */
    private fun addVoxel(x: Int, y: Int, z: Int) {
        // could prob be v optimized but im washed at graphics programming

        // if there is no block there, then dont add one
        if(!getBlock(x, y, z)) return

        var indicesOffset = mesh.verts.size/3

        if(!getBlock(x-1, y, z)) {
            mesh.indices += intArrayOf(
                // left face
                1+indicesOffset, 2+indicesOffset, 5+indicesOffset,
                2+indicesOffset, 6+indicesOffset, 5+indicesOffset,
            )
        }
        if(!getBlock(x+1, y, z)) {
            mesh.indices += intArrayOf(
                // right face
                4 + indicesOffset, 7 + indicesOffset, 0 + indicesOffset,
                7 + indicesOffset, 3 + indicesOffset, 0 + indicesOffset,
            )
        }
        if(!getBlock(x, y+1, z)) {
            mesh.indices += intArrayOf(
                // top face
                2+indicesOffset, 3+indicesOffset, 7+indicesOffset,
                7+indicesOffset, 6+indicesOffset, 2+indicesOffset,
            )
        }
        if(!getBlock(x, y-1, z)) {
            mesh.indices += intArrayOf(
                // bottom face
                1+indicesOffset, 5+indicesOffset, 0+indicesOffset,
                5+indicesOffset, 4+indicesOffset, 0+indicesOffset
            )
        }
        if(!getBlock(x, y, z-1)) {
            mesh.indices += intArrayOf(
                // back face
                5+indicesOffset, 6+indicesOffset, 4+indicesOffset,
                6+indicesOffset, 7+indicesOffset, 4+indicesOffset,
            )
        }
        if(!getBlock(x, y, z+1)) {
            mesh.indices += intArrayOf(
                // front face
                2+indicesOffset, 1+indicesOffset, 0+indicesOffset,
                3+indicesOffset, 2+indicesOffset, 0+indicesOffset,
            )
        }

        // add the chunk offset times 16 since chunks are 16 blocks on the x and z
        val xVertOff = x+chunkX*16
        val zVertOff = z+chunkY*16

        // add the verts for all faces
        // TODO: dont add unused verts
        mesh.verts += floatArrayOf(
             0.5f+xVertOff, -0.5f+y,  0.5f+zVertOff,
            -0.5f+xVertOff, -0.5f+y,  0.5f+zVertOff,
            -0.5f+xVertOff,  0.5f+y,  0.5f+zVertOff,
             0.5f+xVertOff,  0.5f+y,  0.5f+zVertOff,
             0.5f+xVertOff, -0.5f+y, -0.5f+zVertOff,
            -0.5f+xVertOff, -0.5f+y, -0.5f+zVertOff,
            -0.5f+xVertOff,  0.5f+y, -0.5f+zVertOff,
             0.5f+xVertOff,  0.5f+y, -0.5f+zVertOff,
        )
    }

    /**
     * gets the block at the supplied coordinates, return air when invalid coords
     * @param x the x part of the coordinate
     * @param y the y part of the coordinate
     * @param z the z part of the coordinate
     */
    fun getBlock(x: Int, y: Int, z: Int): Boolean {
        if(x > 15 || x < 0 || y > 127 || y < 0 || z > 15 || z < 0) return false

        return blocks[y][x][z]
    }

    /**
     * sets the block at the supplied coordinate to the supplied value
     * @param x the x part of the coordinate
     * @param y the y part of the coordinate
     * @param z the z part of the coordinate
     * @param newBlock the new block state
     */
    fun setBlock(x: Int, y: Int, z: Int, newBlock: Boolean) {
        // check that its in bounds of the chunk
        if(x > 15 || x < 0 || y > 127 || y < 0 || z > 15 || z < 0) return

        blocks[y][x][z] = newBlock
    }

    /**
     * same as [Chunk.setBlock] but it takes a vector
     * @param coords the [Vector3i] holding the coordinates
     * @param newBlock the new block state
     */
    fun setBlock(coords: Vector3i, newBlock: Boolean) = setBlock(coords.x, coords.y, coords.z, newBlock)

    override fun render() = this.mesh.render()

    override fun fini() = this.mesh.fini()

}