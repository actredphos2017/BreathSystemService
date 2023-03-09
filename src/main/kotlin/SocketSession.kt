import java.net.Socket

class SocketSession(
    private val socket: Socket
) {

    var available: Boolean = true

    var requestType: String? = null

    var requestHandler: RequestHandler? = null

    fun acceptHolder() = Thread {
        try {
            val inputStream = socket.getInputStream()
            val buf = ByteArray(1024 * 1024)
            val len = inputStream.read(buf)
            requestType = String(buf, 0, len)
            println(requestType)

            requestHandler = RequestHandler(requestType)

        } catch (_: Exception) {

        }
        socket.close()
        available = false
        println("Session Stop!")
    }.start()
}