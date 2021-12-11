package com.sports.oscaracademy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sports.oscaracademy.service.BookingHistoryService
import com.sports.oscaracademy.service.BookingListData

class PayAndPlayHistoryViewModel : ViewModel() {
    var historyData: MutableLiveData<ArrayList<BookingListData>>? = null
    fun getBookingHistoryData(): MutableLiveData<ArrayList<BookingListData>>? {
        if (historyData == null) {
            historyData = BookingHistoryService().getListOfBooking()
        }
        return historyData
    }
}