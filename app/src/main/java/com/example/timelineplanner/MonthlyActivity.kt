package com.example.timelineplanner

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
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

        //테마 적용
        /*
        PreferenceManager.getDefaultSharedPreferences(this).getString("themes", "light")?.let {
            when(it) {
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
        */

        //툴바 메뉴 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //달력 설정
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

        //달력의 날짜 클릭 이벤트
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.monthly)?.isChecked = true
        for (i in 0 until menu!!.size()) {
            val item = menu.getItem(i)
            if(!item.isChecked) item.iconTintList = getColorStateList(R.color.semi_transparent)
            else if(PreferenceManager.getDefaultSharedPreferences(this).getString("themes", "light") == "dark") item.iconTintList = getColorStateList(R.color.white)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.daily -> {
            val intent = Intent(this,HomeActivity::class.java )
            startActivity(intent)
            true
        }
        R.id.monthly -> {
            val intent = Intent(this,MonthlyActivity::class.java )
            startActivity(intent)
            true
        }
        R.id.settings -> {
            val intent = Intent(this,SettingActivity::class.java )
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}