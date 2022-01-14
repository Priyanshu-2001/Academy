package com.geek.adminpanel.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.SessionViewModel
import com.geek.adminpanel.adapter.OnSessionClick
import com.geek.adminpanel.adapter.SessionAdapter
import com.geek.adminpanel.dataModel.SessionData
import com.geek.adminpanel.databinding.FragmentChangeScheduleBinding
import com.geek.adminpanel.fragments.bottomsheet.EditSessionDetails

class ChangeSchedule : Fragment(R.layout.fragment_change_schedule), OnSessionClick {
    lateinit var binding: FragmentChangeScheduleBinding
    lateinit var viewModel: SessionViewModel
    lateinit var sessionData: ArrayList<SessionData>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        binding = FragmentChangeScheduleBinding.bind(view)
        (context as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val actionBar = (context as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        viewModel.getSessionDetails().observe(requireActivity(), {
            val adapter = SessionAdapter(it, this)
            binding.sessionSlots.adapter = adapter
            sessionData = it
        })
        binding.addBtn.setOnClickListener {
            val bs = EditSessionDetails()
            val bund = bundleOf(
                Pair("fees", " "),
                Pair("time", " "),
                Pair("id", " ")
            )
            bs.arguments = bund
            bs.show(parentFragmentManager, "editSession")
        }
    }

    override fun onSessionClick(position: Int) {
        val bs = EditSessionDetails()
        val bund = bundleOf(
            Pair("fees", sessionData[position].sessionFees),
            Pair("time", sessionData[position].sessionTiming),
            Pair("id", sessionData[position].sessionId)
        )
        bs.arguments = bund
        bs.show(parentFragmentManager, "editSession")
    }
}