package com.example.timelineplanner

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
    val dayOfWeeks: Array<Int>
)

class Alarm (
    val type: Int,
    val time: Time
)

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
    MyApplication.db.collection("Todo")
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