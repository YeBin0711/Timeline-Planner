package com.example.timelineplanner.model

import com.example.timelineplanner.model.Time
import java.time.LocalDate
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.timelineplanner.Homeadapter
import com.example.timelineplanner.MyApplication
import com.example.timelineplanner.MyApplication.Companion.db
import com.example.timelineplanner.Todo
import java.time.format.DateTimeFormatter


data class ItemData(
    val daytitle: String = "",
    val daycolor: Int = 0,
    val dayicon: Int = 0,
    var dayDate1: LocalDate,
    var dayDate2: LocalDate,
    val firstTime: Time = Time("", ""),
    val lastTime: Time = Time("", ""),
    val dayshow: Boolean = true,
    val daymemo: String = "",
    var firestoreDocumentId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readSerializable() as LocalDate,
        parcel.readSerializable() as LocalDate,
        parcel.readParcelable(Time::class.java.classLoader) ?: Time(),
        parcel.readParcelable(Time::class.java.classLoader) ?: Time(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(daytitle)
        parcel.writeInt(daycolor)
        parcel.writeInt(dayicon)
        parcel.writeSerializable(dayDate1)
        parcel.writeSerializable(dayDate2)
        parcel.writeParcelable(firstTime, flags)
        parcel.writeParcelable(lastTime, flags)
        parcel.writeBoolean(dayshow)
        parcel.writeString(daymemo)
        parcel.writeString(firestoreDocumentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemData> {
        override fun createFromParcel(parcel: Parcel): ItemData {
            return ItemData(parcel)
        }

        override fun newArray(size: Int): Array<ItemData?> {
            return arrayOfNulls(size)
        }
    }
}

data class Time(
    val hour: String = "",
    val minute: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hour)
        parcel.writeString(minute)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Time> {
        override fun createFromParcel(parcel: Parcel): Time {
            return Time(parcel)
        }

        override fun newArray(size: Int): Array<Time?> {
            return arrayOfNulls(size)
        }
    }
}