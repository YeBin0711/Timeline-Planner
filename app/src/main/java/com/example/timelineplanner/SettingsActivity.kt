package com.example.timelineplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timelineplanner.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val settingNames = mutableListOf(
            "테마",
            "시간 표시 방식",
            "공휴일 표시",
            "달력 일정 정렬 방식",
            "동기화",
            "버전 정보",
            "도움말",
            "로그아웃"
        )

        binding.settingList.layoutManager = LinearLayoutManager(this)
        binding.settingList.adapter = SettingsAdapter(settingNames)
    }
}