package com.example.timelineplanner.model

import com.example.timelineplanner.Time
import java.time.LocalDate
import android.os.Parcel
import android.os.Parcelable



class ItemData(
    val daytitle: String = "",
    val daycolor: String = "",
    val dayicon: Int = 0,
    val selectedDate: LocalDate,
    val firstTime: Time = Time("", ""),
    val lastTime: Time = Time("", ""),
    val daymemo: String = ""
)

class Time(
    val hour: String = "",
    val minute: String = ""
)

