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

    inner class Select() {
        private var tableName: String? = null
        private var columns: List<String>? = null
        private var conditions: ArrayList<Pair<String, Any>> = ArrayList()

        fun from(table: String): Select {
            tableName = table
            return this
        }

        fun forColumns(vararg column: String): Select {
            columns = column.toList()
            return this
        }

        fun withCondition(column: String, value: Any): Select {
            conditions.add(Pair(column, value))
            return this
        }

        fun commit(errorOs: PrintStream = Globals.logCat): ResultSet? = if (tableName == null) {
            errorOs.println("Insert: 缺少参数")
            null
        } else if (!createStatement(errorOs)) {
            errorOs.println("数据库错误！请联系管理员")
            null
        } else try {
            statement!!.executeQuery(
                StringBuilder().apply {
                    append("select ")
                    if (columns == null)
                        append("*")
                    else for ((index, value) in columns!!.withIndex())
                        if (index == 0)
                            append(value)
                        else
                            append(", $value")
                    append(" from $tableName")
                    if (conditions.isNotEmpty()) {
                        append(" where ")
                        for ((index, value) in conditions.withIndex()) {
                            if (value.second is String) {
                                if (index == 0)
                                    append("${value.first} = ${toVarchar(value.second as String)}")
                                else
                                    append(" and ${value.first} = ${toVarchar(value.second as String)}")
                            } else {
                                if (index == 0)
                                    append("${value.first} = ${value.second}")
                                else
                                    append(" and ${value.first} = ${value.second}")
                            }
                        }
                    }
                }.toString()
            )
        } catch (_: Exception) {
            null
        }
    }

    inner class Insert() {
        private var tableName: String? = null
        private var columns: List<String>? = null
        private var values: List<Any>? = null

        fun into(table: String): Insert {
            tableName = table
            return this
        }

        fun forColumns(vararg column: String): Insert {
            columns = column.toList()
            return this
        }

        fun withItems(vararg item: Any): Insert {
            values = item.toList()
            return this
        }

        fun commit(errorOs: PrintStream = Globals.logCat): Boolean =
            if (tableName == null || columns == null || values == null) {
                errorOs.println("Insert: 缺少参数")
                false
            } else if (columns!!.size != values!!.size) {
                errorOs.println("Insert: 参数数与值不匹配")
                false
            } else try {
                statement!!.execute(
                    StringBuilder().apply {
                        append("insert into $tableName(")
                        for ((index, value) in columns!!.withIndex())
                            if (index == 0)
                                append(value)
                            else
                                append(", $value")
                        append(") values (")
                        for ((index, value) in values!!.withIndex()) {
                            if (value is String) {
                                if (index == 0)
                                    append(toVarchar(value))
                                else
                                    append(", ${toVarchar(value)}")
                            } else {
                                if (index == 0)
                                    append(value)
                                else
                                    append(", $value")
                            }
                        }
                        append(")")
                    }.toString()
                )
                true
            } catch (_: Exception) {
                errorOs.println("Insert: 搜索失败，请检查搜索语句")
                false
            }
    }

    fun toVarchar(string: String): String = "\'$string\'"

}