package fastkeylogger;

import fastkeyboard.FastKeyboard;
import fastkeyboard.FastKeyboardImpl;
import fastkeyboard.FastKeyboardListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Higher-level typing recorder that transforms raw keyboard events into
 * behavioral typing signatures and streaming .keybin logs.
 */
public class FastKeylogger implements FastKeyboardListener, AutoCloseable {

    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

    private final FastKeyboard keyboard;
    private final Path outputDirectory;
    private final int bufferFlushThreshold;
    private final List<TypingListener> listeners = new CopyOnWriteArrayList<>();
    private final List<TypingEvent> memoryBuffer = Collections.synchronizedList(new ArrayList<>(1024));
    private final Map<Integer, Long> activeKeys = new HashMap<>();
    private final AtomicBoolean recording = new AtomicBoolean(false);

    public FastKeylogger() {
        this(null, 5000);
    }

    public FastKeylogger(Path outputDirectory) {
        this(outputDirectory, 5000);
    }

    public FastKeylogger(Path outputDirectory, int bufferFlushThreshold) {
        this.outputDirectory = outputDirectory;
        this.bufferFlushThreshold = bufferFlushThreshold;
        this.keyboard = new FastKeyboardImpl();
        try {
            if (outputDirectory != null) {
                Files.createDirectories(outputDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize key log directory", e);
        }
    }

    public void addListener(TypingListener listener) {
        listeners.add(listener);
    }

    public void removeListener(TypingListener listener) {
        listeners.remove(listener);
    }

    public synchronized void start() {
        if (recording.compareAndSet(false, true)) {
            keyboard.startListening(this);
        }
    }

    public synchronized void stop() {
        if (recording.compareAndSet(true, false)) {
            keyboard.stopListening();
            flush();
        }
    }

    public boolean isRecording() {
        return recording.get();
    }

    @Override
    public void onKeyEvent(long deviceHandle, int vKey, int makeCode, boolean isPressed, boolean isE0, long timestamp, String keyChar) {
        if (!recording.get()) return;

        if (isPressed) {
            activeKeys.put(vKey, timestamp);
        } else {
            Long startTime = activeKeys.remove(vKey);
            if (startTime != null) {
                long duration = timestamp - startTime;
                processEvent(vKey, makeCode, timestamp, duration, keyChar);
            }
        }
    }

    private void processEvent(int vKey, int makeCode, long timestamp, long duration, String keyChar) {
        char c = (keyChar != null && !keyChar.isEmpty()) ? keyChar.charAt(0) : 0;
        boolean isCorrection = (vKey == 0x08); // VK_BACK

        if (c != 0 || isCorrection) {
            TypingEvent event = new TypingEvent(
                    c,
                    timestamp,
                    duration,
                    isCorrection,
                    vKey,
                    makeCode
            );

            memoryBuffer.add(event);

            for (TypingListener listener : listeners) {
                listener.onTypingEvent(event);
            }

            if (outputDirectory != null && memoryBuffer.size() >= bufferFlushThreshold) {
                flush();
            }
        }
    }

    /**
     * Flushes currently buffered typing records to a timestamped .keybin file.
     */
    public synchronized void flush() {
        if (memoryBuffer.isEmpty() || outputDirectory == null) {
            return;
        }

        List<TypingEvent> snapshot;
        synchronized (memoryBuffer) {
            snapshot = new ArrayList<>(memoryBuffer);
            memoryBuffer.clear();
        }

        String fileName = LocalDateTime.now().format(FILE_DATE_FORMAT) + ".keybin";
        Path targetFile = outputDirectory.resolve(fileName);
        try {
            KeybinCodec.writeToFile(targetFile, snapshot);
        } catch (IOException e) {
            System.err.println("Failed to write key records to " + targetFile + ": " + e.getMessage());
        }
    }

    /**
     * Returns a snapshot of memory-buffered records.
     */
    public List<TypingEvent> getBufferedRecords() {
        synchronized (memoryBuffer) {
            return new ArrayList<>(memoryBuffer);
        }
    }

    @Override
    public void close() {
        stop();
    }
}
