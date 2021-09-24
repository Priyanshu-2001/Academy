package com.sports.oscaracademy.data

data class BookedDATA(
    var slotID: String,
    var unitBooking: ArrayList<bookedUnitData>,
    var courtID: String,
    var totalCourtBooked: Long
)
