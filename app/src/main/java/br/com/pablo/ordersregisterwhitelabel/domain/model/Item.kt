package br.com.pablo.ordersregisterwhitelabel.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    var description: String = "",
    var quantity: Int = 0,
    @PropertyName("unit_value")
    var unitValue: Double = 0.0,
    @PropertyName("total_value")
    var totalValue: Double = 0.0,
) : Parcelable