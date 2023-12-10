package br.com.pablo.ordersregisterwhitelabel.ui.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pablo.ordersregisterwhitelabel.R
import br.com.pablo.ordersregisterwhitelabel.domain.model.Item
import br.com.pablo.ordersregisterwhitelabel.util.fromCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor() : ViewModel() {

    private val _descriptionErrorFieldResId = MutableLiveData<Int?>()
    val descriptionFieldErrorResId: LiveData<Int?> = _descriptionErrorFieldResId

    private val _quantityErrorFieldResId = MutableLiveData<Int?>()
    val quantityFieldErrorResId: LiveData<Int?> = _quantityErrorFieldResId

    private val _unitValueErrorFieldResId = MutableLiveData<Int?>()
    val unitValueFieldErrorResId: LiveData<Int?> = _unitValueErrorFieldResId

    private val _itemCreated = MutableLiveData<Item?>()
    val itemCreated: LiveData<Item?> = _itemCreated

    private var isFormValid = false

    fun createItem(description: String, quantity: String, unitValue: String, totalValue: String) =
        viewModelScope.launch {
            isFormValid = true

            _descriptionErrorFieldResId.value = getErrorStringResIdIfEmpty(description)
            _quantityErrorFieldResId.value = getErrorStringResIdIfEmpty(quantity)
            _unitValueErrorFieldResId.value = getErrorStringResIdIfEmpty(unitValue)

            if (isFormValid) {
                _itemCreated.value = Item(
                    description,
                    quantity.toInt(),
                    unitValue.fromCurrency(),
                    totalValue.fromCurrency()
                )
            }
        }

    private fun getErrorStringResIdIfEmpty(value: String): Int? {
        return if (value.isEmpty()) {
            isFormValid = false
            R.string.error_message_mandatory_field
        } else null
    }
}