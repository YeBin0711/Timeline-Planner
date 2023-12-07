package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityAddBinding
import com.example.timelineplanner.databinding.ActivityJoinBinding
import java.text.SimpleDateFormat;
import java.util.Calendar

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //스위치 on/off
        val switchView: SwitchCompat = findViewById(R.id.cswitch)
        switchView.setOnCheckedChangeListener {buttonView, isChecked ->
            if (isChecked) {
                switchView.isChecked = true
            }
        }

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnSave.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            //
        }
    }
}