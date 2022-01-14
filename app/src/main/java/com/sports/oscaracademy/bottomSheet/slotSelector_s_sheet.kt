package com.sports.oscaracademy.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.slots_bsheet_adapter
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.databinding.SlotSelectorBottomSheetBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel

class slotSelector_s_sheet : BottomSheetDialogFragment() {

    var TAG = "BOTTOMSHEET"

    var binding: SlotSelectorBottomSheetBinding? = null
    lateinit var model: Pay_playViewModel
    private var totalSlots: ArrayList<SlotsData> = ArrayList()
    private var totalAvailableSlots: ArrayList<SlotsData> = ArrayList()
    private lateinit var booked_Court: ArrayList<courtSlot>
    private lateinit var slotsBsheetAdapter: slots_bsheet_adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity())[Pay_playViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.slot_selector_bottom_sheet, container, false)


        model.getTotalSlots().observe(this, { TotalSlots ->
            totalSlots = TotalSlots
            totalAvailableSlots = totalSlots
            booked_Court = ArrayList()
            val tempRemoverList = ArrayList<SlotsData>()
            model.getBookedData().observe(viewLifecycleOwner, Observer {
                Log.e(TAG, "onCreateView: bookeddata $it")
                it.forEach {
                    if (it.totalCourtBooked == model.getTotalCourt().value?.toLong()) {
                        try {
                            tempRemoverList.add((totalSlots.filter { s -> s.slotID == it.slotID })[0])
                        } catch (e: Exception) {
                        }
                    } else {
                        booked_Court.add(courtSlot(it.totalCourtBooked, it.slotID))
                    }
                }
                totalAvailableSlots = totalSlots
                totalAvailableSlots.removeAll(tempRemoverList)
                slotsBsheetAdapter =
                    slots_bsheet_adapter(
                        totalAvailableSlots,
                        model,
                        model.getSelectedSlots().value,
                        booked_Court,
                        model.getTotalCourt().value
                    ) // now total has been reduced by boooked slots

                binding!!.SlotsRcv.adapter = slotsBsheetAdapter
            })

        })

        binding!!.cancelBtn.setOnClickListener {
            dismiss()
        }
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

data class courtSlot(var bookedCourt: Long, var slot: String)