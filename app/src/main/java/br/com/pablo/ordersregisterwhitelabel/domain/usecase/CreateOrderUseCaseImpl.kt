package br.com.pablo.ordersregisterwhitelabel.domain.usecase

import br.com.pablo.ordersregisterwhitelabel.data.OrderRepository
import br.com.pablo.ordersregisterwhitelabel.domain.model.Item
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import javax.inject.Inject

class CreateOrderUseCaseImpl @Inject constructor(private val orderRepository: OrderRepository) :
    CreateOrderUseCase {
    override suspend fun invoke(
        clientName: String,
        items: List<Item>,
        itemsQuantity: Int,
        totalValue: Double
    ): Order {
        return try {
            orderRepository.createOrder(Order(0, clientName, items, itemsQuantity, totalValue))
        } catch (e: Exception) {
            throw e
        }
    }

}