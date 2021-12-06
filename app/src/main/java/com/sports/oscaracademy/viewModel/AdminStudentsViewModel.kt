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

    fun getStudentData(): MutableLiveData<ArrayList<Studentdata>>? {
        if (studentData == null) {
            studentData = service.students
        }
        return studentData
    }

    fun getStudentDataFiltered(
        filterType: String,
        filter: Any
    ): MutableLiveData<ArrayList<Studentdata>>? {
        if (studentData == null) {
            studentData = service.students
        }
        val temp = studentData?.value
        return filterProcess(temp, filterType, filter)
//        return service.filteredStudentList(filterType,filter)
    }

    fun getUserDataFiltered(
        filterType: String,
        filter: Any
    ): MutableLiveData<ArrayList<Studentdata>>? {
        if (usersData == null) {
            usersData = service.users
        }
        val temp = usersData?.value
        return filterProcess(temp, filterType, filter)
//        return userDataFiltered
    }

    private fun filterProcess(
        data: ArrayList<Studentdata>?,
        filterType: String,
        filter: Any
    ): MutableLiveData<ArrayList<Studentdata>>? {

        var temp: List<Studentdata>? = null

        if (filterType == "name") {
            temp = data?.filter {
                it.name.toString().contains(filter.toString(), true)
            }

        }
        if (filterType == "phone number") {
            temp = data?.filter {
                it.phone.toString().contains(filter.toString(), true)
            }
        }


        if (filterType == "email") {
            temp = data?.filter {
                it.email.toString().contains(filter.toString(), true)
            }
        }


        if (filterType == "RollNo") {
            temp = data?.filter {
                it.rollno.toString().contains(filter.toString(), true)
            }
        }
        temp?.forEach {
            Log.e("TAG", "filterProcess: ${it.name}")
        }
        return MutableLiveData(temp as ArrayList<Studentdata>)
    }


}