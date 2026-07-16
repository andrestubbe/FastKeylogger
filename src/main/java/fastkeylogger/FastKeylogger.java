package fastkeylogger;

import fastkeyboard.FastKeyboard;
import fastkeyboard.FastKeyboardImpl;
import fastkeyboard.FastKeyboardListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Higher-level typing recorder that transforms raw keyboard events into
 * behavioral typing signatures.
 */
public class FastKeylogger implements FastKeyboardListener {

    private final FastKeyboard keyboard;
    private final List<TypingListener> listeners = new ArrayList<>();
    
    // Map to track dwell time: VirtualKeyCode -> StartTimestamp
    private final Map<Integer, Long> activeKeys = new HashMap<>();

    public FastKeylogger() {
        this.keyboard = new FastKeyboardImpl();
    }

    public void addListener(TypingListener listener) {
        listeners.add(listener);
    }

    public void start() {
        keyboard.startListening(this);
    }

    public void stop() {
        keyboard.stopListening();
    }

    @Override
    public void onKeyEvent(long deviceHandle, int vKey, int makeCode, boolean isPressed, boolean isE0, long timestamp, String keyChar) {
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

        // We only care about characters or the backspace correction
        if (c != 0 || isCorrection) {
            TypingEvent event = new TypingEvent(
                c,
                timestamp,
                duration,
                isCorrection,
                vKey,
                makeCode
            );
            
            for (TypingListener listener : listeners) {
                listener.onTypingEvent(event);
            }
        }
    }
}
