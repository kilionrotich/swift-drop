package com.kilion.swiftdrop.data.model

data class Review(
    val id: String,
    val rating: Int,
    val comment: String,
    val user: String, // Or a User object if you have one
    val item: String
)
