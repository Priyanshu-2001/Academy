package com.sports.oscaracademy.utils

import android.app.Activity
import android.widget.Toast
import com.razorpay.Checkout
import com.sports.oscaracademy.R
import org.json.JSONObject

class FeesPaymentHelper {

    fun startPayment(
        activity: Activity,
        fees: Int,
        email: String,
        name: String,
        phoneNumber: String
    ) {
        val co = Checkout()
        co.setKeyID("rzp_test_anVS19J2dBeADE")
        try {
            val options = JSONObject()
            options.put("name", activity.getString(R.string.app_name))
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#0B7E93")
            options.put("currency", "INR")
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            val finalPayment = Integer.valueOf(fees) * 100
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
            prefill.put("email", email)
            prefill.put("contact", phoneNumber)

            options.put("prefill", prefill)

//            options.put("callback_url","http://192.168.0.125/razorpay/verify_1.php?a=5")
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}