# FastKeylogger v0.1.0 — Behavioral Layer 🪵

## 🎉 Version 0.1.0: Initial Release
This is the first stable release of the **FastKeylogger** module, the logical processing layer of the GhostType stack. It transforms raw hardware events from FastKeyboard into high-level behavioral typing signatures.

---

## ✨ Features

### ⌨️ Behavioral Stream Capture
- Real-time transformation of raw keyboard events into structured `TypingEvent` objects.
- High-precision timing capture for every keystroke.

### ⏱️ Rhythm Analysis (Timing Signatures)
- **Dwell Time Tracking**: Measures the exact duration a key is held down.
- **Inter-key Latency**: Provides the foundation for flight-time analysis between keys.

### 🧹 Correction Awareness
- Specialized detection of backspace events to track user correction patterns and cognitive load.

### 🚀 Integration Ready
- Direct dependency on **FastKeyboard v0.2.0**.
- Lightweight observer pattern (`TypingListener`) for easy integration into higher-level AI modules.

---

## 📦 Installation (JitPack)

### Maven
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
