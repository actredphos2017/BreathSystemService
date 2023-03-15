import gsonClasses.AccountInfo
import myUtils.SQLState
import java.io.PrintStream
import java.sql.*

class DatabaseEntrance(
    driverName: String,
    url: String,
    userName: String,
    password: String
) {

    private var connection: Connection? = null
    private var statement: Statement? = null
    private var resultSet: ResultSet? = null

    private fun createStatement(errorOs: PrintStream = System.out): Boolean = try {
        statement = connection?.createStatement()
        if (statement == null) {
            errorOs.println("数据库：创建会话失败！")
            false
        } else {
            try {
                statement!!.execute("use BreathSystem")
                true
            } catch (e: SQLException) {
                errorOs.println("数据库：不存在 BreathSystem 数据库")
                false
            }
        }
    } catch (e: SQLException) {
        errorOs.println("数据库：连接失效！")
        e.printStackTrace(errorOs)
        false
    }


    init {
        Class.forName(driverName)
        connection = DriverManager.getConnection(url, userName, password)
        Globals.logCat.println("Database Connect Successfully!")
    }

    fun accountRegister(newAccountInfo: AccountInfo, errorOs: PrintStream): Boolean =
        if (!createStatement(errorOs)) {
            errorOs.println("数据库错误！请联系管理员")
            false
        } else try {
            newAccountInfo.run {
                resultSet = statement!!.executeQuery(
                    SQLState
                        .Select()
                        .from("accounts")
                        .forColumns("*")
                        .withCondition("id", newAccountInfo.id)
                        .toString().also {
                            Globals.logCat.println("Running in SQL: $it")
                        }
                )
                if (resultSet!!.next()) {
                    errorOs.println("用户名已存在")
                    Globals.logCat.println("Account Register Error: 用户名已存在 for ${toVarchar(id)}")
                    false
                } else {
                    statement!!.execute(
                        SQLState
                            .Insert()
                            .into("accounts")
                            .forColumns("name", "id", "password")
                            .withItems(newAccountInfo.id, newAccountInfo.id, newAccountInfo.password)
                            .toString().also {
                                Globals.logCat.println("Running in SQL: $it")
                            }
                    )
                    Globals.logCat.println("Account Register Successfully: 新用户创建 for $id")
                    true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace(errorOs)
            false
        }

    fun accountSignIn(accountInfo: AccountInfo, errorOs: PrintStream): Pair<Boolean, String> =
        if (!createStatement(errorOs)) {
            errorOs.println("数据库错误！请联系管理员")
            Pair(false, "")
        } else try {
            accountInfo.let {
                resultSet = statement!!.executeQuery(
                    SQLState
                        .Select()
                        .from("accounts")
                        .forColumns("equipmentIdentificationCode")
                        .withCondition("id", accountInfo.id)
                        .withCondition("password", accountInfo.password)
                        .toString().also {
                            Globals.logCat.println("Running in SQL: $it")
                        }
                )
                if (resultSet!!.next())
                    Pair(true, resultSet!!.getString(1))
                else {
                    resultSet = statement!!.executeQuery(
                        SQLState
                            .Select()
                            .from("accounts")
                            .withCondition("id", accountInfo.id)
                            .toString().also {
                                Globals.logCat.println("Running in SQL: $it")
                            }
                    )
                    if (resultSet!!.next()) errorOs.println("密码错误")
                    else errorOs.println("用户名不存在")
                    Pair(false, "")
                }
            }
        } catch (_: SQLException) {
            Pair(false, "")
        }

    fun accountCancellation(userName: String, errorOs: PrintStream): Boolean = TODO()

    private fun toVarchar(string: String) = "\'${string}\'"
}