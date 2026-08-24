# FastKeylogger Philosophy

> [!IMPORTANT]
> **"Biometric Timing Integrity. Zero Keyboard Polling. Compact Binary Signatures."**

Typical keylogging solutions intercept character strings after operating system processing, completely discarding hardware dwell times (key press duration) and flight times (intervals between keys).

`FastKeylogger` taps directly into low-level Win32 Raw Input via `FastKeyboard`, recording every press and release state transition with microsecond precision, enabling accurate behavioral biometrics and loss-free typing stream preservation via `FastFileFormat`.
