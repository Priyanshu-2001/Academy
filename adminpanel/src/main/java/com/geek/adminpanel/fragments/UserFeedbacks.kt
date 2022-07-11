package com.geek.adminpanel.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geek.adminpanel.R
import com.geek.adminpanel.ViewModel.FeedbackViewModel
import com.geek.adminpanel.adapter.FeedBackInterface
import com.geek.adminpanel.adapter.FeedbackAdapter
import com.geek.adminpanel.databinding.FeedbackFragmentBinding

class UserFeedbacks : Fragment(R.layout.feedback_fragment), FeedBackInterface {
    lateinit var binding: FeedbackFragmentBinding
    lateinit var viewModel: FeedbackViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FeedbackFragmentBinding.bind(view)
        (context as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val actionBar = (context as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        Toast.makeText(context, "Press and hold for more options", Toast.LENGTH_LONG).show()

        viewModel = ViewModelProvider(this)[FeedbackViewModel::class.java]

        viewModel.getFeedBacks().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                Toast.makeText(context, "Nothing to Show", Toast.LENGTH_SHORT).show()
            } else {
                it.sortByDescending { data -> data.starred }
                val adapter = FeedbackAdapter(it, this)
                binding.feedbackRcv.adapter = adapter
            }
        }
    }

    override fun deleteItem(pos: Int) {
        viewModel.delete(pos)
    }

    override fun star(pos: Int) {
        val res = viewModel.star(pos)
        if (res == -1) {
            Toast.makeText(context, "Already Starred", Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateToProfile(adapterPosition: Int) {
//        Navigation.findNavController(binding.root).navigate(
//            R.id.action_userFeedbacks_to_profileFragment,
//            bundleOf(Pair("uid", data[adapterPosition].uid))
//        )
    }

    override fun unStar(pos: Int) {
        val res = viewModel.unStar(pos)
        if (res == -1) {
            Toast.makeText(context, "Can't Perform This Function", Toast.LENGTH_SHORT).show()
        }
    }
}