package com.geek.adminpanel.fragments.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.SlotsViewModel
import com.geek.adminpanel.databinding.FragmentNewSlotAdderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewSlotAdder : BottomSheetDialogFragment() {
    lateinit var binding: FragmentNewSlotAdderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_slot_adder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[SlotsViewModel::class.java]
        binding = FragmentNewSlotAdderBinding.bind(view)
        binding.apply {
            slotAdderBtn.setOnClickListener {
                if (!slotID.text.isNullOrEmpty() && !slotTiming.text.isNullOrEmpty()) {
                    viewModel.setNewSlot(slotID.text.toString(), slotTiming.text.toString())
                    Log.e("TAG", "onViewCreated: $slotID.text.toString()")
                }
            }
            closeBtn.setOnClickListener {
                dismiss()
            }
        }
    }
}