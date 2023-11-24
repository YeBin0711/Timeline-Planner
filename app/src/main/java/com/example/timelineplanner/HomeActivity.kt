package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timelineplanner.databinding.ActivityHomeBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*binding.monthly.setOnClickListener{
            val intent = Intent(this,MonthlyActivity::class.java )
            startActivity(intetnt)
        }

        binding.settings.setOnClickListener{
            val intent = Intent(this,SettingsActivity::class.java )
            startActivity(intetnt)
        }*/

        //달력 출력
        val currentDate = LocalDate.now()
        val dateFormatter0 = DateTimeFormatter.ofPattern("yyyy년 MM월") // 날짜 형식 지정 (예: 2023-11-24)
        val formattedDate0 = currentDate.format(dateFormatter0)

        val dateTextView0: TextView = findViewById(R.id.main_day)
        dateTextView0.text = "$formattedDate0" // TextView에 날짜 설정

        //날짜 출력

        val dateFormatter1 = DateTimeFormatter.ofPattern("dd") // 날짜 형식 지정 (예: 2023-11-24)
        val formattedDate1 = currentDate.format(dateFormatter1)

        val dateTextView1: TextView = findViewById(R.id.day_text1)
        dateTextView1.text = "$formattedDate1"

        //날짜 출력

        val dateFormatter2 = DateTimeFormatter.ofPattern("dd") // 날짜 형식 지정 (예: 2023-11-24)
        val formattedDate2 = currentDate.plusDays(1).format(dateFormatter2) // 날짜를 하루 늘림

        val dateTextView2: TextView = findViewById(R.id.day_text2)
        dateTextView2.text = formattedDate2



        //recyclerview 작성
        recyclerView = findViewById(R.id.weekday_recyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val datas1 = mutableListOf("MON", "TUE","WED" ,"THR","FRI","SAT","SUN")
        val datas2 = mutableListOf("24","25","26","27","28","29","30")

        adapter = HomeAdapter(datas1, datas2)
        recyclerView.adapter = adapter

    }
}