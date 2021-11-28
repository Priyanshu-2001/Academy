package com.sports.oscaracademy.viewModel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sports.oscaracademy.Application.MyApplication
import com.sports.oscaracademy.data.BookedDATA
import com.sports.oscaracademy.data.BookingData
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.service.BookingService
import com.sports.oscaracademy.service.GetSlots
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Pay_playViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "PAY_PLAY_VIEW_MODEL"

    private var selectedDate = MutableLiveData<Date>()
    private var selectedslots = MutableLiveData<ArrayList<SlotsData>>()
    private var totalslots = MutableLiveData<ArrayList<SlotsData>>()
    private var bookingService: BookingService = BookingService() //currently booking data
    private var bookingData = MutableLiveData<BookingData>()
    private var selectedCourts = MutableLiveData<Int>()
    private var BookedData = MutableLiveData<ArrayList<BookedDATA>>() //list of already bookedCourt
    private var totalCourts = MutableLiveData<String>()
    private var minCourtAvailableList = MutableLiveData<HashMap<String, Long>>()
    private var currentCourtPrice = 350
    private val remoteConfig = (app as MyApplication).remoteConfig
    private var name = String()
    private var email = String()
    private var phoneNumber = String()

    fun getBookedData(): MutableLiveData<ArrayList<BookedDATA>> {
        return BookedData
    }

    fun setBookedData() {
        val d = selectedDate.value

        val formatter = DecimalFormat("00")
        val date = d?.year?.plus(1900).toString() + "-" + formatter.format(
            d?.month?.plus(1)?.toLong()
        ) + "-" + formatter.format(d?.date?.toLong())
        BookedData = bookingService.getAllBookedDATA(date)
    }

    fun getSelectedCourtsCount(): MutableLiveData<Int> {
        return selectedCourts
    }

    fun setSelectedCourtCount(selectedCourtCount: Int) {
        selectedCourts.postValue(selectedCourtCount)
    }

    fun setSelectedDate(date: Date) {
        selectedDate.value = date
    }

    fun getSelectedDate(): MutableLiveData<Date> {
        return selectedDate
    }

    fun getTotalSlots(): MutableLiveData<ArrayList<SlotsData>> {

        return totalslots
    }

    fun getCourtDataForBooking(): HashMap<String, ArrayList<String>> { //court to be booked
        val map = HashMap<String, ArrayList<String>>()
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
        return map
    }

    fun setTotalSlots() {
        totalslots = GetSlots().getData()

    }

    fun setSelectedSLots(arr: ArrayList<SlotsData>) {
        selectedslots.value = arr
    }

    fun getSelectedSlots(): MutableLiveData<ArrayList<SlotsData>> {
        return selectedslots
    }

    fun setCurrentBookingData() {
        bookingData.value = FirebaseAuth.getInstance().currentUser?.let {
            FirebaseAuth.getInstance().currentUser?.email?.let { it1 ->
                FirebaseAuth.getInstance().currentUser?.phoneNumber?.let { it2 ->
                    BookingData(
                        name,
                        it.uid,
                        phoneNumber,
                        it1,
                        getCourtDataForBooking() //todo here the Arraylist of courtID and slotID is provided
                    )
                }
            }
        }
    }

    fun getCurrentBookingData(): MutableLiveData<BookingData> {
        return bookingData
    }

    fun payFees(v: View, progress: SpinKitView) {
        setCurrentBookingData()
        if (bookingData.value!!.courtID != null) {
            if (bookingData.value!!.courtID?.size != 0) {
                bookingService.BookCourt(selectedDate, selectedslots, bookingData, progress)
            } else {
                val snackbar =
                    Snackbar.make(v, "Hey... U didn't select any Court", Snackbar.LENGTH_LONG)
                        .setAnimationMode(ANIMATION_MODE_SLIDE)
                snackbar.view.setPadding(5, 0, 5, 20)
                snackbar.show()
            }
        } else {
            val snackbar =
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

    fun getCheckOutPrice(): CharSequence {
        return ((getSelectedSlots().value?.let { getSelectedCourtsCount().value?.times(it.count()) }
            ?: -1) * currentCourtPrice).toString()
    }

    fun setSingleCourtPrice() {
        currentCourtPrice = remoteConfig.getValue("courtPrice").asLong().toInt()
    }

    fun getSingleCourtPrice(): Int {
        return currentCourtPrice
    }

    fun setCurrentBookingUserDetails(text: String, text1: String, text2: String) {
        name = text1
        phoneNumber = text
        email = text2
        updateDataToPref(name, phoneNumber, email)
    }

    private fun updateDataToPref(name: String, phoneNumber: String, email: String) {
        val pref = app.getSharedPreferences("tokenFile", MODE_PRIVATE).edit()
        pref.putString("paymentName", name)
        pref.putString("paymentPhoneNumber", phoneNumber)
        pref.putString("paymentEmail", email)
        pref.apply()
    }


}