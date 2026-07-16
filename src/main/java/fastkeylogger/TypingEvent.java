package fastkeylogger;

/**
 * Represents a high-level typing event with timing telemetry.
 */
public record TypingEvent(
    char character,
    long timestamp,
    long durationMs,
    boolean isCorrection,
    int virtualKeyCode,
    int scanCode
) {
    @Override
    public String toString() {
        String charDisplay = character <= 32 ? "[" + virtualKeyCode + "]" : "'" + character + "'";
        return String.format("TypingEvent{char=%s, time=%d, dur=%dms, corr=%b}", 
            charDisplay, timestamp, durationMs, isCorrection);
    }
}
