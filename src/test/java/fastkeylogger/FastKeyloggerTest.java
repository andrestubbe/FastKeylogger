package fastkeylogger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FastKeyloggerTest {

    @Test
    public void testKeybinEncodingAndDecoding() {
        List<TypingEvent> events = new ArrayList<>();
        long baseTime = 1770000000000L;

        events.add(new TypingEvent('H', baseTime, 45L, false, 0x48, 0x23));
        events.add(new TypingEvent('e', baseTime + 120L, 50L, false, 0x45, 0x12));
        events.add(new TypingEvent('l', baseTime + 210L, 48L, false, 0x4C, 0x26));
        events.add(new TypingEvent((char) 0, baseTime + 350L, 30L, true, 0x08, 0x0E)); // Backspace

        byte[] binary = KeybinCodec.encode(events);
        assertNotNull(binary);
        assertTrue(binary.length >= 12);

        List<TypingEvent> decoded = KeybinCodec.decode(binary);
        assertEquals(4, decoded.size());

        assertEquals('H', decoded.get(0).character());
        assertEquals(baseTime, decoded.get(0).timestamp());
        assertEquals(45L, decoded.get(0).durationMs());
        assertFalse(decoded.get(0).isCorrection());

        assertEquals('e', decoded.get(1).character());
        assertEquals(baseTime + 120L, decoded.get(1).timestamp());

        assertTrue(decoded.get(3).isCorrection());
        assertEquals(0x08, decoded.get(3).virtualKeyCode());
    }

    @Test
    public void testFilePersistence(@TempDir Path tempDir) throws IOException {
        Path logFile = tempDir.resolve("session.keybin");
        List<TypingEvent> events = List.of(
                new TypingEvent('A', 1000L, 60L, false, 0x41, 0x1E),
                new TypingEvent('B', 1150L, 55L, false, 0x42, 0x30)
        );

        KeybinCodec.writeToFile(logFile, events);
        assertTrue(logFile.toFile().exists());

        List<TypingEvent> restored = KeybinCodec.readFromFile(logFile);
        assertEquals(2, restored.size());
        assertEquals('A', restored.get(0).character());
        assertEquals('B', restored.get(1).character());
    }
}
