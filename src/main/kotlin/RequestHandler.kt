import java.lang.Exception
import java.net.Socket

class RequestHandler(
    requestType: String? = null,
    val socket: Socket
) {

    var requestCode: String? = null
        private set

    var outputData: String? = null

    init {
        if (requestType?.startsWith(Globals.verificationPrefix) == true)
            requestCode = requestType.substring(startIndex = 2)
        else throw Exception("Wrong data input!")
    }

    fun start() {
        var len: Int
        Globals.codeDictionary.getOrDefault(requestCode) {
            throw Exception("Error Request Code: $it")
        }(String(ByteArray(1024 * 1024).also {
            socket.getInputStream().run { len = read(it) }
        }, 0, len)).toByteArray().also {
            socket.getOutputStream().write(it)
        }
    }
}