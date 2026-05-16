package fastkeylogger;

/**
 * Demo for FastKeylogger showcasing behavioral stream capture.
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("⚡ FastKeylogger v0.1.0 — Behavioral Typing Sensor");
        System.out.println("Status: LISTENING (System-wide)");
        System.out.println("Note: This demo shows text reconstruction and dwell-time metrics.");
        System.out.println("--------------------------------------------------");

        FastKeylogger logger = new FastKeylogger();
        TextReconstructor reconstructor = new TextReconstructor();
        
        logger.addListener(event -> {
            reconstructor.process(event);
            
            // Clear line and redraw status
            System.out.print("\r");
            String text = reconstructor.getText();
            if (text.length() > 50) text = "..." + text.substring(text.length() - 47);
            
            String metrics = String.format(" [Dwell: %3dms] [Correction: %b]", 
                event.durationMs(), event.isCorrection());
            
            System.out.print("TEXT: " + String.format("%-50s", text) + metrics);
        });

        logger.start();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.stop();
        }
    }
}
