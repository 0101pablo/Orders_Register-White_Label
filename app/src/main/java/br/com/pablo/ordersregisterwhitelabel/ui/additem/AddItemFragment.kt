package br.com.pablo.ordersregisterwhitelabel.ui.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.pablo.ordersregisterwhitelabel.databinding.FragmentAddItemBinding
import br.com.pablo.ordersregisterwhitelabel.util.CurrencyTextWatcher
import br.com.pablo.ordersregisterwhitelabel.util.ITEM_KEY
import br.com.pablo.ordersregisterwhitelabel.util.fromCurrency
import br.com.pablo.ordersregisterwhitelabel.util.toCurrency
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVMEvents()
        setListeners()
    }

    private fun observeVMEvents() {
        viewModel.descriptionFieldErrorResId.observe(viewLifecycleOwner) { stringResId ->
            binding.inputLayoutDescription.setError(stringResId)
        }
        viewModel.quantityFieldErrorResId.observe(viewLifecycleOwner) { stringResId ->
            binding.inputLayoutQuantity.setError(stringResId)
        }
        viewModel.unitValueFieldErrorResId.observe(viewLifecycleOwner) { stringResId ->
            binding.inputLayoutUnitValue.setError(stringResId)
        }

        viewModel.itemCreated.observe(viewLifecycleOwner) { item ->
            findNavController().run {
                previousBackStackEntry?.savedStateHandle?.set(ITEM_KEY, item)
                popBackStack()
            }
        }
    }

    private fun TextInputLayout.setError(stringResId: Int?) {
        error = if (stringResId != null) {
            getString(stringResId)
        } else null
    }

    private fun checkToFillTotalValue() {
        val quantity = binding.editTextQuantity.text.toString()
        val unitValue = binding.editTextUnitValue.text.toString()
        if (quantity.isNotEmpty() && unitValue.isNotEmpty()) {
            binding.textViewTotalValue.text =
                (quantity.toInt() * unitValue.fromCurrency()).toCurrency()
        }
    }

    private fun setListeners() {
        binding.buttonAddItem.setOnClickListener {
            val description = binding.editTextDescription.text.toString()
            val quantity = binding.editTextQuantity.text.toString()
            val unitValue = binding.editTextUnitValue.text.toString()
            val totalValue = binding.textViewTotalValue.text.toString()

            viewModel.createItem(description, quantity, unitValue, totalValue)
        }
        binding.editTextQuantity.run {
            addTextChangedListener {
                checkToFillTotalValue()
            }
        }
        binding.editTextUnitValue.run {
            addTextChangedListener(CurrencyTextWatcher(this))
            addTextChangedListener {
                checkToFillTotalValue()
            }
        }
    }
}