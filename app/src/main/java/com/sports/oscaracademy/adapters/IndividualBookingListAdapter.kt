package com.sports.oscaracademy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.SinglePayPlayUserHistoryBinding
import com.sports.oscaracademy.service.BookingListData
import java.text.SimpleDateFormat
import java.util.*

class IndividualBookingListAdapter(
    val data: List<BookingListData>,
    private val listItemClickListener: onListItemClickListener
) :
    RecyclerView.Adapter<IndividualBookingListAdapter.ListViewHolder>() {
    class ListViewHolder(
        itemView: SinglePayPlayUserHistoryBinding,
        onListItemClickListener: onListItemClickListener
    ) : RecyclerView.ViewHolder(itemView.root),
        View.OnClickListener {
        val binding = itemView
        private val clickListener = onListItemClickListener

        init {
            binding.parentView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onItemClicked(position = adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding: SinglePayPlayUserHistoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.single_pay_play_user_history, parent, false
        )
        return ListViewHolder(binding, listItemClickListener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentData = data[position]
        val sfd = SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault())
        val sfdTime = SimpleDateFormat(" h:mm a", Locale.getDefault())
        val date = sfd.format(currentData.timeStamp.toDate())
        val time = sfdTime.format(currentData.timeStamp.toDate())
        (date + "\n" + time).also { holder.binding.dateTime.text = it }
        ("₹ " + currentData.totalAmountPaid).also { holder.binding.amount.text = it }
        ("OTP \n" + currentData.otp).also { holder.binding.otp.text = it }
    }

    override fun getItemCount() = data.size

    interface onListItemClickListener {
        fun onItemClicked(position: Int)
    }
}
