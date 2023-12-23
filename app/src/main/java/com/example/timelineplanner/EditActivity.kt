package com.example.timelineplanner

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityEditBinding
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    lateinit var selectedItem: ItemData
    private lateinit var editfirstTime: TextView
    private lateinit var editlastTime: TextView

    private val db = FirebaseFirestore.getInstance()
    var selectedDate: LocalDate = LocalDate.now()
    var selectedDate1: LocalDate = LocalDate.now()
    var selectedDate2: LocalDate = LocalDate.now()

    var repeatType = 0
    lateinit var repeatDays : Array<Int>
    var alarmType = 0
    lateinit var alarmTime : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editfirstTime = findViewById(R.id.edit_start_time)
        editlastTime = findViewById(R.id.edit_end_time)

        //HomeActivity에서 선택한 데이터 가져오기
        val intent = intent
        if (intent.hasExtra("selectedItem")) {
            selectedItem = intent.getParcelableExtra<ItemData>("selectedItem")!!

            val editTitle = findViewById<TextView>(R.id.edit_todo_title)
            editTitle.text = selectedItem.daytitle

            val editColor = findViewById<ImageButton>(R.id.edit_color_btn)
            editColor.setBackgroundColor(selectedItem.daycolor)

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

            val editShow = findViewById<SwitchCompat>(R.id.edit_cswitch)
            editShow.isChecked = selectedItem.dayshow

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
                // 색상 리소스를 가져오기
                val colorResource = when (selectedColorId) {
                    Color.parseColor("#FFD5D5") -> R.color.lightred
                    Color.parseColor("#FAFFBD") -> R.color.lightyellow
                    Color.parseColor("#ADFFAC") -> R.color.lightgreen
                    Color.parseColor("#D9D9D9") -> R.color.lightgray
                    Color.parseColor("#F2D5FF") -> R.color.phvink
                    Color.parseColor("#7FE8FF") -> R.color.skyblue
                    else -> R.color.lightgray // 기본값 또는 처리되지 않은 경우
                }
                // 색상을 이미지뷰 배경으로 설정
                binding.editColorBtn.setBackgroundResource(colorResource)
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

        //반복
        binding.editTodoRepeat.setOnClickListener {
            val repeatdialog = RepeatDialog1(this, this)
            repeatdialog.show()
        }

        //알림
        binding.editTodoAlarm.setOnClickListener {
            val alarmdialog = AlarmDialog1(this, this)
            alarmdialog.show()
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
                        //val intent = Intent()
                        intent.putExtra("resultDate", selectedItem.dayDate1.toString())
                        setResult(Activity.RESULT_OK, intent)
                        finish() // 현재 EditActivity 종료
                    }
                    .addOnFailureListener { e ->
                        // 실패 시 처리 (예: 로그 등록)
                    }
            }
        }

        //수정한 것을 저장하는 버튼
        binding.editBtnSave.setOnClickListener {
            editDataToFirestore()
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed

        //시작 날짜 선택
        binding.editDate1.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog1(
                this, this, startMonth.year + 1, endMonth.year - 1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 0
            )
            todoDatePickerDialog.show()
        }

        //끝나는 날짜 선택
        binding.editDate2.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog1(
                this, this, startMonth.year + 1, endMonth.year - 1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 1
            )
            todoDatePickerDialog.show()
        }

        //시작 시간 선택
        binding.editStartTime.setOnClickListener() {
            val timePickerDialog = TimePickerDialog1(this, this, 8, 0, 0)
            timePickerDialog.show()
        }
        //끝나는 시간 선택
        binding.editEndTime.setOnClickListener() {
            val timePickerDialog = TimePickerDialog1(this, this, 9, 0, 1)
            timePickerDialog.show()
        }

        //달력에 출력할 것인지에 대한 스위치 on/off
        val switchView: SwitchCompat = findViewById(R.id.edit_cswitch)
        switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchView.isChecked = true
            }
        }
    }

    //TimePicker에 대한 함수
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

    //DatePicker에서 사용하는 함수
    fun onClickOkButton6(year: Int, month: Int, day: Int, flag: Int) {
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

    //Firestore 데이터 수정하기
    private fun editDataToFirestore() {

        if (selectedItem != null) {
            val updatedTitle = binding.editTodoTitle.text.toString()
            val updatedMemo = binding.editTodoMemo.text.toString()
            val updatedColor = binding.editColorBtn.tag as? Int ?: selectedItem.daycolor
            val updatedIcon = binding.editIconBtn.tag as? Int ?: selectedItem.dayicon

            val updatedDateString1 = selectedDate1.toString()
            val updatedDateString2 = selectedDate2.toString()

            val editfirstTime = editfirstTime.text.toString()
            val editfirstTimeParts = editfirstTime.split(":")
            val editfirstTimeHour = editfirstTimeParts[0]
            val editfirstTimeMinute = editfirstTimeParts[1]

            val editlastTime= editlastTime.text.toString()
            val editlastTimeParts = editlastTime.split(":")
            val editlastTimeHour = editlastTimeParts[0]
            val editlastTimeMinute = editlastTimeParts[1]

            val editShow = binding.editCswitch.isChecked

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
                    "dayshow", editShow,
                    "daymemo", updatedMemo
                )
                .addOnSuccessListener {
                    //val intent = Intent()
                    intent.putExtra("resultDate", selectedItem.dayDate1.toString())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e("EditActivity", "Firestore update failed: $e")
                }
        }

    }
}