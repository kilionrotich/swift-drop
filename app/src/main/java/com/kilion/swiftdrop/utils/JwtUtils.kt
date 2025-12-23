package com.kilion.swiftdrop.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    fun extractRole(token: String): String? {
        return try {
            val parts = token.split(".")
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            json.getString("role")
        } catch (e: Exception) {
            null
        }
    }
}