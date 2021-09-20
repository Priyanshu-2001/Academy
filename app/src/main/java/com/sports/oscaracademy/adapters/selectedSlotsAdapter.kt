package com.sports.oscaracademy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.data.SlotsData

class selectedSlotsAdapter(val data: ArrayList<SlotsData>) :
    RecyclerView.Adapter<selectedSlotsAdapter.selectedSlotVH>() {

    class selectedSlotVH(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtView: TextView = itemView.findViewById(R.id.txtView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectedSlotVH {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_selected_slots, parent, false)
        return selectedSlotVH(v)
    }

    override fun onBindViewHolder(holder: selectedSlotVH, position: Int) {
        holder.txtView.text = data.get(position).slot
    }

    override fun getItemCount(): Int {
        return data.size
    }
}