package com.example.timelineplanner

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timelineplanner.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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
        val layoutmanager = LinearLayoutManager(this)
        binding.settingList.layoutManager = layoutmanager
        binding.settingList.adapter = SettingsAdapter(settingNames)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
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
            val intent = Intent(this,SettingsActivity::class.java )
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}