package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityAddBinding
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat;
import java.util.Calendar

class AddActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var editMemo: EditText
    private lateinit var editFirstTimeHour: EditText
    private lateinit var editFirstTimeMin: EditText
    private lateinit var editLastTimeHour: EditText
    private lateinit var editLastTimeMin: EditText
    private lateinit var buttonSave: Button
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editTitle = findViewById(R.id.todo_title)
        editMemo = findViewById(R.id.todo_memo)
        editFirstTimeHour = findViewById(R.id.hour1)
        editFirstTimeMin = findViewById(R.id.minute1)
        editLastTimeHour = findViewById(R.id.hour2)
        editLastTimeMin = findViewById(R.id.minute2)
        buttonSave = findViewById(R.id.btn_save)

        buttonSave.setOnClickListener {
            addDataToFirestore()
        }
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        //스위치 on/off
        val switchView: SwitchCompat = findViewById(R.id.cswitch)
        switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchView.isChecked = true
            }
        }
    }

    private fun addDataToFirestore() {
        val title = editTitle.text.toString()
        val memo = editMemo.text.toString()
        val firstTimeHour = editFirstTimeHour.text.toString()
        val firstTimeMin = editFirstTimeMin.text.toString()
        val lastTimeHour = editLastTimeHour.text.toString()
        val lastTimeMin = editLastTimeMin.text.toString()

        val newItemData = ItemData()
        newItemData.dayTitle = title
        newItemData.dayMemo = memo
        newItemData.firstTimeHour = firstTimeHour
        newItemData.firstTimeMin = firstTimeMin
        newItemData.lastTimeHour = lastTimeHour
        newItemData.lastTimeMin = lastTimeMin

        db.collection("users")
            .add(newItemData)
            .addOnSuccessListener { documentReference ->
                // 성공적으로 추가됐을 때 처리
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "저장이 안됐습니다", Toast.LENGTH_SHORT).show()
                // 실패했을 때 처리
            }
    }
 }