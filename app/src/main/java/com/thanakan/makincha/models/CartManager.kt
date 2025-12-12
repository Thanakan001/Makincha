package com.thanakan.makincha.models

object CartManager {

    private val cartItems = mutableListOf<CartItem>()

    private val listeners = mutableListOf<() -> Unit>()

    fun addToCart(item: CartItem, replaceAmount: Boolean = false) {
        val existing = cartItems.find {
            it.productName == item.productName &&
                    it.sweetness == item.sweetness &&
                    it.topping == item.topping &&
                    it.note == item.note
        }
        if (existing != null) {
            if (replaceAmount) existing.amount = item.amount
            else existing.amount += item.amount
        } else {
            cartItems.add(item)
        }
        notifyListeners()
    }

    fun removeFromCart(item: CartItem) {
        cartItems.remove(item)
        notifyListeners()
    }

    fun getCartItems(): List<CartItem> = cartItems

    fun getTotalItemCount(): Int = cartItems.sumOf { it.amount }

    fun clearCart() {
        cartItems.clear()
        notifyListeners()
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke() }
    }
}
