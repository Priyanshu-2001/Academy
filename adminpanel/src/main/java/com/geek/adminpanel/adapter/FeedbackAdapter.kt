package com.geek.adminpanel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.geek.adminpanel.R
import com.geek.adminpanel.dataModel.FeedbackData
import com.geek.adminpanel.databinding.SingleFeedbackBinding
import java.text.SimpleDateFormat
import java.util.*

class FeedbackAdapter(val data: ArrayList<FeedbackData>, val feedbackInterface: FeedBackInterface) :
    RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {
    class FeedbackViewHolder(
        val binding: SingleFeedbackBinding,
        private val feedbackInterface: FeedBackInterface
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnLongClickListener {
        init {
            binding.cardView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            val menu = PopupMenu(v!!.context, v)
            menu.inflate(R.menu.feedback_menu)
            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_delete -> {
                        feedbackInterface.deleteItem(adapterPosition)
                        true
                    }
                    R.id.action_starred -> {
                        feedbackInterface.star(adapterPosition)
                        true
                    }
                    R.id.action_userName -> {
                        feedbackInterface.navigateToProfile(adapterPosition)
                        true
                    }
                    R.id.action_unstar -> {
                        feedbackInterface.unStar(adapterPosition)
                        true
                    }
                    else -> {
                        false
                    }

                }
            }
            menu.show()
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val binding = DataBindingUtil.inflate<SingleFeedbackBinding>(
            LayoutInflater.from(parent.context),
            R.layout.single_feedback,
            parent,
            false
        )
        return FeedbackViewHolder(binding, feedbackInterface)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val reqData = data[position]
        holder.binding.apply {
            feedback.text = reqData.feedback
            val seconds: Long = reqData.timestamp as Long
            val millis = seconds * 1000
            val date = Date(millis)
            val sdf = SimpleDateFormat("EEE,d MMM yyyy", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val formattedDate: String = sdf.format(date)
            timeStamp.text = formattedDate
            if (reqData.starred)
                starred.visibility = View.VISIBLE
            if (reqData.new)
                newIcon.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = data.size
}

interface FeedBackInterface {
    fun deleteItem(pos: Int)
    fun star(pos: Int)
    fun navigateToProfile(adapterPosition: Int)
    fun unStar(pos: Int)
}