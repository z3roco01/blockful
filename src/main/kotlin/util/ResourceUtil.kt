package z3roco01.blockful.util

import java.io.InputStream

object ResourceUtil {
    /**
     * attempts to get the data in string form of the resource at the path
     * @param path the path that is trying to be read from
     */
    fun getResourceContents(path: String): String? = object{}.javaClass.getResource(path)?.readText()

    /**
     * attempts to get a [InputStream] for the resource at the path
     * @param path the path that is trying to be read from
     */
    fun getResourceStream(path: String): InputStream? = object{}.javaClass.getResource(path)?.openStream()
}