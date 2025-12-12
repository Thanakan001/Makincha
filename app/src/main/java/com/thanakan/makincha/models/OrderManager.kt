package com.thanakan.makincha.models

object OrderManager {

    private val orderList = mutableListOf<Order>()

    fun addOrder(order: Order) {
        orderList.add(0, order)
    }

    fun getOrders(): List<Order> = orderList
}
