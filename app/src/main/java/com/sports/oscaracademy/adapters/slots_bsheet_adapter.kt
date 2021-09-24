package com.sports.oscaracademy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.bottomSheet.courtSlot
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.databinding.SlotSelectorSingleBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel

class slots_bsheet_adapter(
    val data: ArrayList<SlotsData>,
    val model: Pay_playViewModel,
    val temp: ArrayList<SlotsData>?,
    val booked_Court: ArrayList<courtSlot>,
    val totalCourt: String?
) :
    RecyclerView.Adapter<slots_bsheet_adapter.slotsVH>() {
    var selectedSlots: ArrayList<SlotsData> = ArrayList()
    var tempHashMap = HashMap<String, Long>()
    init {
        if (temp != null) {
            selectedSlots = temp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): slotsVH {
        val binding: SlotSelectorSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.slot_selector_single, parent, false
        )
        return slotsVH(binding)

    }

    override fun onBindViewHolder(holder: slotsVH, position: Int) {
        holder.bind.viemodel = data.get(position)
        holder.bind.availableCourt.text = totalCourt

        if (totalCourt != null) {
            tempHashMap.put(data[position].slotID, totalCourt.toLong())
        } else
            tempHashMap.put(data[position].slotID, 5)


        booked_Court.forEach {
            if (data.get(position).slotID == it.slot) {
                val temp = totalCourt?.toLong()?.minus(it.bookedCourt)
                holder.bind.availableCourt.text = temp.toString()
                if (temp != null) {
                    tempHashMap.put(data[position].slotID, temp)
                }
            }
        }
        model.setMinCourtList(tempHashMap)
        if (!selectedSlots.isEmpty()) {
            if (selectedSlots.contains(data.get(position))) {
                holder.bind.checkBox.isChecked = true
            }
        }
        holder.bind.checkBox.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            if (checked) {
                selectedSlots.add(data.get(position))

            } else {
                selectedSlots.remove(data.get(position))
            }
            model.setSelectedSLots(selectedSlots)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class slotsVH(itemView: SlotSelectorSingleBinding) : RecyclerView.ViewHolder(itemView.root) {
        val bind: SlotSelectorSingleBinding = itemView
    }
}