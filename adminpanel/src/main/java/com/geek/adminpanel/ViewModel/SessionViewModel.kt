package com.geek.adminpanel.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geek.adminpanel.Repository.SessionRepository
import com.geek.adminpanel.dataModel.SessionData

class SessionViewModel(val app: Application) : AndroidViewModel(app) {
    private var SessionData = MutableLiveData<ArrayList<SessionData>>()
    private var tempSessionData: MutableLiveData<ArrayList<SessionData>>? = null
    private val repository = SessionRepository()
    fun getSessionDetails(): LiveData<ArrayList<SessionData>> {
        if (tempSessionData == null) {
            tempSessionData = MutableLiveData<ArrayList<SessionData>>()
            SessionData = repository.getSessionList()
        }
        return SessionData
    }

    fun saveChanges(originalData: SessionData, newData: SessionData) {
        if (originalData == newData) {
            Toast.makeText(app, "No Difference in Data", Toast.LENGTH_SHORT).show()
        } else {
            repository.updateSessionData(newData)
            tempSessionData = null
            getSessionDetails()
        }
    }

    fun delete(id: String) {
        repository.delete(id)
        tempSessionData = null
        getSessionDetails()
    }
}