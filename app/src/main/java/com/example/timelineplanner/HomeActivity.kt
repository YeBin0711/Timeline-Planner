package com.example.timelineplanner

import android.content.Intent
import android.content.res.Configuration
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.ActivityHomeBinding
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.util.Date
import java.util.Locale


class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Homeadapter
    lateinit var binding: ActivityHomeBinding
    lateinit var monthText2TextView: TextView
    private lateinit var Homeadapter:Homeadapter
    private val db = FirebaseFirestore.getInstance()
    private val itemList = ArrayList<ItemData>()

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

        recyclerView = findViewById(R.id.weekday_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        Homeadapter = Homeadapter(this, itemList)
        recyclerView.adapter = Homeadapter

        fetchDataFromFirestore()

        //action bar
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
        binding.weekCalendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: WeekDay) {
                // Initialize the calendar day for this container.
                container.day = data

                // Show the month dates. Remember that views are reused!
                if(container.day.date != selectedDate) {
                    container.calendarDayNumber.setTextColor(
                        ContextCompat.getColor(
                            this@HomeActivity,
                            R.color.gray
                        )
                    )
                    container.calendarDayName.setTextColor(
                        ContextCompat.getColor(
                            this@HomeActivity,
                            R.color.gray
                        )
                    )
                }
                /*
                val colorResId: Unit =
                    if (container.day.date != selectedDate) R.color.gray

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
                */

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
    private fun fetchDataFromFirestore() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemData>()

                for (document in result) {
                    val item = document.toObject(ItemData::class.java)
                    itemList.add(item)
                }
                // itemList의 시간 데이터를 LocalTime으로 변환하여 정렬
                itemList.sortBy { it.firstTimeHour?.toInt() ?: 0 }

                // RecyclerView에 데이터 설정
                val recyclerView = findViewById<RecyclerView>(R.id.weekday_recyclerView)
                val adapter = Homeadapter(this, itemList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // 실패했을 때 처리
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
        for (i in 0 until menu!!.size()) {
            val item = menu.getItem(i)
            if(!item.isChecked) item.iconTintList = getColorStateList(R.color.semi_transparent)
            else if(this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
                item.iconTintList = getColorStateList(R.color.white)
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
