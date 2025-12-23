package com.kilion.swiftdrop.dashboard.shop

sealed class ShopRoutes(val route: String) {
    object Profile : ShopRoutes("shop_profile")
    object Orders : ShopRoutes("shop_orders")
    object OrderHistory : ShopRoutes("shop_order_history")
    object Analytics : ShopRoutes("shop_analytics")
    object EditItem : ShopRoutes("edit_item/{itemId}") {
        fun createRoute(itemId: String) = "edit_item/$itemId"
    }
}