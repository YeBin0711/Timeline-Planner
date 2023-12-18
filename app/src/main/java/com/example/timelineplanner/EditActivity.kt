package com.example.timelineplanner

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.example.timelineplanner.databinding.ActivityEditBinding
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle


class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var selectedItem: ItemData
    private lateinit var editfirstTime: TextView
    private lateinit var editlastTime: TextView

    private val db = FirebaseFirestore.getInstance()
    var selectedDate: LocalDate = LocalDate.now()
    var selectedDate1: LocalDate = LocalDate.now()
    var selectedDate2: LocalDate = LocalDate.now()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editfirstTime = findViewById(R.id.edit_start_time)
        editlastTime = findViewById(R.id.edit_end_time)

        //editTitle = findViewById(R.id.edit_todo_title)
        val intent = intent
        if (intent.hasExtra("selectedItem")) {
            selectedItem = intent.getParcelableExtra<ItemData>("selectedItem")!!
            // ... rest of your existing code

            val editTitle = findViewById<TextView>(R.id.edit_todo_title)
            editTitle.text = selectedItem.daytitle

            val editColor = findViewById<ImageButton>(R.id.edit_color_btn)
            // 이미지뷰의 배경색을 변경하려면 setBackgroundColor을 사용합니다.
            editColor.setBackgroundColor(Color.parseColor(selectedItem.daycolor))

            val editIcon = findViewById<ImageButton>(R.id.edit_icon_btn)
            editIcon.setImageResource(selectedItem.dayicon)

            val editDate1 = findViewById<TextView>(R.id.edit_date1)
            val dateFormatter1 = DateTimeFormatter.ofPattern("MM월 dd일 (E)", Locale.KOREAN)
            val formattedDate1 = selectedItem.dayDate1.format(dateFormatter1)
            editDate1.text = formattedDate1

            val editDate2 = findViewById<TextView>(R.id.edit_date2)
            val dateFormatter2 = DateTimeFormatter.ofPattern("MM월 dd일 (E)", Locale.KOREAN)
            val formattedDate2 = selectedItem.dayDate2.format(dateFormatter2)
            editDate2.text = formattedDate2

            val editstartTime = findViewById<TextView>(R.id.edit_start_time)
            editstartTime.text = "${selectedItem.firstTime.hour}:${selectedItem.firstTime.minute}"

            val editendTime = findViewById<TextView>(R.id.edit_end_time)
            editendTime.text = "${selectedItem.lastTime.hour}:${selectedItem.lastTime.minute}"

            val editMemo = findViewById<TextView>(R.id.edit_todo_memo)
            editMemo.text = selectedItem.daymemo

            val documentId = findViewById<TextView>(R.id.document_id)
            documentId.text = selectedItem.firestoreDocumentId

        }
        //색상
        binding.editColorBtn.setOnClickListener {
            val colorDialog = ColorSelectionDialog()
            colorDialog.setColorSelectedListener { selectedColorId ->
                binding.editColorBtn.tag = selectedColorId
                when (selectedColorId) {
                    "#FFD5D5" -> {
                        binding.editColorBtn.setImageResource(R.color.lightred)
                    }
                    "#FAFFBD" -> {
                        binding.editColorBtn.setImageResource(R.color.lightyellow)
                    }
                    "#ADFFAC" -> {
                        binding.editColorBtn.setImageResource(R.color.lightgreen)
                    }
                    "#D9D9D9" -> {
                        binding.editColorBtn.setImageResource(R.color.lightgray)
                    }
                    "#F2D5FF" -> {
                        binding.editColorBtn.setImageResource(R.color.phvink)
                    }
                    "#7FE8FF" -> {
                        binding.editColorBtn.setImageResource(R.color.skyblue)
                    }
                    else -> {
                        // 선택된 아이콘 ID가 없거나 다른 ID인 경우에 대한 처리
                        binding.editColorBtn.setColorFilter(ContextCompat.getColor(this, R.color.lightgray))
                    }
                }
            }
            colorDialog.show(supportFragmentManager, "color_dialog_tag")
        }

        //아이콘
        binding.editIconBtn.setOnClickListener {
            val iconDialog = IconSelectionDialog()
            iconDialog.setIconSelectedListener { selectedIconId ->
                binding.editIconBtn.tag = selectedIconId
                when (selectedIconId) {
                    R.drawable.wakeup -> binding.editIconBtn.setImageResource(R.drawable.wakeup)
                    R.drawable.sleeping -> binding.editIconBtn.setImageResource(R.drawable.sleeping)
                    R.drawable.train -> binding.editIconBtn.setImageResource(R.drawable.train)
                    R.drawable.car -> binding.editIconBtn.setImageResource(R.drawable.car)
                    R.drawable.computer -> binding.editIconBtn.setImageResource(R.drawable.computer)
                    R.drawable.book -> binding.editIconBtn.setImageResource(R.drawable.book)
                    R.drawable.food -> binding.editIconBtn.setImageResource(R.drawable.food)
                    R.drawable.cleaning -> binding.editIconBtn.setImageResource(R.drawable.cleaning)
                    R.drawable.muscle -> binding.editIconBtn.setImageResource(R.drawable.muscle)
                    R.drawable.rest -> binding.editIconBtn.setImageResource(R.drawable.rest)
                    R.drawable.shower -> binding.editIconBtn.setImageResource(R.drawable.shower)
                    R.drawable.empty -> binding.editIconBtn.setImageResource(R.drawable.game)
                    else -> {

                    }
                }
            }

            iconDialog.show(supportFragmentManager, "icon_dialog_tag")
        }
        // 삭제 버튼에 대한 클릭 리스너 추가
        binding.btnDelete.setOnClickListener {
            if (selectedItem != null) {
                // Firestore에서 해당 문서를 삭제하는 코드를 작성하세요.
                Log.d("id", "${selectedItem.firestoreDocumentId}")
                db.collection("users")
                    .document(selectedItem.firestoreDocumentId) // 해당 문서 ID
                    .delete()
                    .addOnSuccessListener {
                        // 성공적으로 삭제되었을 때, HomeActivity로 돌아가기
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish() // 현재 EditActivity 종료
                    }
                    .addOnFailureListener { e ->
                        // 실패 시 처리 (예: 로그 등록)
                    }
            }
        }

        binding.editBtnSave.setOnClickListener {
            editDataToFirestore()
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed

        //날짜 선택
        binding.editDate1.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog1(
                this, this, startMonth.year + 1, endMonth.year - 1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 0
            )
            todoDatePickerDialog.show()
        }
        binding.editDate2.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog1(
                this, this, startMonth.year + 1, endMonth.year - 1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 1
            )
            todoDatePickerDialog.show()
        }

        //시간
        binding.editStartTime.setOnClickListener() {
            val timePickerDialog = TimePickerDialog1(this, this, 8, 0, 0)
            timePickerDialog.show()
        }
        binding.editEndTime.setOnClickListener() {
            val timePickerDialog = TimePickerDialog1(this, this, 9, 0, 1)
            timePickerDialog.show()
        }

        //스위치 on/off
        val switchView: SwitchCompat = findViewById(R.id.cswitch)
        switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchView.isChecked = true
            }
        }
    }

    fun onClickOkButton5(hour: Int, minute: Int, flag: Int) {
        if (flag == 0){
            binding.editStartTime.setText("%02d:%02d".format(hour, minute))}
        else if (flag == 1) {
            binding.editEndTime.setText("%02d:%02d".format(hour, minute))
            //endTime이 startTime보다 빠르면 끝나는 날짜를 다음날로 변경
            val startDate = binding.editDate1.text.toString()
            val endDate = binding.editDate2.text.toString()
            val startTime = binding.editStartTime.text.toString()
            val endTime = binding.editEndTime.text.toString()

            if (startDate == endDate && (startTime.compareTo(endTime) > 0)) {
                val dateString = endDate.split(" ")
                val endDay = dateString[1].substring(0, 2).toInt()
                val endDate =
                    LocalDate.of(selectedDate.year, selectedDate.month, selectedDate.dayOfMonth + 1)
                binding.editDate2.setText(
                    "${dateString[0]} ${endDay + 1}일 (${
                        endDate.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.KOREAN
                        )
                    })"
                )
            }
        }
    }

    fun onClickOkButton6(year: Int, month: Int, day: Int, flag: Int) {
        //selectedDate = LocalDate.of(year, month, day)
        if (flag == 0) {
            selectedDate1 = LocalDate.of(year, month, day)
            binding.editDate1.setText(
                "${month}월 ${day}일 (${
                    selectedDate.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.KOREAN
                    )
                })"
            )
            selectedItem?.dayDate1 = selectedDate
        } else if (flag == 1) {
            selectedDate2 = LocalDate.of(year, month, day)
            binding.editDate2.setText(
                "${month}월 ${day}일 (${
                    selectedDate.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.KOREAN
                    )
                })"

            )
            selectedItem?.dayDate2 = selectedDate
        }
    }

    private fun editDataToFirestore() {

        if (selectedItem != null) {
            val updatedTitle = binding.editTodoTitle.text.toString()
            val updatedMemo = binding.editTodoMemo.text.toString()
            val updatedColor = binding.editColorBtn.tag as? String
            val updatedIcon = binding.editIconBtn.tag as? Int

            val updatedDateString1 = selectedDate1.toString()
            val updatedDateString2 = selectedDate2.toString()

            val editfirstTime = editfirstTime.text.toString() // 이 부분은 Firestore에서 가져온 문자열이어야 합니다.
            val editfirstTimeParts = editfirstTime.split(":")
            val editfirstTimeHour = editfirstTimeParts[0]
            val editfirstTimeMinute = editfirstTimeParts[1]

            val editlastTime= editlastTime.text.toString()
            val editlastTimeParts = editlastTime.split(":")
            val editlastTimeHour = editlastTimeParts[0]
            val editlastTimeMinute = editlastTimeParts[1]

            db.collection("users")
                .document(selectedItem.firestoreDocumentId)
                .update(
                    "daytitle", updatedTitle,
                    "daycolor", updatedColor,
                    "dayicon", updatedIcon,
                    "dayDate1", updatedDateString1,
                    "dayDate2", updatedDateString2,
                    "firstTime", mapOf(
                        "hour" to editfirstTimeHour,
                        "minute" to editfirstTimeMinute
                    ),
                    "lastTime", mapOf(
                        "hour" to editlastTimeHour,
                        "minute" to editlastTimeMinute
                    ),
                    "daymemo", updatedMemo
                )
                .addOnSuccessListener {
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e("EditActivity", "Firestore update failed: $e")
                }
            }

        }
}

