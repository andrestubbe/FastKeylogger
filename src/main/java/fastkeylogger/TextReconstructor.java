package fastkeylogger;

/**
 * High-level helper to reconstruct the typed string from a stream of
 * TypingEvents.
 */
public class TextReconstructor {
    private final StringBuilder buffer = new StringBuilder();

    public void process(TypingEvent event) {
        if (event.isCorrection()) {
            if (buffer.length() > 0) {
                buffer.setLength(buffer.length() - 1);
            }
        } else if (event.character() >= 32 || event.character() == '\n' || event.character() == '\r') {
            buffer.append(event.character());
        }
    }

    public String getText() {
        return buffer.toString();
    }

    public void clear() {
        buffer.setLength(0);
    }
}
