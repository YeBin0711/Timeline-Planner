package com.example.timelineplanner

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.ActivityHomeBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import com.example.timelineplanner.HomeAdapter
import com.example.timelineplanner.R

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //월별창 뜨게하는 버튼 이벤트
        /*binding.monthly.setOnClickListener{
            val intent = Intent(this,MonthlyActivity::class.java )
            startActivity(intetnt)
        }
        //설정창 뜨게하는 버튼 이벤트
        binding.settings.setOnClickListener{
            val intent = Intent(this,SettingsActivity::class.java )
            startActivity(intetnt)
        }

        //수정창 뜨게하는 버튼 이벤트
        binding.btnPlus.setOnClickListener{
            val intent = Intent(this,수정창:class.java)
            startActivity(intent)
        }*/
        /* 날짜 뜨게하는 버튼 이벤트
        binding.ca.setOnClickListener{
            val intent = Intent(this, 날짜창:class.java)
            startActivity(intent)
        }*/

        //달력 출력
        val currentDate = LocalDate.now()
        val dateFormatter0 = DateTimeFormatter.ofPattern("yyyy년 MM월") // 날짜 형식 지정 (예: 2023-11-24)
        val formattedDate0 = currentDate.format(dateFormatter0)

        val dateTextView0: TextView = findViewById(R.id.main_day)
        dateTextView0.text = "$formattedDate0" // TextView에 날짜 설정

        fun getDate(currentDate: LocalDate): String {
            val dateFormatter = DateTimeFormatter.ofPattern("E\n dd")
            return currentDate.format(dateFormatter)
        }

        fun updateDates() {
            val currentDate = LocalDate.now()
            val startOfWeek = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - DayOfWeek.MONDAY.value.toLong())

            val dateTextViewIds = listOf(
                R.id.day_text1, R.id.day_text2, R.id.day_text3,
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
    }
}