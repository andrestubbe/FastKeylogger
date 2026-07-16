package fastkeylogger;

import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        System.out.println("=== FastKeylogger Behavioral Demo v0.1.0 ===");
        
        FastKeylogger logger = new FastKeylogger();
        TextReconstructor reconstructor = new TextReconstructor();
        SessionStorage storage = new SessionStorage();
        
        System.out.println("Log File: " + storage.getLogFilePath());
        System.out.println("Instruction: Start typing anywhere. Press ENTER in this console to stop.");
        System.out.println("--------------------------------------------------");

        logger.addListener(event -> {
            // 1. Reconstruct text
            reconstructor.process(event);
            
            // 2. Save to native storage
            storage.saveEvent(event);
            
            // 3. Live Feedback
            String charDisplay = event.isCorrection() ? "[BACK]" : "'" + event.character() + "'";
            System.out.printf("[%d ms] Typed %-6s | Dwell: %3d ms | Buffer: [%s]\n", 
                event.timestamp() % 100000, 
                charDisplay, 
                event.durationMs(), 
                reconstructor.getText()
            );
        });

        logger.start();

        // Keep alive until user presses ENTER
        new Scanner(System.in).nextLine();

        logger.stop();
        System.out.println("--------------------------------------------------");
        System.out.println("Demo stopped. Final Text: " + reconstructor.getText());
        System.out.println("Events saved to: " + storage.getLogFilePath());
    }
}
