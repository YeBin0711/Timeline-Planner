package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.timelineplanner.databinding.AlarmDialogBinding
import com.example.timelineplanner.databinding.ColorDialogBinding
import com.example.timelineplanner.databinding.IconDialogBinding
import com.example.timelineplanner.databinding.RepeatDialogBinding
import com.example.timelineplanner.databinding.TimePickerBinding

//다이얼로그 클래스
class IconDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = IconDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}
class ColorDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ColorDialogBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.hourPicker2.minValue = 0
        binding.hourPicker2.maxValue = 24
        binding.hourPicker2.value = hour
        binding.minPicker1.minValue = 0
        binding.minPicker1.maxValue = 59
        binding.minPicker1.value = minute
        binding.minPicker2.minValue = 0
        binding.minPicker2.maxValue = 59
        binding.minPicker2.value = minute

        binding.cancel.setOnClickListener() {
            dismiss()
        }
        binding.ok.setOnClickListener() {
            activity.onClickOkButton3(binding.hourPicker1.value, binding.minPicker1.value,flag)
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