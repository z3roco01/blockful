package z3roco01.blockful.game.chunk

import org.joml.Vector3i
import z3roco01.blockful.render.Renderable
import z3roco01.blockful.render.Renderer
import z3roco01.blockful.render.mesh.Mesh

/**
 * holds the data and handles mesh creation for a chunk of the world
 * @param chunkX the x coordinate of the chunk, not blocks
 * @param chunkY the y coordinate of the chunk
 */
class Chunk(val chunkX: Int, val chunkY: Int): Renderable {
    private val blocks = Array(128){Array(16){Array(16){false}}}

    val mesh = Mesh()

    init {
        // add the chunk position to the transform
        this.mesh.transformation.position.x += chunkX*16
        this.mesh.transformation.position.z += chunkY*16
    }

    override fun init() {
        for(y in 0..7) {
            for(x in 0..15) {
                for(z in 0..15)
                    setBlock(x, y, z, true)
            }
        }
        addAllVoxels()

        this.mesh.init()
    }

    /**
     * creates the worst case mesh, an alternating checkerboard, meaning all cubes will have all faces rendered
     * for testing only
     */
    fun makeWorstCase() {
        for(y in 0..127) {
            for(x in 0..15) {
                for(z in 0..15) {
                    if(z % 2 == y%2)
                        blocks[y][x][z] = x%2 == 0
                    else
                        blocks[y][x][z] = x%2 != 0
                }
            }
        }
    }

    /**
     * rebuilds the mesh based on [Chunk.blocks] by using [Mesh.build] and [Chunk.addVoxel]
     */
    fun rebuildMesh() {
        // empty arrays
        this.mesh.verts = emptyArray()
        this.mesh.indices = emptyArray()

        addAllVoxels()

        this.mesh.build()
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
            mesh.indices += arrayOf(
                // west face
                1+indicesOffset, 2+indicesOffset, 5+indicesOffset,
                2+indicesOffset, 6+indicesOffset, 5+indicesOffset,
            )
        }
        if(!getBlock(x+1, y, z)) {
            mesh.indices += arrayOf(
                // east face
                4 + indicesOffset, 7 + indicesOffset, 0 + indicesOffset,
                7 + indicesOffset, 3 + indicesOffset, 0 + indicesOffset,
            )
        }
        if(!getBlock(x, y+1, z)) {
            mesh.indices += arrayOf(
                // top face
                2+indicesOffset, 3+indicesOffset, 7+indicesOffset,
                7+indicesOffset, 6+indicesOffset, 2+indicesOffset,
            )
        }
        if(!getBlock(x, y-1, z)) {
            mesh.indices += arrayOf(
                // bottom face
                1+indicesOffset, 5+indicesOffset, 0+indicesOffset,
                5+indicesOffset, 4+indicesOffset, 0+indicesOffset
            )
        }
        if(!getBlock(x, y, z-1)) {
            mesh.indices += arrayOf(
                // south face
                5+indicesOffset, 6+indicesOffset, 4+indicesOffset,
                6+indicesOffset, 7+indicesOffset, 4+indicesOffset,
            )
        }
        if(!getBlock(x, y, z+1)) {
            mesh.indices += arrayOf(
                // north face
                2+indicesOffset, 1+indicesOffset, 0+indicesOffset,
                3+indicesOffset, 2+indicesOffset, 0+indicesOffset,
            )
        }

        // add the verts for all faces
        // TODO: dont add unused verts
        mesh.verts += arrayOf(
             0.5f+x, -0.5f+y,  0.5f+z,
            -0.5f+x, -0.5f+y,  0.5f+z,
            -0.5f+x,  0.5f+y,  0.5f+z,
             0.5f+x,  0.5f+y,  0.5f+z,
             0.5f+x, -0.5f+y, -0.5f+z,
            -0.5f+x, -0.5f+y, -0.5f+z,
            -0.5f+x,  0.5f+y, -0.5f+z,
             0.5f+x,  0.5f+y, -0.5f+z,
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

    override fun render(renderer: Renderer) = this.mesh.render(renderer)

    override fun fini() = this.mesh.fini()

}