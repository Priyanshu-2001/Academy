package com.geek.adminpanel.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.SessionViewModel
import com.geek.adminpanel.dataModel.SessionData
import com.geek.adminpanel.databinding.EditSessionDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditSessionDetails : BottomSheetDialogFragment() {
    lateinit var Bund: Bundle
    lateinit var binding: EditSessionDetailsBinding
    lateinit var viewModel: SessionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            Bund = this
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_session_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SessionViewModel::class.java]
        binding = EditSessionDetailsBinding.bind(view)
        binding.sessionTiming.setText(Bund["time"].toString())
        binding.fees.setText(Bund["fees"].toString())
        binding.sessionID.setText(Bund["id"].toString())
        var originalSessionData = SessionData(
            sessionId = Bund["id"].toString(),
            sessionTiming = Bund["time"].toString(),
            sessionFees = Bund["fees"].toString()
        )

        binding.saveBtn.setOnClickListener {
            viewModel.saveChanges(
                originalSessionData,
                SessionData(
                    sessionId = binding.sessionID.text.toString(),
                    sessionTiming = binding.sessionTiming.text.toString(),
                    sessionFees = binding.fees.text.toString()
                )
            )
        }

        binding.closeBtn.setOnClickListener { dismiss() }
        binding.deleteBtn.setOnClickListener {
            viewModel.delete(Bund["id"].toString())

            originalSessionData = SessionData(
                sessionId = "",
                sessionTiming = "",
                sessionFees = ""
            )
        }
    }

}