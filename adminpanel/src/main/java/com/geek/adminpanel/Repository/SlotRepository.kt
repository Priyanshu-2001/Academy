package com.geek.adminpanel.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.geek.adminpanel.dataModel.SlotData
import com.google.firebase.database.FirebaseDatabase


class SlotRepository {

    private val FinalData = MutableLiveData<ArrayList<SlotData>>()

    fun getSlots(): MutableLiveData<ArrayList<SlotData>> {
        val data = ArrayList<SlotData>()
        FirebaseDatabase
            .getInstance()
            .reference
            .child("schedule_slots")
            .get()
            .addOnSuccessListener {
                it.children.forEach { slotData ->
                    data.add(SlotData(slotData.key, slotData.value as String))
                }
                FinalData.value = data
            }.addOnFailureListener {
                Log.e("TAG", "getSlots: failed ${it}")
            }

        return FinalData
    }

    fun delete(pos: Int) {
        val mPostReference = FirebaseDatabase.getInstance().reference
            .child("schedule_slots")
            .child(pos.toString())
        mPostReference.removeValue()
    }

    fun getTotalCourt(): MutableLiveData<String> {
        val temp = MutableLiveData<String>()
        FirebaseDatabase
            .getInstance()
            .reference
            .child("totalCourts")
            .get().addOnSuccessListener {
                temp.value = it.value.toString()
            }
        return temp
    }

    fun updateTotalCourt(courtCount: String) {
        FirebaseDatabase
            .getInstance()
            .reference
            .child("totalCourts")
            .setValue(courtCount)
    }

    fun addNewSlot(id: String, timing: String) {
        val data = HashMap<String, Any>()
        data[id] = timing
        FirebaseDatabase
            .getInstance()
            .reference
            .child("schedule_slots")
            .updateChildren(data)
            .addOnSuccessListener {
                Log.e("TAG", "addNewSlot sucess: $data")
            }
            .addOnFailureListener {
                Log.e("TAG", "getSlots: failed ${it}")
            }
    }
}