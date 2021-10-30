package com.sports.oscaracademy.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.DetailConfirmationCheckoutBSadapter
import com.sports.oscaracademy.data.BookingData
import com.sports.oscaracademy.databinding.BookingConfirmationBsBinding
import com.sports.oscaracademy.dialog.dialogs
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
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
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

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        val doubleBounce: Sprite = WanderingCubes()
        binding.progress.setIndeterminateDrawable(doubleBounce)

        binding.run {
            phoneNumber.setText(bookingData.value?.phoneNumber)
            email.setText(bookingData.value?.email)
            name.setText(bookingData.value?.name)
        }

        binding.run {
            checkOutBtn.setOnClickListener {
                if (phoneNumber.text.length in 10..13) {
                    if (!name.text.isNullOrEmpty()) {
                        if (!email.text.isNullOrEmpty()) {
                            progress.visibility = View.VISIBLE
                            model.setCurrentBookingUserDetails(
                                phoneNumber.text.toString(),
                                name.text.toString(),
                                email.text.toString()
                            )
                            dialogs().bookingT_C(
                                context,
                                model,
                                dialog?.window?.decorView,
                                progress
                            )
                        } else {
                            dialog?.window?.decorView?.let { it1 ->
                                Snackbar.make(
                                    it1,
                                    "Please Check Your Email",
                                    LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        dialog?.window?.decorView?.let { it1 ->
                            Snackbar.make(
                                it1,
                                "Please Fill Your Name",
                                LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    dialog?.window?.let { it1 ->
                        Snackbar.make(
                            it1.decorView,
                            "Please Check Your Phone Number",
                            LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        setDataToViews()
        val format = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.getDefault())
        val format2 = SimpleDateFormat("EE, dd MMM yyyy", Locale.getDefault())
        val tempDate = format.parse(model.getSelectedDate().value.toString())
        val date = tempDate?.run { format2.format(tempDate) }
        binding.selectedDate.text = date!!.toString()
        return binding.root
    }

    private fun setDataToViews() {
        val adapter = DetailConfirmationCheckoutBSadapter(model, model.getSingleCourtPrice())
        binding.checkOutRCV.adapter = adapter
        binding.finalPrice.text = String.format("₹" + model.getCheckOutPrice())
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val touchOutsideView = dialog!!.window
            ?.decorView
            ?.findViewById<View>(R.id.touch_outside)
        touchOutsideView?.setOnClickListener(null)
    } //code to disable outside touch closing problem
}