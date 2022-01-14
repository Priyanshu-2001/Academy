package com.geek.adminpanel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.geek.adminpanel.R
import com.geek.adminpanel.dataModel.SlotData
import com.geek.adminpanel.databinding.SingleSlotTimingBinding

class SlotTimingAdapter(val data: ArrayList<SlotData>, val deleteAction: DeleteAction) :
    RecyclerView.Adapter<SlotTimingAdapter.viewHolder>() {

    open class viewHolder(
        private val binding: SingleSlotTimingBinding,
        deleteAction: DeleteAction
    ) : RecyclerView.ViewHolder(binding.root) {
        val bind = binding

        init {
            bind.deleteBtn.setOnClickListener {
                deleteAction.deleteSlot(adapterPosition + 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding: SingleSlotTimingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.single_slot_timing,
            parent,
            false
        )
        return viewHolder(binding, deleteAction)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind.slotID.text = data[position].slotId
        holder.bind.slotTiming.text = data[position].slotTiming
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

interface DeleteAction {
    fun deleteSlot(pos: Int)
}