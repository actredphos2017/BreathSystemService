class RequestHandler(
    requestType: String? = null
) {

    var mode: RequestMode? = null
        private set

    var requestCode: String? = null
        private set

    init {
        if(requestType?.startsWith("BS") == true) {
            requestCode = requestType.substring(2)
            mode = when(requestCode!![0]) {
                '0' -> RequestMode.obtain
                '1' -> RequestMode.upload
                '2' -> RequestMode.verification
                else -> null
            }
        }
    }
}