# FastKeylogger v0.1.0 — Behavioral Typing Logic for Java

**Behavioral typing sensor and rhythm analysis layer for the FastJava ecosystem.**

[![Build](https://img.shields.io/github/actions/workflow/status/andrestubbe/FastKeylogger/maven.yml?branch=main)](https://github.com/andrestubbe/FastKeylogger/actions)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/andrestubbe/FastKeylogger.svg)](https://jitpack.io/#andrestubbe/FastKeylogger)

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

FastKeylogger requires **three** JARs in your environment: the module itself, `FastKeyboard` (the driver), and `FastCore` (the native loader).

### Maven (JitPack)
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- 1. The Keylogger Logic -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastKeylogger</artifactId>
        <version>0.1.0</version>
    </dependency>
    
    <!-- 2. The Keyboard Driver -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastKeyboard</artifactId>
        <version>0.2.0</version>
    </dependency>

    <!-- 3. The Native Loader -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastcore</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Gradle (JitPack)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastKeylogger:0.1.0'
    implementation 'com.github.andrestubbe:FastKeyboard:0.2.0'
    implementation 'com.github.andrestubbe:fastcore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)
Download the latest JARs directly to add them to your classpath:

1. 📦 [**fastkeylogger-v0.1.0.jar**](https://github.com/andrestubbe/FastKeylogger/releases/download/v0.1.0/fastkeylogger-0.1.0.jar)
2. 📦 [**fastkeyboard-v0.2.0.jar**](https://github.com/andrestubbe/FastKeyboard/releases/download/v0.2.0/FastKeyboard-0.2.0.jar)
3. ⚙️ [**fastcore-v0.1.0.jar**](https://github.com/andrestubbe/fastcore/releases/download/v0.1.0/fastcore-0.1.0.jar)

---

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
