package com.example.timelineplanner

import android.view.View
import com.example.timelineplanner.databinding.CalendarCellBinding
import com.example.timelineplanner.databinding.MonthlyHeaderBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer

class CalendarCellContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay
    val binding = CalendarCellBinding.bind(view)
}
class MonthlyCellBinder : MonthDayBinder<CalendarCellContainer> {
    override fun create(view: View) = CalendarCellContainer(view)
    override fun bind(container: CalendarCellContainer, data: CalendarDay) {
        container.day = data
        container.binding.day.text = data.date.dayOfMonth.toString()
    }
}


class MonthlyHeaderContainer(view: View) : ViewContainer(view) {
    val monthlyHeader = MonthlyHeaderBinding.bind(view)
}

class MonthlyHeaderBinder : MonthHeaderFooterBinder<MonthlyHeaderContainer> {
    override fun create(view: View) = MonthlyHeaderContainer(view)
    override fun bind(container: MonthlyHeaderContainer, month: CalendarMonth) {
        container.monthlyHeader.month.text = month.yearMonth.toString()
    }
}