import java.io.PrintStream
import java.sql.SQLException
import java.util.Scanner

object Globals {
    const val DatabaseDriver = "com.mysql.cj.jdbc.Driver"
    const val DatabaseUrl = "jdbc:mysql://127.0.0.1:3306"
    const val DatabaseUserName = "root"
    const val DatabasePassword = "helloworld"
    const val verificationPrefix = "GZW_BS "
    var dbEntrance: DatabaseEntrance? = null
    var logCat: PrintStream = System.out

    var codeDictionary: Map<String, (String)->String> = mapOf(
        "1001" to RequestProcessor.registerNewAccount,
        "1002" to RequestProcessor.loginAccount
    )
}

fun main() {

    try {
        Globals.dbEntrance = DatabaseEntrance(
            driverName = Globals.DatabaseDriver,
            url = Globals.DatabaseUrl,
            userName = Globals.DatabaseUserName,
            password = Globals.DatabasePassword
        )
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