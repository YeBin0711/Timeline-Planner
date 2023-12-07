package com.example.timelineplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.text.SimpleDateFormat;
import java.util.Calendar

class AddActivity : AppCompatActivity() {
    swNotificationPayment.setOnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            swNotificationAll.isChecked = true
        }
    }
    private fun dayOfWeek(): String? {
        val cal: Calendar = Calendar.getInstance()
        var strweek : String? = null
        val nWeek : Int = cal.get(Calendar.DAY_OF_WEEK)

        strweek = when(nWeek) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> null
        }
        return strweek
    }
    //날짜+요일 -> text 설정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
    }
}