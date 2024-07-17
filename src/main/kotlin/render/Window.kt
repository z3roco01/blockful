package z3roco01.blockful.render

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.opengl.GL33.*
import java.nio.IntBuffer

class Window {
    private var id = NULL

    fun init() {
        glfwDefaultWindowHints()
        this.setHint(GLFW_VISIBLE, false)
        this.setHint(GLFW_RESIZABLE, true)
        this.setHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        this.setHint(GLFW_CONTEXT_VERSION_MINOR, 3)

        this.id = glfwCreateWindow(1024, 768, "blockful", NULL, NULL)
        if(this.id == NULL)
            throw IllegalStateException("couldnt create window :(")

        val stack = stackPush()

        val pWidth = stack.mallocInt(1)
        val pHeight = stack.mallocInt(1)

        glfwGetWindowSize(this.id, pWidth, pHeight)

        val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

        if(vidMode == null)
            throw IllegalStateException("couldnt get vidmode :(")

        glfwSetWindowPos(this.id,
            (vidMode.width() - pWidth.get(0)) / 2,
            (vidMode.height() - pHeight.get(0)) / 2)

        glfwMakeContextCurrent(this.id)
        glfwSwapInterval(1)
        glfwShowWindow(this.id)

        stack.pop()

        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LESS)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
    }

    fun getId(): Long {
        return this.id
    }

    fun getAspectRatio(): Float {
        val height = this.getWindowHeightPx()
        val width = this.getWindowWidthPx()
        val ar = width.toFloat()/height.toFloat()
        return ar;
    }

    fun getWindowWidthPx(): Int {
        val widthBuf = intArrayOf(1)
        glfwGetFramebufferSize(this.id, widthBuf, null)
        return widthBuf[0]
    }

    fun getWindowHeightPx(): Int {
        val heightBuf = intArrayOf(1)
        glfwGetFramebufferSize(this.id, null, heightBuf)
        return heightBuf[0]
    }

    private fun setHint(hint: Int, value: Boolean) {
        this.setHint(hint, if(value) GLFW_TRUE else GLFW_FALSE)
    }

    private fun setHint(hint: Int, value: Int) {
        glfwWindowHint(hint, value)
    }

    fun setKeyCallback(callback: GLFWKeyCallbackI) {
        glfwSetKeyCallback(id, callback)
    }

    fun setRefreshCallback(callback: GLFWWindowRefreshCallbackI) {
        glfwSetWindowRefreshCallback(this.id, callback)
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(this.id)
    }

    fun swapBuffers() {
        glfwSwapBuffers(this.id)
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return if(glfwGetKey(this.id, keyCode) == GLFW_PRESS) true else false
    }

    fun fini() {
        glfwFreeCallbacks(this.id)
        glfwDestroyWindow(this.id);
    }
}