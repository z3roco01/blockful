package z3roco01.blockful.render

/**
 * inherited by classes that can be rendered
 */
interface Renderable {
    /**
     * called to initialize the object
     */
    fun init()

    /**
     * called to render the object
     */
    fun render()

    /**
     * called to de initialize the object
     */
    fun fini()
}