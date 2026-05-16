package fastkeylogger;

import fastkeyboard.FastKeyboard;
import fastkeyboard.KeyboardEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Higher-level typing recorder that transforms raw keyboard events into
 * behavioral typing signatures.
 */
public class FastKeylogger {

    private final FastKeyboard keyboard;
    private final List<TypingListener> listeners = new ArrayList<>();
    
    // Map to track dwell time: KeyCode -> StartTimestamp
    private final Map<Integer, Long> activeKeys = new HashMap<>();

    public FastKeylogger() {
        this.keyboard = new FastKeyboard();
        setupInternalListener();
    }

    public void addListener(TypingListener listener) {
        listeners.add(listener);
    }

    public void start() {
        keyboard.start();
    }

    public void stop() {
        keyboard.stop();
    }

    private void setupInternalListener() {
        keyboard.addListener(event -> {
            if (event.isKeyDown()) {
                activeKeys.put(event.virtualKeyCode(), event.timestamp());
            } else {
                Long startTime = activeKeys.remove(event.virtualKeyCode());
                if (startTime != null) {
                    long duration = event.timestamp() - startTime;
                    processEvent(event, duration);
                }
            }
        });
    }

    private void processEvent(KeyboardEvent raw, long duration) {
        char c = raw.translatedChar();
        boolean isCorrection = (raw.virtualKeyCode() == 0x08); // VK_BACK

        // We only care about characters or the backspace correction
        if (c != 0 || isCorrection) {
            TypingEvent event = new TypingEvent(
                c,
                raw.timestamp(),
                duration,
                isCorrection,
                raw.virtualKeyCode(),
                raw.scanCode()
            );
            
            for (TypingListener listener : listeners) {
                listener.onTypingEvent(event);
            }
        }
    }
}
