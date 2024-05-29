package com.example.currencyconverterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverterapp.databinding.ItemCurrencyBinding

class ConvertedCurrencyAdapter private constructor(
    callback: DiffUtil.ItemCallback<CurrencyItem>
): ListAdapter<CurrencyItem, CurrencyViewHolder>(callback) {

    companion object {
        private val callback = object : DiffUtil.ItemCallback<CurrencyItem>() {
            override fun areItemsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean =
                oldItem.convertedAmount == newItem.convertedAmount
        }

        fun newInstance(): ConvertedCurrencyAdapter = ConvertedCurrencyAdapter(callback)
    }

    fun submit(currencyList: List<CurrencyItem>) {
        currencyList.forEachIndexed { index, newItem ->
            val oldItem = currentList[index]
            if (oldItem.convertedAmount != newItem.convertedAmount) {
                oldItem.convertedAmount = newItem.convertedAmount
                notifyItemChanged(index)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(
            ItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CurrencyViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: CurrencyItem) {
        with(binding) {
            ("Rate : "+data.rate.toString()).also { tvRate.text = it }
            (data.currency + "  " + data.convertedAmount).also { tvAmount.text = it }
        }
    }
}
data class CurrencyItem(var id: Int, val currency: String, val rate: Double, var convertedAmount: Double)