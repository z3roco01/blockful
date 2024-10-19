package z3roco01.blockful.game.chunk

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

    override fun init() {
        // loop over every chunk and init it
        for(x in 0..15) {
            for(y in 0..15){
                chunks[x][y].init()
            }
        }
    }

    override fun render(renderer: Renderer) {
        // loop over every chunk and render it
        for(x in 0..15) {
            for(y in 0..15) {
                chunks[x][y].render(renderer)
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