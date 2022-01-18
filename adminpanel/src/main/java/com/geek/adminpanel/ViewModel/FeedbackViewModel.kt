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

    fun star(pos: Int): Int {
        return if (!finalData.value!![pos].starred) {
            repository.star(finalData.value!![pos])
            1
        } else
            -1
    }

    fun delete(pos: Int) {
        repository.delete(finalData.value!![pos])
    }

    fun unStar(pos: Int): Int {
        return if (finalData.value!![pos].starred) {
            repository.unStar(finalData.value!![pos])
            1
        } else
            -1
    }
}