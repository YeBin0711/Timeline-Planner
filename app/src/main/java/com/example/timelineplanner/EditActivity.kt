package com.example.timelineplanner

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.timelineplanner.databinding.ActivityEditBinding
import com.example.timelineplanner.model.ItemData
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter
import java.util.Locale


class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent.hasExtra("selectedItem")) {
            val selectedItem = intent.getParcelableExtra<ItemData>("selectedItem")!!
            val editTitle = findViewById<TextView>(R.id.edit_todo_title)
            editTitle.text = selectedItem.daytitle

            val editColor = findViewById<ImageButton>(R.id.edit_color_btn)
            // 이미지뷰의 배경색을 변경하려면 setBackgroundColor을 사용합니다.
            editColor.setBackgroundColor(Color.parseColor(selectedItem.daycolor))

            val editIcon = findViewById<ImageButton>(R.id.edit_icon_btn)
            editIcon.setImageResource(selectedItem.dayicon)

            val editDate = findViewById<TextView>(R.id.edit_date1)
            val dateFormatter = DateTimeFormatter.ofPattern("MM월 dd일 (E)", Locale.KOREAN)
            val formattedDate = selectedItem.selectedDate.format(dateFormatter)
            editDate.text = formattedDate

            val editstartTime = findViewById<TextView>(R.id.edit_start_time)
            editstartTime.text = "${selectedItem.firstTime.hour}:${selectedItem.firstTime.minute}"

            val editendTime = findViewById<TextView>(R.id.edit_end_time)
            editendTime.text = "${selectedItem.lastTime.hour}:${selectedItem.lastTime.minute}"

            val editMemo = findViewById<TextView>(R.id.edit_todo_memo)
            editMemo.text = selectedItem.daymemo

        }

        //구현 중
        binding.btnDelete.setOnClickListener {
            val selectedItem = intent.getParcelableExtra<ItemData>("selectedItem")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            /*
            if (selectedItem != null) {
                // Firestore에서 해당 문서를 삭제하는 코드를 작성하세요.
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .document(selectedItem.firestoreDocumentId)// 해당 문서 ID
                    .delete()
                    .addOnSuccessListener {
                        // 성공적으로 삭제되었을 때, HomeActivity로 돌아가기
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // 현재 EditActivity 종료
                        Log.d("delete","데이터가 잘 삭제되었다")
                    }
                    .addOnFailureListener { e ->
                        // 실패 시 처리 (예: 로그 등록)
                        Log.w("EditActivity", "Error deleting document", e)
                    }
            }*/
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
