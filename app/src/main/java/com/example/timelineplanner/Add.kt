package com.example.timelineplanner

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.timelineplanner.databinding.AlarmDialogBinding
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.timelineplanner.databinding.DatePickerBinding
import com.example.timelineplanner.databinding.RepeatDialogBinding
import com.example.timelineplanner.databinding.TimePickerBinding
import androidx.core.content.ContextCompat

//색상 선택 다이얼로그
class ColorSelectionDialog : DialogFragment() {

    private var colorSelectedListener: ((Int) -> Unit)? = null

    fun setColorSelectedListener(listener: (Int) -> Unit) {
        colorSelectedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.color_dialog, container, false)

        val c1 = view.findViewById<ImageView>(R.id.color1)
        val c2 = view.findViewById<ImageView>(R.id.color2)
        val c3 = view.findViewById<ImageView>(R.id.color3)
        val c4 = view.findViewById<ImageView>(R.id.color4)
        val c5 = view.findViewById<ImageView>(R.id.color5)
        val c6 = view.findViewById<ImageView>(R.id.color6)

        c1.setOnClickListener {
            val selectedColorId = ContextCompat.getColor(requireContext(), R.color.lightred)
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }

        c2.setOnClickListener {
            val selectedColorId = ContextCompat.getColor(requireContext(), R.color.lightyellow)
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c3.setOnClickListener {
            val selectedColorId = ContextCompat.getColor(requireContext(), R.color.lightgreen)
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c4.setOnClickListener {
            val selectedColorId = ContextCompat.getColor(requireContext(), R.color.lightgray)
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c5.setOnClickListener {
            val selectedColorId = ContextCompat.getColor(requireContext(), R.color.phvink)
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        c6.setOnClickListener {
            val selectedColorId = ContextCompat.getColor(requireContext(), R.color.skyblue)
            colorSelectedListener?.invoke(selectedColorId)
            dismiss()
        }
        return view
    }
}

//아이콘 선택 다이얼로그
class IconSelectionDialog : DialogFragment() {

    private var iconSelectedListener: ((Int) -> Unit)? = null

    fun setIconSelectedListener(listener: (Int) -> Unit) {
        iconSelectedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.icon_dialog, container, false)

        val wakeupIcon = view.findViewById<ImageView>(R.id.wakeup)
        val sleepingIcon = view.findViewById<ImageView>(R.id.sleeping)
        val trainIcon= view.findViewById<ImageView>(R.id.train)
        val carIcon= view.findViewById<ImageView>(R.id.car)
        val computerIcon= view.findViewById<ImageView>(R.id.computer)
        val bookIcon= view.findViewById<ImageView>(R.id.book)
        val foodIcon= view.findViewById<ImageView>(R.id.food)
        val cleaningIcon= view.findViewById<ImageView>(R.id.cleaning)
        val muscleIcon= view.findViewById<ImageView>(R.id.muscle)
        val restIcon= view.findViewById<ImageView>(R.id.rest)
        val showerIcon= view.findViewById<ImageView>(R.id.shower)
        val emptyIcon= view.findViewById<ImageView>(R.id.empty)

        wakeupIcon.setOnClickListener {
            Log.d("bin","기상 눌림")
            val selectedIconId = R.drawable.wakeup
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }

        sleepingIcon.setOnClickListener {
            val selectedIconId = R.drawable.sleeping
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        trainIcon.setOnClickListener{
            val selectedIconId = R.drawable.train
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }

        carIcon.setOnClickListener{
            val selectedIconId = R.drawable.car
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        computerIcon.setOnClickListener{
            val selectedIconId = R.drawable.computer
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        bookIcon.setOnClickListener{
            val selectedIconId = R.drawable.book
            iconSelectedListener?.invoke(selectedIconId)

            dismiss()
        }
        foodIcon.setOnClickListener{
            val selectedIconId = R.drawable.food
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        cleaningIcon.setOnClickListener{
            val selectedIconId = R.drawable.cleaning
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        muscleIcon.setOnClickListener{
            val selectedIconId = R.drawable.muscle
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        restIcon.setOnClickListener{
            val selectedIconId = R.drawable.rest
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        showerIcon.setOnClickListener{
            val selectedIconId = R.drawable.shower
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        emptyIcon.setOnClickListener{
            val selectedIconId = R.drawable.empty // 예시: 선택한 아이콘의 리소스 ID
            iconSelectedListener?.invoke(selectedIconId)
            dismiss()
        }
        return view
    }
}
//날짜 선택 데이트피커, 다이얼로그
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

//시간 선택 타임피커, 다이얼로그
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

//반복 다이얼로그
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

//알람 다이얼로그
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