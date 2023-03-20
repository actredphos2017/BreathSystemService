import java.net.InetSocketAddress
import java.net.ServerSocket

class SocketServer(
    host: String,
    port: Int
) {
    private val server = ServerSocket()

    var gonnaStop = false

    init {
        server.bind(InetSocketAddress(host, port))
    }

    fun startAccept() {
        Globals.logCat.println("Start Listening...")
        while (!gonnaStop)
            SocketSession(
                server.accept().apply {
                    Globals.logCat.println("Connect Successfully! Remote ip: $inetAddress Session Created!")
                }
            ).acceptHolder()
    }
}