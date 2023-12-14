package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityAddBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private lateinit var editTitle: EditText
    private lateinit var editMemo: EditText
    //private lateinit var editFirstTimeHour: EditText //addData함수까지 수정 필요
    //private lateinit var editFirstTimeMin: EditText
    private lateinit var editFirstTime: EditText
    //private lateinit var editLastTimeHour: EditText
    //private lateinit var editLastTimeMin: EditText
    private lateinit var editLastTIme: EditText
    private lateinit var buttonSave: Button

    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTitle = findViewById(R.id.todo_title)
        editMemo = findViewById(R.id.todo_memo)
        //editFirstTimeHour = findViewById(R.id.hour1)
        //editFirstTimeMin = findViewById(R.id.minute1)
        editFirstTime = findViewById(R.id.start_time)
        editLastTIme = findViewById(R.id.end_time)
        //editLastTimeHour = findViewById(R.id.hour2)
        //editLastTimeMin = findViewById(R.id.minute2)
        buttonSave = findViewById(R.id.btn_save)

        //아이콘
        binding.iconBtn.setOnClickListener {
            val icondialog = IconDialog()
            icondialog.show(supportFragmentManager, "")
        }
        //색상
        binding.colorBtn.setOnClickListener {
            val colordialog = ColorDialog()
            colordialog.show(supportFragmentManager, "")
        }

        //날짜

        //시간
        binding.startTime.setOnClickListener() {
            val timePickerDialog = TimePickerDialog(this, this, 8, 0, 0)
            timePickerDialog.show()
        }
        binding.endTime.setOnClickListener() {
            val timePickerDialog = TimePickerDialog(this, this, 9, 0, 1)
            timePickerDialog.show()
        }

        //메모

        //스위치 on/off
        val switchView: SwitchCompat = findViewById(R.id.cswitch)
        switchView.setOnCheckedChangeListener {buttonView, isChecked ->
            if (isChecked) {
                switchView.isChecked = true
            }
        }
        //달력에 표시 되게...

        //반복
        binding.todoRepeat.setOnClickListener {
            val repeatdialog = RepeatDialog()
            repeatdialog.show(supportFragmentManager, "")
        }
        //알림
        binding.todoAlarm.setOnClickListener {
            val alarmdialog = AlarmDialog()
            alarmdialog.show(supportFragmentManager, "")
        }

        //취소 버튼
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        //저장 버튼
        buttonSave.setOnClickListener {
            addDataToFirestore()
        }
    }

    fun onClickOkButton3(hour: Int, minute: Int, flag: Int) {
        if(flag==0) binding.startTime.setText("$hour : $minute")
        else if(flag==1) binding.endTime.setText("$hour : $minute")
        //endTime이 startTime보다 빠르면 날짜 넘어가게 설정 필요
    }

    private fun addDataToFirestore() {
        val title = editTitle.text.toString()
        val memo = editMemo.text.toString()
        /*val firstTimeHour = editFirstTimeHour.text.toString()
        val firstTimeMin = editFirstTimeMin.text.toString()
        val lastTimeHour = editLastTimeHour.text.toString()
        val lastTimeMin = editLastTimeMin.text.toString()

         */

        val newItemData = ItemData()
        newItemData.dayTitle = title
        newItemData.dayMemo = memo
        /*newItemData.firstTimeHour = firstTimeHour
        newItemData.firstTimeMin = firstTimeMin
        newItemData.lastTimeHour = lastTimeHour
        newItemData.lastTimeMin = lastTimeMin

         */

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