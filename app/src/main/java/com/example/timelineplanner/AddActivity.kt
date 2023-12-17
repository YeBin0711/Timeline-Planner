package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityAddBinding
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private lateinit var editTitle: EditText
    private lateinit var editMemo: EditText
    private lateinit var editFirstTime: TextView
    private lateinit var editLastTIme: TextView
    private lateinit var buttonSave: Button

    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTitle = findViewById(R.id.todo_title)
        editMemo = findViewById(R.id.todo_memo)
        editFirstTime = findViewById(R.id.start_time)
        editLastTIme = findViewById(R.id.end_time)
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


        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed

        //오늘 날짜로 기본 text set
        binding.date1.setText("${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일" +
                " (${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})")
        binding.date2.setText("${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일" +
                " (${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})")
        //날짜 선택
        binding.date1.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog(this, this, startMonth.year+1, endMonth.year-1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 0)
            todoDatePickerDialog.show()
        }
        binding.date2.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog(this, this, startMonth.year+1, endMonth.year-1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 1)
            todoDatePickerDialog.show()
        }

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

    //timePicker OkButton 함수
    fun onClickOkButton3(hour: Int, minute: Int, flag: Int) {
        if(flag==0) binding.startTime.setText("%02d : %02d".format(hour, minute))
        else if(flag==1) {
            binding.endTime.setText("%02d : %02d".format(hour, minute))

            //endTime이 startTime보다 빠르면 끝나는 날짜를 다음날로 변경
            val startDate = binding.date1.text.toString()
            val endDate = binding.date2.text.toString()
            val startTime = binding.startTime.text.toString()
            val endTime = binding.endTime.text.toString()

            if(startDate==endDate && (startTime.compareTo(endTime) > 0)) {
                val dateString = endDate.split(" ")
                val endDay = dateString[1].substring(0, 2).toInt()
                val endDate = LocalDate.of(selectedDate.year, selectedDate.month, selectedDate.dayOfMonth+1)
                binding.date2.setText("${dateString[0]} ${endDay + 1}일 (${endDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})")
            }
        }


    }
    //todoDatePicker OkButton 함수
    fun onClickOkButton4(year: Int, month: Int, day: Int, flag: Int) {
        selectedDate = LocalDate.of(year, month, day)
        if(flag==0) binding.date1.setText("${month}월 ${day}일 (${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})")
        else if(flag==1) binding.date2.setText("${month}월 ${day}일 (${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})")
    }

    private fun addDataToFirestore() {
        val title = editTitle.text.toString()
        val memo = editMemo.text.toString()
        val firstTime = editFirstTime.text.toString()
        val lastTime= editLastTIme.text.toString()

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