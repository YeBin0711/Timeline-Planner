package com.example.timelineplanner

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.ActivityHomeBinding
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
import java.text.SimpleDateFormat


class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    lateinit var binding: ActivityHomeBinding

    var selectedDate: LocalDate = LocalDate.now() // today
    // this month
    val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH)
    val monthName = SimpleDateFormat("MMMM", Locale.US).format(calendar.time)
    var year = Calendar.getInstance().get(Calendar.YEAR).toString() // this year

    var calendarHeaderTitle = "$monthName - $year" // default header

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*월별창 뜨게하는 버튼 이벤트
        binding.monthly.setOnClickListener{
            val intent = Intent(this,MonthlyActivity::class.java )
            startActivity(intetnt)
        }
        //설정창 뜨게하는 버튼 이벤트
        binding.settings.setOnClickListener{
            val intent = Intent(this,SettingsActivity::class.java )
            startActivity(intetnt)
        }*/

        //추가창 뜨게 하는 버튼 이벤트
        binding.btnPlus.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        /*
        // 날짜 뜨게하는 버튼 이벤트
        binding.ca.setOnClickListener{
            val intent = Intent(this, 날짜창::class.java)
            startActivity(intent)
        }*/

        //달력 출력
        val currentDate = LocalDate.now()
        val dateFormatter0 = DateTimeFormatter.ofPattern("yyyy년 MM월") // 날짜 형식 지정 (예: 2023-11-24)
        val formattedDate0 = currentDate.format(dateFormatter0)

        val dateTextView0: TextView = findViewById(R.id.month)
        dateTextView0.text = "$formattedDate0" // TextView에 날짜 설정

        fun getDate(currentDate: LocalDate): String {
            val dateFormatter = DateTimeFormatter.ofPattern("E\n dd")
            return currentDate.format(dateFormatter)
        }
        /*
        fun updateDates() {
            val currentDate = LocalDate.now()
            val startOfWeek = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - DayOfWeek.MONDAY.value.toLong())

            val dateTextViewIds = listOf(R.id.day_text1, R.id.day_text2, R.id.day_text3,
                R.id.day_text4, R.id.day_text5, R.id.day_text6, R.id.day_text7)

            var offset = 0

            dateTextViewIds.forEachIndexed { index, textViewId ->
                val currentDay = startOfWeek.plusDays(offset.toLong())
                //val dateFormatter = DateTimeFormatter.ofPattern("MM/dd (E)")
                val formattedDate = getDate(currentDay)

                val dateTextView: TextView = findViewById(textViewId)
                dateTextView.text = formattedDate

                //폰트 수정
                val dayOfWeek = currentDay.dayOfWeek.toString().toUpperCase(Locale.US)
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                val boldDayOfWeek = "<b>$dayOfWeek</b>"
                dateTextView.text = formattedDate.replace(dayOfWeek, boldDayOfWeek)
                dateTextView.setTypeface(boldTypeface)

                val shape = ShapeDrawable(RectShape())
                shape.paint.color = if (currentDay == currentDate) Color.LTGRAY else Color.TRANSPARENT
                dateTextView.background = shape

                offset++ // 다음 날짜로 이동

                // 주마다 출력되는 기능
                if (offset == 7 && currentDay.dayOfWeek == DayOfWeek.SUNDAY) {
                    offset = 0 // 월요일부터 시작하도록 오프셋 초기화
                }
            }
        }
        updateDates()
        */


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

        /*
        val imageView = findViewById<ImageView>(R.id.ticon)

        // 다른 색을 받아 배경색을 변경하는 함수
        fun changeBackgroundColor(color: String) {
            val parsedColor = Color.parseColor(color)
            imageView.setBackgroundColor(parsedColor)
        }

        // 예시: 사용자로부터 입력을 받아 색상 변경
        val userInputColor = "#00FF00" // 여기에 사용자로부터 입력 받은 색상이 들어가야 합니다.
        changeBackgroundColor(userInputColor)*/

        binding.weekCalendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: WeekDay) {
                // Initialize the calendar day for this container.
                container.day = data

                // Show the month dates. Remember that views are reused!
                val colorResId: Int =
                    if (container.day.date == selectedDate) R.color.drakgray else R.color.black

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

                //monthName = data.date.month.toString() // for use outside (in header)
                year = data.date.year.toString()
                calendarHeaderTitle = "$monthName - $year"
                container.calendarDayNumber.text = data.date.dayOfMonth.toString()
                container.calendarDayName.text = data.date.dayOfWeek.toString().substring(0..2)
            }
        }

        binding.weekCalendarView.weekHeaderBinder =
            object : WeekHeaderFooterBinder<MonthHeaderViewContainer> {
                override fun create(view: View) = MonthHeaderViewContainer(view)

                override fun bind(container: MonthHeaderViewContainer, data: Week) {
                    container.calendarMonthTitle.text = calendarHeaderTitle
                }
            }

        //val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(10).atStartOfMonth()
        val endDate = currentMonth.plusMonths(10).atEndOfMonth()
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        binding.weekCalendarView.setup(startDate, endDate, firstDayOfWeek)
        binding.weekCalendarView.scrollToWeek((currentDate))
    }
}
