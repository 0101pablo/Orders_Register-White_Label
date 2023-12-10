package br.com.pablo.ordersregisterwhitelabel.ui.orderslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pablo.ordersregisterwhitelabel.config.Config
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import br.com.pablo.ordersregisterwhitelabel.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersListViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val config: Config
) : ViewModel() {
    private val _ordersData = MutableLiveData<List<Order>>()
    val ordersData: LiveData<List<Order>> = _ordersData

    private val _addButtonVisibilityData = MutableLiveData(config.addButtonVisibility)
    val addButtonVisibilityData: LiveData<Int> = _addButtonVisibilityData

    fun getOrders() = viewModelScope.launch {
        try {
            val orders = getOrdersUseCase()
            _ordersData.value = orders
        } catch (e: Exception) {
            Log.d("ProductsViewModel", e.toString())
        }
    }

}