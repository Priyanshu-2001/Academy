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
    fun setData(courtID: ArrayList<String>, model: Pay_playViewModel) {
        this.courtId = courtID
        this.model = model
    }

    class courtVH(itemView: CourtSelectorBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding: CourtSelectorBinding = itemView
    }

    val tempList: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): courtVH {
        val binding: CourtSelectorBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.court_selector, parent, false
        )
        return courtVH(binding)
    }

    override fun onBindViewHolder(holder: courtVH, position: Int) {
        holder.binding.courtName.text = "Wooden Badminton Court"
        holder.binding.checkBox.setOnCheckedChangeListener { button, b ->
            if (b) {
                Log.e("TAG", "BookCourt: courtID chaanged " + b + " " + courtId.get(position))
                tempList.add(courtId.get(position))
                model.setSelectedCourt(tempList)
                Log.e("TAG", "BookCourt: courtID changed " + model.getSelectedCourts().value)
            } else {
                Log.e("TAG", "BookCourt: courtID chaanged " + b)
                tempList.remove(courtId.get(position))
                model.setSelectedCourt(tempList)
            }
        }
    }

    override fun getItemCount(): Int {
        return courtId.size
    }
}