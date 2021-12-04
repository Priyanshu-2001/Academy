package com.sports.oscaracademy.service

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.sports.oscaracademy.dialog.dialogs

class SessionDetailsService {
    private val sessionData = MutableLiveData<SessionData>()

    private fun getSchedule(session: String) {
        Log.e("TAG", "getSchedule: $session")
        FirebaseDatabase.getInstance().reference
            .child("session")
            .child(session)
            .get()
            .addOnSuccessListener {
                sessionData.value = SessionData(
                    it.child("schedule").value.toString(),
                    it.child("fees").value.toString(),
                    session
                )
            }
    }

    fun getSpecificSchedule(context: Context): MutableLiveData<SessionData> {
        val pref = context.getSharedPreferences("tokenFile", MODE_PRIVATE)
        if (pref.getString("session", "-1") != "-1") {
            Log.e("TAG", "getSchedule 1: ${pref.getString("session", "-1")}")
            getSchedule(pref.getString("session", "-1")!!)
        } else {
            FirebaseFirestore.getInstance()
                .collection("students")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val Session = documentSnapshot.getString("session")
                    val edit = pref.edit()
                    edit.putString("session", Session)
                    edit.apply()
                    Session?.let {
                        Log.e("TAG", "getSpecificSchedule: $it")
                        getSchedule(session = it)
                    }
                }.addOnFailureListener { e ->
                    val dialog = dialogs()
                    dialog.displayDialog(e.localizedMessage, context)
                }
        }
        return sessionData
    }
}

data class SessionData(val schedule: String, val fees: String, val session: String)