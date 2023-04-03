import java.net.Socket
import kotlin.Exception

class RequestHandler(
    requestType: String? = null,
    private val socket: Socket
) {

    private var requestCode: String? = null

    private var requestBody: String? = null

    init {
        if (requestType?.startsWith(Globals.verificationPrefix) != true)
            throw Exception("Wrong data input!")
        val mainData = requestType.substring(startIndex = Globals.verificationPrefix.length).split("|")
        if (mainData.size != 2)
            throw Exception("Wrong data input!")
        requestCode = mainData[0]
        requestBody = mainData[1]
    }

    fun start() {
        val work = Globals.codeDictionary.getOrElse(requestCode!!) {
            throw Exception("Error Request Code: $requestCode")
        }

        try {
            val response = work.onTaskStart(requestBody!!, SocketHelper(socket))
            Globals.logCat.println("Work Done!")
        } catch (e: Exception) {
            work.onTaskFailed(requestBody!!, SocketHelper(socket), e)
        } finally {
            socket.close()
            work.onTaskClose(requestBody!!)
        }
    }
}