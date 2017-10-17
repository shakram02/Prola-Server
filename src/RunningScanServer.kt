import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

class RunningScanServer(private val messageQueue: LinkedBlockingQueue<String>, tcpPort: Int,
                        udpPort: Optional<Int> = Optional.empty()) {
    private val server = Server()

    init {
        server.start()

        if (udpPort.isPresent) {
            server.bind(tcpPort, udpPort.get())
        } else {
            server.bind(tcpPort)
        }

        server.addListener(object : Listener() {
            override fun received(clientSocket: Connection?, payload: Any?) {
                val message = payload as String
                messageQueue.add(message)
            }
        })
    }

    fun stop() {
        server.stop()
    }
}