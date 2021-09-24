package com.sports.oscaracademy.data

data class BookingData(
    val name: String?,
    val userID: String,
    val phoneNumber: String,
    val email: String,
    val courtID: ArrayList<String>?
)
