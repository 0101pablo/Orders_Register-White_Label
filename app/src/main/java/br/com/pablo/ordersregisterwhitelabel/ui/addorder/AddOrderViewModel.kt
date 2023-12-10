package br.com.pablo.ordersregisterwhitelabel.ui.addorder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pablo.ordersregisterwhitelabel.R
import br.com.pablo.ordersregisterwhitelabel.domain.model.Item
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import br.com.pablo.ordersregisterwhitelabel.domain.usecase.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _clientNameErrorFieldResId = MutableLiveData<Int?>()
    val clientNameErrorFieldResId: LiveData<Int?> = _clientNameErrorFieldResId

    private val _isItemListEmpty = MutableLiveData<Boolean>()
    val isItemListEmpty: LiveData<Boolean> = _isItemListEmpty

    private val _orderCreated = MutableLiveData<Order>()
    val orderCreated: LiveData<Order> = _orderCreated

    private var isFormValid = false

    fun createOrder(
        clientName: String,
        itemsList: List<Item>,
        totalItems: Int,
        totalValue: Double
    ) = viewModelScope.launch {
        isFormValid = true

        _clientNameErrorFieldResId.value = getErrorStringResIdIfEmpty(clientName)
        _isItemListEmpty.value = itemsList.isEmpty()
        if (itemsList.isEmpty()) isFormValid = false


        if (isFormValid) {
            try {
                val order = createOrderUseCase(clientName, itemsList, totalItems, totalValue)
                _orderCreated.value = order
            } catch (e: Exception) {
                Log.d("CreateOrder", e.toString())
            }
        }
    }

    private fun getErrorStringResIdIfEmpty(value: String): Int? {
        return if (value.isEmpty()) {
            isFormValid = false
            R.string.error_message_mandatory_field
        } else null
    }
}