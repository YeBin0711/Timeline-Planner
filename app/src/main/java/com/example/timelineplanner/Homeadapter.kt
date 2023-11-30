package com.example.timelineplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.DayRecyclerviewBinding
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.core.WeekDay
import com.example.timelineplanner.databinding.CalendarMonthHeaderBinding
import com.example.timelineplanner.databinding.ItemCalendarDayBinding

class HomeViewHolder(val binding: DayRecyclerviewBinding):
        RecyclerView.ViewHolder(binding.root)

class HomeAdapter(var stime: MutableList<String>,var ltime: MutableList<String>, var ticon: MutableList<Int>, var mname: MutableList<String>, var note: MutableList<String>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return mname.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder = HomeViewHolder(
        DayRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as HomeViewHolder).binding

        binding.sTime.text = stime[position]
        binding.lTime.text = ltime[position]
        binding.ticon.setImageResource((ticon[position]))
        binding.itemName.text = mname[position]
        binding.itemNote.text = note[position]

        // 예시: 시간에 따라 ticon의 크기를 조절
        val sTimeParts = stime[position].split(":")
        val lTimeParts = ltime[position].split(":")

        val sHour = sTimeParts[0].toIntOrNull() ?: 0
        val sMinute = sTimeParts[1].toIntOrNull() ?: 0
        val lHour = lTimeParts[0].toIntOrNull() ?: 0
        val lMinute = lTimeParts[1].toIntOrNull() ?: 0

        val timeDifference = (lHour * 60 + lMinute) - (sHour * 60 + sMinute)

        // 원하는 방식에 따라 ticon의 크기를 계산하여 설정

        val pixelsPerMinute = 4// 예시로 시간당 픽셀 수를 정의
        val desiredHeight = timeDifference * pixelsPerMinute

        val ticonLayoutParams = binding.ticon.layoutParams
        ticonLayoutParams.height = desiredHeight
        binding.ticon.layoutParams = ticonLayoutParams

        val lTimeLayoutParams = binding.lTime.layoutParams as ViewGroup.MarginLayoutParams
        lTimeLayoutParams.topMargin = desiredHeight - timeDifference // 원하는 위치로 조정 가능
        binding.lTime.layoutParams = lTimeLayoutParams


    }

}

class DayViewContainer(view: View) : ViewContainer(view) {
    val calendarDayNumber = ItemCalendarDayBinding.bind(view).calendarDayNumber
    val calendarDayName = ItemCalendarDayBinding.bind(view).calendarDayName

    // Will be set when this container is bound
    lateinit var day: WeekDay

    init {
        view.setOnClickListener {
            val date = "${day.date.dayOfMonth}-${day.date.monthValue}-${day.date.year}"
            onDayClickListener.onDayClicked(date)
        }
    }

    lateinit var onDayClickListener: OnDayClickListener
    fun interface OnDayClickListener {
        fun onDayClicked(date: String)
    }
}

class MonthHeaderViewContainer(view: View) : ViewContainer(view) {
    val calendarMonthTitle = CalendarMonthHeaderBinding.bind(view).monthTitle
}





