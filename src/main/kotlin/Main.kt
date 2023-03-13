import java.io.PrintStream
import java.sql.SQLException
import java.util.Scanner

object Globals {
    val verificationPrefix = "GZW_BS "
    var dbEntrance: DatabaseEntrance? = null
    var logCat: PrintStream = System.out
    var codeDictionary: Map<String, (String)->String> = mapOf(

    )
}

fun main(args: Array<String>) {

    try {
        Globals.dbEntrance = DatabaseEntrance()
    } catch (e: SQLException) {
        e.printStackTrace(Globals.logCat)
        Globals.logCat.println("Database Entrance Create Failed!")
        return
    }

    Globals.logCat.println("Server Start!")

    val server = SocketServer("0.0.0.0", 21301)
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
            when(this) {
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