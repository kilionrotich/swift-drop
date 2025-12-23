package com.kilion.swiftdrop.data.model

data class Address(
    val id: String,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val isDefault: Boolean = false
)