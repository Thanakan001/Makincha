package com.thanakan.makincha.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thanakan.makincha.models.CartItem

class CartViewModel : ViewModel() {

    val cartList = MutableLiveData<ArrayList<CartItem>>(arrayListOf())

    fun addItem(item: CartItem) {
        val list = cartList.value ?: arrayListOf()
        list.add(item)
        cartList.value = list
    }

    fun getCount(): Int {
        return cartList.value?.size ?: 0
    }

    fun getTotalPrice(): Double {
        return cartList.value?.sumOf { it.totalPrice() } ?: 0.0
    }
}
