package com.bachnn.timeout.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AppLaunchDetectorService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            Log.e("AppLaunchDetectorService", "App opened: $packageName")
        }
    }

    override fun onInterrupt() {
        // Required override, but can be left empty
    }
}
