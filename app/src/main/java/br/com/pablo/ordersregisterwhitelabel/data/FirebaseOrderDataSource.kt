package br.com.pablo.ordersregisterwhitelabel.data

import br.com.pablo.ordersregisterwhitelabel.BuildConfig
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import br.com.pablo.ordersregisterwhitelabel.util.COLLECTION_ORDERS
import br.com.pablo.ordersregisterwhitelabel.util.COLLECTION_ROOT
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class FirebaseOrderDataSource @Inject constructor(
    firebaseFirestore: FirebaseFirestore,
) : OrderDataSource {

    private val documentReference =
        firebaseFirestore.document("$COLLECTION_ROOT/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}")

    override suspend fun createOrder(order: Order): Order {
        return suspendCoroutine { continuation ->
            order.id = System.currentTimeMillis()
            documentReference
                .collection(COLLECTION_ORDERS)
                .document(order.clientName)
                .set(order)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(order))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun getOrders(): List<Order> {
        return suspendCoroutine { continuation ->
            val ordersReference = documentReference.collection(COLLECTION_ORDERS)
            ordersReference.get().addOnSuccessListener { documents ->
                val orders = mutableListOf<Order>()
                for (document in documents) {
                    document.toObject(Order::class.java).run {
                        orders.add(this)
                    }
                }
                continuation.resumeWith(Result.success(orders))
            }
            ordersReference.get().addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }
}