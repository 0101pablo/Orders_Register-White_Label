package br.com.pablo.ordersregisterwhitelabel.domain.usecase

import br.com.pablo.ordersregisterwhitelabel.domain.model.Order

interface GetOrdersUseCase {
    suspend operator fun invoke(): List<Order>
}