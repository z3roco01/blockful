package z3roco01.blockful.game.chunk

import org.joml.Vector3i
import z3roco01.blockful.render.Renderable
import z3roco01.blockful.render.Renderer
import z3roco01.blockful.render.mesh.Mesh
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * holds the data and handles mesh creation for a chunk of the world
 * @param chunkX the x coordinate of the chunk, not blocks
 * @param chunkY the y coordinate of the chunk
 */
class Chunk(val chunkX: Int, val chunkY: Int): Renderable {
    private val blocks = BooleanArray(128 * 16 * 16)

    // create a vertex array with 8 vertex pairs ( each having an x, y and z component  )
    val mesh = Mesh(FloatArray(blocks.size * 8 * 3), IntArray(0), floatArrayOf())

    init {
        // add the chunk position to the transform
        this.mesh.transformation.position.x += chunkX*16
        this.mesh.transformation.position.z += chunkY*16
    }

    override fun init() {
        makeWorstCase()

        this.mesh.init()
        buildMesh()
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
                        setBlock(x, y, z, x%2 == 0)
                    else
                        setBlock(x, y, z, x%2 != 0)
                }
            }
        }
    }

    /**
     * rebuilds the mesh based on [Chunk.blocks] by using [Mesh.rebuildMesh] and [Chunk.addVoxel]
     */
    fun buildMesh() {
        // empty arrays
        this.mesh.indices = IntArray(1180000)
        this.mesh.verts.fill(0f)

        addAllVoxels()

        this.mesh.rebuildMesh()
    }

    /**
     * loops over every voxel and calls [Chunk.addVoxel] for it
     */
    @OptIn(ExperimentalTime::class)
    private fun addAllVoxels() {
        var indOffset = 0
        println(measureTime {
            for(y in 0..127) {
                for(x in 0..15) {
                    for(z in 0..15) {
                        indOffset += addVoxel(x, y, z, indOffset)
                    }
                }
            }
            this.mesh.indices = this.mesh.indices.copyOf(indOffset+1)
        })
        println(mesh.indices.size)
    }

    /**
     * adds a voxel to the mesh at the supplied coords
     * @param x the x part of the coordinate
     * @param y the y part of the coordinate
     * @param z the z part of the coordinate
     * @param indArrayOffset current running offset into the index array
     */
    private fun addVoxel(x: Int, y: Int, z: Int, indArrayOffset: Int): Int {
        // could prob be v optimized but im washed at graphics programming

        // if there is no block there, then dont add one
        if(!getBlock(x, y, z)) return 0

        // base indices offset
        var indicesOffset = mesh.verts.size/3
        // tracks how many indices have been used
        var usedInds = 0

        if(!getBlock(x-1, y, z)) {
            // left face
            mesh.indices[indArrayOffset] = 1+indicesOffset
            mesh.indices[indArrayOffset+1] = 2+indicesOffset
            mesh.indices[indArrayOffset+2] = 5+indicesOffset
            mesh.indices[indArrayOffset+3] = 2+indicesOffset
            mesh.indices[indArrayOffset+4] = 6+indicesOffset
            mesh.indices[indArrayOffset+5] = 5+indicesOffset
            usedInds += 6
        }
        if(!getBlock(x+1, y, z)) {
            // right face
            mesh.indices[indArrayOffset+usedInds] = 4+indicesOffset
            mesh.indices[indArrayOffset+usedInds+1] = 7+indicesOffset
            mesh.indices[indArrayOffset+usedInds+2] = 0+indicesOffset
            mesh.indices[indArrayOffset+usedInds+3] = 7+indicesOffset
            mesh.indices[indArrayOffset+usedInds+4] = 3+indicesOffset
            mesh.indices[indArrayOffset+usedInds+5] = 0+indicesOffset
            usedInds += 6
        }
        if(!getBlock(x, y+1, z)) {
            // top face
            mesh.indices[indArrayOffset+usedInds] = 2+indicesOffset
            mesh.indices[indArrayOffset+usedInds+1] = 3+indicesOffset
            mesh.indices[indArrayOffset+usedInds+2] = 7+indicesOffset
            mesh.indices[indArrayOffset+usedInds+3] = 7+indicesOffset
            mesh.indices[indArrayOffset+usedInds+4] = 6+indicesOffset
            mesh.indices[indArrayOffset+usedInds+5] = 2+indicesOffset
            usedInds += 6
        }
        if(!getBlock(x, y-1, z)) {
            // bottom face
            mesh.indices[indArrayOffset+usedInds] = 1+indicesOffset
            mesh.indices[indArrayOffset+usedInds+1] = 5+indicesOffset
            mesh.indices[indArrayOffset+usedInds+2] = 0+indicesOffset
            mesh.indices[indArrayOffset+usedInds+3] = 5+indicesOffset
            mesh.indices[indArrayOffset+usedInds+4] = 4+indicesOffset
            mesh.indices[indArrayOffset+usedInds+5] = 0+indicesOffset
            usedInds += 6
        }
        if(!getBlock(x, y, z-1)) {
            // back face
            mesh.indices[indArrayOffset+usedInds] = 5+indicesOffset
            mesh.indices[indArrayOffset+usedInds+1] = 6+indicesOffset
            mesh.indices[indArrayOffset+usedInds+2] = 4+indicesOffset
            mesh.indices[indArrayOffset+usedInds+3] = 6+indicesOffset
            mesh.indices[indArrayOffset+usedInds+4] = 7+indicesOffset
            mesh.indices[indArrayOffset+usedInds+5] = 4+indicesOffset
            usedInds += 6
        }
        if(!getBlock(x, y, z+1)) {
            // front face
            mesh.indices[indArrayOffset+usedInds] = 2+indicesOffset
            mesh.indices[indArrayOffset+usedInds+1] = 1+indicesOffset
            mesh.indices[indArrayOffset+usedInds+2] = 0+indicesOffset
            mesh.indices[indArrayOffset+usedInds+3] = 3+indicesOffset
            mesh.indices[indArrayOffset+usedInds+4] = 2+indicesOffset
            mesh.indices[indArrayOffset+usedInds+5] = 0+indicesOffset
            usedInds += 6
        }

        // add the verts for all faces
        // TODO: dont add unused verts
        floatArrayOf(
             0.5f+x, -0.5f+y,  0.5f+z,
            -0.5f+x, -0.5f+y,  0.5f+z,
            -0.5f+x,  0.5f+y,  0.5f+z,
             0.5f+x,  0.5f+y,  0.5f+z,
             0.5f+x, -0.5f+y, -0.5f+z,
            -0.5f+x, -0.5f+y, -0.5f+z,
            -0.5f+x,  0.5f+y, -0.5f+z,
             0.5f+x,  0.5f+y, -0.5f+z,
        ).copyInto(mesh.verts, blockIndex(x, y, z) * 8 * 3)

        return usedInds
    }

    /**
     * gets the block at the supplied coordinates, return air when invalid coords
     * @param x the x part of the coordinate
     * @param y the y part of the coordinate
     * @param z the z part of the coordinate
     */
    fun getBlock(x: Int, y: Int, z: Int): Boolean {
        if(x > 15 || x < 0 || y > 127 || y < 0 || z > 15 || z < 0) return false

        return blocks[blockIndex(x, y, z)]
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

        blocks[blockIndex(x, y, z)] = newBlock
    }

    private fun blockIndex(x: Int, y: Int, z: Int) = y*16*16 + x*16 + z

    /**
     * same as [Chunk.setBlock] but it takes a vector
     * @param coords the [Vector3i] holding the coordinates
     * @param newBlock the new block state
     */
    fun setBlock(coords: Vector3i, newBlock: Boolean) = setBlock(coords.x, coords.y, coords.z, newBlock)

    override fun render(renderer: Renderer) = this.mesh.render(renderer)

    override fun fini() = this.mesh.fini()

}