package z3roco01.blockful.render

/**
 * inherited by classes that can be rendered
 */
interface Renderable {
    /**
     * called to initialize the object
     * @param renderer the [Renderer] that is rendering this
     */
    fun init(renderer: Renderer)

    /**
     * called to render the object
     * @param renderer the [Renderer] that is rendering this
     */
    fun render(renderer: Renderer, camera: Camera)

    /**
     * called to de initialize the object
     */
    fun fini()
}