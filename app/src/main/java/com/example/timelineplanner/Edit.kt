package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.timelineplanner.databinding.AlarmDialogBinding
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.RepeatDialogBinding
import com.example.timelineplanner.databinding.TimePickerBinding

class TodoDatePickerDialog1(context: Context, val activity: EditActivity, val minYear: Int, val maxYear: Int, var year: Int, var month: Int, var day: Int, var flag: Int): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DatePickerBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        binding.yearPicker.minValue = minYear
        binding.yearPicker.maxValue = maxYear
        binding.yearPicker.value = year
        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = 12
        binding.monthPicker.value = month
        binding.dayPicker.minValue = 1
        binding.dayPicker.maxValue = 31
        binding.dayPicker.value = day

        binding.cancel.setOnClickListener() {
            dismiss()
        }
        binding.ok.setOnClickListener() {
            activity.onClickOkButton6(binding.yearPicker.value, binding.monthPicker.value, binding.dayPicker.value, flag)
            dismiss()
        }
    }
}

class TimePickerDialog1(context: Context, val activity: EditActivity, var hour: Int, var minute: Int, var flag: Int): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = TimePickerBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        binding.hourPicker1.minValue = 0
        binding.hourPicker1.maxValue = 24
        binding.hourPicker1.value = hour
        binding.minPicker1.minValue = 0
        binding.minPicker1.maxValue = 59
        binding.minPicker1.value = minute

        //항상 두 글자로 출력
        binding.hourPicker1.setFormatter { hour -> String.format("%02d", hour) }
        binding.minPicker1.setFormatter { minute -> String.format("%02d", minute) }

        binding.cancel.setOnClickListener() {
            dismiss()
        }
        binding.ok.setOnClickListener() {
            activity.onClickOkButton5(binding.hourPicker1.value, binding.minPicker1.value, flag)
            dismiss()
        }
    }
}

class RepeatDialog1(context: Context, val activity: EditActivity): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = RepeatDialogBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        var repeatType = 0
        var repeatDays = Array(7, {1})
        var dayofWeeks = arrayOf(binding.mon, binding.tue, binding.wed, binding.thu, binding.fri, binding.sat, binding.sun)
        binding.repeatGroup.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.repeat_no -> {
                    activity.binding.editTodoRepeat.setText("안 함")
                    repeatType = 0
                }
                R.id.repeat_day -> {
                    activity.binding.editTodoRepeat.setText("매일")
                    repeatType = 1
                }
                R.id.repeat_week -> {
                    repeatType = 2
                    for(i in 0..6) { //요일별 onClickLister 등록
                        dayofWeeks[i].setOnClickListener {
                            dayofWeeks[i].setTextColor(Color.BLUE)
                            repeatDays[i] = 0
                        }
                    }
                    activity.binding.editTodoRepeat.setText("매주")
                }
                R.id.repeat_month -> {
                    activity.binding.editTodoRepeat.setText("매월")
                    repeatType = 3
                }
                R.id.repeat_year -> {
                    activity.binding.editTodoRepeat.setText("매년")
                    repeatType = 4
                }
            }
        }

        binding.repeatCancel.setOnClickListener() {
            dismiss()
        }

        binding.repeatOk.setOnClickListener() {
            activity.repeatType = repeatType
            activity.repeatDays = repeatDays
            dismiss()
        }
    }
}

class AlarmDialog1(context: Context, val activity: EditActivity): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AlarmDialogBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        var alarmType = 0
        var alarmTime = arrayOf(0, 0, 0, 0)
        val periodArray = arrayOf("분", "시간", "일", "주")

        binding.alarmNumberPicker.minValue = 0
        binding.alarmNumberPicker.maxValue = 100
        binding.alarmNumberPicker.value = 10
        binding.alarmPeriodPicker.minValue = 0
        binding.alarmPeriodPicker.maxValue = 3
        binding.alarmPeriodPicker.setDisplayedValues(periodArray)

        binding.alarmNumberPicker.isVisible = false
        binding.alarmPeriodPicker.isVisible = false

        binding.alarmGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.alarm_no -> {
                    alarmType = 0
                    binding.alarmNumberPicker.isVisible = false
                    binding.alarmPeriodPicker.isVisible = false
                }

                R.id.alarm_yes -> {
                    alarmType = 1
                    binding.alarmNumberPicker.isVisible = true
                    binding.alarmPeriodPicker.isVisible = true
                    binding.alarmYes.setText("${binding.alarmNumberPicker.value}${periodArray[binding.alarmPeriodPicker.value]} 전")
                }
            }
        }

        binding.alarmCancel.setOnClickListener() {
            dismiss()
        }

        binding.alarmOk.setOnClickListener() {
            activity.alarmType = alarmType
            when(alarmType) {
                0 -> activity.binding.editTodoAlarm.setText("안 함")
                1 -> {
                    alarmTime[binding.alarmPeriodPicker.value] = binding.alarmNumberPicker.value
                    activity.binding.editTodoAlarm.setText("${binding.alarmNumberPicker.value}${periodArray[binding.alarmPeriodPicker.value]} 전")
                }
            }
            activity.alarmTime = alarmTime
            dismiss()
        }
    }
}
