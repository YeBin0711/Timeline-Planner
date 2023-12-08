package com.example.timelineplanner

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.Locale
import java.time.YearMonth

import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import java.time.format.DateTimeFormatter

import com.example.timelineplanner.R
import com.example.timelineplanner.DayViewContainer
import com.example.timelineplanner.databinding.ActivityHomeBinding
import com.example.timelineplanner.databinding.ItemCalendarDayBinding
import com.kizitonwose.calendar.core.daysOfWeek
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.util.Date


class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    lateinit var binding: ActivityHomeBinding
    lateinit var monthText2TextView: TextView

    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜
    val calendar = Calendar.getInstance()
    val year = selectedDate.year.toString()
    val month = selectedDate.month.toString()
    var weekyear = Calendar.getInstance().get(Calendar.YEAR).toString() // this year
    var weekmonth = Calendar.getInstance().get(Calendar.MONTH).toString()
    var weekday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()

    var calendarHeaderTitle = "$month - $year" // default header

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        monthText2TextView = binding.monthSelector2.findViewById(R.id.monthText2)

        //수정창 뜨게하는 버튼 이벤트
        binding.btnPlus.setOnClickListener{
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }

        //달력 출력
        val currentDate = LocalDate.now()

        //recyclerview 작성
        recyclerView = findViewById(R.id.weekday_recyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        //예시로 담아 놓은 것이고 id의 내용들이 추가되어야함
        val stime = mutableListOf("9:00","10:00","13:00","17:00","20:00")
        val ltime = mutableListOf("9:30","11:00","15:00","20:00","21:00")
        val ticon = mutableListOf(R.drawable.wakeup,R.drawable.book,R.drawable.muscle,R.drawable.computer,R.drawable.sleeping)
        val mname = mutableListOf("기상하기", "수업듣기","운동하기","과제하기","취침준비")
        val note = mutableListOf("약 챙겨먹기","노트북 필요"," ","모소 lab03 하기"," ")

        adapter = HomeAdapter(stime,ltime,ticon,mname,note)
        recyclerView.adapter = adapter

        binding.weekCalendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: WeekDay) {
                // Initialize the calendar day for this container.
                container.day = data

                // Show the month dates. Remember that views are reused!
                val colorResId: Int =
                    if (container.day.date == selectedDate) R.color.black else R.color.gray

                container.calendarDayNumber.setTextColor(
                    ContextCompat.getColor(
                        this@HomeActivity,
                        colorResId
                    )
                )
                container.calendarDayName.setTextColor(
                    ContextCompat.getColor(
                        this@HomeActivity,
                        colorResId
                    )
                )
                weekyear = data.date.year.toString()
                weekmonth = data.date.month.toString() // for use outside (in header)
                weekday = data.date.dayOfMonth.toString()

                container.calendarDayNumber.text = data.date.dayOfMonth.toString()
                container.calendarDayName.text = data.date.dayOfWeek.toString().substring(0..2)

            }
        }

        //val currentDate = LocalDate.now()
        val currentYear = Year.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(1000).atStartOfMonth()
        val endDate = currentMonth.plusMonths(1000).atEndOfMonth()
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        val startMonth = currentMonth.minusMonths(10000)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(10000)  // Adjust as needed
        binding.weekCalendarView.setup(startDate, endDate, firstDayOfWeek)
        binding.weekCalendarView.scrollToWeek((currentDate))

        //달력의 날짜 클릭 이벤트
        binding.monthSelector2.setOnClickListener() {
            val datepickerdialog = DatePickerDialog2(this, this, startMonth.year+1, endMonth.year-1, selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
            datepickerdialog.show()
        }

    }
    //날짜 변경
    fun onClickOkButton2(year: Int, month: Int, day: Int) {
        selectedDate = LocalDate.of(year, month, day)
        binding.weekCalendarView.scrollToDate(selectedDate)
        binding.weekCalendarView.notifyDateChanged(selectedDate)

        // 선택된 날짜의 연도와 월을 한국어로 변환
        val koreanDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
        val koreanDateString = koreanDateFormat.format(Date(selectedDate.year - 1900, selectedDate.monthValue - 1, selectedDate.dayOfMonth))

        // monthText2의 TextView를 찾아 업데이트

        val monthText2TextView = binding.monthSelector2.findViewById<TextView>(R.id.monthText2)
        monthText2TextView.text = koreanDateString

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.daily)?.isChecked = true
        //menu?.findItem(R.id.monthly)?.isChecked = true
        //menu?.findItem(R.id.settings)?.isChecked = true
        for (i in 0 until menu!!.size()) {
            val item = menu.getItem(i)
            if(item.isChecked) item.iconTintList = getColorStateList(R.color.black)
            else item.iconTintList = getColorStateList(R.color.darkgray)
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