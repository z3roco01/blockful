package z3roco01.blockful.util

class ResourceUtil {
    companion object {
        /**
         * attempts to get the data in string form of the resource at the path
         * @param path the path that is trying to be read from
         */
        fun getResourceContents(path: String): String? = object{}.javaClass.getResource(path)?.readText()
    }
}