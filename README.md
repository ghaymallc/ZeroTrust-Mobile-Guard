# ZeroTrust-Mobile-Guard
### Enterprise Mobile Runtime App Self-Protection (RASP) and API Defense

[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Android](https://img.shields.io/badge/Android-Hardened-green.svg)](https://developer.android.com)
[![iOS](https://img.shields.io/badge/iOS-Hardened-black.svg)](https://developer.apple.com)
[![Security](https://img.shields.io/badge/Security-Zero--Trust-red.svg)](https://ghayma.net)

ZeroTrust-Mobile-Guard is an open-source enterprise security toolkit developed by [Ghayma LLC](https://ghayma.net) to harden mobile applications against reverse engineering, decompilation, debugging hooks (Frida, Xposed), and SSL Man-in-the-Middle (MitM) interception.

---

## Security Capabilities
* Anti-Frida and Memory Tamper Detection: Continuously scans for memory hooks and suspicious native ptrace listeners.
* Dynamic SSL Pinning: Validates SHA-256 public key certificates with hot-swappable fallback pins.
* Hardware-Backed KeyStore: Generates asymmetric cryptographic keys inside the Titan M / Apple Secure Enclave.
* Root and Magisk Detection: Detects SU binaries, test-keys, root cloaking, and unlocked bootloaders.

---

## Quick Integration

```kotlin
import net.ghayma.security.ZeroTrustGuard

// 1. Initialize RASP Guard
val guard = ZeroTrustGuard.init(context) {
    enableRootDetection = true
    enableFridaDefense = true
    enableDebuggerInterception = true
    blockUntrustedKeyboards = false
}

// 2. Perform Pre-Flight Integrity Check
val integrityResult = guard.evaluateEnvironment()

if (!integrityResult.isSecure) {
    println("Security Breach Detected: ${integrityResult.violationReasons}")
    guard.terminateSession()
}
```

---

## Maintained by
Architected with highest security standards by Ghayma LLC (New Mexico, USA).  
Website: [https://ghayma.net](https://ghayma.net) | Email: [Info@ghayma.net](mailto:Info@ghayma.net)