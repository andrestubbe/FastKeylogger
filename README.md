# FastKeylogger v0.1.0 ⚡⌨️

> **The behavioral typing sensor for the FastJava ecosystem.**

Standard keyloggers focus on *what* was typed. **FastKeylogger** focuses on *how* it was typed. By analyzing high-precision hardware events from `FastKeyboard`, it reconstructs the text stream while extracting the underlying biological rhythm (timing signatures) of the user.

---

## ✨ Features

- **Text Reconstruction**: Real-time conversion of raw virtual keys and scan codes into a logical character stream.
- **Timing Signatures**: Captures precise **Dwell Time** (key hold duration) and **Flight Time** (latency between keystrokes).
- **Correction Tracking**: Detailed monitoring of backspaces and correction patterns to analyze user cognitive load.
- **Pure Java Logic**: Lightweight processing layer that consumes raw input streams without native overhead.
- **Observer Pattern**: Simple API for integration into AI-driven language models or biometric systems.

---

## 🚀 Quick Start

```java
FastKeylogger logger = new FastKeylogger();

logger.addListener(event -> {
    System.out.println("Typed: " + event.character() + " | Hold Duration: " + event.durationMs() + "ms");
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

## 🙏 Credits
Developed by **Andre Stubbe**.
Part of the **FastJava** ecosystem — *Making the JVM faster.*
