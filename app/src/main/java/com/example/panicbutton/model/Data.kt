package com.example.panicbutton.model

import com.google.firebase.database.PropertyName

data class User (
    val name: String = "",
    val houseNumber: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val note: String = "",

    @get:PropertyName("profileImage") //getter
    @set:PropertyName("profileImage") //setter
    var imageProfile: String = "",

    @get:PropertyName("coverImage")
    @set:PropertyName("coverImage")
    var coverImage: String = ""
)

data class MonitorRecord(
    val id: String = "",
    val name: String = "",
    val houseNumber: String = "",
    val priority: String = "",
    val status: String = "",
    val message: String = "",
    val time: String = ""
)

data class OnBoardingData(
    val image: Int,
    val title: String,
    val desc: String
)

