package fastkeylogger;

/**
 * Interface for observing processed typing streams.
 */
public interface TypingListener {
    /**
     * Called when a new character or correction is recorded.
     * @param event The processed typing event.
     */
    void onTypingEvent(TypingEvent event);
}
