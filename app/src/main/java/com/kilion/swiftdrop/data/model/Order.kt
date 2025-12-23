package com.kilion.swiftdrop.data.model

// Data model for an order, aligned with backend Order.js
data class Order(
    val id: String,
    val items: List<Item>,              // Items in the order
    val status: String,                   // Available, Assigned, Delivered, etc.
    val pickupLocation: String,           // Shop address or coordinates
    val dropoffLocation: String,          // Customer address or coordinates
    val pickupLat: Double? = null,
    val pickupLng: Double? = null,
    val dropoffLat: Double? = null,
    val dropoffLng: Double? = null,
    val customerQrUrl: String? = null,
    val assignedRiderId: String? = null,
    val shopId: String? = null,
    val customerId: String? = null,
    val total: Double
)