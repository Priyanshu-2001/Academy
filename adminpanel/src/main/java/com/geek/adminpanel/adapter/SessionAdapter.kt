package com.geek.adminpanel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.geek.adminpanel.R
import com.geek.adminpanel.dataModel.SessionData
import com.geek.adminpanel.databinding.SingleSessionDetailsBinding

class SessionAdapter(val data: ArrayList<SessionData>, private val onSessionClick: OnSessionClick) :
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {
    open class SessionViewHolder(
        itemView: SingleSessionDetailsBinding,
        private val onSessionClick: OnSessionClick
    ) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView

        init {
            binding.cardView.setOnClickListener {
                onSessionClick.onSessionClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val binding: SingleSessionDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.single_session_details,
            parent,
            false
        )
        return SessionViewHolder(binding, onSessionClick)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val currentData = data[position]
        holder.binding.fees.text = "₹ " + currentData.sessionFees
        holder.binding.sessionID.text = currentData.sessionId
        holder.binding.sessionTiming.text = currentData.sessionTiming
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

interface OnSessionClick {
    fun onSessionClick(position: Int)
}