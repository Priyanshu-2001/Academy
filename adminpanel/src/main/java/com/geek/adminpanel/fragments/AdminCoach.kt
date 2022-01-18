package com.geek.adminpanel.fragments

import android.animation.LayoutTransition
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.AdminCoachViewModel
import com.geek.adminpanel.adapter.AdminCoachAdapter
import com.geek.adminpanel.adapter.onLongClickAdmin
import com.geek.adminpanel.dataModel.UserData
import com.geek.adminpanel.databinding.AdminCoachBinding

class AdminCoach : Fragment(R.layout.admin_coach), onLongClickAdmin {
    private lateinit var tempFilterSelection: Button
    private lateinit var dataList: ArrayList<UserData>
    lateinit var binding: AdminCoachBinding
    lateinit var mainViewModel: AdminCoachViewModel
    lateinit var adapter: AdminCoachAdapter
    var filterType = "name"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AdminCoachBinding.bind(view)
        binding.lifecycleOwner = this
        mainViewModel = ViewModelProvider(requireActivity())[AdminCoachViewModel::class.java]
        updateFullData()
        binding.topBar.searchBtn.setOnClickListener { v ->
            val layoutTransition =
                binding.layoutContainer.layoutTransition
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            openSearchbar()
        }

        binding.topBar.topTitleName.text = "Admin/Coaches"
        binding.topBar.EndSearchBtn.setOnClickListener { exitSearchBar() }
        binding.topBar.ActionSearchBtn.setOnClickListener {
            val query: Any = binding.topBar.editSearch.text.toString()
            applyFilter(query)
        }
        binding.topBar.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                applyFilter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val filter_rollNo = binding.topBar.filterRollNo
        val filter_name = binding.topBar.filerName
        val filter_email = binding.topBar.filterEmail
        val filter_phoneNumber = binding.topBar.filterPhoneNumber

        tempFilterSelection = filter_name

        filter_phoneNumber.setOnClickListener { v: View? ->
            setColorToDefault()
            filterType = "phone number"
            tempFilterSelection = filter_phoneNumber
            setColorToSelected(tempFilterSelection)
        }

        filter_name.setOnClickListener { v: View? ->
            setColorToDefault()
            filterType = "name"
            tempFilterSelection = filter_name
            setColorToSelected(tempFilterSelection)
        }

        filter_email.setOnClickListener { v: View? ->
            setColorToDefault()
            tempFilterSelection = filter_email
            filterType = "email"
            setColorToSelected(tempFilterSelection)
        }

        filter_rollNo.setOnClickListener { v: View? ->
            setColorToDefault()
            filterType = "RollNo"
            tempFilterSelection = filter_rollNo
            setColorToSelected(tempFilterSelection)
        }
        setColorToSelected(tempFilterSelection)
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

    private fun setColorToDefault() {
        tempFilterSelection.background = AppCompatResources.getDrawable(
            context!!,
            R.drawable.btn_theme_2
        )
    }

    private fun setColorToSelected(btn: Button) {
        btn.background = AppCompatResources.getDrawable(context!!, R.drawable.btn_theme_1)
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

        mainViewModel.combineList().observe(requireActivity(), {
            it.sortByDescending { sort -> sort.isCoach }
            it.sortByDescending { sort -> sort.isAdmin }
            dataList = it
            Log.e("TAG", "updateFullData: cALLED")
            adapter = AdminCoachAdapter(it, this)
            binding.studentrcv.adapter = adapter
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

    fun applyFilter(filter: Any) {
        Log.e("TAG", "applyFilter: $filterType $filter")
        mainViewModel.getUserDataFiltered(filterType, filter).observe(this, {
            it.sortByDescending { sort -> sort.isCoach }
            it.sortByDescending { sort -> sort.isAdmin }
            dataList = it
            Log.e("TAG", "updateFullData: cALLED")
            adapter = AdminCoachAdapter(it, this)
            binding.studentrcv.adapter = adapter
        })
    }


}