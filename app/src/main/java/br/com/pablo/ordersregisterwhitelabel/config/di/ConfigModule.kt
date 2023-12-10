package br.com.pablo.ordersregisterwhitelabel.config.di

import br.com.pablo.ordersregisterwhitelabel.config.Config
import br.com.pablo.ordersregisterwhitelabel.config.ConfigImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ConfigModule {

    @Binds
    fun bindConfig(config: ConfigImpl): Config
}