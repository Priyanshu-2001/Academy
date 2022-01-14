package com.geek.adminpanel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.geek.adminpanel.databinding.FragmentSignInBinding

class SignIn : Fragment(R.layout.fragment_sign_in) {
    lateinit var navController: NavController
    lateinit var binding: FragmentSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding = FragmentSignInBinding.bind(view)
        binding.signInBtn.setOnClickListener {
            navController.navigate(R.id.action_signIn2_to_adminDashboard2)
        }

    }
}