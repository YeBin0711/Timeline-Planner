package com.example.timelineplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timelineplanner.databinding.ActivityMonthlyBinding
import com.example.timelineplanner.databinding.CalendarCellBinding
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MarginValues
import java.time.LocalDate
import java.time.YearMonth

class MonthlyActivity : AppCompatActivity() {
    lateinit var binding: ActivityMonthlyBinding
    lateinit var cellBinding : CalendarCellBinding
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyBinding.inflate(layoutInflater)
        cellBinding = CalendarCellBinding.inflate(layoutInflater)
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
            selectedDate = LocalDate.of(month.yearMonth.year, month.yearMonth.monthValue, selectedDate.dayOfMonth)
            binding.monthText.text = "${month.yearMonth.year}년 ${month.yearMonth.monthValue}월"
            binding.calendarView.notifyMonthChanged(month.yearMonth)
        }

        binding.monthSelector.setOnClickListener() {
            val datepickerdialog = DatePickerDialog(this, this, startMonth.year+1, endMonth.year-1, selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
            datepickerdialog.show()
        }
    }

    fun onClickOkButton(year: Int, month: Int, day: Int) {
        selectedDate = LocalDate.of(year, month, day)
        binding.calendarView.scrollToDate(selectedDate)
        binding.calendarView.notifyMonthChanged(YearMonth.of(year, month))
    }
}