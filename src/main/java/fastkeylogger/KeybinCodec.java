package fastkeylogger;

import fastfileformat.BinaryHeader;
import fastfileformat.BinaryReader;
import fastfileformat.BinaryWriter;
import fastfileformat.FastFileFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * High-speed binary serializer and stream decoder for typing event log files (.keybin).
 * Built on top of FastFileFormat and FastBinary VarInt compression.
 */
public final class KeybinCodec {
    /**
     * Payload type identifier for FastJava Raw Keyboard Logs (0x0004).
     */
    public static final short PAYLOAD_TYPE_KEYBIN = 0x0004;

    private KeybinCodec() {}

    /**
     * Encodes a list of typing events into a compressed FastFileFormat binary byte array.
     *
     * @param events List of typing events.
     * @return Compact binary byte array with 12-byte header.
     */
    public static byte[] encode(List<TypingEvent> events) {
        if (events == null || events.isEmpty()) {
            BinaryWriter finalWriter = FastFileFormat.binaryWriter(12);
            finalWriter.writeHeader(FastFileFormat.DEFAULT_MAGIC, FastFileFormat.DEFAULT_VERSION, PAYLOAD_TYPE_KEYBIN, 0);
            return finalWriter.toByteArray();
        }

        BinaryWriter payloadWriter = FastFileFormat.binaryWriter(events.size() * 16);
        payloadWriter.writeVarInt(events.size());

        long baseTime = events.get(0).timestamp();
        payloadWriter.writeLong(baseTime);

        long lastTime = baseTime;
        for (TypingEvent ev : events) {
            long delta = ev.timestamp() - lastTime;
            lastTime = ev.timestamp();

            payloadWriter.writeVarLong(delta);
            payloadWriter.writeVarLong(ev.durationMs());
            payloadWriter.writeVarInt((int) ev.character());
            payloadWriter.writeByte((byte) (ev.isCorrection() ? 1 : 0));
            payloadWriter.writeVarInt(ev.virtualKeyCode());
            payloadWriter.writeVarInt(ev.scanCode());
        }

        byte[] payload = payloadWriter.toByteArray();

        BinaryWriter finalWriter = FastFileFormat.binaryWriter(12 + payload.length);
        finalWriter.writeHeader(
                FastFileFormat.DEFAULT_MAGIC,
                FastFileFormat.DEFAULT_VERSION,
                PAYLOAD_TYPE_KEYBIN,
                payload.length
        );
        finalWriter.writeBytes(payload);
        return finalWriter.toByteArray();
    }

    /**
     * Decodes a .keybin binary payload into a list of TypingEvent instances.
     *
     * @param bytes Binary payload.
     * @return List of reconstructed TypingEvents.
     */
    public static List<TypingEvent> decode(byte[] bytes) {
        if (bytes == null || bytes.length < 12) {
            return Collections.emptyList();
        }

        BinaryReader reader = FastFileFormat.binaryReader(bytes);
        BinaryHeader header = reader.readHeader();

        if (header.getMagic() != FastFileFormat.DEFAULT_MAGIC) {
            throw new IllegalArgumentException("Invalid FastFileFormat magic header: " + Integer.toHexString(header.getMagic()));
        }
        if (header.getPayloadType() != PAYLOAD_TYPE_KEYBIN) {
            throw new IllegalArgumentException("Unexpected payload type for Keybin: " + header.getPayloadType());
        }
        if (header.getPayloadLength() == 0) {
            return Collections.emptyList();
        }

        int count = reader.readVarInt();
        long currentTimestamp = reader.readLong();

        List<TypingEvent> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            long delta = reader.readVarLong();
            currentTimestamp += delta;

            long durationMs = reader.readVarLong();
            char c = (char) reader.readVarInt();
            boolean isCorrection = reader.readByte() == 1;
            int vKey = reader.readVarInt();
            int scanCode = reader.readVarInt();

            list.add(new TypingEvent(c, currentTimestamp, durationMs, isCorrection, vKey, scanCode));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Saves typing events directly to a .keybin file.
     */
    public static void writeToFile(Path path, List<TypingEvent> events) throws IOException {
        byte[] bytes = encode(events);
        Files.write(path, bytes);
    }

    /**
     * Reads typing events directly from a .keybin file.
     */
    public static List<TypingEvent> readFromFile(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return decode(bytes);
    }
}
