package com.example.timelineplanner.model

import com.example.timelineplanner.model.Time
import java.time.LocalDate
import android.os.Parcel
import android.os.Parcelable


data class ItemData(
    val daytitle: String = "",
    val daycolor: String = "",
    val dayicon: Int = 0,
    val selectedDate1: LocalDate,
    val selectedDate2: LocalDate,
    val firstTime: Time = Time("", ""),
    val lastTime: Time = Time("", ""),
    val daymemo: String = "",
    var firestoreDocumentId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readSerializable() as LocalDate,
        parcel.readSerializable() as LocalDate,
        parcel.readParcelable(Time::class.java.classLoader) ?: Time(),
        parcel.readParcelable(Time::class.java.classLoader) ?: Time(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(daytitle)
        parcel.writeString(daycolor)
        parcel.writeInt(dayicon)
        parcel.writeSerializable(selectedDate1)
        parcel.writeSerializable(selectedDate2)
        parcel.writeParcelable(firstTime, flags)
        parcel.writeParcelable(lastTime, flags)
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

