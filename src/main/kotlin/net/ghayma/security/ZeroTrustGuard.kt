package net.ghayma.security

import android.content.Context
import android.os.Build
import android.os.Debug
import java.io.File

data class IntegrityAssessment(
    val isSecure: Boolean,
    val isRooted: Boolean,
    val isDebuggerAttached: Boolean,
    val isHookFrameworkDetected: Boolean,
    val violationReasons: List<String>
)

/**
 * Mobile Zero-Trust Defense Shield
 * Developed by Ghayma LLC (New Mexico, USA)
 */
class ZeroTrustGuard private constructor(private val context: Context) {

    fun evaluateEnvironment(): IntegrityAssessment {
        val violations = mutableListOf<String>()

        val rooted = checkRootBinaries()
        if (rooted) violations.add("Root / SU binaries present on device")

        val debugger = Debug.isDebuggerConnected() || Debug.waitingForDebugger()
        if (debugger) violations.add("Active debugger attached to application runtime")

        val hookDetected = checkFridaOrXposed()
        if (hookDetected) violations.add("Frida / Xposed instrumentation framework detected in memory")

        return IntegrityAssessment(
            isSecure = violations.isEmpty(),
            isRooted = rooted,
            isDebuggerAttached = debugger,
            isHookFrameworkDetected = hookDetected,
            violationReasons = violations
        )
    }

    private fun checkRootBinaries(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return Build.TAGS != null && Build.TAGS.contains("test-keys")
    }

    private fun checkFridaOrXposed(): Boolean {
        // Memory scan heuristic
        return try {
            val maps = File("/proc/self/maps")
            if (maps.exists()) {
                val content = maps.readText()
                content.contains("frida") || content.contains("xposed")
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        fun init(context: Context, configure: (ZeroTrustGuard.() -> Unit)? = null): ZeroTrustGuard {
            val guard = ZeroTrustGuard(context)
            configure?.invoke(guard)
            return guard
        }
    }
}