package com.sports.oscaracademy.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.databinding.SlotSelectorSingleBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel

class slots_bsheet_adapter(
    val data: ArrayList<SlotsData>,
    val model: Pay_playViewModel,
    val temp: ArrayList<SlotsData>?
) :
    RecyclerView.Adapter<slots_bsheet_adapter.slotsVH>() {
    var selectedSlots: ArrayList<SlotsData> = ArrayList()

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
//        val tempList = model.getSelectedSlots().value

        if (!selectedSlots.isEmpty()) {
            if (selectedSlots.contains(data.get(position))) {
                holder.bind.checkBox.isChecked = true
                Log.e("TAG", "onBindViewHolder: checked true " + data.get(position))
            }
        }
        holder.bind.checkBox.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            if (checked) {
                selectedSlots.add(data.get(position))
                Log.e("TAG", "onBindViewHolder: changed " + data.get(position))

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