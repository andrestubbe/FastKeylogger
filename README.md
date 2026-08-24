# FastKeylogger 0.1.2 [ALPHA] — Native Raw Keystroke Logger, Typing Biometrics & Stream Compression

[![Status](https://img.shields.io/badge/status-0.1.2-brightgreen.svg)](https://github.com/andrestubbe/FastKeylogger/releases/tag/0.1.2)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastKeylogger)

---

**⚡ High-performance raw keystroke logger, biometric typing cadence telemetry, and `.keybin` dual-format streaming engine for Java.**

**FastKeylogger** intercepts raw Windows keyboard events directly via **[FastKeyboard](https://github.com/andrestubbe/FastKeyboard)**, extracts high-resolution behavioral dwell/flight-time dynamics and keystroke corrections, and compresses streams in real-time into binary `.keybin` logs via **[FastFileFormat](https://github.com/andrestubbe/FastFileFormat)** & **[FastBinary](https://github.com/andrestubbe/FastBinary)**.

---

## Quick Start

```java
import fastkeylogger.*;
import java.nio.file.Path;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
        Path logDir = Path.of("logs/keyboard");
        TextReconstructor reconstructor = new TextReconstructor();

        // 1. Live background capture
        try (FastKeylogger logger = new FastKeylogger(logDir, 5000)) {
            logger.addListener(reconstructor);
            logger.addListener(event -> {
                System.out.printf("Key '%c' dwell=%dms corr=%b\n",
                        event.character(), event.durationMs(), event.isCorrection());
            });

            logger.start();
            Thread.sleep(4000);
            logger.stop(); // Flushes to timestamped .keybin
        }

        // 2. High-speed FastFileFormat codec & reconstructed text
        Path sessionFile = logDir.resolve("session.keybin");
        List<TypingEvent> events = KeybinCodec.readFromFile(sessionFile);
        System.out.println("Reconstructed text: " + reconstructor.getText());
    }
}
```

---

## Key Features

- **⌨️ Win32 Raw Input Interception** — Sub-millisecond keystroke telemetry capturing raw scan codes and virtual keys via `FastKeyboard`.
- **⏱️ Biometric Cadence Dynamics** — Precise dwell-time (key press duration) and flight-time (inter-key intervals) measurement.
- **⚡ FastFileFormat `.keybin` Compression** — Delta-timestamped VarInt event serialization (Payload ID `0x0004`).
- **🔤 Real-Time Text Reconstruction** — Backspace-aware text accumulator and state tracker (`TextReconstructor`).
- **📦 Zero Heavy Dependencies** — Native-speed pure Java 17+ core backed by `FastCore`, `FastBinary`, and `FastFileFormat`.

---

## Real-World Scenarios

- **🛡️ Continuous Biometric Authentication** — Verifying user identity via unique keystroke rhythm patterns and behavioral cadence.
- **🤖 AI Agent Telemetry & Imitation** — Capturing fine-grained typing cadences, hesitation pauses, and self-corrections for autonomous agents.
- **📊 Ergonomics & Speed Analytics** — Profiling real-world WPM, error rates, and burst typing velocities.
- **📑 Audit & Recovery Logging** — Crash-resilient background typing preservation with microsecond precision.

---

## Performance Benchmarks

FastKeylogger is profiled using **JMH** to guarantee maximum stream throughput and zero dropped input packets.

| Benchmark Operation | Score (ops/ms) | Event Throughput | Memory Overhead |
|---|---|---|---|
| **Binary Stream Decoding (`.keybin`)** | **~58,800 ops/ms** | **> 58.8 Million events/sec** | **Zero-Copy Streaming** |
| **Binary Stream Encoding (`.keybin`)** | **~26,800 ops/ms** | **> 26.8 Million events/sec** | **Compact VarInt Delta Buffer** |

*Run the benchmarks locally:* `.\run-benchmark.bat`

---

## API Quick Reference

| Method / Class | Description |
|---|---|
| `new FastKeylogger(path, threshold)` | Creates a logger flushing every N records into timestamped `.keybin` files. |
| `logger.start()` / `logger.stop()` | Starts and stops native raw keyboard event recording. |
| `logger.addListener(listener)` | Subscribes to real-time typing events and dwell times. |
| `KeybinCodec.encode(events)` | Serializes event list into compressed FastFileFormat binary byte array. |
| `KeybinCodec.decode(bytes)` | Deserializes `.keybin` binary bytes back into `List<TypingEvent>`. |
| `new TextReconstructor()` | Observer that maintains live reconstructed buffer with correction handling. |

---

## Technical Examples & Hero Demos

| Case | Java Example | Launcher | Description |
|---|---|---|---|
| **Live Typing Streamer & Reconstructor** | [Demo.java](examples/Demo/src/main/java/fastkeylogger/demo/Demo.java) | `run-demo.bat` | 4-second live raw recording, dwell time logging, `.keybin` compression, and text reconstruction. |
| **JMH Microbenchmark Suite** | [Benchmark.java](examples/Benchmark/src/main/java/fastkeylogger/benchmark/Benchmark.java) | `run-benchmark.bat` | High-throughput encoding/decoding benchmarks for 1,000-event telemetry streams. |

---

## Installation

### Option 1: Maven (JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastKeylogger</artifactId>
        <version>0.1.2</version>
    </dependency>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastKeyboard</artifactId>
        <version>0.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastFileFormat</artifactId>
        <version>0.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastBinary</artifactId>
        <version>0.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastcore</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastKeylogger:0.1.2'
    implementation 'com.github.andrestubbe:FastKeyboard:0.1.0'
    implementation 'com.github.andrestubbe:FastFileFormat:0.1.0'
    implementation 'com.github.andrestubbe:FastBinary:0.1.0'
    implementation 'com.github.andrestubbe:fastcore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. ⌨️ **[FastKeylogger-0.1.2.jar](https://github.com/andrestubbe/FastKeylogger/releases/download/0.1.2/FastKeylogger-0.1.2.jar)** (Typing Logger & Rhythm Engine)
2. ⚡ **[FastKeyboard-0.1.0.jar](https://github.com/andrestubbe/FastKeyboard/releases/download/0.1.0/FastKeyboard-0.1.0.jar)** (Native Win32 Raw Keyboard Input)
3. 📄 **[FastFileFormat-0.1.0.jar](https://github.com/andrestubbe/FastFileFormat/releases/download/0.1.0/FastFileFormat-0.1.0.jar)** (Dual Binary & Text File Format)
4. ⚡ **[FastBinary-0.1.0.jar](https://github.com/andrestubbe/FastBinary/releases/download/0.1.0/FastBinary-0.1.0.jar)** (VarInt & Binary Packing)
5. ⚙️ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (Foundation Library)

---

## Documentation

* **[REFERENCE.md](docs/REFERENCE.md)**: Full API reference and method signatures.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: Architectural design principles and biometric rhythm telemetry.
* **[CHANGELOG.md](docs/CHANGELOG.md)**: Release history and version notes.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.
* **[COMPILE.md](docs/COMPILE.md)**: Instructions for compiling from source.

---

## Platform Support

| Platform | Status |
|---|---|
| Windows 10/11 (x64) | ✅ Fully Supported (Win32 Raw Input) |
| Linux | 🚧 Planned (evdev) |
| macOS | 🚧 Planned (CGEventTap) |

---

## License

MIT License. See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastKeyboard](https://github.com/andrestubbe/FastKeyboard) — Low-level raw keyboard event interceptor
- [FastMouseLogger](https://github.com/andrestubbe/FastMouseLogger) — Raw mouse event logger, `.mousebin` streaming & heatmaps
- [FastHotkey](https://github.com/andrestubbe/FastHotkey) — High-speed global hotkey listener
- [FastFileFormat](https://github.com/andrestubbe/FastFileFormat) — Universal dual-format binary & text document engine
- [FastSharedMemory](https://github.com/andrestubbe/FastSharedMemory) — Zero-copy inter-process shared memory for Java

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*
