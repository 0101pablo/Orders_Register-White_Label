package br.com.pablo.ordersregisterwhitelabel.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Order(
    var id: Long = 0,
    @PropertyName("client_name")
    var clientName: String = "",
    val items: List<Item> = listOf(),
    @PropertyName("items_quantity")
    var itemsQuantity: Int = 0,
    @PropertyName("total_value")
    var totalValue: Double = 0.0,
) : Parcelable