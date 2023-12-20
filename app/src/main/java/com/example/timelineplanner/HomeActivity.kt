package com.example.timelineplanner

import android.content.Intent
import android.content.res.Configuration
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.ActivityHomeBinding
import com.example.timelineplanner.model.ItemData
import com.example.timelineplanner.model.Time
import com.google.firebase.firestore.FirebaseFirestore
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class HomeActivity : AppCompatActivity(), DayViewContainer.RecyclerViewClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Homeadapter
    lateinit var binding: ActivityHomeBinding
    lateinit var monthText2TextView: TextView
    private lateinit var Homeadapter:Homeadapter
    private val db = FirebaseFirestore.getInstance()// 문서 ID를 저장할 변수
    private val itemList = ArrayList<ItemData>()
    private var beforeDate: LocalDate ?= null    //이전 날짜 추적 변수

    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜
    val calendar = Calendar.getInstance()
    val year = selectedDate.year.toString()
    val month = selectedDate.month.toString()
    var weekyear = Calendar.getInstance().get(Calendar.YEAR).toString() // this year
    var weekmonth = Calendar.getInstance().get(Calendar.MONTH).toString()
    var weekday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
    var selectedDate1: LocalDate? = LocalDate.now()

    var calendarHeaderTitle = "$month - $year" // default header

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchDataFromFirestore(selectedDate)

        recyclerView = findViewById(R.id.weekday_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchDataFromFirestore(selectedDate)

        adapter = Homeadapter(this,itemList, this)
        recyclerView.adapter = adapter

        //action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        monthText2TextView = binding.monthSelector2.findViewById(R.id.monthText2)

        //추가창 뜨게 하는 버튼 이벤트
        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())   //Contract
        {
            //사후 처리
            binding.weekCalendarView.notifyDateChanged(LocalDate.parse(it.data?.getStringExtra("date")))
        }
        binding.btnPlus.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("date", selectedDate1.toString())
            //startActivity(intent)
            requestLauncher.launch(intent)
        }

        //주간 달력 출력
        val currentDate = LocalDate.now()
        binding.weekCalendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: WeekDay) {
                // Initialize the calendar day for this container.
                container.day = data
                if(intent.getStringExtra("date") != null) {
                    val intentDate = LocalDate.parse(intent.getStringExtra("date"))
                    selectedDate1 = intentDate
                    fetchDataFromFirestore(intentDate)
                }

                // Show the month dates. Remember that views are reused!
                if (container.day.date != selectedDate1) {
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
                else {
                    container.calendarDayNumber.setTextColor(
                        ContextCompat.getColor(
                            this@HomeActivity,
                            R.color.black
                        )
                    )
                    container.calendarDayName.setTextColor(
                        ContextCompat.getColor(
                            this@HomeActivity,
                            R.color.black
                        )
                    )
                }

                weekyear = data.date.year.toString()
                weekmonth = data.date.month.toString() // for use outside (in header)
                weekday = data.date.dayOfMonth.toString()

                container.calendarDayNumber.text = data.date.dayOfMonth.toString()
                container.calendarDayName.text = data.date.dayOfWeek.toString().substring(0..2)
                container.calendarDay.setOnClickListener {
                    var clickedDate = container.day.date
                    val currentSelection = selectedDate1
                    if (currentSelection == container.day.date) {
                        // If the user clicks the same date, clear selection.
                        selectedDate1 = null
                        // Reload this date so the dayBinder is called
                        // and we can REMOVE the selection background.
                        binding.weekCalendarView.notifyDateChanged(currentSelection)
                    } else {
                        selectedDate1 = container.day.date
                        // Reload the newly selected date so the dayBinder is
                        // called and we can ADD the selection background.
                        binding.weekCalendarView.notifyDateChanged(container.day.date)
                        if (currentSelection != null) {
                            // We need to also reload the previously selected
                            // date so we can REMOVE the selection background.
                            binding.weekCalendarView.notifyDateChanged(currentSelection)
                        }
                    }
                    // 클릭한 날짜에 대한 처리
                    fetchDataFromFirestore(clickedDate)
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK) {
            //사후처리
            if(data?.getStringExtra("date") != null) {
                fetchDataFromFirestore(LocalDate.parse(data?.getStringExtra("date")))
                //fetchDataFromFirestore(selectedDate1!!)
            }
        }
    }

    override fun onItemClick(position: Int) {
        if (position >= 0 && position < itemList.size) {
            val clickedItem = itemList[position]
            // 클릭된 아이템을 처리하는 코드 작성
            Log.d("bin","recyclerview 눌렸다")
        } else {
            // itemList가 비어있거나 유효하지 않은 인덱스일 때 처리하는 로직 추가
            Log.d("bin","elserecyclerview 눌렸다")
        }
    }
    override fun onResume() {
        super.onResume()
        fetchDataFromFirestore(selectedDate)
    }

    private fun fetchDataFromFirestore(selectedDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateString = selectedDate.format(formatter)

        db.collection("users")
            .whereEqualTo("daydate1", dateString)
            .whereEqualTo("daydate2", dateString) // daydate1과 daydate2가 동일한 날짜인 경우 필터링
            .orderBy("firstTime.hour")
            .orderBy("firstTime.minute")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemData>()

                for (document in result) {
                    val daytitle = document.getString("daytitle") ?: ""

                    val daycolor = document.getString("daycolor") ?:"#D9D9D9"

                    val dayicon = document.getLong("dayicon")?.toInt() ?: 0

                    val daydateString1 = document.getString("daydate1") ?: ""
                    val daydate1 = LocalDate.parse(daydateString1)

                    val daydateString2 = document.getString("daydate2") ?: ""
                    val daydate2 = LocalDate.parse(daydateString2)

                    val firstTimeMap = document.get("firstTime") as HashMap<*, *>
                    val firstTimeHour = firstTimeMap["hour"] as String
                    val firstTimeMinute = firstTimeMap["minute"] as String
                    val firstTimeObj = Time(firstTimeHour, firstTimeMinute)

                    val lastTimeMap = document.get("lastTime") as HashMap<*, *>
                    val lastTimeHour = lastTimeMap["hour"] as String
                    val lastTimeMinute = lastTimeMap["minute"] as String
                    val lastTimeObj = Time(lastTimeHour, lastTimeMinute)

                    val dayshow = document.getBoolean("dayshow") ?: false // 기본값을 false로 설정

                    val daymemo = document.getString("daymemo") ?: ""

                    val documentId = document.id // 여기서 문서 ID를 가져옵니다.

                    val itemData = ItemData(daytitle, daycolor, dayicon, daydate1, daydate2, firstTimeObj, lastTimeObj, dayshow, daymemo, documentId)
                    itemData.firestoreDocumentId = documentId
                    itemList.add(itemData)
                }
                                // RecyclerView에 새로운 데이터 설정
                adapter = Homeadapter(this@HomeActivity, itemList, this@HomeActivity)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("FetchData", "Error getting documents.", exception)
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

        fetchDataFromFirestore(selectedDate)
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
