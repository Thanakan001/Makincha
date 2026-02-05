package com.thanakan.makincha.models

object CartManager {

    private val cartItems = mutableListOf<CartItem>()
    private val listeners = mutableListOf<() -> Unit>()

    /* =========================
       Cart Logic
    ========================= */

    fun addToCart(item: CartItem, replaceAmount: Boolean = false) {

        val index = cartItems.indexOfFirst {
            it.productId == item.productId &&
                    it.sweetness == item.sweetness &&
                    it.topping == item.topping &&
                    it.note == item.note
        }

        if (index != -1) {
            val existing = cartItems[index]

            val newAmount =
                if (replaceAmount) {
                    item.amount
                } else {
                    existing.amount + item.amount
                }

            // ✅ สร้าง object ใหม่แทนการแก้ val
            cartItems[index] = existing.copy(
                amount = newAmount
            )

        } else {
            cartItems.add(item.copy())
        }

        notifyListeners()
    }

    fun removeFromCart(item: CartItem) {
        cartItems.removeAll {
            it.productId == item.productId &&
                    it.sweetness == item.sweetness &&
                    it.topping == item.topping &&
                    it.note == item.note
        }
        notifyListeners()
    }

    fun clearCart() {
        cartItems.clear()
        notifyListeners()
    }

    /* =========================
       Getter
    ========================= */

    fun getCartItems(): List<CartItem> =
        cartItems.map { it.copy() }

    fun getTotalItemCount(): Int =
        cartItems.sumOf { it.amount }

    fun isEmpty(): Boolean =
        cartItems.isEmpty()

    /* =========================
       Listener
    ========================= */

    fun addListener(listener: () -> Unit) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.toList().forEach { it.invoke() }
    }
}
