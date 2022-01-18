package com.geek.adminpanel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.geek.adminpanel.R
import com.geek.adminpanel.dataModel.UserData
import com.geek.adminpanel.databinding.SingleStudentRcvBinding

class AdminCoachAdapter(
//    val context: Context,
    val data: ArrayList<UserData>,
    private val onLongClickAdmin: onLongClickAdmin
) :
    RecyclerView.Adapter<AdminCoachAdapter.viewHolder>() {

    open class viewHolder(
        val binding: SingleStudentRcvBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.single_student_rcv, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val d = data[position]
        if (d.isAdmin) {
            holder.binding.back.background =
                ResourcesCompat.getDrawable(holder.binding.root.resources, R.drawable.admin, null)
        }
        if (d.isCoach) {
            holder.binding.back.background =
                ResourcesCompat.getDrawable(holder.binding.root.resources, R.drawable.coach, null)
        }
        holder.binding.roll.text = d.rollNo.toString()
        holder.binding.name.text = d.name
        holder.binding.contact.text = d.phone


        val popup = PopupMenu(holder.binding.root.context, holder.binding.root)
        when {
            data[position].isAdmin -> {
                popup.inflate(R.menu.is_admin)
            }
            data[position].isCoach -> {
                popup.inflate(R.menu.is_coach)
            }
            else -> {
                popup.inflate(R.menu.is_user)
            }
        }
        holder.binding.cardView.setOnLongClickListener {
            popup.show()
            true
        }
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.assignAdmin -> {
                    onLongClickAdmin.assignAdmin(position)
                }
                R.id.assignCoach -> {
                    onLongClickAdmin.assignCoach(position)
                }
                R.id.removeCoach -> {
                    onLongClickAdmin.removeCoach(position)
                }
                R.id.removeAdmin -> {
                    onLongClickAdmin.removeAdmin(position)
                }
            }
            true
        }
    }

    override fun getItemCount() = data.size
}

interface onLongClickAdmin {
    fun assignAdmin(pos: Int)
    fun assignCoach(pos: Int)
    fun removeCoach(pos: Int)
    fun removeAdmin(pos: Int)

}