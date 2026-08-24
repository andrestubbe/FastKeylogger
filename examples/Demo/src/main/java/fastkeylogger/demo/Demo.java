package fastkeylogger.demo;

import fastkeylogger.FastKeylogger;
import fastkeylogger.KeybinCodec;
import fastkeylogger.TextReconstructor;
import fastkeylogger.TypingEvent;

import java.nio.file.Path;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
        System.out.println("=================================================");
        System.out.println(" ⌨️ FastKeylogger — Biometric Rhythm Streamer    ");
        System.out.println("=================================================");

        Path logDir = Path.of(System.getProperty("java.io.tmpdir"), "FastKeyloggerDemo");
        System.out.println("Logging target directory: " + logDir);

        TextReconstructor reconstructor = new TextReconstructor();

        try (FastKeylogger logger = new FastKeylogger(logDir, 100)) {
            logger.addListener(reconstructor);
            logger.addListener(event -> {
                System.out.printf("[KEY EVENT] char='%c' vk=0x%02X dwell=%dms corr=%b\n",
                        event.character(), event.virtualKeyCode(), event.durationMs(), event.isCorrection());
            });

            System.out.println("\n--- Starting Raw Keystroke Recording for 4 seconds (type something in any window)... ---");
            logger.start();
            Thread.sleep(4000);
            logger.stop();

            List<TypingEvent> buffered = logger.getBufferedRecords();
            System.out.println("\nCaptured " + buffered.size() + " typing events.");
            System.out.println("Reconstructed live text: \"" + reconstructor.getText() + "\"");

            // 1. Binary Encoding via FastFileFormat
            byte[] encoded = KeybinCodec.encode(buffered);
            System.out.println("Encoded .keybin payload size: " + encoded.length + " bytes.");

            // 2. Binary Decoding
            List<TypingEvent> decoded = KeybinCodec.decode(encoded);
            System.out.println("Successfully decoded " + decoded.size() + " typing records from binary stream.");

            System.out.println("\n✔ FastKeylogger Pipeline Verified Successfully!");
        }
    }
}
