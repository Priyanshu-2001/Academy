package com.sports.oscaracademy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.FeesHistorySingleBinding
import com.sports.oscaracademy.service.PaymentData

class FeesHistoryAdapter(val data: List<PaymentData>) :
    RecyclerView.Adapter<FeesHistoryAdapter.FeesViewHolder>() {

    open class FeesViewHolder(binding: FeesHistorySingleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val bind = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeesViewHolder {
        val binding: FeesHistorySingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fees_history_single, parent, false
        )
        return FeesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeesViewHolder, position: Int) {
        holder.bind.amountTV.text = String.format("₹ " + data[position].amount)
        holder.bind.dateTv.text = data[position].date
        holder.bind.referenceTV.text = data[position].reference
    }

    override fun getItemCount(): Int {
        return data.size
    }
}