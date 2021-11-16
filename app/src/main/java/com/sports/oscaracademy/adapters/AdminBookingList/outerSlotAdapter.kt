package com.sports.oscaracademy.adapters.AdminBookingList

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.data.AdminBookedSlotsData
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.databinding.SingleAdminSloltsBinding


class outerSlotAdapter(
    val BookedSlotsData: AdminBookedSlotsData,
    val slotsID_slotsTiming: ArrayList<SlotsData>
) : RecyclerView.Adapter<outerSlotAdapter.viewHolder>() {

    class viewHolder(itemView: SingleAdminSloltsBinding) : RecyclerView.ViewHolder(itemView.root) {
        var bind: SingleAdminSloltsBinding = itemView

        fun clickHandle(BookedSlotsData: AdminBookedSlotsData) {
            val adapter = NestedAdapter(BookedSlotsData.singleSlotsData[adapterPosition])
            bind.childRv.layoutManager = LinearLayoutManager(itemView.context)
            bind.childRv.setHasFixedSize(true)
            bind.childRv.adapter = adapter
            adapter.notifyItemChanged(adapterPosition)


            if (BookedSlotsData.singleSlotsData[adapterPosition].isExpanded) {
                bind.expandableLayout.visibility = GONE
            } else {
                bind.expandableLayout.visibility = VISIBLE
            }

            BookedSlotsData.singleSlotsData[adapterPosition].isExpanded =
                !BookedSlotsData.singleSlotsData[adapterPosition].isExpanded
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = DataBindingUtil.inflate<SingleAdminSloltsBinding>(
            LayoutInflater.from(
                parent.context
            ),
            R.layout.single_admin_slolts,
            parent,
            false
        )
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val slot = slotsID_slotsTiming.filter {
            it.slotID == BookedSlotsData.slotsID[position]
        }
        holder.bind.slotID.text = slot[0].slot
        holder.bind.click2Expand.setOnClickListener {
            holder.clickHandle(BookedSlotsData)
        }
    }

    override fun getItemCount(): Int {
        return BookedSlotsData.slotsID.size
    }
}