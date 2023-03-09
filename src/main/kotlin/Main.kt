import java.util.Scanner

fun main(args: Array<String>) {
    println("Server Start!")
    val server = SocketServer("127.0.0.1", 21301)
    val serverThread = Thread {
        try {
            server.startAccept()
        } catch (_: Exception) {
        }
    }
    serverThread.start()
    val scanner = Scanner(System.`in`)
    while (true)
        scanner.nextLine().run {
            when(this) {
                "stop" -> {
                    println("Trying stop server...")
                    server.gonnaStop = true
                    return
                }

                else -> {
                    println("Unknown Command")
                }
            }
        }
}