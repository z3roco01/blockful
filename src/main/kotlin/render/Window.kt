package z3roco01.blockful.render

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.opengl.GL33.*

/**
 * handles all the stuff to do with the window
 */
class Window {
    // id of the window
    private var id = NULL

    /**
     * does all the initialization for the window
     */
    fun init() {
        // set the default hints
        glfwDefaultWindowHints()
        // make the window not visible
        this.setHint(GLFW_VISIBLE, false)
        // and resizable
        this.setHint(GLFW_RESIZABLE, true)
        // and set the glfw version to 33
        this.setHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        this.setHint(GLFW_CONTEXT_VERSION_MINOR, 3)

        // create a 1024x768 window
        this.id = glfwCreateWindow(1024, 768, "blockful", NULL, NULL)
        // if it couldnt be created, throw an exception
        if(this.id == NULL) throw IllegalStateException("couldnt create window :(")

        // push the stack and get access to it
        val stack = MemoryStack.stackPush()

        // allocate an int for width and one for height
        val pWidth = stack.mallocInt(1)
        val pHeight = stack.mallocInt(1)

        // get the window size and store them in the buffers
        glfwGetWindowSize(this.id, pWidth, pHeight)

        // get the video mode
        val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        // if it couldnt throw an exception
        if(vidMode == null)
            throw IllegalStateException("couldnt get vidmode :(")

        // set the window position to the center of the screen
        glfwSetWindowPos(this.id, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2)

        // make this window the current context
        glfwMakeContextCurrent(this.id)
        // set vsync to 1
        glfwSwapInterval(1)
        // show this window
        glfwShowWindow(this.id)

        // pop the stack
        stack.pop()

        GL.createCapabilities()

        // enable depth test
        glEnable(GL_DEPTH_TEST)
        // and set the depth function to GL_LESS
        glDepthFunc(GL_LESS)

        // enable culling
        glEnable(GL_CULL_FACE)
        // and set the culled face to the back face
        glCullFace(GL_BACK)
    }

    /**
     * gets the windows id
     * @return the window id
     */
    fun getId() = this.id

    /**
     * calculates the aspect ratio of the window
     * @return the calculated value
     */
    fun getAspectRatio(): Float {
        // get both the height and width in pixels
        val height = this.getHeightPx()
        val width = this.getWidthPx()

        // calculate the aspect ratio
        val ar = width.toFloat() / height.toFloat()
        // then return it
        return ar
    }

    /**
     * gets the windows width in pixels
     * @return window width in pixels
     */
    fun getWidthPx(): Int {
        // create a buffer for the width
        val widthBuf = intArrayOf(1)
        // get the frame buffer size ( window pixel size ) and set the width buffer to the width
        glfwGetFramebufferSize(this.id, widthBuf, null)
        // return the first value in the width buffer ( the width )
        return widthBuf[0]
    }

    /**
     * gets the windows height in pixels
     * @return window height in pixels
     */
    fun getHeightPx(): Int {
        // does the same as getWidthPx
        val heightBuf = intArrayOf(1)
        glfwGetFramebufferSize(this.id, null, heightBuf)
        return heightBuf[0]
    }

    /**
     * sets a boolean hint to the supplied value
     * @param hint the hint that is being set
     * @param value the boolean it will be set to
     */
    private fun setHint(hint: Int, value: Boolean) {
        this.setHint(hint, if(value) GLFW_TRUE else GLFW_FALSE)
    }

    /**
     * sets an integer hint to the supplied value
     * @param hint the hint that is being set
     * @param value the int it will be set to
     */
    private fun setHint(hint: Int, value: Int) {
        glfwWindowHint(hint, value)
    }

    /**
     * sets the key callback for this window
     * @param callback the callback method for this window
     */
    fun setKeyCallback(callback: GLFWKeyCallbackI) {
        glfwSetKeyCallback(id, callback)
    }

    /**
     * sets the refresh callback for this window
     * @param callback the return callback
     */
    fun setRefreshCallback(callback: GLFWWindowRefreshCallbackI) {
        glfwSetWindowRefreshCallback(this.id, callback)
    }

    /**
     * gets if the window should close, gets the value from [glfwWindowShouldClose]
     * @return true if the window should close
     */
    fun shouldClose() = glfwWindowShouldClose(this.id)

    /**
     * swaps the front and back buffers to show the back buffer
     */
    fun swapBuffers() {
        glfwSwapBuffers(this.id)
    }

    /**
     * gets if a key has been pressed
     * @return true if it has been pressed
     */
    fun isKeyPressed(keyCode: Int) = glfwGetKey(this.id, keyCode) == GLFW_PRESS

    /**
     * called when the window will be destroyed, handles all the freeing
     */
    fun fini() {
        glfwFreeCallbacks(this.id)
        glfwDestroyWindow(this.id)
    }
}