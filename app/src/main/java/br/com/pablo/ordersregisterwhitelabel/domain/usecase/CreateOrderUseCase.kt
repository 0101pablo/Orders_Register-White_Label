package br.com.pablo.ordersregisterwhitelabel.domain.usecase

import br.com.pablo.ordersregisterwhitelabel.domain.model.Item
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order

interface CreateOrderUseCase {
    suspend operator fun invoke(
        clientName: String,
        items: List<Item>,
        itemsQuantity: Int,
        totalValue: Double
    ): Order
}