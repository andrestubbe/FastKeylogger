package fastkeylogger.benchmark;

import fastkeylogger.KeybinCodec;
import fastkeylogger.TypingEvent;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class Benchmark {

    private List<TypingEvent> sampleEvents;
    private byte[] sampleBinary;

    @Setup
    public void setup() {
        sampleEvents = new ArrayList<>(1000);
        long baseTime = 1770000000000L;
        String text = "The quick brown fox jumps over the lazy dog. FastKeylogger high-speed typing telemetry benchmark stream.";
        for (int i = 0; i < 1000; i++) {
            char c = text.charAt(i % text.length());
            boolean isCorr = (i % 40 == 0);
            sampleEvents.add(new TypingEvent(c, baseTime + (i * 110L), 45L + (i % 15), isCorr, 0x41 + (i % 26), 0x1E + (i % 20)));
        }
        sampleBinary = KeybinCodec.encode(sampleEvents);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public byte[] benchmarkEncode1000TypingEvents() {
        return KeybinCodec.encode(sampleEvents);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public List<TypingEvent> benchmarkDecode1000TypingEvents() {
        return KeybinCodec.decode(sampleBinary);
    }
}
