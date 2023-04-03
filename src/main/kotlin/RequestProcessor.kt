import gsonClasses.AccountInfo
import com.google.gson.Gson
import gsonClasses.VerifyResponse

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.Charset

object RequestProcessor {

    // 注册 1001
    val registerNewAccount = object : RemoteTask {
        override fun onTaskStart(headMsg: String, helper: SocketHelper) {
            Globals.logCat.println("Start Register Account")

            val registerInfo = try {
                Gson().fromJson(headMsg, AccountInfo::class.java)
            } catch (_: Exception) {
                throw Exception("客户端提供了不合法的请求体")
            }

            val failReason = ByteArrayOutputStream()

            val errorOs = PrintStream(failReason)

            val sameResult = Globals.dbEntrance!!
                .Select()
                .from("accounts")
                .forColumns("*")
                .withCondition("id", registerInfo.id)
                .commit(errorOs)

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

            val response = if (success) Gson().toJson(VerifyResponse.success())
            else {
                Globals.logCat.println("Request Work Failed: $failReason")
                Gson().toJson(VerifyResponse.failed(failReason.toString()))
            }

            helper.write(response.toByteArray(Charset.forName("UTF-8")))
        }

        override fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception) {
            helper.write("Unknown Response Body: $headMsg".toByteArray(Charset.forName("UTF-8")))
            error.printStackTrace(Globals.logCat)
        }

        override fun onTaskClose(headMsg: String) {}

    }

    // 登陆 1002
    val loginAccount = object : RemoteTask {
        override fun onTaskStart(headMsg: String, helper: SocketHelper) {
            Globals.logCat.println("Start Login Account")
            val loginInfo = try {
                Gson().fromJson(headMsg, AccountInfo::class.java)
            } catch (_: Exception) {
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

            val response = if (success.first) Gson().toJson(VerifyResponse.success())
            else {
                Globals.logCat.println("Request Work Failed: $failReason")
                Gson().toJson(VerifyResponse.failed(failReason.toString()))
            }

            helper.write(response.toByteArray(Charset.forName("UTF-8")))
        }

        override fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception) {
            helper.write("Unknown Response Body: $headMsg".toByteArray(Charset.forName("UTF-8")))
            error.printStackTrace(Globals.logCat)
        }

        override fun onTaskClose(headMsg: String) {
        }

    }

    // 记录呼吸训练 1003
    val recordTraining = object : RemoteTask {
        override fun onTaskStart(headMsg: String, helper: SocketHelper) {
            TODO("Not yet implemented")
        }

        override fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception) {
            TODO("Not yet implemented")
        }

        override fun onTaskClose(headMsg: String) {
            TODO("Not yet implemented")
        }
    }

    // 获取使用天数 1004
    val queryUsageDays = object : RemoteTask {
        override fun onTaskStart(headMsg: String, helper: SocketHelper) {
            TODO("Not yet implemented")
        }

        override fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception) {
            TODO("Not yet implemented")
        }

        override fun onTaskClose(headMsg: String) {
            TODO("Not yet implemented")
        }
    }

    // 获取总训练时间 1005
    val totalTrainingTime = object : RemoteTask {
        override fun onTaskStart(headMsg: String, helper: SocketHelper) {
            TODO("Not yet implemented")
        }

        override fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception) {
            TODO("Not yet implemented")
        }

        override fun onTaskClose(headMsg: String) {
            TODO("Not yet implemented")
        }

    }

    // 获取服务器时间 1006
    val totalDatetime = object :RemoteTask {
        override fun onTaskStart(headMsg: String, helper: SocketHelper) {
            TODO("Not yet implemented")
        }

        override fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception) {
            TODO("Not yet implemented")
        }

        override fun onTaskClose(headMsg: String) {
            TODO("Not yet implemented")
        }

    }

    // 今日训练时间 1007
    val todayTrainingTime = object : RemoteTask {
        override fun onTaskStart(headMsg: String, helper: SocketHelper) {
            TODO("Not yet implemented")
        }

        override fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception) {
            TODO("Not yet implemented")
        }

        override fun onTaskClose(headMsg: String) {
            TODO("Not yet implemented")
        }

    }

}