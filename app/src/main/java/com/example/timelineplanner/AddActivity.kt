package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityAddBinding
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private lateinit var addTitle: EditText
    private lateinit var addColor: ImageView
    private lateinit var addIcon: ImageView
    private lateinit var date: TextView
    private lateinit var addFirstTime: TextView
    private lateinit var addLastTIme: TextView
    private lateinit var addMemo: EditText
    private lateinit var buttonSave: Button

    var icon = 0 //아이콘 ID
    var color = "" //색상 코드
    var repeatType = 0
    lateinit var repeatDays : Array<Int>
    var alarmType = 0
    lateinit var alarmTime : Array<Int>

    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜
    var selectedDate1: LocalDate = LocalDate.now() // 현재 날짜
    var selectedDate2: LocalDate = LocalDate.now() // 현재 날짜
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addTitle = findViewById(R.id.todo_title)
        addIcon = findViewById(R.id.icon_btn)
        addColor = findViewById(R.id.color_btn)
        addFirstTime = findViewById(R.id.start_time)
        addLastTIme = findViewById(R.id.end_time)
        addMemo = findViewById(R.id.todo_memo)
        buttonSave = findViewById(R.id.btn_save)

        //색상
        binding.colorBtn.setOnClickListener {
            val colorDialog = ColorSelectionDialog()
            colorDialog.setColorSelectedListener { selectedColorId ->
                binding.colorBtn.tag = selectedColorId
                when (selectedColorId) {
                    "#FFD5D5" -> binding.colorBtn.setImageResource(R.color.lightred)
                    "#FAFFBD" -> binding.colorBtn.setImageResource(R.color.lightyellow)
                    "#ADFFAC" -> binding.colorBtn.setImageResource(R.color.lightgreen)
                    "#D9D9D9" -> binding.colorBtn.setImageResource(R.color.lightgray)
                    "#F2D5FF" -> binding.colorBtn.setImageResource(R.color.phvink)
                    "#7FE8FF" -> binding.colorBtn.setImageResource(R.color.skyblue)
                    else -> {
                        // 선택된 아이콘 ID가 없거나 다른 ID인 경우에 대한 처리
                    }
                }
                // 선택된 아이콘 ID를 로그에 출력하는 부분을 람다식 바깥으로 빼냅니다.
                Log.d("Selected Icon", "Icon ID: $selectedColorId")
            }

            // 이 부분은 람다식 밖에서 실행되도록 수정합니다.
            Log.d("bin", "됐냐")
            colorDialog.show(supportFragmentManager, "color_dialog_tag")
        }

        //아이콘
        binding.iconBtn.setOnClickListener {
            val iconDialog = IconSelectionDialog()
            iconDialog.setIconSelectedListener { selectedIconId ->
                binding.iconBtn.tag = selectedIconId
                when (selectedIconId) {
                    R.drawable.wakeup -> binding.iconBtn.setImageResource(R.drawable.wakeup)
                    R.drawable.sleeping -> binding.iconBtn.setImageResource(R.drawable.sleeping)
                    R.drawable.train -> binding.iconBtn.setImageResource(R.drawable.train)
                    R.drawable.car -> binding.iconBtn.setImageResource(R.drawable.car)
                    R.drawable.computer -> binding.iconBtn.setImageResource(R.drawable.computer)
                    R.drawable.book -> binding.iconBtn.setImageResource(R.drawable.book)
                    R.drawable.food -> binding.iconBtn.setImageResource(R.drawable.food)
                    R.drawable.cleaning -> binding.iconBtn.setImageResource(R.drawable.cleaning)
                    R.drawable.muscle -> binding.iconBtn.setImageResource(R.drawable.muscle)
                    R.drawable.rest -> binding.iconBtn.setImageResource(R.drawable.rest)
                    R.drawable.shower -> binding.iconBtn.setImageResource(R.drawable.shower)
                    R.drawable.game -> binding.iconBtn.setImageResource(R.drawable.empty)
                    else -> {
                        // 선택된 아이콘 ID가 없거나 다른 ID인 경우에 대한 처리
                    }
                }
                // 선택된 아이콘 ID를 로그에 출력하는 부분을 람다식 바깥으로 빼냅니다.
                Log.d("Selected Icon", "Icon ID: $selectedIconId")
            }

            // 이 부분은 람다식 밖에서 실행되도록 수정합니다.
            Log.d("bin", "됐냐")
            iconDialog.show(supportFragmentManager, "icon_dialog_tag")
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed

        //오늘 날짜로 기본 text set
        binding.date1.setText(
            "${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일" +
                    " (${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})"
        )
        binding.date2.setText(
            "${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일" +
                    " (${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})"
        )
        //날짜 선택
        binding.date1.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog(
                this, this, startMonth.year + 1, endMonth.year - 1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 0
            )
            todoDatePickerDialog.show()
        }
        binding.date2.setOnClickListener() {
            val todoDatePickerDialog = TodoDatePickerDialog(
                this, this, startMonth.year + 1, endMonth.year - 1,
                selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, 1
            )
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
            val repeatdialog = RepeatDialog(this, this)
            repeatdialog.show()
        }

        //알림
        binding.todoAlarm.setOnClickListener {
            val alarmdialog = AlarmDialog(this, this)
            alarmdialog.show()
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
        if (flag == 0){
            binding.startTime.setText("%02d:%02d".format(hour, minute))}
        else if (flag == 1) {
            binding.endTime.setText("%02d:%02d".format(hour, minute))
            //endTime이 startTime보다 빠르면 끝나는 날짜를 다음날로 변경
            val startDate = binding.date1.text.toString()
            val endDate = binding.date2.text.toString()
            val startTime = binding.startTime.text.toString()
            val endTime = binding.endTime.text.toString()

            if (startDate == endDate && (startTime.compareTo(endTime) > 0)) {
                val dateString = endDate.split(" ")
                val endDay = dateString[1].substring(0, 2).toInt()
                val endDate =
                    LocalDate.of(selectedDate.year, selectedDate.month, selectedDate.dayOfMonth + 1)
                binding.date2.setText(
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

    //todoDatePicker OkButton 함수
    fun onClickOkButton4(year: Int, month: Int, day: Int, flag: Int) {
        if (flag == 0) {
            selectedDate1 = LocalDate.of(year, month, day)
            binding.date1.setText(
                "${month}월 ${day}일 (${
                    selectedDate1.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.KOREAN
                    )
                })"
            )
        }
        else if (flag == 1){
            selectedDate2 = LocalDate.of(year, month, day)
            binding.date2.setText(
                "${month}월 ${day}일 (${
                    selectedDate2.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.KOREAN
                    )
                })"
            )

        }
    }

    private fun addDataToFirestore() {
        val title = addTitle.text.toString()

        val icon = binding.iconBtn.tag as? Int
        val color = binding.colorBtn.tag as? String

        val dateString1 = selectedDate1.toString()
        val dateString2 = selectedDate2.toString()

        val firstTime = addFirstTime.text.toString() // 이 부분은 Firestore에서 가져온 문자열이어야 합니다.
        val firstTimeParts = firstTime.split(":")
        val firstTimeHour = firstTimeParts[0]
        val firstTimeMinute = firstTimeParts[1]

        val lastTime= addLastTIme.text.toString()
        val lastTimeParts = lastTime.split(":")
        val lastTimeHour = lastTimeParts[0]
        val lastTimeMinute = lastTimeParts[1]

        val memo = addMemo.text.toString()

        val newItemData = hashMapOf(
            "daytitle" to title,
            "daycolor" to color,
            "dayicon" to icon,
            "daydate1" to dateString1,
            "daydate2" to dateString2,
            "firstTime" to hashMapOf(
                "hour" to firstTimeHour,
                "minute" to firstTimeMinute
            ),
            "lastTime" to hashMapOf(
                "hour" to lastTimeHour,
                "minute" to lastTimeMinute
            ),
            "daymemo" to memo,
        )
        db.collection("users")
            .add(newItemData)
            .addOnSuccessListener { documentReference ->
                // 성공적으로 추가됐을 때 처리
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                Log.d("bin","데이터 저장됨")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "저장이 안됐습니다", Toast.LENGTH_SHORT).show()
                Log.d("bin","되긴 개뿔")
                // 실패했을 때 처리
            }
    }
}