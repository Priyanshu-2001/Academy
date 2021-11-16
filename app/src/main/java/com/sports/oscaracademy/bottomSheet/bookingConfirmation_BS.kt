package com.sports.oscaracademy.bottomSheet

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.razorpay.Checkout
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.DetailConfirmationCheckoutBSadapter
import com.sports.oscaracademy.data.BookingData
import com.sports.oscaracademy.databinding.BookingConfirmationBsBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class bookingConfirmation_BS : BottomSheetDialogFragment() {


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

        val pref = context?.getSharedPreferences("tokenFile", Context.MODE_PRIVATE)

        binding.run {
            //if user has modified last time using it
            name.setText(pref?.getString("paymentName", bookingData.value?.name).toString())
            phoneNumber.setText(
                pref?.getString(
                    "paymentPhoneNumber",
                    bookingData.value?.phoneNumber
                )
            )
            email.setText(pref?.getString("paymentEmail", bookingData.value?.email))

            checkOutBtn.setOnClickListener {
                if (phoneNumber.text.length in 10..13) {
                    if (!name.text.isNullOrEmpty()) {
                        if (!email.text.isNullOrEmpty()) {
                            model.setCurrentBookingUserDetails(
                                phoneNumber.text.toString(),
                                name.text.toString(),
                                email.text.toString()
                            )
                            bookingT_C()
//                            startPayment()
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

    private fun startPayment() {
        val activity: Activity = requireActivity()
        val co = Checkout()
        co.setKeyID("rzp_test_anVS19J2dBeADE")
        try {
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#0B7E93")
            options.put("currency", "INR")
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            val finalPayment = Integer.valueOf(model.getCheckOutPrice().toString()) * 100
            options.put("amount", finalPayment)//pass amount in currency subunits

            val method = JSONObject()
            method.put("card", "0")
            method.put("netbanking", "0")
            method.put("upi", "1")
            method.put("wallet", "1")
            method.put("paylater", "0")
            options.put("method", method)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", binding.email.text)
            prefill.put("contact", binding.phoneNumber.text)

            options.put("prefill", prefill)

//            options.put("callback_url","http://192.168.0.125/razorpay/verify_1.php?a=5")
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    companion object {
        fun newInstance(): bookingConfirmation_BS {
            return bookingConfirmation_BS()
        }
    }

    private fun bookingT_C() {
        val builder = AlertDialog.Builder(context!!, R.style.AlertDialog)
        builder.setPositiveButton(
            context!!.getString(R.string.Accept)
        ) { _, _ ->
//            model.payFees(decorView!!, progress)
            startPayment()
        }
        builder.setNegativeButton(
            context!!.getString(R.string.dismiss)
        ) { dialog, _ -> dialog.dismiss() }
        builder.setTitle("TERMS AND CONDITIONS -")
        builder.setMessage(
            """
            
            - A maximum of 5 members per booking per badminton court is admissible
            
            - Non Marking Shoes compulsory for Badminton. Shoes must be worn after entering the facility.
            
            - Barefoot play is strictly prohibited.
            
            - Sports equipment not available on rent.
            """.trimIndent()
        )
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window!!.windowStyle
        alertDialog.setOnDismissListener { dialogInterface: DialogInterface? ->
            binding.progress.visibility = View.GONE
        }
    }
}