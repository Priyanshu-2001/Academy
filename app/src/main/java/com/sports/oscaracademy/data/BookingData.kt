package com.sports.oscaracademy.data

data class BookingData(
    val name: String?,
    val userID: String,
    val phoneNumber: String,
    val email: String,
    val courtID: HashMap<String, ArrayList<String>>? //slotId, list of court ID Booked
)
