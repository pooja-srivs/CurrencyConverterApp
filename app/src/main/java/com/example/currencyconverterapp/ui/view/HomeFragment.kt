package com.example.currencyconverterapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.common.afterTextChanged
import com.example.currencyconverterapp.common.hideKeyboard
import com.example.currencyconverterapp.databinding.FragmentHomeBinding
import com.example.currencyconverterapp.ui.adapter.ConvertedCurrencyAdapter
import com.example.currencyconverterapp.ui.adapter.CurrencyItem
import com.example.currencyconverterapp.ui.adapter.CurrencySpinnerAdapter
import com.example.currencyconverterapp.ui.model.UIState
import com.example.currencyconverterapp.viewmodel.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        private const val AMOUNT_GREATER_THAN_ZERO = "101"
    }

    private val viewModel: CurrencyViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CurrencySpinnerAdapter
    private lateinit var convertedCurrencyAdapter: ConvertedCurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        viewModel.fetchCurrencies()
        initializeAdapter()
        setUpSinnerView()
        setListener()
        return binding.root
    }

    private fun initializeAdapter() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvList.layoutManager = layoutManager
        convertedCurrencyAdapter = ConvertedCurrencyAdapter.newInstance()
        binding.rvList.adapter = convertedCurrencyAdapter
    }

    private fun setUpSinnerView() {
        lifecycleScope.launch {
            viewModel.currencyStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { uiState ->
                    when(uiState) {
                        is UIState.Loading -> { binding.progress.visibility = View.VISIBLE }
                        is UIState.Success -> {
                            binding.apply {
                                progress.visibility = View.GONE
                                adapter = CurrencySpinnerAdapter(requireContext(), uiState.value)
                                toCurrencySpinner.adapter = adapter
                                toCurrencySpinner.setSelection(viewModel.getSelectedCurrencyPosition())
                                val currencyList = uiState.value.mapIndexed() { index, item ->
                                    CurrencyItem(
                                        id = index+1,
                                        rate = item.rate,
                                        currency = item.currency,
                                        convertedAmount = 0.0
                                    )
                                }
                                viewModel.setCurrencyList(currencyList)
                                convertedCurrencyAdapter.submitList(currencyList)
                            }
                        }

                        is UIState.Failure ->{
                            binding.progress.visibility = View.GONE
                            Toast.makeText(requireContext(),
                                getString(R.string.failed_to_fetch_currencies), Toast.LENGTH_SHORT).show()
                        }
                    }

                }
        }
    }

    private fun setListener() {
        binding.apply {
            toCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    requireActivity().hideKeyboard()
                    val selectedItem = adapter.getItem(position)
                    selectedItem?.let { viewModel.selectedCurrency(position, it) }
                    toCurrencySpinner.setSelection(viewModel.getSelectedCurrencyPosition())
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            etAmount.afterTextChanged { amount ->
                if (viewModel.isValidAmount(amount)) {
                    viewModel.setInputAmount(amount)
                } else {
                    Toast.makeText(requireContext(),
                        getString(R.string.please_enter_the_valid_amount), Toast.LENGTH_SHORT).show()
                }
            }

            convertButton.setOnClickListener {
                requireActivity().hideKeyboard()
                if (viewModel.getInputAmount() == 0.0) {
                    Toast.makeText(requireContext(),
                        getString(R.string.please_enter_the_amount), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.convert(
                    inputAmount = viewModel.getInputAmount(),
                    currentRate = viewModel.getSelectedCurrencyUiModel().rate,
                    currencyList = viewModel.getCurrencyList()
                )
            }

            lifecycleScope.launch {
                viewModel.convertedAmountStateFlow
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { uiState ->
                        when(uiState) {
                            is UIState.Loading -> { binding.progress.visibility = View.VISIBLE }
                            is UIState.Success -> {
                                binding.progress.visibility = View.GONE
                                convertedCurrencyAdapter.submit(uiState.value)
                            }
                            is UIState.Failure ->{
                                binding.progress.visibility = View.GONE
                                when(uiState.throwable.message) {
                                    AMOUNT_GREATER_THAN_ZERO -> {
                                        Toast.makeText(requireContext(),requireActivity().getString(R.string.amount_must_be_greater_than_zero), Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(requireContext(),requireActivity().getString(R.string.amount_must_be_greater_than_zero), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}