package com.example.timelineplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.example.timelineplanner.databinding.ActivityAddBinding
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.timelineplanner.databinding.ActivityHomeBinding
import com.example.timelineplanner.databinding.AlarmDialogBinding
import com.example.timelineplanner.databinding.ColorDialogBinding
import com.example.timelineplanner.databinding.IconDialogBinding
import com.example.timelineplanner.databinding.RepeatDialogBinding
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat;
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private lateinit var editTitle: EditText
    private lateinit var editMemo: EditText
    private lateinit var editFirstTimeHour: EditText
    private lateinit var editFirstTimeMin: EditText
    private lateinit var editLastTimeHour: EditText
    private lateinit var editLastTimeMin: EditText
    private lateinit var buttonSave: Button

    var selectedDate: LocalDate = LocalDate.now() // 현재 날짜
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        binding.btnSave.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            //
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

//다이얼로그 클래스
class IconDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = IconDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}
class ColorDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ColorDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}
class RepeatDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = RepeatDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}
class AlarmDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AlarmDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}