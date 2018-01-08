import java.util.*
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
    val udpPort = 51213

    val runningScanServer = RunningScanServer(blockingQueue, tcpPort, Optional.of(udpPort))
    val barcodeTyper = BarcodeTyper(blockingQueue)

    barcodeTyper.start()
    println("Running..., Press any key to exit")
    readLine()

    barcodeTyper.stop()
    runningScanServer.stop()
}