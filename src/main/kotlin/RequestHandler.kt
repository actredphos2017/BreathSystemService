import java.lang.Exception
import java.net.Socket

class RequestHandler(
    requestType: String? = null,
    val socket: Socket
) {

    var requestCode: String? = null
        private set

    var requestBody: String? = null
        private set

    var outputData: String? = null


    init {
        if (requestType?.startsWith(Globals.verificationPrefix) != true)
            throw Exception("Wrong data input!")
        val mainData = requestType.substring(startIndex = Globals.verificationPrefix.length).split("|")
        if(mainData.size != 2)
            throw Exception("Wrong data input!")
        requestCode = mainData[0]
        requestBody = mainData[1]
    }

    fun start() {
        val work = Globals.codeDictionary.getOrElse(requestCode!!) {
            throw Exception("Error Request Code: $requestCode")
        }
        val response = work(requestBody!!).also {
            Globals.logCat.println("Work Done! Response Get: $it")
        }.toByteArray()

        val socketOutputStream = socket.getOutputStream()
        socketOutputStream.write(response)
        socketOutputStream.close()
    }
}