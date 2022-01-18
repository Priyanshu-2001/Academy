package com.geek.adminpanel.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.geek.adminpanel.R
import com.geek.adminpanel.databinding.SingleDashboardRcvBinding
import java.util.*

class DashboardAdapter(
    val data: ArrayList<DashboardData>?,
    private val context: Context,
    private val navController: NavController,
    private val clickListener: OnClickInterface
) :
    RecyclerView.Adapter<DashboardAdapter.DashViewHolder>() {
    private var pref = context.getSharedPreferences("tokenFile", Context.MODE_PRIVATE)
    private var isStudent: String? = null
    private var role: String? = null

    init {
        isStudent = pref.getString("isStudent", "false")
        role = pref.getString("userType", "-1")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashViewHolder {
        val binding: SingleDashboardRcvBinding =
            SingleDashboardRcvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: DashViewHolder, position: Int) {
        val d: DashboardData = data!![position]
        val imageID = d.imageID
        holder.binding.image.setImageResource(imageID)
        holder.binding.textView.text = d.fieldName
        Log.d("TAG", "onBindViewHolder: " + d.fieldName)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class DashViewHolder(binding: SingleDashboardRcvBinding, val clickListener: OnClickInterface) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: SingleDashboardRcvBinding = binding
        fun nextScreen(text: String) {
            when (text.trim()) {
                "Change Schedule" -> {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_adminDashboard_to_changeSchedule)
                }
                "Slots Timings" -> {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_adminDashboard_to_slotTimings)
                }
                "Fees History" -> {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_adminDashboard_to_feesPayments)
                }
                "Academy Contacts" -> {
                    clickListener.OnRcvItemSelected("Academy Contacts")
                }
                "Edit Court Price" -> {
                    clickListener.OnRcvItemSelected("Edit Court Price")
                }
                "User Feedback" -> {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_adminDashboard_to_userFeedbacks)
                }
                "Admin/Coaches" -> {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_adminDashboard_to_adminCoach)
                }
            }
        }

        init {
            binding.cardView.setOnClickListener {
                nextScreen(
                    java.lang.String.valueOf(
                        binding.textView.text
                    )
                )
            }
        }
    }

}

interface OnClickInterface {
    fun OnRcvItemSelected(name: String)
}

data class DashboardData(val imageID: Int, val fieldName: String)