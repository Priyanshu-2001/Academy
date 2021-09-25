package com.sports.oscaracademy.viewModel

import android.app.Application
import android.util.Log
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
    private var selectedCourts = MutableLiveData<Int>()
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

    fun getSelectedCourtsCount(): MutableLiveData<Int> {
        return selectedCourts
    }

    fun setSelectedCourtCount(selectedCourtCount: Int) {
        selectedCourts.postValue(selectedCourtCount)
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

    fun getCourtDataForBooking(): HashMap<String, ArrayList<String>> { //court to be booked
        var temp = ArrayList<Int>()
        var map = HashMap<String, ArrayList<String>>()

//        for(i in 1..selectedCourts.value?.toInt()!!){
//
//        }

        selectedslots.value?.forEach { selectedSlotData ->
            val tempAvailableCourtList = getMinCourtList().value?.get(selectedSlotData.slotID)
//            Log.e("TAG", "getCourtDataForBooking: CourtList $tempAvailableCourtList" )
            if (tempAvailableCourtList != null) {
//                tempAvailableCourtList += 1
                val list = ArrayList<String>()
                Log.e("TAG", "getCourtDataForBooking: CourtList total " + totalCourts.value)
                Log.e("TAG", "getCourtDataForBooking: CourtList selected " + selectedCourts.value)
                val start = (totalCourts.value?.toInt())?.minus(tempAvailableCourtList)?.plus(1)
                val end = start?.plus(selectedCourts.value!!)?.minus(1)
                for (r in start!!..end!!) {
                    list.add(r.toString())
                }
                map.put(selectedSlotData.slotID, list)
                Log.e("TAG", "getCourtDataForBooking: $map")
            }

//            map[selectedSlotData.slotID] = tempAvailableCourtList

        }

//        getMinCourtList().value?.forEach {
//            // i have slotID and courtaVAILABLE
//            var temp_booked = (totalCourts.value?.toInt()?.minus(it.value))
//
//            if (temp_booked != null) {
//                temp_booked += 1
//                val list = ArrayList<String>()
//                for(r in temp_booked..totalCourts.value?.toInt()!!){
//                    list.add(r.toString())
//                }
//                map.put(it.key,list)
//            }
//        }
        return map
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
                        it.uid,
                        it2,
                        it1,
                        getCourtDataForBooking() //todo here the Arraylist of courtID and slotID is needed
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

    fun getMinCourtList(): MutableLiveData<HashMap<String, Long>> { // gives minimum court booked with their slot id
        return minCourtAvailableList
    }

}