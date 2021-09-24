package com.sports.oscaracademy.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sports.oscaracademy.data.BookedDATA
import com.sports.oscaracademy.data.BookingData
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.service.BookingService
import com.sports.oscaracademy.service.getSlots
import org.joda.time.DateTime

class Pay_playViewModel(application: Application) : AndroidViewModel(application) {

    private var selectedDate = MutableLiveData<DateTime>()
    private var selectedslots = MutableLiveData<ArrayList<SlotsData>>()
    private var totalslots = MutableLiveData<ArrayList<SlotsData>>()
    private var bookingService: BookingService = BookingService()
    private var bookingData = MutableLiveData<BookingData>()
    private var selectedCourts = MutableLiveData<ArrayList<String>>()
    private var AllBookedDATA = MutableLiveData<ArrayList<BookedDATA>>()
    private var HouseFullSlots = MutableLiveData<ArrayList<String>>()
    private var BookedData = MutableLiveData<ArrayList<BookedDATA>>()
    private var totalCourts = MutableLiveData<String>()
    private var minCourtAvailableList = MutableLiveData<HashMap<String, Long>>()
    fun getHouseFullSlotsList(): MutableLiveData<ArrayList<String>> {
        return HouseFullSlots
    }

    fun getBookedData(): MutableLiveData<ArrayList<BookedDATA>> {
        return BookedData
    }

    fun setBookedData() {
        BookedData = bookingService.getAllBookedDATA(bookingService.DateFormater(selectedDate))
    }

    fun getSelectedCourts(): MutableLiveData<ArrayList<String>> {
        return selectedCourts
    }

    fun setSelectedCourt(list: ArrayList<String>) {
        selectedCourts.value = list
    }

    fun setSelectedDate(date: DateTime) {
        selectedDate.value = date
    }

    fun getSelectedDate(): MutableLiveData<DateTime> {
        return selectedDate
    }

    fun getTotalSlots(): MutableLiveData<ArrayList<SlotsData>> {

        return totalslots
    }

    fun setTotalSlots() {
        totalslots = getSlots().getData()

    }

    fun setSelectedSLots(arr: ArrayList<SlotsData>) {
        selectedslots.value = arr
    }

    fun getSelectedSlots(): MutableLiveData<ArrayList<SlotsData>> {
        return selectedslots
    }


    fun payFees(v: View) {
        bookingData.value = FirebaseAuth.getInstance().currentUser?.let {
            FirebaseAuth.getInstance().currentUser?.email?.let { it1 ->
                FirebaseAuth.getInstance().currentUser?.phoneNumber?.let { it2 ->
                    BookingData(
                        FirebaseAuth.getInstance().currentUser?.displayName,
                        it.uid, it2, it1, getSelectedCourts().value
                    )
                }
            }
        }
        if (bookingData.value!!.courtID != null) {
            if (bookingData.value!!.courtID?.size != 0)
                bookingService.BookCourt(selectedDate, selectedslots, bookingData)
            else {
                val snackbar =
                    Snackbar.make(v, "Hey... U didn't select any Court", Snackbar.LENGTH_LONG)
                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                snackbar.view.setPadding(5, 0, 5, 15)
                snackbar.show()
            }
//                Snackbar.make(v, "Hey... U didn't select any Court", Snackbar.LENGTH_LONG)
//                    .setAnimationMode(ANIMATION_MODE_SLIDE).show()
        } else {
            var snackbar =
                Snackbar.make(v, "Hey... U didn't select any Court", Snackbar.LENGTH_LONG)
                    .setAnimationMode(ANIMATION_MODE_SLIDE)
            snackbar.view.setPadding(5, 0, 5, 15)
            snackbar.show()
        }
    }

    fun setTotalCourt() {
        totalCourts = BookingService().getTotalCount()
    }

    fun getTotalCourt(): MutableLiveData<String> {
        return totalCourts
    }

    fun setMinCourtList(minCourtAvailableList: HashMap<String, Long>) {
        this.minCourtAvailableList.postValue(minCourtAvailableList)
    }

    fun getMinCourtList(): MutableLiveData<HashMap<String, Long>> {
        return minCourtAvailableList
    }

}