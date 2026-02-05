package com.thanakan.makincha.models

object OrderManager {

    private val orderList = mutableListOf<OrderHistory>()

    fun addOrder(order: OrderHistory) {
        orderList.add(0, order)
    }

    fun getOrders(): List<OrderHistory> = orderList
}
