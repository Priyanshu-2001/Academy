package com.sports.oscaracademy.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.sports.oscaracademy.data.SlotsData

class getSlots {
    var dataList: MutableLiveData<ArrayList<SlotsData>> = MutableLiveData()

    fun getData(): MutableLiveData<ArrayList<SlotsData>> {
        FirebaseDatabase.getInstance()
            .reference
            .child("schedule_slots")
            .get()
            .addOnSuccessListener {
                val temp: ArrayList<SlotsData> = ArrayList()
                it.children.forEach {
                    val data: String? = it.getValue(String::class.java)
                    if (data != null) {
                        val temp1: SlotsData = SlotsData(data, it.key!!)
                        temp.add(temp1)
                        Log.e("TAG", "getData: called")
                    }
                }
                dataList.value = temp
            }.addOnFailureListener {

            }
        return dataList
    }
}