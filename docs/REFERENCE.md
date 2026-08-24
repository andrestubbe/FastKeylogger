# FastKeylogger API Reference

## Core Classes

### 1. `fastkeylogger.FastKeylogger`
* `public FastKeylogger(Path outputDirectory, int bufferFlushThreshold)`: Creates background keylogger instance.
* `public void start()`: Starts raw keyboard input hook thread.
* `public void stop()`: Stops capture and flushes pending buffer.
* `public void addListener(TypingListener listener)`: Registers typing callback.
* `public void removeListener(TypingListener listener)`: Unregisters typing callback.
* `public List<TypingEvent> getBufferedRecords()`: Returns current memory buffer snapshot.
* `public void flush()`: Writes memory buffer to timestamped `.keybin` file.

### 2. `fastkeylogger.KeybinCodec`
* `public static byte[] encode(List<TypingEvent> events)`: Encodes events to FastFileFormat binary stream.
* `public static List<TypingEvent> decode(byte[] bytes)`: Decodes `.keybin` binary payload.
* `public static void writeToFile(Path path, List<TypingEvent> events)`: Writes events to file.
* `public static List<TypingEvent> readFromFile(Path path)`: Reads events from file.

### 3. `fastkeylogger.TextReconstructor`
* `public String getText()`: Returns live reconstructed text string.
* `public void clear()`: Resets reconstructed text buffer.