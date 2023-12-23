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
import android.widget.Toast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class HomeActivity : AppCompatActivity(), DayViewContainer.RecyclerViewClickListener {
    private lateinit var recyclerView: RecyclerView
    private var adapter: Homeadapter = Homeadapter(this, listOf<ItemData>(), this)
    lateinit var binding: ActivityHomeBinding
    lateinit var monthText2TextView: TextView
    private val db = FirebaseFirestore.getInstance()// 문서 ID를 저장할 변수
    private var itemList = mutableListOf<ItemData>()

    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜

    var weekyear = Calendar.getInstance().get(Calendar.YEAR).toString()
    var weekmonth = Calendar.getInstance().get(Calendar.MONTH).toString()
    var weekday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
    var selectedDate1: LocalDate? = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        monthText2TextView = binding.monthSelector2.findViewById(R.id.monthText2)

        recyclerView = findViewById(R.id.weekday_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData(selectedDate)

        //추가창에 대한 데이터 업데이트하는
        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            //사후 처리
            val intentDate = LocalDate.parse(it.data?.getStringExtra("resultDate"))
            selectedDate1 = intentDate
            selectedDate = intentDate
            binding.weekCalendarView.notifyDateChanged(intentDate)
        }
        //추가창 뜨게 하는 버튼 이벤트
        binding.btnPlus.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("date", selectedDate1.toString())
            requestLauncher.launch(intent)
        }

        //주간 달력 출력
        val currentDate = LocalDate.now()
        binding.weekCalendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: WeekDay) {

                container.day = data

                //선택한 날짜만 검은 글씨로 출력, 다른 날짜는 회색으로 출력
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
                    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    if(currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                        container.calendarDayNumber.setTextColor(
                            ContextCompat.getColor(
                                this@HomeActivity,
                                R.color.white
                            )
                        )
                        container.calendarDayName.setTextColor(
                            ContextCompat.getColor(
                                this@HomeActivity,
                                R.color.white
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
                }

                weekyear = data.date.year.toString()
                weekmonth = data.date.month.toString()
                weekday = data.date.dayOfMonth.toString()

                container.calendarDayNumber.text = data.date.dayOfMonth.toString()
                container.calendarDayName.text = data.date.dayOfWeek.toString().substring(0..2)

                container.calendarDay.setOnClickListener {

                    var clickedDate = container.day.date
                    val currentSelection = selectedDate1

                    if (currentSelection == container.day.date) {
                        binding.weekCalendarView.notifyDateChanged(currentSelection)
                    } else {
                        selectedDate1 = container.day.date
                        binding.weekCalendarView.notifyDateChanged(container.day.date)

                        if (currentSelection != null) {
                            binding.weekCalendarView.notifyDateChanged(currentSelection)
                        }
                    }
                    // 클릭한 날짜에 대한 처리
                    fetchDataFromFirestore(clickedDate)
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(1000).atStartOfMonth()
        val endDate = currentMonth.plusMonths(1000).atEndOfMonth()
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        val startMonth = currentMonth.minusMonths(10000)
        val endMonth = currentMonth.plusMonths(10000)
        binding.weekCalendarView.setup(startDate, endDate, firstDayOfWeek)
        binding.weekCalendarView.scrollToWeek((currentDate))

        //달력의 날짜 선택에 대한 이벤트
        binding.monthSelector2.setOnClickListener() {
            val datepickerdialog = DatePickerDialog2(this, this, startMonth.year+1, endMonth.year-1, selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
            datepickerdialog.show()
        }

    }

    //선택했던 activity로 돌아오는 기능
    override fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK) {
            //사후처리
            if(data?.getStringExtra("resultDate") != null) {
                val intentDate = LocalDate.parse(data?.getStringExtra("resultDate"))
                selectedDate1 = intentDate
                selectedDate = intentDate
                binding.weekCalendarView.notifyDateChanged(intentDate)
            }
        }
    }

    //recyclerview 안의 아이템 선택 시 실행
    override fun onItemClick(position: Int) {
        if (position >= 0 && position < itemList.size) {
            val clickedItem = itemList[position]
        } else {
            Toast.makeText(baseContext, "데이터가 비어있습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    //바로바로 업데이트 하는 기능
    override fun onResume() {
        super.onResume()
        fetchDataFromFirestore(selectedDate)
    }

    private fun fetchData(selectedDate: LocalDate) {
        val channel = Channel<Homeadapter>()

        val backgroundScope = CoroutineScope(Dispatchers.IO + Job())
        backgroundScope.launch {
            getDataFromFirestore(selectedDate) {items ->
                itemList = items?: mutableListOf()
                adapter = Homeadapter(this@HomeActivity, itemList, this@HomeActivity)
            }
            channel.send(adapter)
        }
        val mainScope = GlobalScope.launch(Dispatchers.Main) {
            channel.consumeEach {
                recyclerView.adapter = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    //Firestore에 있는 데이터 recyclerview에 출력하기
    private fun getDataFromFirestore(selectedDate: LocalDate, callback: (MutableList<ItemData>?) -> Unit) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateString = selectedDate.format(formatter)

        db.collection("users")
            .whereEqualTo("daydate1", dateString)
            .whereEqualTo("daydate2", dateString)
            .orderBy("firstTime.hour")
            .orderBy("firstTime.minute")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemData>()

                for (document in result) {
                    val daytitle = document.getString("daytitle") ?: ""

                    val daycolor = document.getLong("daycolor")?.toInt() ?: 0

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

                    val dayshow = document.getBoolean("dayshow") ?: false

                    val daymemo = document.getString("daymemo") ?: ""

                    val documentId = document.id

                    val itemData = ItemData(daytitle, daycolor, dayicon, daydate1, daydate2, firstTimeObj, lastTimeObj, dayshow, daymemo, documentId)
                    itemData.firestoreDocumentId = documentId
                    itemList.add(itemData)
                }
                callback(itemList)
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }

    //Firestore에 있는 데이터 recyclerview에 출력하기
    private fun fetchDataFromFirestore(selectedDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateString = selectedDate.format(formatter)

        db.collection("users")
            .whereEqualTo("daydate1", dateString)
            .whereEqualTo("daydate2", dateString)
            .orderBy("firstTime.hour")
            .orderBy("firstTime.minute")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemData>()

                for (document in result) {
                    val daytitle = document.getString("daytitle") ?: ""

                    val daycolor = document.getLong("daycolor")?.toInt() ?: 0

                    val dayicon = document.getLong("dayicon")?.toInt() ?: 0

                    val daydateString1 = document.getString("daydate1") ?: ""
                    val daydate1 = LocalDate.parse(daydateString1)

                    val daydateString2 = document.getString("daydate2") ?: ""
                    val daydate2 = LocalDate.parse(daydateString2)

                    val firstTimeMap = document.get("firstTime") as HashMap<*, *>
                    val firstTimeHour = firstTimeMap["hour"].toString()
                    val firstTimeMinute = firstTimeMap["minute"].toString()
                    val firstTimeObj = Time(firstTimeHour, firstTimeMinute)

                    val lastTimeMap = document.get("lastTime") as HashMap<*, *>
                    val lastTimeHour = lastTimeMap["hour"].toString()
                    val lastTimeMinute = lastTimeMap["minute"].toString()
                    val lastTimeObj = Time(lastTimeHour, lastTimeMinute)

                    val dayshow = document.getBoolean("dayshow") ?: false

                    val daymemo = document.getString("daymemo") ?: ""

                    val documentId = document.id

                    val itemData = ItemData(daytitle, daycolor, dayicon, daydate1, daydate2, firstTimeObj, lastTimeObj, dayshow, daymemo, documentId)
                    itemData.firestoreDocumentId = documentId
                    itemList.add(itemData)
                }
                // RecyclerView에 새로운 데이터 설정
                adapter = Homeadapter(this@HomeActivity, itemList, this@HomeActivity)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
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

        val monthText2TextView = binding.monthSelector2.findViewById<TextView>(R.id.monthText2)
        monthText2TextView.text = koreanDateString

        fetchDataFromFirestore(selectedDate)
    }

    //오른쪽 상단의 menu 버튼
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