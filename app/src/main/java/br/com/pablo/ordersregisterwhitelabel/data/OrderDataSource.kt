package br.com.pablo.ordersregisterwhitelabel.data

import br.com.pablo.ordersregisterwhitelabel.domain.model.Order

interface OrderDataSource {
    suspend fun createOrder(order: Order): Order

    suspend fun getOrders(): List<Order>
}