package com.geek.adminpanel.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geek.adminpanel.Repository.MainRepository

class MainViewModel : ViewModel() {

    lateinit var currentPrice: MutableLiveData<String>
    val repository = MainRepository()

    fun getCurrentCourtPrice(): LiveData<String> {
        if (currentPrice == null) {
            currentPrice = MutableLiveData()
//            currentPrice = repository.getCurrentCourtPrice()

        }
        return currentPrice
    }

}