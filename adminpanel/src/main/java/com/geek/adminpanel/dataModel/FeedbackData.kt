package com.geek.adminpanel.dataModel

data class FeedbackData(
    val feedback: String,
    val uid: String,
    val timestamp: Any,
    val starred: Boolean,
    val new: Boolean,
    val feedbackID: String
)