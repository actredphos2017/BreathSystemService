import java.net.Socket

class SocketSession(
    private val socket: Socket
) {
    var available: Boolean = true

    fun acceptHolder() = Thread {
        try {
            var len: Int
            ByteArray(1024 * 1024).also {
                socket.getInputStream().run {
                    len = read(it)
                }
            }.also {
                RequestHandler(String(it, 0, len).apply {
                    Globals.logCat.println("Require Code Get: $this")
                }, socket).start()
            }

        } catch (e: Exception) {
            Globals.logCat.print("Work Error: ")
            e.printStackTrace(Globals.logCat)
        }
        socket.close()
        available = false
        Globals.logCat.println("Session Stop!")
    }.start()
}