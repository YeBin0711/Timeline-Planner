package com.example.timelineplanner.model

import com.example.timelineplanner.Alarm
import com.example.timelineplanner.Repeat
import com.example.timelineplanner.Time
import java.security.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
/*
class ItemData {
    var dayTitle: String ? =null
    //var daycolor: Int ? = null
    //var dayIcon: Int ? = null
    var firstTime: com.google.firebase.Timestamp ? =null
    var lastTime: com.google.firebase.Timestamp ? =null
    //var date: LocalDate ?=null
    var dayMemo: String ? =null
    //var dayshow: Boolean ?= null
    //var dayRepeat: Repeat ?=null
    //var dayAlarm: Alarm ?= null
}*/

class ItemData(
    val daytitle: String = "",
    val dayicon: Int = 0,
    val firstTime: Time = Time("", ""),
    val lastTime: Time = Time("", ""),
    val daymemo: String = ""
)

class Time(
    val hour: String = "",
    val minute: String = ""
)
