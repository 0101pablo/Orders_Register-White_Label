package br.com.pablo.ordersregisterwhitelabel.data.di

import br.com.pablo.ordersregisterwhitelabel.data.FirebaseOrderDataSource
import br.com.pablo.ordersregisterwhitelabel.data.OrderDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindOrderDataSource(dataSource: FirebaseOrderDataSource): OrderDataSource
}