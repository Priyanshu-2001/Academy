package com.sports.oscaracademy.HomeActivities.payAndplayFragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.CourtListAdapter
import com.sports.oscaracademy.adapters.selectedSlotsAdapter
import com.sports.oscaracademy.bottomSheet.bookingConfirmation_BS
import com.sports.oscaracademy.bottomSheet.slotSelector_s_sheet
import com.sports.oscaracademy.databinding.FragmentPayPlayBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel
import org.joda.time.DateTime


class pay_play : Fragment(), DatePickerListener {
    lateinit var binding: FragmentPayPlayBinding
    val TAG = "PayANDplay"
    val slot_bs: slotSelector_s_sheet = slotSelector_s_sheet()
    val checkOutSheet: bookingConfirmation_BS = bookingConfirmation_BS()
    var court_adapter: CourtListAdapter = CourtListAdapter()
    val courtID = ArrayList<String>()
    val totalCourts = 6
    val stagLayout = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    val layoutManager = GridLayoutManager(context, 3)

    companion object {
        fun newInstance(): pay_play {
            return pay_play()
        }
    }

    lateinit var model: Pay_playViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(Pay_playViewModel::class.java)
        model.setSelectedDate(DateTime.now().toLocalDate().toDate())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_play, container, false)
        val datePicker = binding.datePicker
        binding.lifecycleOwner = this
        binding.model = model

        datePicker.backgroundColor = Color.BLACK

        datePicker.setListener(this)
            .setDays(20)
            .setOffset(0)
            .setDateSelectedColor(Color.WHITE)
            .setDateSelectedTextColor(Color.LTGRAY)
            .setMonthAndYearTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            .setTodayButtonTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimaryDarker
                )
            )
            .setTodayDateTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimaryDarker
                )
            )
            .setTodayDateBackgroundColor(Color.TRANSPARENT)
            .setUnselectedDayTextColor(Color.WHITE)
            .setUnselectedDayTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            .showTodayButton(false)
            .init()
        datePicker.setDate(DateTime(model.getSelectedDate().value))

        binding.selectedSlotsRCV.layoutManager = layoutManager

        model.getSelectedSlots().observe(requireActivity(), {
            if (it.size == 0) {
                binding.selectedSlotsRCV.visibility = GONE
                binding.proceedBtn.visibility = GONE
                binding.btnSlotSelector.text = getString(R.string.selectSlot)
            } else {
                binding.selectedSlotsRCV.visibility = VISIBLE
                binding.selectedSlotsRCV.adapter = selectedSlotsAdapter(it)
                binding.proceedBtn.visibility = VISIBLE
                binding.btnSlotSelector.text = getString(R.string.editSlot)
            }
        })

        model.getTotalSlots()
        model.setTotalCourt()
        model.setTotalSlots()

        model.getSelectedSlots().observe(viewLifecycleOwner, {
            if (it != null) {
                var minCourt: Long = Long.MAX_VALUE
                Log.e(TAG, "onCreateView: arraylist $it")
                val selectedSlots = it
                if (it.size != 0) {
                    model.getMinCourtList().observe(viewLifecycleOwner, { hashMap ->
                        if (hashMap != null) {
                            Log.e(TAG, "onCreateView: $hashMap")
                            selectedSlots.forEach { slotData ->
                                if (hashMap[slotData.slotID] != null)
                                    minCourt =
                                        minCourt.coerceAtMost(hashMap[slotData.slotID]!!)
                            }
                            courtID.clear()
                            Log.e(TAG, "onCreateView: $minCourt")
                            var temp = minCourt

                            while (temp.toInt() != 0) {
                                courtID.add(totalCourts.toString())
                                temp--
                            }
                            court_adapter.notifyDataSetChanged()
                        }
                    })


                    binding.courtRcv.visibility = VISIBLE
                } else
                    binding.courtRcv.visibility = GONE
            } else {
                binding.courtRcv.visibility = GONE
            }
        })

        binding.proceedBtn.setOnClickListener {
            if (model.getSelectedCourtsCount().value != 0)
                checkOutSheet.show(parentFragmentManager, "checkOutFragment")
        }

        binding.courtRcv.layoutManager = stagLayout
        court_adapter.setData(courtID, model)
        binding.courtRcv.adapter = court_adapter

        binding.btnSlotSelector.setOnClickListener {
            val selectedDate = model.getSelectedDate().value
            if (selectedDate == null) {
                Toast.makeText(context, "Please Select Date First", Toast.LENGTH_LONG).show()
            } else {
                if (selectedDate.day == 6 || selectedDate.day == 0) {
                    model.setBookedData()
                    if (model.getSelectedSlots().value !== null) {
                        model.setSelectedSLots(ArrayList())
                    }
                    slot_bs.show(parentFragmentManager, "SlotBottomSheet")
                } else {
                    Toast.makeText(
                        context,
                        "Currently This Service is Only Available on Saturday and Sundays",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
        return binding.root
    }
    override fun onDateSelected(dateSelected: DateTime?) {
        if (dateSelected != null) {
            Log.e(TAG, "onDateSelected: ${dateSelected.toLocalDate().toDate()}")
        }
        if (dateSelected != null) {
            model.setSelectedDate(dateSelected.toLocalDate().toDate())
            if ((dateSelected.dayOfWeek == 6 || dateSelected.dayOfWeek == 7) && model.getSelectedSlots().value != null) {
                binding.proceedBtn.visibility = VISIBLE
            } else {
                binding.proceedBtn.visibility = GONE
            }

        }
    }
}