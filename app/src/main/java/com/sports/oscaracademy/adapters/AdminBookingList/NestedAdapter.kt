package com.sports.oscaracademy.adapters.AdminBookingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.data.SingleSlotData
import com.sports.oscaracademy.databinding.SingleAdminBookedListNestedBinding

class NestedAdapter(private val singleSlotData: SingleSlotData) :
    RecyclerView.Adapter<NestedAdapter.viewHolder>() {

    class viewHolder(bind: SingleAdminBookedListNestedBinding) :
        RecyclerView.ViewHolder(bind.root) {
        val binding = bind
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = DataBindingUtil.inflate<SingleAdminBookedListNestedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.single_admin_booked_list_nested,
            parent,
            false
        )
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.otp.text = singleSlotData.courtData[position]?.otp.toString()
        holder.binding.bookName.text = singleSlotData.courtData[position]?.name.toString()
        holder.binding.phoneNumber.text =
            singleSlotData.courtData[position]?.PhoneNumber.toString()
    }

    override fun getItemCount(): Int {
        return singleSlotData.courtBooked.size
    }
}