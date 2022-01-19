package com.sports.oscaracademy.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sports.oscaracademy.data.Studentdata
import com.sports.oscaracademy.service.studentsList

class AdminStudentsViewModel(app: Application) : AndroidViewModel(app) {
    private var usersData: MutableLiveData<ArrayList<Studentdata>>? = null
    private var studentData: MutableLiveData<ArrayList<Studentdata>>? = null
    private val service = studentsList(app.applicationContext)

    fun getUserData(): MutableLiveData<ArrayList<Studentdata>>? {
        if (usersData == null) {
            usersData = service.users
        }
        return usersData
    }

    fun refreshData() {
        usersData = null
        studentData = null
    }

    fun getStudentData(): MutableLiveData<ArrayList<Studentdata>>? {
        if (studentData == null) {
            studentData = service.students
        }
        return studentData
    }

    fun getStudentDataFiltered(
        filterType: String,
        filter: Any,
        isInActive: Boolean
    ): MutableLiveData<ArrayList<Studentdata>>? {
        if (studentData == null) {
            studentData = service.students
        }
        val temp = studentData?.value
        return filterProcess(temp!!, filterType, filter, isInActive)

    }

    fun getUserDataFiltered(
        filterType: String,
        filter: Any,
        isInActive: Boolean
    ): MutableLiveData<ArrayList<Studentdata>>? {
        if (usersData == null) {
            usersData = service.users
        }
        val temp = usersData!!.value
        return filterProcess(temp!!, filterType, filter, isInActive)
    }

    private fun filterProcess(
        data: ArrayList<Studentdata>,
        filterType: String,
        filter: Any,
        inActiveFilter: Boolean
    ): MutableLiveData<ArrayList<Studentdata>> {

        var temp: List<Studentdata>? = null

        Log.e("TAG", "filterProcess: out $filter + $filterType")
        if (inActiveFilter) {
            temp = data.filter {
                if (it.memberShip != null)
                    it.memberShip.toString().contains("inactive", true)
                else
                    false
            }
        }
        if (filterType == "name") {
            if (temp == null) {
                temp = data
            }
            Log.e("TAG", "filterProcess: name+ $filter")
            temp = temp.filter {
                if (it.name != null)
                    it.name.toString().contains(filter.toString(), true)
                else
                    false
            }

        }
        if (filterType == "phone number") {
            if (temp == null) {
                temp = data
            }
            temp = temp.filter {
                Log.e("TAG", "filterProcess: ${it.phone}")
                if (it.phone != null)
                    it.phone.toString().contains(filter.toString(), true)
                else
                    false
            }
        }


        if (filterType == "email") {
            if (temp == null) {
                temp = data
            }
            temp = temp.filter {
                if (it.email != null)
                    it.email.toString().contains(filter.toString(), true)
                else
                    false
            }
        }

        if (filterType == "RollNo") {
            temp = data.filter {
                if (temp == null) {
                    temp = data
                }
                if (it.rollno != null)
                    it.rollno.toString().contains(filter.toString(), true)
                else
                    false
            }
        }
        if (temp == null)
            temp = data
        if (data == null)
            temp = ArrayList()
        return MutableLiveData(temp as ArrayList<Studentdata>)
    }


}