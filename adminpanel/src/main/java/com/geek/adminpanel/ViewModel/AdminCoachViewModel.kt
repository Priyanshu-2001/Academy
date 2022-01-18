package com.geek.adminpanel.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geek.adminpanel.Repository.AdminCoachRepository
import com.geek.adminpanel.dataModel.UserData

class AdminCoachViewModel : ViewModel() {
    var data: MutableLiveData<ArrayList<UserData>>? = null
    var admindata: MutableLiveData<List<String>>? = null
    var coachData: MutableLiveData<List<String>>? = null

    var finalData = MutableLiveData<ArrayList<UserData>>()
    var finalAdminList = MutableLiveData<List<String>>()
    var finalCoachList = MutableLiveData<List<String>>()
    private val tempData = MutableLiveData<ArrayList<UserData>>()
    private val service = AdminCoachRepository()

    private fun getUserList(): LiveData<ArrayList<UserData>> {
        if (data == null) {
            getAdminList()
            getCoachesList()
            finalData = service.getUserList()
            data = MutableLiveData()
        }
        return finalData
    }

    private fun getCoachesList(): LiveData<List<String>> {
        if (coachData == null) {
            finalCoachList = service.getCoachesList()
            coachData = MutableLiveData()
        }
        return finalCoachList
    }

    private fun getAdminList(): LiveData<List<String>> {
        if (admindata == null) {
            finalAdminList = service.getAdminList()
            admindata = MutableLiveData()
        }
        return finalAdminList
    }

    fun combineList(): MutableLiveData<ArrayList<UserData>> {

        var temp: ArrayList<UserData>
        coachData = null
        admindata = null
        data = null
        getUserList().observeForever { finalData ->
            temp = finalData
            getAdminList().observeForever { finalAdminList ->
                Log.e("TAG", "combineList: adminList chaned $finalAdminList")
                getCoachesList().observeForever { finalCoachList ->
                    Log.e("TAG", "combineList: coach chaned $finalCoachList")

                    temp.forEach {
                        if (finalAdminList!!.contains(it.userID)) {
                            it.enableAdmin()
                        }
                        if (finalCoachList!!.contains(it.userID)) {
                            it.enableCoach()
                        }
                        Log.e("TAG", "combineList: ${it.isAdmin} ${it.userID} ")

                    }
                    tempData.value = temp
                    temp = finalData
                }
            }
        }
        return tempData
    }

    fun assignAdmin(userData: UserData) {
        service.assignAdmin(userData)
        refreshFullData()
    }

    fun assignCoach(userData: UserData) {
        service.assignCoach(userData)
        refreshFullData()
    }

    fun removeCoach(userData: UserData) {
        service.removeCoach(userData)
        refreshFullData()
    }

    fun removeAdmin(userData: UserData) {
        service.removeAdmin(userData)
        refreshFullData()
    }

    private fun refreshFullData() {
        combineList()
    }

    fun getUserDataFiltered(
        filterType: String,
        filter: Any
    ): MutableLiveData<ArrayList<UserData>> {
        val temp = tempData.value
        return filterProcess(temp, filterType, filter)
    }

    private fun filterProcess(
        data: ArrayList<UserData>?,
        filterType: String,
        filter: Any
    ): MutableLiveData<ArrayList<UserData>> {

        var temp: List<UserData>? = null

        if (filterType == "name") {
            temp = data?.filter {
                if (it.name != null)
                    it.name.toString().contains(filter.toString(), true)
                else
                    false
            }

        }
        if (filterType == "phone number") {
            temp = data?.filter {
                Log.e("TAG", "filterProcess: ${it.phone}")
                if (it.phone != null)
                    it.phone.toString().contains(filter.toString(), true)
                else
                    false
            }
        }


        if (filterType == "email") {
            temp = data?.filter {
                if (it.email != null)
                    it.email.toString().contains(filter.toString(), true)
                else
                    false
            }
        }


        if (filterType == "RollNo") {
            temp = data?.filter {
                if (it.rollNo != null)
                    it.rollNo.toString().contains(filter.toString(), true)
                else
                    false
            }
        }
        return MutableLiveData(temp as ArrayList<UserData>)
    }

}