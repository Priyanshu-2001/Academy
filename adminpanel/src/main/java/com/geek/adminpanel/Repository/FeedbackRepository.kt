package com.geek.adminpanel.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.geek.adminpanel.dataModel.FeedbackData
import com.google.firebase.database.FirebaseDatabase

class FeedbackRepository {

    val finalData = MutableLiveData<ArrayList<FeedbackData>>()

    fun getFeedBack(): MutableLiveData<ArrayList<FeedbackData>> {
        val temp = ArrayList<FeedbackData>()
        val newFeeds = ArrayList<FeedbackData>()
        FirebaseDatabase.getInstance()
            .reference
            .child("feedback")
            .get()
            .addOnSuccessListener { dataSnap ->
                dataSnap.children.forEach { data ->
                    Log.e("TAG", "getFeedBack: ${data.child("timestamp").value}")
                    val d = FeedbackData(
                        feedback = data.child("feedback").value.toString(),
                        uid = data.child("uid").value.toString(),
                        new = data.child("new").value as Boolean,
                        starred = data.child("starred").value as Boolean,
                        timestamp = data.child("timestamp").value as Long,
                        feedbackID = data.key!!
                    )
                    if (data.child("new").value as Boolean) newFeeds.add(d)
                    temp.add(d)
                }
                if (newFeeds.isNotEmpty()) {
                    removeNewStatus(newFeeds)
                }
                temp.reverse()
                finalData.value = temp
            }
        return finalData
    }

    private fun removeNewStatus(newFeeds: ArrayList<FeedbackData>) {
        newFeeds.forEach {
            Log.e("TAG", "removeNewStatus: removing new status ${it.feedback}")
            val map = hashMapOf<String, Any>(Pair("new", false))

            FirebaseDatabase.getInstance()
                .reference
                .child("feedback")
                .child(it.feedbackID)
                .updateChildren(map)
        }
    }

    fun star(feedbackID: FeedbackData) {
        val map = hashMapOf<String, Any>(Pair("starred", true))
        FirebaseDatabase.getInstance()
            .reference
            .child("feedback")
            .child(feedbackID.feedbackID)
            .updateChildren(map)
            .addOnSuccessListener {
                getFeedBack()
            }
    }

    fun delete(feedbackID: FeedbackData) {
        FirebaseDatabase.getInstance()
            .reference
            .child("feedback")
            .child(feedbackID.feedbackID)
            .removeValue()
            .addOnSuccessListener {
                getFeedBack()
            }
    }

}
