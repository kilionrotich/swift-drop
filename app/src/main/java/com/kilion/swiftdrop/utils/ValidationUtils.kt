package com.kilion.swiftdrop.utils

import android.util.Patterns

object ValidationUtils {
    fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^07\\d{8}$")) // Kenyan format: starts with 07 and 10 digits
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // You can add more rules later
    }

    fun isNotEmpty(vararg fields: String): Boolean {
        return fields.all { it.trim().isNotEmpty() }
    }
}