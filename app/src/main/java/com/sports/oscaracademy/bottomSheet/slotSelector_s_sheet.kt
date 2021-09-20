package com.sports.oscaracademy.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.slots_bsheet_adapter
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.databinding.SlotSelectorBottomSheetBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel

class slotSelector_s_sheet : BottomSheetDialogFragment() {
    lateinit var binding: SlotSelectorBottomSheetBinding
    lateinit var model: Pay_playViewModel
    var slots: MutableLiveData<ArrayList<SlotsData>> = MutableLiveData<ArrayList<SlotsData>>()
    lateinit var slotsBsheetAdapter: slots_bsheet_adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(Pay_playViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.slot_selector_bottom_sheet, container, false)
        model.getTotalSlots().observe(this, Observer {
            slots.value = it
            slotsBsheetAdapter =
                slots_bsheet_adapter(slots.value!!, model, model.getSelectedSlots().value)
            binding.SlotsRcv.adapter = slotsBsheetAdapter
            Log.e("TAG", "onCreateView: viewmodel " + model)
            Log.e("TAG", "onCreateView: viewmodel " + model.getTotalSlots().value)
            Log.e("TAG", "onCreateView: viewmodel " + model.getSelectedSlots().value)
        })


        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}