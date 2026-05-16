package fastkeylogger;

/**
 * Demo for FastKeylogger showcasing behavioral stream capture.
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("⚡ FastKeylogger v0.1.0 — Behavioral Typing Sensor");
        System.out.println("Recording typing rhythm... (Press CTRL+C to stop)");
        System.out.println("--------------------------------------------------");

        FastKeylogger logger = new FastKeylogger();
        
        logger.addListener(event -> {
            if (event.isCorrection()) {
                System.out.print("\b \b"); // Visual backspace in some terminals
                System.err.println(" [CORRECTION] " + event.durationMs() + "ms");
            } else {
                System.out.println(event + " -> " + event.character());
            }
        });

        logger.start();

        // Keep main thread alive
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.stop();
        }
    }
}
