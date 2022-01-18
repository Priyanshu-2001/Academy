package com.geek.adminpanel.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.geek.adminpanel.R
import com.geek.adminpanel.adapter.DashboardAdapter
import com.geek.adminpanel.adapter.DashboardData
import com.geek.adminpanel.adapter.OnClickInterface
import com.geek.adminpanel.databinding.FragmentAdminDashboardBinding
import com.geek.adminpanel.fragments.bottomsheet.Contacts
import com.geek.adminpanel.fragments.bottomsheet.EditCourtPrice

class AdminDashboard : Fragment(R.layout.fragment_admin_dashboard), OnClickInterface {
    lateinit var navController: NavController
    lateinit var binding: FragmentAdminDashboardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding = FragmentAdminDashboardBinding.bind(view)
        val data = ArrayList<DashboardData>()
        data.add(DashboardData(R.drawable.ic_baseline_schedule_24, "Change Schedule"))
//        data.add(DashboardData(R.drawable.app_back,"Edit Court Price"))
        data.add(DashboardData(R.drawable.app_back, "Slots Timings"))
//        data.add(DashboardData(R.drawable.app_back,"Academy Contacts"))
        data.add(DashboardData(R.drawable.app_back, "Fees History"))
        data.add(DashboardData(R.drawable.app_back, "User Feedback"))
        data.add(DashboardData(R.drawable.app_back, "Admin/Coaches"))

        val adapter = DashboardAdapter(data, requireContext(), navController, this)
        binding.MainRCV.adapter = adapter
    }

    override fun OnRcvItemSelected(name: String) {
        if (name == "Edit Court Price") {
            EditCourtPrice().show(parentFragmentManager, "editCourtPrice")
        }
        if (name == "Academy Contacts") {
            Contacts().show(parentFragmentManager, "editContacts")
        }
    }
}