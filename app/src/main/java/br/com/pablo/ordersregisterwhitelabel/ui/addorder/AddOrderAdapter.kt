package br.com.pablo.ordersregisterwhitelabel.ui.addorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.pablo.ordersregisterwhitelabel.databinding.ItemItemBinding
import br.com.pablo.ordersregisterwhitelabel.domain.model.Item
import br.com.pablo.ordersregisterwhitelabel.util.toCurrency

class AddOrderAdapter : ListAdapter<Item, AddOrderAdapter.AddOrderViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOrderViewHolder {
        return AddOrderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AddOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AddOrderViewHolder(private val itemBinding: ItemItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Item) {
            itemBinding.run {
                itemDescription.text = item.description
                itemQuantity.text = item.quantity.toString()
                itemUnitValue.text = item.unitValue.toCurrency()
                itemTotalValue.text = item.totalValue.toCurrency()
            }
        }

        companion object {
            fun create(parent: ViewGroup): AddOrderViewHolder {
                val itemBinding = ItemItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)

                return AddOrderViewHolder(itemBinding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem.description == newItem.description && oldItem.unitValue == newItem.unitValue
            }

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}