package com.geek.adminpanel.ViewModel

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

    val service = AdminCoachRepository()

    fun getUserList(): LiveData<ArrayList<UserData>> {
        if (data == null) {
            getAdminList()
            getCoachesList()
            finalData = service.getUserList()
            data = MutableLiveData()
        }
        return finalData
    }

    fun getCoachesList(): LiveData<List<String>> {
        if (coachData == null) {
            finalCoachList = service.getCoachesList()
            coachData = MutableLiveData()
        }
        return finalCoachList
    }

    fun getAdminList(): LiveData<List<String>> {
        if (admindata == null) {
            finalAdminList = service.getAdminList()
            admindata = MutableLiveData()
        }
        return finalAdminList
    }

    fun combineList(): MutableLiveData<ArrayList<UserData>> {
        val tempData = MutableLiveData<ArrayList<UserData>>()
        val templist = finalData.value
        templist?.forEach {
            if (finalAdminList.value!!.contains(it.userID)) {
                it.enableAdmin()
            }
            if (finalCoachList.value!!.contains(it.userID)) {
                it.enableCoach()
            }
        }
        tempData.value = templist!!
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
//        data = null
//        admindata = null
//        coachData = null
//        getAdminList()
//        getCoachesList()
//        getUserList()
        combineList()
    }
}