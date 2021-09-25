package com.sports.oscaracademy.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.CourtSelectorBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel

class CourtListAdapter :
    RecyclerView.Adapter<CourtListAdapter.courtVH>() {

    var courtId: ArrayList<String> = ArrayList()
    lateinit var model: Pay_playViewModel
    var selectedCourtCount = 0 //todo need to start from where left last time
    fun setData(courtID: ArrayList<String>, model: Pay_playViewModel) {
        this.courtId = courtID
        this.model = model
    }

    class courtVH(itemView: CourtSelectorBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: CourtSelectorBinding = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): courtVH {
        val binding: CourtSelectorBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.court_selector, parent, false
        )
        return courtVH(binding)
    }

    override fun onBindViewHolder(holder: courtVH, position: Int) {
        val pos = position + 1
        holder.binding.courtName.text = "Wooden Badminton Court $pos"
        holder.binding.checkBox.isChecked = false
        holder.binding.checkBox.setOnCheckedChangeListener { button, b ->
            if (b) {
                Log.e("TAG", "BookCourt: courtID chaanged " + b + " " + courtId.get(position))
                selectedCourtCount++
                Log.e("TAG", "BookCourt: courtID changed " + model.getSelectedCourtsCount().value)
            } else {
                selectedCourtCount--
                Log.e("TAG", "BookCourt: courtID chaanged " + b)
            }
            Log.e("selectedCourtCount ", "onBindViewHolder: $selectedCourtCount")
            model.setSelectedCourtCount(selectedCourtCount)

        }
    }

    override fun getItemCount(): Int {
        return courtId.size
    }
}