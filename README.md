# FastKeylogger v0.1.0 ⚡⌨️

> **The behavioral typing sensor for the FastJava ecosystem.**

Standard keyloggers focus on *what* was typed. **FastKeylogger** focuses on *how* it was typed. By analyzing the raw hardware stream from `FastKeyboard`, it reconstructs text and extracts the underlying biological rhythm of the user.

---

## ✨ Features

- **Text Reconstruction**: Converts raw virtual keys and scan codes into a readable character stream.
- **Timing Signatures**: Captures precise dwell times (how long a key was held) and flight times (latency between keys).
- **Correction Tracking**: Detects backspaces and deletions to measure user correction behavior.
- **Non-Intrusive**: Runs in the background with zero impact on system performance.
- **Native Precision**: Built on top of the Windows RawInput API via `FastKeyboard`.

---

## 🚀 Quick Start

```java
FastKeylogger logger = new FastKeylogger();

logger.addListener(event -> {
    System.out.println("Typed: " + event.character() + " | Duration: " + event.durationMs() + "ms");
});

logger.start();
```

---

## 📦 Installation (Maven)

```xml
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastkeylogger</artifactId>
    <version>0.1.0</version>
</dependency>
```

---

## 🏛 Architecture

FastKeylogger is the **Logic Layer** in the GhostType stack:
1. **FastKeyboard** (Driver Layer): Captures RawInput events from the Windows kernel.
2. **FastKeylogger** (Logic Layer): Processes events into behavioral typing signatures.
3. **FastType** (AI Layer): Predicts the next word based on the behavioral stream.

---

## 🙏 Credits
Developed by **Andre Stubbe**.
Part of the **FastJava** ecosystem — *Making the JVM faster.*
