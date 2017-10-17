import com.esotericsoftware.kryonet.Client
import java.util.concurrent.LinkedBlockingQueue


/**
 * The server opens a receiver thread that waits for clients,
 * when a client is connected a new thread is created, this
 * method is used for simplicity since the number of clients will be
 * really limited.
 *
 * When a client connects, the messages it receives will be placed
 * in the blocking queue, the main thread will be reading from
 * the blocking queue and then makes the input through the virtual number pad
 *
 */

fun main(args: Array<String>) {
    val blockingQueue = LinkedBlockingQueue<String>()
    val tcpPort = 60111
    val localhost = "127.0.0.1"
    val runningScanServer = RunningScanServer(blockingQueue, tcpPort)
    val barcodeTyper = BarcodeTyper(blockingQueue)

    barcodeTyper.start()
    println("Press any key to send message")
    readLine()

    val client = Client()
    client.start()
    client.connect(5000, localhost, tcpPort)

    val request = "3211332"
    client.sendTCP(request)

    println("Press any key to exit")
    readLine()

    barcodeTyper.stop()
    runningScanServer.stop()
}