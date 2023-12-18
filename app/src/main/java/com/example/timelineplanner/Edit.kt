package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
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
