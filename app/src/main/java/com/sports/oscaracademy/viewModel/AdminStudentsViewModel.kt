package com.sports.oscaracademy.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sports.oscaracademy.data.Studentdata
import com.sports.oscaracademy.service.studentsList
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

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
        isInActive: Boolean,
        isBatchWiseBtnActive: Boolean
    ): MutableLiveData<ArrayList<Studentdata>>? {
        if (studentData == null) {
            studentData = service.students
        }
        val temp = studentData?.value
        return filterProcess(temp!!, filterType, filter, isInActive, isBatchWiseBtnActive)

    }

    fun getUserDataFiltered(
        filterType: @NotNull String,
        filter: @NotNull Any,
        isInActive: Boolean,
        isBatchWiseBtnActive: Boolean
    ): @Nullable MutableLiveData<java.util.ArrayList<Studentdata>>? {
        if (usersData == null) {
            usersData = service.users
        }
        val temp = usersData!!.value
        return filterProcess(temp!!, filterType, filter, isInActive, isBatchWiseBtnActive)
    }

    var temp: List<Studentdata>? = null
    private fun filterProcess(
        data: ArrayList<Studentdata>,
        filterType: String,
        filter: Any,
        inActiveFilter: Boolean,
        isBatchWiseBtnActive: Boolean
    ): MutableLiveData<ArrayList<Studentdata>> {


        temp = data
        Log.e("TAG", "filterProcess: out $filter + $filterType $inActiveFilter")
        if (inActiveFilter) {
            temp = data.filter {
                if (it.memberShip != null)
                    it.memberShip.toString().contains("inactive", true)
                else
                    false
            }
        }
        if (temp == null) {
            temp = data
        }
        if (isBatchWiseBtnActive) {

            temp = temp?.sortedBy { it.session }
        }


        if (filterType == "name") {
            Log.e("TAG", "filterProcess: name+ $filter")
            temp = temp?.filter {
                if (it.name != null)
                    it.name.toString().contains(filter.toString(), true)
                else
                    false
            }

        }
        if (filterType == "phone number") {
            temp = temp?.filter {
                Log.e("TAG", "filterProcess: ${it.phone}")
                if (it.phone != null)
                    it.phone.toString().contains(filter.toString(), true)
                else
                    false
            }
        }


        if (filterType == "email") {
            temp = temp?.filter {
                if (it.email != null)
                    it.email.toString().contains(filter.toString(), true)
                else
                    false
            }
        }

        if (filterType == "RollNo") {
            temp = data.filter {
                if (it.rollno != null)
                    it.rollno.toString().contains(filter.toString(), true)
                else
                    false
            }
        }
        if (data == null)
            temp = ArrayList()
        return MutableLiveData(temp as ArrayList<Studentdata>)
    }


}