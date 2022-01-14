package com.geek.adminpanel.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.SlotsViewModel
import com.geek.adminpanel.adapter.DeleteAction
import com.geek.adminpanel.adapter.SlotTimingAdapter
import com.geek.adminpanel.databinding.FragmentSlotTimingsBinding
import com.geek.adminpanel.fragments.bottomsheet.NewSlotAdder

class SlotTimings : Fragment(R.layout.fragment_slot_timings), DeleteAction {

    lateinit var binding: FragmentSlotTimingsBinding
    private lateinit var adapter: SlotTimingAdapter
    private lateinit var totalCourt: String
    private lateinit var viewModel: SlotsViewModel
    private val slotAdder = NewSlotAdder()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SlotsViewModel::class.java]

        binding = FragmentSlotTimingsBinding.bind(view)

        (context as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val actionBar = (context as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        viewModel.getSlotsData().observe(requireActivity(), {
            adapter = SlotTimingAdapter(it, this)
            binding.slotTimingsRCV.adapter = adapter
        })
        viewModel.getTotalCourt().observe(requireActivity(), {
            binding.totalCourt.setText(it)
            totalCourt = it
        })
        binding.button.setOnClickListener {
            if (totalCourt != binding.totalCourt.text.toString())
                viewModel.saveCourtNumber(binding.totalCourt.text.toString())
        }
        binding.addBtn.setOnClickListener {
            slotAdder.show(parentFragmentManager, "slotAdder")
        }

    }

    override fun deleteSlot(pos: Int) {
        viewModel.deleteList(pos)
    }
}