import VirtualNumpad.lib.VirtualNumpad
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class BarcodeTyper(private val messageQueue: LinkedBlockingQueue<String>) {
    private val willContinue = AtomicBoolean(true)
    private val virtualNumpad = VirtualNumpad()
    private val typerThread = Thread({ run() })

    fun start() {
        typerThread.isDaemon = true
        typerThread.start()
    }

    private fun run() {
        while (willContinue.get()) {
            try {

                val message: String? = messageQueue.poll(1, TimeUnit.SECONDS)

                // Waited and the queue is empty
                if (message.isNullOrEmpty()) {
                    continue
                }

                virtualNumpad.type(message)
            } catch (e: InterruptedException) {
                println("Ok, time to leave")
                return
            }
        }
    }

    fun stop() {
        willContinue.set(false)
        typerThread.join()
    }
}