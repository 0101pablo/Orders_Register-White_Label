package br.com.pablo.ordersregisterwhitelabel.domain.usecase

import br.com.pablo.ordersregisterwhitelabel.data.OrderRepository
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import javax.inject.Inject

class GetOrdersUseCaseImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : GetOrdersUseCase {
    override suspend fun invoke(): List<Order> {
        return orderRepository.getOrders()
    }
}