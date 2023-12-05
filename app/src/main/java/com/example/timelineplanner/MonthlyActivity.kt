package com.example.timelineplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timelineplanner.databinding.ActivityMonthlyBinding
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MarginValues
import java.time.YearMonth

class MonthlyActivity : AppCompatActivity() {
    lateinit var binding: ActivityMonthlyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.monthMargins = MarginValues(15, 0, 15, 0)
        binding.calendarView.scrollToMonth(currentMonth)
        binding.calendarView.dayBinder = MonthlyCellBinder()
        binding.calendarView.monthHeaderBinder = MonthlyHeaderBinder()
        binding.calendarView.monthScrollListener = { month ->
            binding.calendarView.notifyMonthChanged(month.yearMonth)
            binding.month.text = "${month.yearMonth.year}년 ${month.yearMonth.monthValue}월"
        }
    }
}