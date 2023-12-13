package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityEditBinding


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