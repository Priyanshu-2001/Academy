package com.geek.adminpanel.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.geek.adminpanel.dataModel.UserData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class AdminCoachRepository {
    private val store = FirebaseFirestore.getInstance()

    fun getUserList(): MutableLiveData<ArrayList<UserData>> {
        val users = MutableLiveData<ArrayList<UserData>>()
        store.collection("user")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val tempData: ArrayList<UserData> = ArrayList<UserData>()
                    var name: String
                    var phone: String
                    var RollNo: Int?
                    var userId: String
                    var email: String
                    try {
                        for (i in task.result.documents.indices) {
                            val snap = task.result.documents[i]
                            name = snap.getString("name").toString()
                            phone = snap.getString("phone number").toString()
                            userId = snap.getString("userID").toString()
                            email = snap.getString("email").toString()
                            val data = UserData(name, phone, userId, email)
                            RollNo = try {
                                snap.get("RollNo", Int::class.java)
                            } catch (e: Exception) {
                                null
                            }
                            if (RollNo != null) {
                                data.setRoll(RollNo)
                            }
                            tempData.add(data)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    getAdminList()
//                    getCoachesList()
                    users.value = tempData
                }
            }
        return users
    }

    fun getAdminList(): MutableLiveData<List<String>> {
        val adminList = MutableLiveData<List<String>>()
        store
            .collection("chatResponders")
            .document("Admin")
            .get()
            .addOnCompleteListener { task ->
                val document = task.result
                if (document.exists()) {
                    val users = document["adminID"] as List<String>
                    Log.e("TAG", "getAdminList: $users")
                    adminList.value = users
                }
            }
        return adminList
    }

    fun getCoachesList(): MutableLiveData<List<String>> {
        val coachesList = MutableLiveData<List<String>>()
        store
            .collection("chatResponders")
            .document("coaches")
            .get()
            .addOnCompleteListener { task ->
                val document = task.result
                if (document.exists()) {
                    val users = document["coachesList"] as List<String>
                    Log.e("TAG", "getCoachesList: $users")
                    coachesList.value = users
                }
            }
        return coachesList
    }


    fun assignAdmin(userData: UserData) {

        store
            .collection("chatResponders")
            .document("Admin")
            .update("adminID", FieldValue.arrayUnion(userData.userID))

        val map = HashMap<String, Any>()
        map["role"] = "admin_dashboard"
        store
            .collection("userType_private")
            .document(userData.userID)
            .set(map, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful)
                    getAdminList()
            }

    }

    fun assignCoach(userData: UserData) {
        store
            .collection("chatResponders")
            .document("coaches")
            .update("coachesList", FieldValue.arrayUnion(userData.userID))
            .addOnSuccessListener {
                getCoachesList()
            }
    }

    fun removeCoach(userData: UserData) {
        store
            .collection("chatResponders")
            .document("coaches")
            .update("coachesList", FieldValue.arrayRemove(userData.userID))
            .addOnSuccessListener {
                getCoachesList()
            }
    }

    fun removeAdmin(userData: UserData) {
        store
            .collection("chatResponders")
            .document("Admin")
            .update("adminID", FieldValue.arrayRemove(userData.userID))
        val map = HashMap<String, Any>()
        map["role"] = "Student_dashboard"
        store
            .collection("userType_private")
            .document(userData.userID)
            .set(map, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful)
                    getAdminList()
            }
    }

}