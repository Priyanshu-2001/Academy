package com.geek.adminpanel.dataModel

data class UserData(
    val name: String,
    val phone: String,
    val userID: String,
    val email: String
) {
    var rollNo: Int = 0
    fun setRoll(roll: Int) {
        rollNo = roll
    }

    var isCoach = false
    var isAdmin = false

    fun enableAdmin() {
        isAdmin = true
    }

    fun enableCoach() {
        isCoach = true
    }

}

