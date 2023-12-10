package br.com.pablo.ordersregisterwhitelabel.domain.usecase.di

import br.com.pablo.ordersregisterwhitelabel.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {
    @Binds
    fun bindCreateOrderUseCase(useCase: CreateOrderUseCaseImpl): CreateOrderUseCase

    @Binds
    fun bindGetOrdersUseCase(useCase: GetOrdersUseCaseImpl): GetOrdersUseCase
}