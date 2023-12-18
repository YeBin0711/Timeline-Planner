package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.timelineplanner.databinding.AlarmDialogBinding
import com.example.timelineplanner.databinding.ColorDialogBinding
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.IconDialogBinding
import com.example.timelineplanner.databinding.RepeatDialogBinding
import com.example.timelineplanner.databinding.TimePickerBinding
import com.example.timelineplanner.databinding.TodoDatePickerBinding
import com.google.type.DayOfWeek

//다이얼로그 클래스
/*
class IconDialog(context: Context, val activity: AddActivity): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = IconDialogBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        var iconImageViews = arrayOf(binding.wakeup, binding.sleeping, binding.train, binding.car,
            binding.computer, binding.book, binding.food, binding.cleaning,
            binding.muscle, binding.rest, binding.shower, binding.game)

        for (iconImage in iconImageViews) {
            iconImage.setOnClickListener() {
                activity.icon = iconImage.id //선택한 색상 코드 저장
                dismiss()
            }
        }
    }
}
class ColorDialog(context: Context, val activity: AddActivity): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ColorDialogBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        var colorImageViews = arrayOf(binding.color1, binding.color2, binding.color3, binding.color4, binding.color5, binding.color6)
        var colors = arrayOf("#FFD5D5", "#FAFFBD", "#ADFFAC", "#D9D9D9", "#F2D5FF", "#7FE8FF")

        for (i in 0 until colorImageViews.size) {
            colorImageViews[i].setOnClickListener() {
                activity.color = colors[i] //선택한 색상 코드 저장
                activity.binding.colorBtn.setBackgroundColor(Color.parseColor(colors[i]))
                dismiss()
            }
        }
    }
}
 */

class TodoDatePickerDialog(context: Context, val activity: AddActivity, val minYear: Int, val maxYear: Int, var year: Int, var month: Int, var day: Int, var flag: Int): Dialog(context) {
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
            activity.onClickOkButton4(binding.yearPicker.value, binding.monthPicker.value, binding.dayPicker.value, flag)
            dismiss()
        }
    }
}

class TimePickerDialog(context: Context, val activity: AddActivity, var hour: Int, var minute: Int, var flag: Int): Dialog(context) {
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
            activity.onClickOkButton3(binding.hourPicker1.value, binding.minPicker1.value, flag)
            dismiss()
        }
    }
}

class RepeatDialog(context: Context, val activity: AddActivity): Dialog(context) {
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
                    activity.binding.todoRepeat.setText("안 함")
                    repeatType = 0
                }
                R.id.repeat_day -> {
                    activity.binding.todoRepeat.setText("매일")
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
                    activity.binding.todoRepeat.setText("매주")
                }
                R.id.repeat_month -> {
                    activity.binding.todoRepeat.setText("매월")
                    repeatType = 3
                }
                R.id.repeat_year -> {
                    activity.binding.todoRepeat.setText("매년")
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

class AlarmDialog(context: Context, val activity: AddActivity): Dialog(context) {
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

        binding.alarmGroup.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
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
                0 -> activity.binding.todoAlarm.setText("안 함")
                1 -> {
                    alarmTime[binding.alarmPeriodPicker.value] = binding.alarmNumberPicker.value
                    activity.binding.todoAlarm.setText("${binding.alarmNumberPicker.value}${periodArray[binding.alarmPeriodPicker.value]} 전")
                }
            }
            activity.alarmTime = alarmTime
            dismiss()
        }
    }
}