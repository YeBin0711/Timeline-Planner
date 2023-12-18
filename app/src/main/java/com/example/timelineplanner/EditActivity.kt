package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityEditBinding

import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat;
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDelete.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        //스위치 on/off
        val switchView: SwitchCompat = findViewById(R.id.cswitch)
        switchView.setOnCheckedChangeListener {buttonView, isChecked ->
            if (isChecked) {
                switchView.isChecked = true
            }
        }
    }
}