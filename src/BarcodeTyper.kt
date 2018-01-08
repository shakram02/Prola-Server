import VirtualNumpad.lib.VirtualNumpad
import java.util.concurrent.LinkedBlockingQueue
import java.util.logging.Logger

class BarcodeTyper(private val messageQueue: LinkedBlockingQueue<String>) {
    private val virtualNumpad = VirtualNumpad()
    private val typerThread = Thread({ run() })
    private val logger = Logger.getLogger(this.javaClass.simpleName)

    fun start() {
        typerThread.isDaemon = true
        typerThread.start()
    }

    private fun run() {
        while (!typerThread.isInterrupted) {
            try {

                val message: String? = messageQueue.take()

                virtualNumpad.type(message)
                virtualNumpad.pressEnter()
                logger.info("Entered:" + message)

            } catch (e: InterruptedException) {
                println("Ok, time to leave")
                return
            }
        }
    }

    fun stop() {
        typerThread.interrupt()
    }
}