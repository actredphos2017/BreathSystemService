package myUtils


class SQLState {

    companion object {
        fun toVarchar(string: String): String = "\'$string\'"


    }

    class Insert {
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

        override fun toString(): String {
            if (tableName == null || columns == null || values == null)
                throw Exception("Insert: 缺少参数")
            else if (columns!!.size != values!!.size)
                throw Exception("Insert: 参数数与值不匹配")
            return StringBuilder().apply {
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
        }

    }

    class Select {
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

        override fun toString(): String {
            if (tableName == null)
                throw Exception("Insert: 缺少参数")
            return StringBuilder().apply {
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
        }
    }


}