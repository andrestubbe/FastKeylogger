package fastkeylogger;

import fastio.FastIO;
import fastjson.FastJSON;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * High-performance persistence for TypingEvents using FastJSON and FastIO.
 */
public class SessionStorage {
    private final String filePath;

    public SessionStorage() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        File logsDir = new File("logs");
        if (!logsDir.exists()) logsDir.mkdir();
        
        this.filePath = new File(logsDir, "session_" + timestamp + ".jsonl").getAbsolutePath();
    }

    public void saveEvent(TypingEvent event) {
        // Use FastJSON to build the event line
        byte[] jsonBytes = FastJSON.object()
            .add("char", String.valueOf(event.character()))
            .add("time", event.timestamp())
            .add("dwell", event.durationMs())
            .add("corr", event.isCorrection())
            .add("vk", event.virtualKeyCode())
            .build();
        
        // Use FastIO to append the bytes to the file
        // Note: Adding a newline for JSONL format
        byte[] lineWithNewline = new byte[jsonBytes.length + 1];
        System.arraycopy(jsonBytes, 0, lineWithNewline, 0, jsonBytes.length);
        lineWithNewline[jsonBytes.length] = '\n';
        
        try {
            FastIO.appendBytes(filePath, lineWithNewline);
        } catch (java.io.IOException e) {
            System.err.println("FastKeylogger: Failed to save event to " + filePath);
            e.printStackTrace();
        }
    }

    public String getLogFilePath() {
        return filePath;
    }
}
