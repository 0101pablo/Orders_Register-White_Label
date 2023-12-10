package br.com.pablo.ordersregisterwhitelabel.data

import br.com.pablo.ordersregisterwhitelabel.domain.model.Item
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val dataSource: OrderDataSource
) {
    suspend fun createOrder(order: Order): Order =
        dataSource.createOrder(order)

    suspend fun getOrders(): List<Order> = dataSource.getOrders()
}