import java.io.PrintStream

object Globals {
    const val databaseDriver = "com.mysql.cj.jdbc.Driver"
    const val databaseUrl = "jdbc:mysql://127.0.0.1:3306"
    const val databaseUserName = "root"
    const val databasePassword = "helloworld"
    const val verificationPrefix = "GZW_BS "
    const val databaseName = "BreathSystem"
    const val serverHost = "0.0.0.0"
    const val serverPort = 21301
    var dbEntrance: DatabaseEntrance? = null
    var logCat: PrintStream = System.out

    var codeDictionary: Map<String, RemoteTask> = mapOf(
        "1001" to RequestProcessor.registerNewAccount,
        "1002" to RequestProcessor.loginAccount,
        "1003" to RequestProcessor.recordTraining,
        "1004" to RequestProcessor.queryUsageDays,
        "1005" to RequestProcessor.totalTrainingTime,
        "1006" to RequestProcessor.totalDatetime,
        "1007" to RequestProcessor.todayTrainingTime
    )
}