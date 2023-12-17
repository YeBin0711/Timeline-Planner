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

class ItemData {
    var dayTitle: String? = null
    var firstTime: com.google.firebase.Timestamp? = null
    var lastTime: com.google.firebase.Timestamp? = null
    var dayMemo: String? = null
    var firstTimeAsString: String? = null // Timestamp를 변환한 문자열 필드
    var lastTimeAsString: String? = null // Timestamp를 변환한 문자열 필드
}