interface RemoteTask {
    fun onTaskStart(headMsg: String, helper: SocketHelper)
    fun onTaskFailed(headMsg: String, helper: SocketHelper, error: Exception)
    fun onTaskClose(headMsg: String)
}