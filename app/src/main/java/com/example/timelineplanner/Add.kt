package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
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
                dismiss()
            }
        }
    }
}

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

class RepeatDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = RepeatDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}
class AlarmDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AlarmDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}