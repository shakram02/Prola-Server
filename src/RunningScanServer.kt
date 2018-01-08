import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.logging.Logger

class RunningScanServer(private val messageQueue: LinkedBlockingQueue<String>, tcpPort: Int,
                        udpPort: Optional<Int> = Optional.empty()) {
    private val server = Server()
    private val logger = Logger.getLogger(this.javaClass.simpleName)

    init {
        server.start()

        if (udpPort.isPresent) {
            server.bind(tcpPort, udpPort.get())
        } else {
            server.bind(tcpPort)
        }

        server.addListener(object : Listener() {
            override fun connected(connection: Connection?) {
                super.connected(connection)
                logger.info("A client connected")
            }

            override fun received(clientSocket: Connection?, payload: Any?) {
                if (payload !is String) return

                logger.info("Received:" + payload)
                messageQueue.add(payload)
            }
        })
    }

    fun stop() {
        server.stop()
    }
}