# FastKeylogger — Behavioral Typing Logic for Java v0.1.0 [ALPHA] - v0.1.0
**Behavioral typing sensor and rhythm analysis layer for the FastJava ecosystem.**

FastKeylogger transforms raw hardware events into high-level **behavioral typing signatures**. While standard loggers only capture characters, FastKeylogger captures the **biological rhythm** (dwell times, flight times) and cognitive patterns (correction behavior) of the user.

```java
// Quick Start — Example
import fastkeylogger.FastKeylogger;

public class Demo {
    public static void main(String[] args) {
        FastKeylogger logger = new FastKeylogger();
        
        logger.addListener(event -> {
            System.out.println("Typed: " + event.character() + " (Hold: " + event.durationMs() + "ms)");
        });
        
        logger.start();
    }
}
```

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastKeylogger/releases/tag/v0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

## Table of Contents
- [Key Features](#key-features)
- [Installation](#installation)
- [API Reference](#api-reference)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Key Features

- **🚀 Text Reconstruction** — Converts raw hardware scancodes into a logical character stream.
- **⏱️ Timing Signatures** — Captures precise **Dwell Time** (hold duration) and **Flight Time** (latency).
- **🧹 Correction Awareness** — Monitors backspaces and deletions to analyze cognitive load and error patterns.
- **⚡ Zero Polling** — Purely event-driven logic based on `FastKeyboard`.

---

## Installation

### Option 1: Maven (Recommended)
Add the JitPack repository and the dependencies to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastKeylogger Library -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastkeylogger</artifactId>
        <version>v0.1.0</version>
    </dependency>

    <!-- FastCore (Required Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastcore</artifactId>
        <version>v0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastkeylogger:v0.1.0'
    implementation 'com.github.andrestubbe:fastcore:v0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)
Download the latest JARs directly to add them to your classpath:

1. 📦 **[fastkeylogger-v0.1.0.jar](https://github.com/andrestubbe/FastKeylogger/releases/download/v0.1.0/fastkeylogger-v0.1.0.jar)** (The Core Library)
2. ⚙️ **[fastcore-v0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/v0.1.0/fastcore-v0.1.0.jar)** (The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.


## API Reference

| Method | Description |
|--------|-------------|
| `void start()` | Starts the underlying keyboard listener. |
| `void stop()` | Stops the listener and releases resources. |
| `void addListener(TypingListener)` | Registers a new observer for processed events. |

---

## Platform Support

| Platform | Status |
|----------|--------|
| Windows 10/11 (x64) | ✅ Fully Supported |
| Linux | 🚧 Dependent on FastKeyboard |
| macOS | 🚧 Dependent on FastKeyboard |

---

## License
MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects
- [FastKeyboard](https://github.com/andrestubbe/FastKeyboard) — Windows RawInput Driver
- [FastCore](https://github.com/andrestubbe/FastCore) — Native Library Loader

---
**Made with ⚡ by Andre Stubbe**

