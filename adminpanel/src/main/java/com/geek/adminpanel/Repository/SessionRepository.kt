package com.geek.adminpanel.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.geek.adminpanel.dataModel.SessionData
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class SessionRepository {
    private val finalData = MutableLiveData<ArrayList<SessionData>>()
    fun getSessionList(): MutableLiveData<ArrayList<SessionData>> {
        val tempData = ArrayList<SessionData>()

        FirebaseDatabase.getInstance()
            .reference
            .child("session")
            .orderByKey()
            .get()
            .addOnSuccessListener {
                var fees: String = ""
                var time: String = ""

                it.children.forEach { sessionData ->
                    sessionData.children.forEach { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.key == "fees") {
                                fees = dataSnapshot.value.toString()
                            }
                            if (dataSnapshot.key == "schedule") {
                                time = dataSnapshot.value.toString()
                            }
                        }
                    }
                    tempData.add(
                        SessionData(
                            sessionId = sessionData.key.toString(),
                            sessionFees = fees,
                            sessionTiming = time
                        )
                    )
                }
                finalData.value = tempData
            }

        return finalData
    }

    fun updateSessionData(newData: SessionData) {
        val map = HashMap<String, Any>()
        Log.e("TAG", "updateSessionData: sessionId ${newData.sessionId}")
        map[newData.sessionId] =
            hashMapOf(Pair("fees", newData.sessionFees), Pair("schedule", newData.sessionTiming))

        FirebaseDatabase.getInstance()
            .reference
            .child("session")
            .updateChildren(map)
            .addOnSuccessListener {
                Log.e("TAG", "updateSessionData: $map")

                Log.e("TAG", "updateSessionData: sucess")
            }.addOnFailureListener {
                Log.e("TAG", "updateSessionData: $map")
                Log.e("TAG", "updateSessionData: fail $it")
            }
    }

    fun delete(id: String) {
        val mPostReference = FirebaseDatabase.getInstance().reference
            .child("session")
            .child(id)
        mPostReference.removeValue()
    }
}
