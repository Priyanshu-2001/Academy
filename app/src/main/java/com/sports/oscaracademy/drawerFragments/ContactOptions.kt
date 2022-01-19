package com.sports.oscaracademy.drawerFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.FragmentContactOptionsBinding

class ContactOptions : BottomSheetDialogFragment() {
    lateinit var binding: FragmentContactOptionsBinding
    lateinit var phone: String
    lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.apply {
            phone = getString(PHONE_ARG, "123")
            name = getString(NAME_ARG, "Unknown")
        }
        return inflater.inflate(R.layout.fragment_contact_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactOptionsBinding.bind(view)

        binding.textView15.text = "Contact ${name}"
        if (phone.length != 10) {
            Toast.makeText(
                context,
                "Looks like there is some issue in contact Details please dial it manually",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.call.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }
        binding.sms.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)))
        }
        binding.whatsapp.setOnClickListener {
            val text =
                "Hey $name..\n\nLooks like You Forget to pay this month Fees. If so please go to fees section in Our Android App and just pay over there else you can even Directly deposit at reception\n\n\nif you feel the above info is wrong please contact reception\n\ncheers\nGreetings From Oscar Academy"
            val url = "https://wa.me/+91$phone?text=$text"
            Log.e("TAG", "onViewCreated: $url")
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val NAME_ARG = "param1"
        private const val PHONE_ARG = "editable"

        @JvmStatic
        fun newInstance(name: String, phone: String): ContactOptions {
            val fragment = ContactOptions()
            val args = Bundle()
            args.putString(NAME_ARG, name)
            args.putString(PHONE_ARG, phone)
            fragment.arguments = args
            return fragment
        }
    }
}