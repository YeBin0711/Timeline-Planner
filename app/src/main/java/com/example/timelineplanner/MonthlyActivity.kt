package com.example.timelineplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timelineplanner.databinding.ActivityMonthlyBinding
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.YearMonth

class MonthlyActivity : AppCompatActivity() {
    val binding = ActivityMonthlyBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.calendarView.dayBinder = MonthlyCellBinder()
        binding.calendarView.monthHeaderBinder = MonthlyHeaderBinder()

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
    }
}