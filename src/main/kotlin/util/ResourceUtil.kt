package z3roco01.blockful.util

class ResourceUtil {
    companion object {
        fun getResourceContents(path: String): String? {
            return object{}.javaClass.getResource(path)?.readText()
        }
    }
}