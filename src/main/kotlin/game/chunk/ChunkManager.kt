package z3roco01.blockful.game.chunk

import z3roco01.blockful.render.Camera
import z3roco01.blockful.render.Renderable
import z3roco01.blockful.render.Renderer

/**
 * handles all the chunks together
 */
class ChunkManager: Renderable {
    val chunks = Array<Array<Chunk>>(16){Array<Chunk>(16){Chunk(0, 0)}}

    init {
        // create a 16x16 grid of chunks on init
        for(x in 0..15) {
            for(y in 0..15)
                chunks[x][y] = Chunk(x, y)
        }
    }

    override fun init(renderer: Renderer) {
        // loop over every chunk and init it
        for(x in 0..15) {
            for(y in 0..15){
                chunks[x][y].init(renderer)
            }
        }
    }

    override fun render(renderer: Renderer, camera: Camera) {
        // loop over every chunk and render it
        for(x in 0..15) {
            for(y in 0..15) {
                chunks[x][y].render(renderer, camera)
            }
        }
    }

    override fun fini() {
        // loop over every chunk and fini it
        for(x in 0..15) {
            for(y in 0..15)
                chunks[x][y].fini()
        }
    }
}