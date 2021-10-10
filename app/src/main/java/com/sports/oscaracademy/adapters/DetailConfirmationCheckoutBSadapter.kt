package com.sports.oscaracademy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.SingleOrderReviewBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel

class DetailConfirmationCheckoutBSadapter(val model: Pay_playViewModel, val singleCourtPrice: Int) :
    RecyclerView.Adapter<DetailConfirmationCheckoutBSadapter.vHolder>() {

    class vHolder(val binding: SingleOrderReviewBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vHolder {
        val binding: SingleOrderReviewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.single_order_review,
            parent,
            false
        )
        return vHolder(binding)
    }

    override fun onBindViewHolder(holder: vHolder, position: Int) {
        holder.binding.totalCourtBooked.text =
            String.format(model.getSelectedCourtsCount().value.toString() + " X \u20B9" + singleCourtPrice)
        holder.binding.slotTiming.text =
            model.getSelectedSlots().value?.get(position)?.slot ?: "Select Some Slots first"
        holder.binding.priceOfSlot.text =
            String.format("₹" + (model.getSelectedCourtsCount().value!! * singleCourtPrice))
    }

    override fun getItemCount(): Int {
        return model.getSelectedSlots().value!!.count()
    }
}