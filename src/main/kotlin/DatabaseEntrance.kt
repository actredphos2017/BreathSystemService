import java.io.PrintStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class DatabaseEntrance {
    private val driverName = "com.mysql.jdbc.Driver"
    private val url = "127.0.0.1:3306"
    private val userName = "root"
    private val password = "helloworld"

    private var connection: Connection? = null
    private var preparedStatement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    init {
        Class.forName(driverName)
        connection = DriverManager.getConnection(url, userName, password)
    }

    fun accountRegister(userName: String, password: String, errorOs: PrintStream): Boolean {
        TODO()
    }

    fun accountSignIn(userName: String, password: String, errorOs: PrintStream): Pair<Boolean, String> {
        TODO()
    }

    fun accountCancellation(userName: String, errorOs: PrintStream): Boolean {
        TODO()
    }
}