# BetterBlueKit KMP

A Kotlin Multiplatform library for interacting with Hyundai BlueLink and Kia Connect services.
Kotlin port of [schmidtwmark/BetterBlueKit](https://github.com/schmidtwmark/BetterBlueKit).

Targets: **iOS** · **Android** · **JVM**

## Modules

- **`:betterbluekit`** — the shared KMP library (commonMain + platform engines)

## Building

```bash
# Compile commonMain + JVM target (Linux/macOS/Windows)
./gradlew :betterbluekit:compileKotlinJvm

# Run JVM tests
./gradlew :betterbluekit:jvmTest

# Build iOS XCFramework (macOS only)
./gradlew :betterbluekit:assembleXCFramework
```

## License

MIT — see [LICENSE](LICENSE).
Original Swift implementation © 2025 Mark Schmidt.
KMP port © 2026 Undermark5 Software.
