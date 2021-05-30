package com.workfromhome.checkvac.Models

data class CenterModel(
    val centerName: String,
    val centerAddress: String,
    val centerFromTime: String,
    val centerToTime: String,
    val feeType: String,
    val ageLimit: Int,
    var vaccineName: String,
    val availableCapacity: Int
)
