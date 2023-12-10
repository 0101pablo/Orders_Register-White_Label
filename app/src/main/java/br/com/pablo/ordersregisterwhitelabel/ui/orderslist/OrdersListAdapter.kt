package br.com.pablo.ordersregisterwhitelabel.ui.orderslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.pablo.ordersregisterwhitelabel.databinding.ItemOrderBinding
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import br.com.pablo.ordersregisterwhitelabel.util.toCurrency

class OrdersListAdapter : ListAdapter<Order, OrdersListAdapter.OrdersViewHolder>(DIFF_CALLBACK) {

    var onOrderClick : ((order: Order) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order: Order = getItem(position)
        holder.bind(order)
        holder.itemView.setOnClickListener{
            onOrderClick?.invoke(order)
        }
    }

    class OrdersViewHolder(private val itemBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(order: Order) {
            itemBinding.run {
                textClientName.text = order.clientName
                textTotalValue.text = order.totalValue.toCurrency()
            }
        }

        companion object {

            fun create(parent: ViewGroup): OrdersViewHolder {
                val itemBinding = ItemOrderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)

                return OrdersViewHolder(itemBinding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Order,
                newItem: Order
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}