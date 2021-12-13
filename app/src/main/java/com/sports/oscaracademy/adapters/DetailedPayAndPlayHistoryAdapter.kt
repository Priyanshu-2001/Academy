package com.sports.oscaracademy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.SingleSlotDetailBinding

class DetailedPayAndPlayHistoryAdapter(
    private val slotData: HashMap<*, *> //{4=7:00 - 8:00, 6=9:00 - 10:00, 8=11:00 - 12:00}
) :
    RecyclerView.Adapter<DetailedPayAndPlayHistoryAdapter.vHolder>() {
    class vHolder(val binding: SingleSlotDetailBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    private val slotsList = ArrayList<String>()
    private val slotsIDList = ArrayList<String>()

    init {
        slotData.keys.forEach {
            slotsIDList.add(it as String)
            slotsList.add(slotData[it] as String)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vHolder {
        val binding: SingleSlotDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.single_slot_detail,
            parent,
            false
        )
        return vHolder(binding)
    }

    override fun onBindViewHolder(holder: vHolder, position: Int) {
        val slot = slotsList[position]
        holder.binding.slotTiming.text = slot
    }

    override fun getItemCount(): Int {
        return slotData.size
    }
}