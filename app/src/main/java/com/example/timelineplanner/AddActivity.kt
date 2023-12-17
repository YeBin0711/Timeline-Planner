package com.example.timelineplanner

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityAddBinding
import com.example.timelineplanner.databinding.IconDialogBinding
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.timelineplanner.model.ItemData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.properties.Delegates

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private lateinit var editTitle: EditText
    private lateinit var editMemo: EditText
    private lateinit var editIcon: ImageView
    private lateinit var date: TextView
    private lateinit var editFirstTime: TextView
    private lateinit var editLastTIme: TextView
    private lateinit var buttonSave: Button

    var color = "" //색상 코드
    var icon = 0 //아이콘 ID
    // selectedTime을 클래스의 멤버 변수로 선언// 기본값 설정
    var selectedTime: LocalTime = LocalTime.now()
    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTitle = findViewById(R.id.todo_title)
        editIcon = findViewById(R.id.icon_btn)
        editFirstTime = findViewById(R.id.start_time)
        editLastTIme = findViewById(R.id.end_time)
        editMemo = findViewById(R.id.todo_memo)
        buttonSave = findViewById(R.id.btn_save)

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
                    R.drawable.game-> binding.iconBtn.setImageResource(R.drawable.game)
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
        //색상
        binding.colorBtn.setOnClickListener {
            val colordialog = ColorDialog(this, this)
            colordialog.show()
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
        selectedDate = LocalDate.of(year, month, day)
        if (flag == 0) binding.date1.setText(
            "${month}월 ${day}일 (${
                selectedDate.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.KOREAN
                )
            })"
        )
        else if (flag == 1) binding.date2.setText(
            "${month}월 ${day}일 (${
                selectedDate.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.KOREAN
                )
            })"
        )
    }

    private fun addDataToFirestore() {
        val title = editTitle.text.toString()
        val memo = editMemo.text.toString()

        val iconResourceId = binding.iconBtn.tag as? Int

        val firstTime = editFirstTime.text.toString() // 이 부분은 Firestore에서 가져온 문자열이어야 합니다.
        val firstTimeParts = firstTime.split(":")
        val firstTimeHour = firstTimeParts[0]
        val firstTimeMinute = firstTimeParts[1]

        val lastTime= editLastTIme.text.toString()
        val lastTimeParts = lastTime.split(":")
        val lastTimeHour = lastTimeParts[0]
        val lastTimeMinute = lastTimeParts[1]


        val newItemData = hashMapOf(
            "daytitle" to title,
            "daymemo" to memo,
            "daycolor" to color.toInt(),
            "dayicon" to iconResourceId,
            "firstTime" to hashMapOf(
                "hour" to firstTimeHour,
                "minute" to firstTimeMinute
            ),
            "lastTime" to hashMapOf(
                "hour" to lastTimeHour,
                "minute" to lastTimeMinute
            )
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