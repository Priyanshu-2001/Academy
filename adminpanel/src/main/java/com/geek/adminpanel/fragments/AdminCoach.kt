package com.geek.adminpanel.fragments

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.AdminCoachViewModel
import com.geek.adminpanel.adapter.AdminCoachAdapter
import com.geek.adminpanel.adapter.onLongClickAdmin
import com.geek.adminpanel.dataModel.UserData
import com.geek.adminpanel.databinding.AdminCoachBinding

class AdminCoach : Fragment(R.layout.admin_coach), onLongClickAdmin {
    private lateinit var dataList: ArrayList<UserData>
    lateinit var binding: AdminCoachBinding
    lateinit var mainViewModel: AdminCoachViewModel
    lateinit var adapter: AdminCoachAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AdminCoachBinding.bind(view)

        val viewModel: AdminCoachViewModel by viewModels()
        mainViewModel = viewModel
        updateFullData()
        binding.topBar.searchBtn.setOnClickListener { v ->
            val layoutTransition =
                binding.layoutContainer.layoutTransition
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            openSearchbar()
        }

        binding.topBar.EndSearchBtn.setOnClickListener { v -> exitSearchBar() }
    }

    private fun openSearchbar() {
        binding.topBar.searchBtn.animate().translationY(170f).withEndAction {
            binding.topBar.ActionSearchBtn.visibility = View.VISIBLE
            binding.topBar.searchBtn.visibility = View.GONE
        }
        binding.topBar.EndSearchBtn.visibility = View.VISIBLE
        binding.topBar.EndSearchBtn.animate().translationX(0f)
        binding.topBar.editSearch.visibility = View.VISIBLE
        binding.topBar.searchFilter.visibility = View.VISIBLE
    }

    private fun exitSearchBar() {
        binding.topBar.searchBtn.visibility = View.VISIBLE
        binding.topBar.EndSearchBtn.visibility = View.GONE
        binding.topBar.EndSearchBtn.animate().translationX(0f)
        binding.topBar.searchBtn.animate().translationY(0f)
        binding.topBar.ActionSearchBtn.animate().translationY(-20f)
        binding.topBar.ActionSearchBtn.visibility = View.INVISIBLE
        binding.topBar.EndSearchBtn.animate().translationX(1000f)
        binding.topBar.editSearch.visibility = View.GONE
        binding.topBar.searchFilter.visibility = View.GONE
        updateFullData()
    }

    private fun updateFullData() {
        mainViewModel.getUserList().observe(this, {
            mainViewModel.getAdminList().observe(this, {
                mainViewModel.getCoachesList().observe(this, {
                    mainViewModel.combineList().observe(this, {
                        it.sortByDescending { sort -> sort.isCoach }
                        it.sortByDescending { sort -> sort.isAdmin }
                        dataList = it
                        Log.e("TAG", "updateFullData: cALLED")
                        adapter = AdminCoachAdapter(context!!, it, this)
                        binding.studentrcv.adapter = adapter
                    })
                })
            })
        })
    }

    override fun assignAdmin(pos: Int) {
        mainViewModel.assignAdmin(dataList[pos])
    }

    override fun assignCoach(pos: Int) {
        mainViewModel.assignCoach(dataList[pos])
    }

    override fun removeCoach(pos: Int) {
        mainViewModel.removeCoach(dataList[pos])
    }

    override fun removeAdmin(pos: Int) {
        mainViewModel.removeAdmin(dataList[pos])
    }


}