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
            jsonProcessor.fromJson(it, AccountInfo::class.java)
        } catch (_: Exception) {
            Globals.logCat.println("客户端提供了不合法的请求体")
            throw Exception("客户端提供了不合法的请求体")
        }
        val success: Boolean
        val failReason = ByteArrayOutputStream()

        success = Globals.dbEntrance?.accountRegister(registerInfo, PrintStream(failReason)) ?: false

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
        var success = Pair(false, "")
        val failReason = ByteArrayOutputStream()

        PrintStream(failReason).run {
            try {
                success = Globals.dbEntrance?.accountSignIn(loginInfo, this@run) ?: Pair(false, "")
            } catch (e: Exception) {
                e.printStackTrace(this)
            }
        }
        if (success.first) jsonProcessor.toJson(VerifyResponse.success(success.second))
        else {
            Globals.logCat.println("Request Work Failed: $failReason")
            jsonProcessor.toJson(VerifyResponse.failed(failReason.toString()))
        }
    }
}