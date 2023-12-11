package com.example.timelineplanner

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class Todo(
    val title: String,
    val color: Int,
    val icon: Int,
    val firstTime: Time,
    val lastTime: Time,
    val date: LocalDate,
    val memo: String,
    val show: Boolean,
    val repeat: Repeat,
    val alarm: Alarm
)

class Time(
    val hour: String,
    val minute: String
)

class Repeat (
    val type: Int,
    val dayOfWeek: Int
)

class Alarm (
    val type: Int,
    val time: Time
)

fun getHolidayData(year: String, month: String) : HolidayBody{
    lateinit var holidayBody : HolidayBody
    Holiday(year, month).returnCall().enqueue(object : Callback<HolidayData> {
        override fun onResponse(call: Call<HolidayData>, response: Response<HolidayData>) {
            if (response.isSuccessful) {
                holidayBody = response.body()?.response?.body!!
            } else {
                // 응답 실패 시 처리
            }
        }
        override fun onFailure(call: Call<HolidayData>, t: Throwable) {
            // 네트워크 문제
        }
    })
    return holidayBody
}



fun isEqualDate(date: LocalDate, holiday: Int): Boolean {
    if(date.year == holiday/10000 && date.monthValue == holiday%10000/100 && date.dayOfMonth == holiday%100)
        return true
    return false
}

fun transIntoTimeForm(originTime: String?, timeForm: String?): String {
    val originHour = originTime?.toInt()?: 0
    var resultHour = originTime?: "0"

    if(timeForm == "12") {
        if(originHour == 12) {
            resultHour = "12"
        } else if(originHour == 24) {
            resultHour = "12"
        }else if(originHour > 12) {
            resultHour = (originHour % 12).toString()
        } else if(originHour < 12) {
            resultHour = (originHour % 12).toString()
        }
    }

    return resultHour
}

fun getTodoList(): MutableList<Todo> {
    val todo = mutableListOf<Todo>()
    MyApplication.db.collection("users")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val item = document.toObject(Todo::class.java)
                todo.add(item)
            }
        }
        .addOnFailureListener { exception ->
            // 실패했을 때 처리
        }

    return todo
}