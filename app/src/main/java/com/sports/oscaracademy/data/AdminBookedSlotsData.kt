package com.sports.oscaracademy.data

data class AdminBookedSlotsData(
    val slotsID: List<String>,
    val singleSlotsData: ArrayList<SingleSlotData>
)

data class SingleSlotData(
    val slotID: String,
    val courtBooked: List<String>, // list of booked court id
    val courtData: ArrayList<SingleCourtBookingData?> //list of data of booked Court
)

data class SingleCourtBookingData(
    val otp: Long? = null,
    val referenceID: String? = "",
    val userID: String? = ""
)