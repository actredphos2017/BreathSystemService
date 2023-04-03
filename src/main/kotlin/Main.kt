import java.sql.SQLException
import java.util.Scanner

fun main() {

    try {
        Globals.dbEntrance = DatabaseEntrance(
            driverName = Globals.databaseDriver,
            url = Globals.databaseUrl,
            userName = Globals.databaseUserName,
            password = Globals.databasePassword
        )
    } catch (e: SQLException) {
        e.printStackTrace(Globals.logCat)
        Globals.logCat.println("Database Entrance Create Failed!")
        return
    }

    Globals.logCat.println("Server Start!")

    val server = SocketServer(
        host = Globals.serverHost,
        port = Globals.serverPort
    )
    val serverThread = Thread {
        try {
            server.startAccept()
        } catch (e: Exception) {
            e.printStackTrace(Globals.logCat)
        }
    }
    serverThread.start()
    val scanner = Scanner(System.`in`)
    while (true)
        scanner.nextLine().run {
            when (this) {
                "stop" -> {
                    Globals.logCat.println("Trying stop server...")
                    server.gonnaStop = true
                    return
                }

                else -> {
                    Globals.logCat.println("Unknown Command")
                }
            }
        }
}