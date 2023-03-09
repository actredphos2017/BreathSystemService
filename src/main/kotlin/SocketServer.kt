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
        println("Start Listening...")
        while (!gonnaStop) {
            val session = SocketSession(server.accept())
            println("Connect Successfully! Session Created!")
            session.acceptHolder()
        }
    }
}