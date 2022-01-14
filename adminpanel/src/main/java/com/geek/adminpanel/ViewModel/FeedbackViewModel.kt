package com.geek.adminpanel.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geek.adminpanel.Repository.FeedbackRepository
import com.geek.adminpanel.dataModel.FeedbackData

class FeedbackViewModel : ViewModel() {
    private var tempFeedbackData: MutableLiveData<FeedbackData>? = null
    lateinit var finalData: MutableLiveData<ArrayList<FeedbackData>>
    private val repository = FeedbackRepository()
    fun getFeedBacks(): MutableLiveData<ArrayList<FeedbackData>> {
        if (tempFeedbackData == null) {
            tempFeedbackData = MutableLiveData()
            finalData = repository.getFeedBack()
        }
        return finalData
    }

    fun star(pos: Int) {
        repository.star(finalData.value!![pos])
    }

    fun delete(pos: Int) {
        repository.delete(finalData.value!![pos])
    }
}