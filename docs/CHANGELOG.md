# Changelog: FastKeylogger

All notable changes to this project will be documented in this file.

## [0.1.0] - 2026-08-24
### Added
- **Native Raw Keystroke Logger (`FastKeylogger`)**: Win32 Raw Input capture preserving exact dwell-time and flight-time dynamics.
- **FastFileFormat Binary Streamer (`KeybinCodec`)**: VarInt timestamp & scan code compression (`.keybin` Payload 0x0004).
- **Live Text Reconstruction (`TextReconstructor`)**: Real-time backspace correction handler.
- **Interactive Showcase & JMH Benchmark Suite**: Profiling >58M events/sec decoding and >26M events/sec encoding.