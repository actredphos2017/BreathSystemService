import gsonClasses.AccountInfo
import com.google.gson.Gson
import gsonClasses.VerifyResponse
import java.io.ByteArrayOutputStream
import java.io.PrintStream

object RequestProcessor {
    private val jsonProcessor = Gson()

    val registerNewAccount: (String) -> String = {
        Globals.logCat.println("Start Register Account")

        val registerInfo = try {
            jsonProcessor.fromJson(it, AccountInfo::class.java) // 解析收到的 Json 数据，判断合法性
        } catch (_: Exception) {
            Globals.logCat.println("客户端提供了不合法的请求体")
            throw Exception("客户端提供了不合法的请求体")
        }

        val failReason = ByteArrayOutputStream()
        // 创建一个输入流，类似 C++ 的 stringstream
        // 用于获取失败后的错误信息

        val errorOs = PrintStream(failReason)
        // 为流创建一个 PrintStream 接口，以实现像终端打印的效果

        val sameResult = Globals.dbEntrance!!
            .Select()
            .from("accounts")
            .forColumns("*")
            .withCondition("id", registerInfo.id)
            .commit(errorOs)
        // 链式生成、运行 SQL 语句

        val success = if (sameResult == null)
            false
        else if (sameResult.next()) {
            Globals.logCat.println("Account Register Error: 用户名已存在 for ${registerInfo.id}")
            errorOs.println("用户名已存在")
            false
        } else Globals.dbEntrance!!
            .Insert()
            .into("accounts")
            .forColumns("name", "id", "password")
            .withItems(registerInfo.name, registerInfo.id, registerInfo.password)
            .commit(errorOs)

        if (success) jsonProcessor.toJson(VerifyResponse.success())
        else {
            Globals.logCat.println("Request Work Failed: $failReason")
            jsonProcessor.toJson(VerifyResponse.failed(failReason.toString()))
        }
    }

    val loginAccount: (String) -> String = {
        Globals.logCat.println("Start Login Account")

        val loginInfo = try {
            jsonProcessor.fromJson(it, AccountInfo::class.java)
        } catch (_: Exception) {
            Globals.logCat.println("客户端提供了不合法的请求体")
            throw Exception("客户端提供了不合法的请求体")
        }

        val failReason = ByteArrayOutputStream()
        val errorOs = PrintStream(failReason)

        var selectResult = Globals.dbEntrance!!
            .Select()
            .from("accounts")
            .forColumns("equipmentIdentificationCode")
            .withCondition("id", loginInfo.id)
            .withCondition("password", loginInfo.password)
            .commit(errorOs)

        val success = if (selectResult == null)
            Pair(false, errorOs)
        else if (selectResult.next())
            Pair(true, "")
        else {
            selectResult = Globals.dbEntrance!!
                .Select()
                .from("accounts")
                .forColumns("*")
                .withCondition("id", loginInfo.id)
                .commit(errorOs)

            if (selectResult == null) Pair(false, errorOs)
            else if (selectResult.next()) Pair(false, "密码错误！")
            else Pair(false, "账号不存在！")
        }

        if (success.first) jsonProcessor.toJson(VerifyResponse.success())
        else {
            Globals.logCat.println("Request Work Failed: $failReason")
            jsonProcessor.toJson(VerifyResponse.failed(failReason.toString()))
        }
    }

}