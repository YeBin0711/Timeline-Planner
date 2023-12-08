package com.example.timelineplanner

import java.time.LocalDate

class Todo(
    val title: String,
    val color: Int,
    val icon: Int,
    val time: Time,
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