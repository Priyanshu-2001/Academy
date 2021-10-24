package com.sports.oscaracademy.HomeActivities.payAndplayFragments

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
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
    private val slot_bs: slotSelector_s_sheet = slotSelector_s_sheet()
    private val checkOutSheet: bookingConfirmation_BS = bookingConfirmation_BS()
    private var court_adapter: CourtListAdapter = CourtListAdapter()
    private val courtID = ArrayList<String>()
    private val totalCourts = 6
    private lateinit var navController: NavController
    private lateinit var role: String

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_play, container, false)
        context?.apply {
            val prefs = getSharedPreferences("tokenFile", AppCompatActivity.MODE_PRIVATE)
            role = prefs.getString("userType", "-1")!!
        }
        val datePicker = binding.datePicker
        binding.lifecycleOwner = this
        binding.model = model

        (context as AppCompatActivity).setSupportActionBar(binding.toolbar)

        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.topTitleName.text = "PAY AND PLAY"
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

//        binding.selectedSlotsRCV.layoutManager = layoutManager

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
        model.setSingleCourtPrice()

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
            if (model.getSelectedCourtsCount().value != null) {
                if (model.getSelectedCourtsCount().value != 0)
                    checkOutSheet.show(parentFragmentManager, "checkOutFragment")
            } else {
                val snackbar =
                    Snackbar.make(
                        requireView(),
                        "Hey... U didn't select any Court",
                        Snackbar.LENGTH_LONG
                    )
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                snackbar.view.setPadding(5, 0, 5, 20)
                snackbar.show()
            }
        }


//        binding.courtRcv.layoutManager = stagLayout
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

        if (role == "-2") {
            val c = context
            (binding.topTitleName.layoutParams as ConstraintLayout.LayoutParams).apply {
                marginEnd = c?.let {
                    0.dpToPixels(it)
                }!!
            }
        }
        return binding.root
    }

    fun Int.dpToPixels(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        context?.apply {
            val prefs = getSharedPreferences("tokenFile", AppCompatActivity.MODE_PRIVATE)
            role = prefs.getString("userType", "-1")!!

            if (role == "-2")
                inflater.inflate(R.menu.pay_play_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (navController.currentDestination?.id == navController.graph.startDestination) {
                    (context as Activity).finish()
                    return true
                }
                navController.navigateUp()
                return true
            }

            R.id.viewBookings -> {
                navController.navigate(
                    R.id.action_pay_play2_to_adminBooking,
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}