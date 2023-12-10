package br.com.pablo.ordersregisterwhitelabel.ui.addorder

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import br.com.pablo.ordersregisterwhitelabel.R
import br.com.pablo.ordersregisterwhitelabel.databinding.FragmentAddOrderBinding
import br.com.pablo.ordersregisterwhitelabel.domain.model.Item
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import br.com.pablo.ordersregisterwhitelabel.ui.MainActivity
import br.com.pablo.ordersregisterwhitelabel.util.ITEM_KEY
import br.com.pablo.ordersregisterwhitelabel.util.ORDER_KEY
import br.com.pablo.ordersregisterwhitelabel.util.toCurrency
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrderFragment : Fragment() {

    private var _binding: FragmentAddOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddOrderViewModel by viewModels()

    private val addOrderAdapter = AddOrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        observeNavBackStack()
        observeFragmentResult()
        observeVMEvents()

        getItems()
    }

    private fun setRecyclerView() {
        binding.recyclerItems.run {
            setHasFixedSize(true)
            adapter = addOrderAdapter
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonAddItem.setOnClickListener {
                findNavController().navigate(R.id.action_addOrderFragment_to_addItemFragment)
            }

            buttonSaveOrder.setOnClickListener {
                val clientName = binding.editTextClientName.text.toString()
                val items = addOrderAdapter.currentList.toMutableList()
                val totalItems = items.sumOf { it.quantity }
                val orderTotalValue = items.sumOf { it.totalValue }

                viewModel.createOrder(clientName, items, totalItems, orderTotalValue)
            }
        }
    }

    private fun getItems() {
        //viewModel.getItems()
    }

    private fun observeNavBackStack() {
        findNavController().run {
            val navBackStackEntry = getBackStackEntry(R.id.addOrderFragment)
            val savedStateHandle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME && savedStateHandle.contains(ITEM_KEY)) {
                    val newList = addOrMergeItem(
                        addOrderAdapter.currentList,
                        savedStateHandle.get<Item>(ITEM_KEY)
                    )
                    addOrderAdapter.submitList(newList)
                    binding.recyclerItems.smoothScrollToPosition(newList.size - 1)
                    savedStateHandle.remove<Item>(ITEM_KEY)

                    binding.orderItemsQuantity.text = newList.sumOf { it.quantity }.toString()
                    binding.orderGrandTotalValue.text = newList.sumOf { it.totalValue }.toCurrency()
                }
            }
            navBackStackEntry.lifecycle.addObserver(observer)
            viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }

    private fun observeFragmentResult() {
        setFragmentResultListener(ORDER_KEY) { ORDER_KEY, readOnlyOrder ->
            val order = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                readOnlyOrder.getParcelable(ORDER_KEY, Order::class.java)
            } else {
                @Suppress("DEPRECATION")
                readOnlyOrder.getParcelable(ORDER_KEY)
            }

            order?.let {
                (activity as MainActivity).setToolbarTitle("Consultar Pedido")
                binding.editTextClientName.setText(it.clientName)
                binding.editTextClientName.isEnabled = false
                binding.buttonAddItem.visibility = View.GONE
                binding.buttonSaveOrder.visibility = View.GONE
                binding.orderItemsQuantity.text = it.items.sumOf { it.quantity }.toString()
                binding.orderGrandTotalValue.text = it.items.sumOf { it.totalValue }.toCurrency()
                addOrderAdapter.submitList(it.items)
            }
        }
    }

    private fun observeVMEvents() {
        viewModel.clientNameErrorFieldResId.observe(viewLifecycleOwner) { stringResId ->
            binding.inputLayoutClientName.setError(stringResId)
        }

        viewModel.isItemListEmpty.observe(viewLifecycleOwner) { isItemListEmpty ->
            if (isItemListEmpty) Toast.makeText(
                context?.applicationContext,
                "Não é possível salvar o pedido com a lista de itens vazia",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.orderCreated.observe(viewLifecycleOwner) { order ->
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(ORDER_KEY, order)
                popBackStack()
            }
        }
    }

    private fun TextInputLayout.setError(stringResId: Int?) {
        error = if (stringResId != null) {
            getString(stringResId)
        } else null
    }

    private fun addOrMergeItem(currentList: List<Item>, item: Item?): MutableList<Item> {
        val resultList: MutableList<Item> = currentList.toMutableList()
        item?.let {
            currentList.find { currentItem ->
                currentItem.description == item.description && currentItem.unitValue == item.unitValue
            }?.apply {
                quantity.plus(item.quantity)
                totalValue = quantity * unitValue
            } ?: run {
                resultList.add(item)
            }
        }
        return resultList
    }
}