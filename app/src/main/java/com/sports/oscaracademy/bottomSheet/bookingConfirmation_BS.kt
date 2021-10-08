package com.sports.oscaracademy.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sports.oscaracademy.R
import com.sports.oscaracademy.data.BookingData
import com.sports.oscaracademy.databinding.BookingConfirmationBsBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel
import java.text.SimpleDateFormat
import java.util.*

class bookingConfirmation_BS : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): bookingConfirmation_BS {
            val fragment = bookingConfirmation_BS()
            return fragment
        }
    }

    lateinit var bookingData: MutableLiveData<BookingData>
    lateinit var model: Pay_playViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(Pay_playViewModel::class.java)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialogThemeNoFloating)
    }

    lateinit var binding: BookingConfirmationBsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.booking_confirmation_bs, container, false)
        model.setCurrentBookingData()
        bookingData = model.getCurrentBookingData()

        binding.run {
            phoneNumber.setText(bookingData.value?.phoneNumber)
            email.setText(bookingData.value?.email)
            name.setText(bookingData.value?.name)
        }

//        val formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd",Locale.getDefault())
        val format = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.getDefault())
        val format2 = SimpleDateFormat("EE, dd MMM yyyy", Locale.getDefault())

//        val firstApiFormat = SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault())
        val tempDate = format.parse(model.getSelectedDate().value.toString())
        val date = tempDate?.run { format2.format(tempDate) }
        binding.selectedDate.text = date!!.toString()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val touchOutsideView = dialog!!.window
            ?.decorView
            ?.findViewById<View>(R.id.touch_outside)
        touchOutsideView?.setOnClickListener(null)
    }
}