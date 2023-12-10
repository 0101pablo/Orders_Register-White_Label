package br.com.pablo.ordersregisterwhitelabel.ui.orderslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import br.com.pablo.ordersregisterwhitelabel.R
import br.com.pablo.ordersregisterwhitelabel.databinding.FragmentOrdersListBinding
import br.com.pablo.ordersregisterwhitelabel.domain.model.Order
import br.com.pablo.ordersregisterwhitelabel.util.ORDER_KEY
import br.com.pablo.ordersregisterwhitelabel.util.toCurrency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersListFragment : Fragment() {

    private var _binding: FragmentOrdersListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrdersListViewModel by viewModels()

    private val ordersListAdapter = OrdersListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        observeNavBackStack()
        observeVMEvents()
        getOrders()
    }

    private fun setRecyclerView() {
        binding.recyclerOrders.run {
            setHasFixedSize(true)
            adapter = ordersListAdapter
        }
    }

    private fun setListeners() {
        with(binding) {

            swipeOrders.setOnRefreshListener {
                getOrders()
            }

            fabAdd.setOnClickListener {
                goToOrderDetails()
            }

            ordersListAdapter.onOrderClick = { order ->
                goToOrderDetails(order)
            }
        }
    }

    private fun getOrders() {
        viewModel.getOrders()
    }

    private fun goToOrderDetails(readOnlyOrder: Order? = null) {
        findNavController().run {
            readOnlyOrder?.let {
                previousBackStackEntry?.savedStateHandle?.set(ORDER_KEY, it)
                setFragmentResult(ORDER_KEY, bundleOf(ORDER_KEY to readOnlyOrder))
            }
            navigate(R.id.action_ordersListFragment_to_addOrderFragment)
        }
    }

    private fun observeNavBackStack() {
        findNavController().run {
            val navBackStackEntry = getBackStackEntry(R.id.ordersListFragment)
            val savedStateHandle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME && savedStateHandle.contains(ORDER_KEY)) {
                    val order = savedStateHandle.get<Order>(ORDER_KEY)
                    val oldList = ordersListAdapter.currentList
                    val newList = oldList.toMutableList().apply {
                        add(order)
                    }
                    ordersListAdapter.submitList(newList)
                    binding.recyclerOrders.smoothScrollToPosition(newList.size - 1)
                    savedStateHandle.remove<Order>(ORDER_KEY)
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

    private fun observeVMEvents() {
        viewModel.ordersData.observe(viewLifecycleOwner) { orders ->
            binding.swipeOrders.isRefreshing = false
            ordersListAdapter.submitList(orders)
            binding.emptyListText.visibility =
                if (ordersListAdapter.itemCount == 0) View.VISIBLE else View.GONE
            binding.ordersTotalValue.text = orders.sumOf { it.totalValue }.toCurrency()
        }

        viewModel.addButtonVisibilityData.observe(viewLifecycleOwner) { visibility ->
            binding.fabAdd.visibility = visibility
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}