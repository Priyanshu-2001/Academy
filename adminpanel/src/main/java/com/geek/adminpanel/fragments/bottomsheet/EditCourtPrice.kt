package com.geek.adminpanel.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.MainViewModel
import com.geek.adminpanel.databinding.FragmentEditCourtPriceBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditCourtPrice : BottomSheetDialogFragment() {

    lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentEditCourtPriceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_court_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditCourtPriceBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
//        viewModel.getCurrentCourtPrice()
    }
}