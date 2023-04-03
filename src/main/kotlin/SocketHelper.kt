import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class SocketHelper (
    private val socket: Socket
) {

    val remoteInput: InputStream = socket.getInputStream()

    val remoteOutput: OutputStream = socket.getOutputStream()

    fun write(bytes: ByteArray) {
        remoteOutput.write(bytes)
    }

    fun read(): ByteArray {
        val buf = ByteArray(1024 * 1024)
        val len = remoteInput.read(buf)
        return buf.copyOf(len)
    }
}